var express = require('express');
var app = express()

var pg = require('pg');
var conString = "postgres://username:password@localhost/database";

app.get('/', function (req, res) {
  res.send('Hello World! This is the reengineering SEA server');
})

var server = app.listen(3000, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Sea app listening at http://%s:%s', host, port);

})