//Main server file to run the server
const http = require('http')

//Adding dependency to the middleware
const app = require('./app')

//Currently using localhost port "3003" - Arbitrary port
const port = process.env.PORT || 3003

const server = http.createServer(app)

server.listen(port)