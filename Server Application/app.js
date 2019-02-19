const express = require('express')
const app = express()

const fileRoute = require('./api/routes/files')

app.use('/files', fileRoute)

module.exports = app