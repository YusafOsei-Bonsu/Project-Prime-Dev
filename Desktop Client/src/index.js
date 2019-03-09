//Middleware between server and the routes

//Express for building APIs
const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const crypto = require('crypto');
const mongoose = require('mongoose');
const multer = require('multer');
const GridFsStorage = require('multer-gridfs-storage');
const Grid = require('gridfs-stream');
const methodOverride = require('method-override');

const app = express();

// Node.js built-in Middleware
app.use(bodyParser.json());

// Use a query string to create form, in order to make a delete request
app.use(methodOverride('_method'));

// To serve the static CSS file
app.use('/', express.static('./'));

// Mongo URI
const mongoURI = 'mongodb://projectPrime:projectPrime@projectprime-shard-00-00-wreg9.mongodb.net:27017,projectprime-shard-00-01-wreg9.mongodb.net:27017,projectprime-shard-00-02-wreg9.mongodb.net:27017/test?ssl=true&replicaSet=ProjectPrime-shard-0&authSource=admin&retryWrites=true'
mongoose.set('useNewUrlParser', true);

// Create mongo connection
const connection = mongoose.createConnection(mongoURI);

// Init gfs
let GFS;

connection.once('open', () => {
  // Init stream
  GFS = Grid(connection.database, mongoose.mongo);
  GFS.collection('uploads');
})

// Create storage engine
const storage = new GridFsStorage({url: mongoURI,file: (req, file) => {
    return new Promise((resolve, reject) => {
      crypto.randomBytes(16, (err, buf) => {
        if (err) {
          return reject(err)
        }
        const filename = buf.toString('hex') + path.extname(file.originalname)
        const fileInfo = {filename: filename, bucketName: 'uploads'}
        resolve(fileInfo)
      });
    });
  }
});

// Use as middleware so it uploads to the DB
const upload = multer({storage});

// @route GET /
// @desc Loads homepage
app.get('/', (req, response) => {
  // res.sendFile('src/upload.html', {root: __dirname});
  
  GFS.files.find().toArray((err, files) => {
    // Check if files exist
    if (!files || files.length === 0) {
      // No files
      response.render('upload', {files: false});
    } else {
      files.map(file => {
        if(file.contentType == 'image/jpeg' || file.contentType === 'image/png') {
          file.isImage = true;
        } else {
          file.isImage = false;
        }
      });
      response.render('upload', {files: files});
    }

    // Files exist
    return response.json(files);
  });
})

// @route POST /upload
// @desc  Uploads 1 file to DB
app.post('/upload', upload.single('file'), (req, res) => {
  // Responds with file properties (in JSON)
  // res.json({file: req.file});
  // Navigate back to the desktop client
  res.redirect('/');
})

// @route GET /files
// @desc  Display all files in JSON
app.get('/files', (req, response) => {
  GFS.files.find().toArray((err, files) => {
    // Check if files exist
    if (!files || files.length === 0) {
      return response.status(404).json({
        err: 'No files exist'
      });
    }

    // Files exist
    return response.json(files);
  });
});

// @route GET /files/:filename
// @desc  Display single file object
app.get('/files/:filename', (req, res) => {
  GFS.files.findOne({filename: req.params.filename}, (err, file) => {
    // Check if file
    if (!file || file.length === 0) {
      return res.status(404).json({
        err: 'No file exists'
      });
    }
    // File exists
    return res.json(file)
  });
});

// @route DELETE /files/:id
// @desc  Delete file
app.delete('/files/:id', (request, response) => {
  GFS.remove({_id: request.params.id, root: 'uploads'}, (err, gridStore) => {
    if (err) {
      return response.status(404).json({err: err})
    }
    response.redirect('/'); 
  })
})

module.exports = app