// The database
const mongoURI = 'mongodb://projectPrime:projectPrime@projectprime-shard-00-00-wreg9.mongodb.net:27017,projectprime-shard-00-01-wreg9.mongodb.net:27017,projectprime-shard-00-02-wreg9.mongodb.net:27017/test?ssl=true&replicaSet=ProjectPrime-shard-0&authSource=admin&retryWrites=true'
var mongo = require('mongodb').MongoClient;

mongo.connect(mongoURI, function(err, db) {
    if (!err) {
        alert("Connected to the database");
    } else {
        alert("something went wrong");
    }
});