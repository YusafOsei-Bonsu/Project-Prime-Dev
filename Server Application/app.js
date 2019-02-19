//Middleware between server and the routes

//Express for building APIs
const express = require('express')
const app = express()

const fileRoute = require('./api/routes/files')

//Routing to files.js to handle requests
app.use('/files', fileRoute)

module.exports = app
