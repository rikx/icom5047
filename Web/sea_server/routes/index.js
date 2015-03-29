var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  var db = req.db;
  db.connect(req.conString, function(err, client, done) {
  	if(err) {
    	return console.error('error fetching client from pool', err);
  	}
	  client.query('SELECT * FROM userlist', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();
	    if(err) {
	      return console.error('error running query', err);
	    }
	    res.render('index', { title: 'Express', userlist: result.rows });
	  });
  });
});

module.exports = router;
