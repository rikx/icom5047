var express = require('express');
var router = express.Router();

/* Get all data for synchronization
 * http://136.145.116.231:/synchronization
 * TODO modify queries to do comparison last_modified (from each table) > last_sync (from device table) 
 * TODO: add asset_id column to device
 * TODO change last_sync to epoch and add last_modified columns (also epoch)
 */
router.post('/', function(req, res, next){
/*	var id_number = req.body.id_number;
	var sync_type = req.body.sync_type;

	if(sync_type == 'INITIAL'){
		// hay pending device-agent assignment
		// return JSON response object  {sync_status: 1}
		// 1 = success, 0 = fail, -1 = error

		// get current time from new Date() to use as sync_time
	} else if (sync_type == 'FULL'){
		// return user data
		// include JSON response {sync_status: 1, data: {db_data}}
	}
	if(sync_type == 'INC'){
		var user_id = req.body.user_id;
		var user_type = req.body.type;
		var device_data = req.body.data;

		// return in JSON response object  {sync_status: 1, new_data: {db_data where last_modified (from each table) > last_sync (from device table) }}
	}*/

	var user_id = req.body.user_id;
	var user_type = req.body.type;

	var flowchart_rows
		, item_rows
		, option_rows
		, address_rows
		,	person_rows
		, location_rows
		, category_rows
		, location_category_rows
		, users_rows
		, specialization_rows
		, users_specialization_rows
		, path_rows
		, report_rows
		, appointments_rows
	
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// select all rows from flowchart
		client.query('SELECT * FROM flowchart', function(err, result) {
	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		flowchart_rows= result.rows;
	  	}
	  });
		// select all rows from item
		client.query('SELECT * FROM item', function(err, result) {
	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		item_rows= result.rows;
	  	}
	  });
		// select all rows from option
		client.query('SELECT * FROM option', function(err, result) {
	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		option_rows= result.rows;
	  	}
	  });
 		// select all rows from address
 		client.query("SELECT * FROM address", function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		address_rows= result.rows;
			}
		});
 		// select all rows from person
 		client.query("SELECT * FROM person", function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		person_rows= result.rows;
			}
		});
		// select all rows from location
		client.query('SELECT * FROM location', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		location_rows= result.rows;
			}
		});
		// select all rows from category
		client.query('SELECT * FROM category', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		category_rows= result.rows;
			}
		});
		// select all rows from location_category
		client.query('SELECT * FROM location_category', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		location_category_rows= result.rows;
			}
		});
		// select row matching user_id
		client.query('SELECT * FROM users WHERE user_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		users_rows= result.rows;
			}
		});
		// select rows from specialization
		client.query('SELECT spec_id, name FROM specialization natural join users_specialization WHERE user_id = $1',
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		specialization_rows= result.rows;
			}
		});
		// select rows from users_specialization
		client.query('SELECT user_id, spec_id FROM users_specialization WHERE user_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		users_specialization_rows= result.rows;
			}
		});
		// select row matching user_id
		client.query('SELECT report_id, option_id, data, sequence FROM path natural join report WHERE creator_id = $1', 
									[user_id], function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
	  		path_rows= result.rows;
			}
		});


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
			if(err) {
				return console.error('error running query', err);
			} else {
				report_rows= result.rows;
			}
		});

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
				appointments_rows= result.rows;

				// json response with all sync data from db
				res.json({ 
					flowchart: flowchart_rows,
					item: item_rows,
					option: option_rows,
					address: address_rows,
					person: person_rows,
					location: location_rows,
					category: category_rows,
					location_category: location_category_rows,
					users: users_rows,
					specialization: specialization_rows,
					users_specialization: users_specialization_rows,
					path: path_rows,
					report: report_rows,
					appointments: appointments_rows
				});
			}
		});
	});
});




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
 			//call `done()` to release the client back to the pool
	  	done();
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
 			//call `done()` to release the client back to the pool
	  	done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ 
	  			category: result.rows
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
 			//call `done()` to release the client back to the pool
	  	done();
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
 			//call `done()` to release the client back to the pool
	  	done();
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
		client.query('SELECT spec_id, name FROM specialization natural join users_specialization WHERE user_id = $1',
									[user_id], function(err, result) {
			//call `done()` to release the client back to the pool
	  	done();
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
		client.query('SELECT user_id, spec_id FROM users_specialization WHERE user_id = $1', 
									[user_id], function(err, result) {
			//call `done()` to release the client back to the pool
	  	done();
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
			//call `done()` to release the client back to the pool
	  	done();
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
	 			text: 'SELECT * FROM appointments WHERE maker_id = $1',
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
					appointments: result.rows
				});
			}
		});
 	});
});

module.exports = router;