var mongo = require('mongodb').MongoClient;
const mongoURI = 'mongodb://projectPrime:projectPrime@projectprime-shard-00-00-wreg9.mongodb.net:27017,projectprime-shard-00-01-wreg9.mongodb.net:27017,projectprime-shard-00-02-wreg9.mongodb.net:27017/test?ssl=true&replicaSet=ProjectPrime-shard-0&authSource=admin&retryWrites=true';

/* 
//mongo.connect(mongoURI, function(error, db) {
  if(!error) {
    alert("Connected to MongoDB database woo!");
  } else {
    alert("Something went wrong bro.. this sucks bro.");
  }
});
*/
