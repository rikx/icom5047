var express = require('express');
var router = express.Router();

/* GET home (login) page. */
router.get('/', function(req, res, next) {
	res.render('index', { title: 'Servicio De Extension Agricola',});
});

/* POST login */
router.post('/login', function(req, res, next) {
	if(!req.body.hasOwnProperty('input_username') || !req.body.hasOwnProperty('input_password') ) {
  	res.statusCode = 400;
  	return res.send('Error: Missing fields for user login.');
	} else {
	  var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	    	return console.error('error fetching client from pool', err);
	  	}
	  	// TODO: modify query to also give you account type
		  client.query('SELECT user_id, username FROM person natural join users WHERE username=$1', [req.body.input_username], function(err, result) {
		  	//call `done()` to release the client back to the pool
		    done();
		    if(err) {
		      return console.error('error running query', err);
		    }
		    if(result.rowCount == 0){
  				// not found
  				client.end();
  				res.statusCode = 404;
					res.send("User not found.");
  			} else {
  					console.log(result.rows);
  					switch(get_user_type(result.rows)) {
  						case 'admin': {
  							console.log('o hi there');
  							res.location('admin');
  							res.redirect('users/admin');
  							break;
  						}
  						case 'agent': {
  							res.location("agent");
  							res.redirect('users/agent');
  							break;
  						}
  						case 'specialist': {
  							res.location("specialist");
  							res.redirect('users/specialist');
  							break;
  						}
						}
  			}
		  });
	  });
	}
});

// TODO: this will have to be some account_type parameter in the final version
function get_user_type(query_result){
	console.log('well we got to get_user_type');
	var id = query_result[0].user_id; 
	if (id == 1) {
		return 'admin';
	};
	if (id == 2) {
		return 'agent';
	};
	if (id == 3) {
		return 'specialist';
	};
}

module.exports = router;
