//Middleware between server and the routes

//Express for building APIs
const express = require('express')
const bodyParser = require('body-parser')
const path = require('path')
const crypto = require('crypto')
const mongoose = require('mongoose')
const multer = require('multer')
const GridFsStorage = require('multer-gridfs-storage')
const Grid = require('gridfs-stream')
const methodOverride = require('method-override')
const rsync = require('rsync');

const app = express()

// Node.js Middleware
app.use(bodyParser.json())
app.use(methodOverride('_method'))
app.use('/', express.static('./'))

// Mongo URI
const mongoURI = 'mongodb://projectPrime:projectPrime@projectprime-shard-00-00-wreg9.mongodb.net:27017,projectprime-shard-00-01-wreg9.mongodb.net:27017,projectprime-shard-00-02-wreg9.mongodb.net:27017/test?ssl=true&replicaSet=ProjectPrime-shard-0&authSource=admin&retryWrites=true'
mongoose.set('useNewUrlParser', true);

// Create mongo connection
const conn = mongoose.createConnection(mongoURI)

// Init gfs
let gfs

conn.once('open', () => {
  // Init stream
  gfs = Grid(conn.db, mongoose.mongo)
  gfs.collection('uploads')
})

// Create storage engine
const storage = new GridFsStorage({
  url: mongoURI,
  file: (req, file) => {
    return new Promise((resolve, reject) => {
      const fileInfo = {
        filename: file.originalname,
          /** With gridfs we can store aditional meta-data along with the file */
        metadata: function(req, file, cb) {
          cb(null, { originalname: file.originalname });
        },
        bucketName: 'uploads'
      }
      resolve(fileInfo)
    })
  }
})
const upload = multer({ storage })

app.get('/', (req, res) => {
  res.sendFile('./index.html', {root: __dirname})
})

// @route GET /files
// @desc  Display all files in JSON
app.get('/files', (req, res) => {
  console.log('GET request to /files')
  gfs.files.find().toArray((err, files) => {
    // Check if files exist
    if (!files || files.length === 0) {
      return res.status(404).json({
        err: 'No files exist'
      })
    }

    // Files exist
    return res.json(files)
  })
})

// @route POST /upload
// @desc  Uploads file to DB
app.post('/upload', upload.single('file'), (req, res) => {
  console.log('POST request to /upload')
   //res.json({ file: req.file })
})

// @route GET /files/:filename
// @desc  Display single file object
app.get('/files/:filename', (req, res) => {
  gfs.files.findOne({ filename: req.params.filename }, (err, file) => {
    // Check if file
    if (!file || file.length === 0) {
      return res.status(404).json({
        err: 'No file exists'
      })
    }
    // File exists
    return res.json(file)
  })
})

// @route GET /download
// @desc  Download single file object
app.get('/download/:filename', (req, res) => {
  // Check file exists on MongoDB
  gfs.files.findOne({ filename: req.params.filename }, (err, file) => {
    // Check if file
    if (!file || file.length === 0) {
      return res.status(404).json({
        err: 'No file exists'
      })
    }
    console.log("File Found")
    var readstream = gfs.createReadStream({ filename: req.params.filename });
    readstream.pipe(res);            
  });
});  

// @route DELETE /files/:id
// @desc  Delete file
app.delete('/files/:id', (req, res) => {
  gfs.remove({ _id: req.params.id, root: 'uploads' }, (err, gridStore) => {
    if (err) {
      return res.status(404).json({ err: err })
    }
    res.redirect('/files')
  })
})

module.exports = app