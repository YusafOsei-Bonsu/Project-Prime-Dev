//load app server using express

const express = require('express')
const app = express()

app.get("/", (req, res) => {
  console.log("Responding to root route")
  res.send("Hello from R0000T")
})

app.get("/users", (req, res) => {
  var user1 = {firstname: "John", lastname: "Smith"}
  const user2 = {firstname: "Jane", lastname: "Doe"}
  res.json([user1, user2])
})

app.listen(3003, () => {
  console.log("Server is up and listening on 3003...")
})
