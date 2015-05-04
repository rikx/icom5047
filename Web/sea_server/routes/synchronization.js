var express = require('express');
var router = express.Router();

/* 
 * GET All flowchart Rows
 */
router.get('/flowchart', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// select all rows from flowchart
		client.query('SELECT * FROM flowchart', function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		res.json({ 
	  			flowchart: result.rows
	  		});
	  	}
	  });
	});
});

/* 
 * GET All item Rows
 */
router.get('/item', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// select all rows from item
		client.query('SELECT * FROM item', function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		res.json({ 
	  			item: result.rows
	  		});
	  	}
	  });
	});
});

/* 
 * GET All option Rows
 */
router.get('/option', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// select all rows from option
		client.query('SELECT * FROM option', function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		res.json({ 
	  			option: result.rows
	  		});
	  	}
	  });
	});
});

/* 
 * GET All address Rows
 */
router.get('/address', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
 		// select all rows from address
 		client.query("SELECT * FROM address", function(err, result) {
 			//call `done()` to release the client back to the pool
	  	done();

			if(err) {
				return console.error('error running query', err);
			} else {
	  		res.json({ 
	  			address: result.rows
	  		});
			}
		});
 	});
});

/* 
 * GET All person Rows
 */
router.get('/person', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
 		// select all rows from person
 		client.query("SELECT * FROM person", function(err, result) {
 			//call `done()` to release the client back to the pool
	  	done();

			if(err) {
				return console.error('error running query', err);
			} else {
	  		res.json({ 
	  			person: result.rows
	  		});
			}
		});
 	});
});

/* 
 * GET All location Rows
 */
router.get('/location', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select all rows from location
		client.query('SELECT * FROM location', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			location: result.rows
	  		});
			}
		});
	});
});

/* 
 * GET All category Rows
 */
router.get('/category', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select all rows from category
		client.query('SELECT * FROM category', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			location: result.rows
	  		});
			}
		});
	});
});

/* 
 * GET All location_category Rows
 */
router.get('/location_category', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select all rows from location_category
		client.query('SELECT * FROM location_category', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			location_category: result.rows
	  		});
			}
		});
	});
});

/* 
 * GET user row by matching :id
 */
router.get('/user/:id', function(req, res, next) {
	var user_id = req.params.id;

 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select row matching user_id
		client.query('SELECT * FROM users WHERE user_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			user: result.rows
	  		});
			}
		});
	});
});

/* 
 * GET path rows by matching user :id
 */
router.get('/path/:id', function(req, res, next) {
	var user_id = req.params.id;

 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select row matching user_id
		client.query('SELECT * FROM report WHERE user_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			user: result.rows
	  		});
			}
		});
	});
});

/* GET Reportes
 * alphabetically ordered by location name
 */
router.get('/reportes/:uid/:utype', function(req, res, next) {
	var user_id = req.params.uid;
	var user_type = req.params.utype;

	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}

 		var query_config = {};
 		if(user_type == 'specialist'){
 			query_config = {
 				text: 'SELECT * FROM report'
 			};
 		} else if (user_type == 'agent'){
 			query_config = {
	 			text: 'SELECT * FROM report WHERE creator_id = $1',
	 			values: [user_id]
	 		};
 		}
		// select row matching user_id
		client.query(query_config, function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({
					report: result.rows
				});
			}
		});
 	});
});


/* GET Admin Manejar Citas
 * renders citas page with first 20 citas associated with a user
 */
router.get('/citas', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (!username) {
  	user_id = req.session.user_id = '';
    username = req.session.username = '';
    user_type = req.session.user_type = '';
    res.redirect('/');
  } else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			var query_config = {};
	 		if(user_type == 'admin' || user_type == 'specialist') {
	 			// get first 20 citas regardless of creator
	 			query_config = {
	 				text: "SELECT appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location.location_id, location.name AS location_name, report_id, report.name AS report_name, appointments.maker_id, username \
									FROM appointments natural join report \
									LEFT JOIN users ON user_id = maker_id \
									INNER JOIN location ON report.location_id = location.location_id \
									ORDER BY date ASC, time ASC \
									LIMIT 20"
	 			}
	 		} else {
	 			//get first 20 citas created by this user
	 			query_config = {
	 				text: "SELECT appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location.location_id, location.name AS location_name, report_id, report.name AS report_name, appointments.maker_id, username \
									FROM appointments natural join report \
									LEFT JOIN users ON user_id = maker_id \
									INNER JOIN location ON report.location_id = location.location_id \
									WHERE appointments.maker_id = $1 \
									ORDER BY date ASC, time ASC \
									LIMIT 20",
					values: [user_id]
	 			}
	 		}
	 		client.query(query_config, function(err, result) {
		  	//call `done()` to release the client back to the pool
		  	done();
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		var current_user = {
		  			user_id: user_id,
		  			username: username,
		  			user_type: user_type
		  		}
		  		res.render('manejar_citas', { 
		  			title: 'Manejar Citas', 
		  			citas: result.rows,
		  			user: current_user
		  		});
		  	}
		  });
	 	});
	}
});

module.exports = router;