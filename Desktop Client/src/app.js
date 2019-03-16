const express = require('express')
const bodyParser = require('body-parser')
const path = require('path')
const crypto = require('crypto')
const mongoose = require('mongoose')
const multer = require('multer')
const GridFsStorage = require('multer-gridfs-storage')
const Grid = require('gridfs-stream')
const methodOverride = require('method-override')

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
let GFS

conn.once('open', () => {
  // Init stream
  GFS = Grid(conn.db, mongoose.mongo)
  GFS.collection('uploads')
})

// Create storage engine
const storage = new GridFsStorage({
  url: mongoURI, file: (req, file) => {
    return new Promise((resolve, reject) => {
      crypto.randomBytes(16, (err, buf) => {
        if (err) {
          return reject(err)
        }
        const filename = buf.toString('hex') + path.extname(file.originalname)
        const fileInfo = {
          filename: filename,
          bucketName: 'uploads'
        }
        resolve(fileInfo)
      })
    })
  }
})
const upload = multer({storage})

// @route GET /files
// @desc  Display all files in JSON
app.get('/files', (req, res) => {
  console.log('GET request to /files')
  GFS.files.find().toArray((err, files) => {
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
   res.json({ file: req.file })
   res.redirect('/files')
})

// @route GET /files/:filename
// @desc  Display single file object
app.get('/files/:filename', (req, res) => {
  GFS.files.findOne({ filename: req.params.filename }, (err, file) => {
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

// @route DELETE /files/:id
// @desc  Delete file
app.delete('/files/:id', (req, res) => {
  GFS.remove({ _id: req.params.id, root: 'uploads' }, (err, gridStore) => {
    if (err) {
      return res.status(404).json({ err: err })
    }
    res.redirect('/files')
  })
})

module.exports = app