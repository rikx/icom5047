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
 * GET All specialization Rows
 */
router.get('/specialization', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select rows from specialization
		client.query('SELECT * FROM specialization natural join users_specialization WHERE user_id = $1',
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			specialization: result.rows
	  		});
			}
		});
	});
});


/* 
 * GET All users_specialization Rows matching :id
 */
router.get('/users_specialization/:id', function(req, res, next) {
	var user_id = req.params.id;

 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// select rows from users_specialization
		client.query('SELECT spec_id, name FROM users_specialization WHERE user_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			users_specialization: result.rows
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
		client.query('SELECT report_id, option_id, data, sequence FROM path natural join report WHERE creator_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			path: result.rows
	  		});
			}
		});
	});
});

/* GET report rows by matching user :id
 * row amount depends on :utype
 */
router.get('/report/:uid/:utype', function(req, res, next) {
	var user_id = req.params.uid;
	var user_type = req.params.utype;

	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}

 		var query_config = {};
 		if(user_type == 'specialist'){
 			// select all rows from report
 			query_config = {
 				text: 'SELECT * FROM report'
 			};
 		} else if (user_type == 'agent'){
 			// select rows from report matching user_id
 			query_config = {
	 			text: 'SELECT * FROM report WHERE creator_id = $1',
	 			values: [user_id]
	 		};
 		}
		
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


/* GET appointments rows by matching user :id
 * row amount depends on :utype
 */
router.get('/appointments/:uid/:utype', function(req, res, next) {
	var user_id = req.params.uid;
	var user_type = req.params.utype;

	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}

 		var query_config = {};
 		if(user_type == 'specialist'){
 			// select all rows from report
 			query_config = {
 				text: 'SELECT * FROM appointments'
 			};
 		} else if (user_type == 'agent'){
 			// select rows from report matching user_id
 			query_config = {
	 			text: 'SELECT * FROM appointments WHERE creator_id = $1',
	 			values: [user_id]
	 		};
 		}
		
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

module.exports = router;