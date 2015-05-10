var express = require('express');
var router = express.Router();

/* 
 * GET home (login) page
 */
router.get('/', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (!username) {
  	user_id = req.session.user_id = '';
    username = req.session.username = '';
    user_type = req.session.user_type = '';
    res.render('index', { title: 'Servicio De Extension Agricola'});
  } else {
  	res.redirect('/users/'+user_type);
  }
});

/* 
 * POST login 
 */
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
		  client.query('SELECT user_id, username, passhash, type FROM users WHERE username=$1', 
		  							[req.body.input_username], function(err, result) {
		    if(err) {
		      return console.error('error running query', err);
		    }
		    if(result.rowCount == 0){
					res.send({user_found: false});
  			} else {
  				var user = result.rows[0];
  				// compare passsword with hash to check match
  				client.query('SELECT (passhash = crypt($1, passhash)) AS pswmatch FROM users WHERE user_id = $2', 
  											[req.body.input_password, user.user_id], function(err, result){
				  	//call `done()` to release the client back to the pool
				    done();
				    if(err) {
				      return console.error('error running query', err);
				    }
				    // pswmatch is true if input_password matches; else false
				    if(result.rows[0].pswmatch){
				    	var user_id = req.session.user_id = user.user_id;
		  				var username = req.session.username = user.username;
						  var user_type = req.session.user_type = user.type;
						  res.send({redirect: '/users'});
				    } else {
				    	res.send({pass_found: false});
				    }
  				});
  			}
		  });
	  });
	}
});

/* 
 * GET logout 
 */
router.get('/logout', function(req, res, next) {
	req.session.destroy(function(err){
		if(err){
			console.log(err);
		}
		else {
			res.json(true);
		}
	});
});

/* POST survey start
 * Creates new report
 */
router.post('/report', function(req, res, next) {
 	if(!req.body.hasOwnProperty('take_survey_user_id')
 		|| !req.body.hasOwnProperty('take_survey_location_id')
 		|| !req.body.hasOwnProperty('take_survey_id')
 		|| !req.body.hasOwnProperty('take_survey_date')
 		|| !req.body.hasOwnProperty('report_name')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for post report.');
	} else {
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
			if(err) {
		  	return console.error('error fetching client from pool', err);
			}
			// insert new report
		  client.query('INSERT into report (creator_id, location_id, flowchart_id, date_filed, name, status) \
										VALUES ($1, $2, $3, $4, $5, $6) \
										RETURNING report_id', 
										[req.body.take_survey_user_id, req.body.take_survey_location_id, req.body.take_survey_id, req.body.take_survey_date, req.body.report_name, 1], function(err, result) {
		  	//call `done()` to release the client back to the pool
		    done();
	    	if(err) {
		      return console.error('error running query', err);
		    } else {
		    	var report_id = req.session.report_id = result.rows[0].report_id;
		    	res.json({report_id: report_id});
		    }
		  });
		});
	}
});

/* POST survey answer
 *
 */
router.post('/cuestionario/path', function(req, res, next) {
 	if(!req.body.hasOwnProperty('report_id') || !req.body.hasOwnProperty('option_id') 
 		||!req.body.hasOwnProperty('user_input') || !req.body.hasOwnProperty('sequence')) {
 		return res.send('Error: Missing fields for post path.');
 	} else {
		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
			if(err) {
		  	return console.error('error fetching client from pool', err);
			}
			// insert new report
		  client.query('INSERT into path (report_id, option_id, data, sequence) VALUES ($1, $2, $3, $4)', 
										[req.body.report_id, req.body.option_id, req.body.user_input, req.body.sequence], function(err, result) {
		  	//call `done()` to release the client back to the pool
		    done();
	    	if(err) {
		      return console.error('error running query', err);
		    } else {
		    	var report_id = req.session.report_id;
		    	res.json({report_id: report_id});
		    }
		  });
		});
 	}
});

// SEARCH FUNCTIONS START
/* GET search ganaderos 
 * returns ganaderos matching :user_input and their associated information 
 */
router.get('/ganaderos/:user_input', function(req, res, next) {
	var user_input = req.params.user_input;
	var ganaderos_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// get ganaderos
	  client.query("SELECT * \
									FROM (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
										FROM person \
										WHERE person_id NOT IN (SELECT person_id FROM users) \
										ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC) as ganaderos \
									WHERE LOWER(person_name) LIKE LOWER('%"+user_input+"%') OR email LIKE '%"+user_input+"%'", function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    }
	  });
	  // get associated locations
	  client.query("WITH ganaderos AS (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
										FROM person \
										WHERE person_id NOT IN (SELECT person_id FROM users) \
										ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC) \
									SELECT person_id, location_id, location.name AS location_name \
									FROM ganaderos INNER JOIN location ON (person_id = owner_id OR person_id = manager_id) \
									WHERE LOWER(person_name) LIKE LOWER('%"+user_input+"%') OR email LIKE '%"+user_input+"%'", function(err, result){
			//call `done()` to release the client back to the pool
			done();
			if(err) {
	      return console.error('error running query', err);
	    } else {
	    	locations_list = result.rows;
	    	// response
				res.json({
					ganaderos: ganaderos_list, 
					locations: locations_list
				});
	    }
		});	
	});
});

/* GET search usuarios 
 * returns users matching :user_input and their associated information 
 */
router.get('/usuarios/:user_input', function(req, res, next) {
	var user_input = req.params.user_input;
	var usuarios_list, specialties_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// get users
	  client.query("SELECT user_id, username \
									FROM users \
									WHERE username LIKE '%"+user_input+"%'\
									ORDER BY username ASC", function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    }
	  });

	  // get user associated specialties
	  client.query("WITH usuarios AS (SELECT user_id, username \
								  	FROM users \
								  	WHERE username LIKE '%"+user_input+"%'\
								  	ORDER BY username ASC) \
									SELECT usuarios.user_id, us.spec_id, spec.name \
									FROM usuarios \
									LEFT JOIN users_specialization AS us ON us.user_id = usuarios.user_id \
									LEFT JOIN specialization AS spec ON us.spec_id = spec.spec_id", function(err, result){
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	specialties_list = result.rows;
	    }
	  });

	  // get user associated locations
	  client.query("WITH usuarios AS (SELECT user_id, username \
								  	FROM users \
								  	WHERE username LIKE '%"+user_input+"%' \
								  	ORDER BY username ASC \
								  SELECT user_id, location_id, location.name AS location_name \
								  FROM usuarios \
								  INNER JOIN location ON user_id = agent_id", function(err, result){
	  	//call `done()` to release the client back to the pool
	    done();
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	locations_list = result.rows;
	    	res.json({
	    		usuarios : usuarios_list,
	    		specialties: specialties_list,
	    		locations: locations_list
	    	});
	    }
	  });
	});
});

/* GET search agents
 * returns agents matching :user_input and their associated information 
 */
router.get('/agents/:user_input', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// get all users 
		client.query("SELECT user_id, username \
									FROM users \
									WHERE type = 'agent' AND username LIKE '%"+req.params.user_input+"%' \
									ORDER BY username", function(err, result){
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json({agents: result.rows});
	    }
		});
	});
});

/* GET search flowcharts
 * returns flowcharts matching :user_input and their basic information
 */
router.get('/cuestionarios/:user_input', function(req, res, next){
	var user_input = req.params.user_input;
	var db = req.db;
	db.connect(req.conString, function(err, client, done){
		if(err) {
		return console.error('error fetching client from pool', err);
		}
		// get all matching flowcharts and their basic data
		client.query("SELECT flowchart_id, name AS flowchart_name, version, creator_id, username \
									FROM flowchart \
									INNER JOIN users ON user_id = creator_id \
									WHERE LOWER(name) LIKE LOWER('%"+user_input+"%') AND flowchart.status != $1 \
									ORDER BY flowchart_name", [-1], function(err, result){
		if(err) {
      return console.error('error running query', err);
    } else {
    	res.json({cuestionarios: result.rows});
    }
		});
	})
});

/* GET search take survey flowcharts
 * returns flowcharts matching :user_input and their basic information
 */
router.get('/cuestionarios/take/:user_input', function(req, res, next){
	var user_input = req.params.user_input;
	var db = req.db;
	db.connect(req.conString, function(err, client, done){
		if(err) {
		return console.error('error fetching client from pool', err);
		}
		// get all matching flowcharts and their basic data
		client.query("SELECT flowchart_id, name AS flowchart_name, version, creator_id, username \
									FROM flowchart \
									INNER JOIN users ON user_id = creator_id \
									WHERE LOWER(name) LIKE LOWER('%"+user_input+"%') AND flowchart.status = $1\
									ORDER BY flowchart_name", [1], function(err, result){
		if(err) {
      return console.error('error running query', err);
    } else {
    	res.json({cuestionarios: result.rows});
    }
		});
	})
});

/* GET search reportes
 * returns reports matching :user_input and their basic information
 * query results depend on logged in user
 */
router.get('/reportes/:user_input', function(req, res, next){
	var user_input = req.params.user_input;

	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		var query_config = {};
 		if(user_type == 'admin' || user_type == 'specialist') {
 			// get first 20 reports regardles of creator
 			query_config = {
				text: "SELECT report_id, report.creator_id, users.username, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date, report.location_id, report.name as report_name, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name \
								FROM report INNER JOIN location ON report.location_id = location.location_id \
								INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
								INNER JOIN users ON report.creator_id = user_id \
								WHERE LOWER(report.name) LIKE LOWER('%"+user_input+"%') OR users.username LIKE '%"+user_input+"%' OR LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR to_char(report.date_filed, 'DD/MM/YYYY') LIKE '%"+user_input+"%' \
					 			ORDER BY report_name ASC"
			}
 		} else {
 			// get first 20 reports created by this user
			query_config = {
				text: "SELECT report_id, report.creator_id, users.username, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date, report.location_id, report.name as report_name, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name \
								FROM report INNER JOIN location ON report.location_id = location.location_id \
								INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
								INNER JOIN users ON report.creator_id = user_id \
								WHERE report.creator_id = $1 AND (LOWER(report.name) LIKE LOWER('%"+user_input+"%') OR users.username LIKE '%"+user_input+"%' OR LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR to_char(report.date_filed, 'DD/MM/YYYY') LIKE '%"+user_input+"%') \
					 			ORDER BY report_name ASC",
				values: [user_id]
			};
	 	}
 		// get reports matching user_type
 		client.query(query_config, function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({reports: result.rows});
			}
		});
 	});
});

/* GET search citas
 * returns appointments matching :user_input and their basic information
 * query results depend on logged in user
 */
router.get('/citas/:user_input', function(req, res, next) {
	var user_input = req.params.user_input;

	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		var query_config = {};
 		if(user_type == 'admin' || user_type == 'specialist') {
 			// get first 20 citas regardless of creator
 			query_config = {
 				text: "SELECT appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location.location_id, location.name AS location_name, appointments.report_id, report.name AS report_name, appointments.maker_id, username \
								FROM appointments INNER JOIN report ON appointments.report_id = report.report_id \
								LEFT JOIN users ON user_id = maker_id \
								INNER JOIN location ON report.location_id = location.location_id \
								WHERE appointments.status != $1 AND (LOWER(report.name) LIKE LOWER('%"+user_input+"%') OR LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR to_char(appointments.date, 'DD/MM/YYYY') LIKE '%"+user_input+"%' OR to_char(appointments.time, 'HH12:MI AM') LIKE '%"+user_input+"%') \
								ORDER BY location.name ASC, date ASC, time ASC",
				values: [-1]
 			}
 		} else {
 			//get first 20 citas created by this user
 			query_config = {
 				text: "SELECT appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location.location_id, location.name AS location_name, appointments.report_id, report.name AS report_name, appointments.maker_id, username \
								FROM appointments INNER JOIN report ON appointments.report_id = report.report_id \
								LEFT JOIN users ON user_id = maker_id \
								INNER JOIN location ON report.location_id = location.location_id \
								WHERE appointments.maker_id = $1 AND appointments.status != $2 AND (LOWER(report.name) LIKE LOWER('%"+user_input+"%') OR LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR to_char(appointments.date, 'DD/MM/YYYY') LIKE '%"+user_input+"%' OR to_char(appointments.time, 'HH12:MI AM') LIKE '%"+user_input+"%') \
								ORDER BY location.name ASC, date ASC, time ASC",
				values: [user_id, -1]
 			}
 		}
 		client.query(query_config, function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();
	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		res.json({citas: result.rows});
	  	}
	  });
 	});
});

/* TODO: GET search locations
 * returns locations matching :user_input and their associated information 
 */
router.get('/localizaciones/:user_input', function(req, res, next) {
	var user_input = req.params.user_input;

	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (!username) {
  	user_id = req.session.user_id = '';
    username = req.session.username = '';
    user_type = req.session.user_type = '';
    res.redirect('/');
  } else {
	 	var localizaciones_list, categories_list, all_categories, agentes_list, ganaderos_list;
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// query for location data
			client.query("SELECT location.location_id, location.name AS location_name, location.address_id, license, address_line1, address_line2, city, zipcode \
										FROM location INNER JOIN address ON location.address_id = address.address_id \
										WHERE LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR location.license LIKE '%"+user_input+"%' \
										ORDER BY location_name", function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {
					localizaciones_list = result.rows;
				}
			});

			// query for location categories
			client.query("WITH locations AS (SELECT location.location_id, location.name AS location_name, agent_id \
											FROM location \
											WHERE LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR location.license LIKE '%"+user_input+"%' \
											ORDER BY location_name) \
										SELECT locations.location_id, locations.location_name, lc.category_id, cat.name \
										FROM locations \
										LEFT JOIN location_category AS lc ON lc.location_id = locations.location_id \
										LEFT JOIN category AS cat ON lc.category_id = cat.category_id", function(err, result){
				if(err) {
					return console.error('error running query', err);
				} else {
					categories_list = result.rows;
				}
			});

		  // query for associated agentes
		  client.query("WITH locations AS (SELECT location.location_id, location.name AS location_name, agent_id \
									  	FROM location \
									  	WHERE LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR location.license LIKE '%"+user_input+"%' \
									  	ORDER BY location_name) \
									  SELECT locations.location_id, agent_id, username \
									  FROM locations,users \
									  WHERE user_id = agent_id", function(err, result) {
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		agentes_list = result.rows;
		  	}
		  });

		  // query for associated ganaderos
		  client.query("WITH locations AS (SELECT location_id, location.name AS location_name, owner_id, manager_id \
									  	FROM location \
									  	WHERE LOWER(location.name) LIKE LOWER('%"+user_input+"%') OR location.license LIKE '%"+user_input+"%' \
									  	ORDER BY location_name) \
									  SELECT person_id, locations.location_id,\
									  CASE WHEN person_id = owner_id THEN 'owner' \
									  WHEN person_id = manager_id THEN 'manager' \
									  END AS relation_type, \
									  (first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as person_name \
									  FROM locations, person \
									  WHERE person_id = owner_id or person_id = manager_id", function(err, result) {
		  	//call `done()` to release the client back to the pool
		  	done();

		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		ganaderos_list = result.rows; 		
		  		res.json({
		  			locations: localizaciones_list, 
		  			location_categories: categories_list, 
		  			agents: agentes_list, 
		  			ganaderos: ganaderos_list, 
		  		});
		  	}
		  });
		});
	}
});

/* GET search dispositivos
 * returns devices matching :user_input and their associated information 
 */
router.get('/dispositivos/:user_input', function(req, res, next) {
	var dispositivos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// get devices and their assigned user (if any)
	  client.query("SELECT device_id, devices.name as device_name, id_number, to_char(latest_sync, 'DD/MM/YYYY @ HH12:MI PM') AS last_sync, devices.user_id as assigned_user, username \
									FROM devices LEFT JOIN users ON devices.user_id = users.user_id \
									WHERE id_number LIKE '%"+req.params.user_input+"%' OR username LIKE '%"+req.params.user_input+"%' OR LOWER(name) LIKE LOWER('%"+req.params.user_input+"%') \
									ORDER BY username ASC ", function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	dispositivos_list = result.rows;
	    	res.json({devices: dispositivos_list});
	    }
	  });
	});
});

// SEARCH FUNCTIONS END

/* GET flowchart element family 
 * returns family info of element matching :id
 */
router.get('/element/:id', function(req, res, next) {
	var element_id = req.params.id
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT item_id, item.label AS question, type AS item_type, option_id, option.label AS answer, next_id \
									FROM item INNER JOIN option ON item_id = parent_id \
									WHERE item_id = $1', [element_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json({question_family : result.rows});
	    }
	  });
	});
});

/* GET location
 * returns location information matching :id
 */
router.get('/location/:id', function(req, res, next) {
	var location_id = req.params.id;
	var location_info, categories_list, agentes_list, ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// query for location data
	  client.query('SELECT location.location_id, location.name AS location_name, cat.name AS location_category, location.address_id, license, address_line1, address_line2, city, zipcode \
									FROM location INNER JOIN address ON location.address_id = address.address_id \
									LEFT JOIN location_category AS lc ON lc.location_id = location.location_id \
									LEFT JOIN category AS cat ON lc.category_id = cat.category_id \
									WHERE location.location_id = $1', [location_id], 
									function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	location_info = result.rows;
	    }
	  });
  	// query for location categories
		client.query('SELECT location.location_id, location.name AS location_name, lc.category_id, cat.name \
									FROM location \
									LEFT JOIN location_category AS lc ON lc.location_id = location.location_id \
									LEFT JOIN category AS cat ON lc.category_id = cat.category_id \
									WHERE location.location_id = $1', [location_id],  function(err, result){
			if(err) {
				return console.error('error running query', err);
			} else {
				categories_list = result.rows;
			}
		});
	  // query for associated agentes
	  client.query('WITH locations AS (SELECT location_id, location.name AS location_name, agent_id, location.name AS location_name \
										FROM location natural join address \
										WHERE location_id = $1) \
									SELECT location_id, agent_id, username \
									FROM locations,users \
									WHERE user_id = agent_id;', [location_id], function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	agentes_list = result.rows;
	    }
	  });
	  // query for associated ganaderos
	  client.query("WITH locations AS (SELECT location_id, location.name AS location_name, owner_id, manager_id \
										FROM location natural join address \
										WHERE location_id = $1) \
										SELECT person_id, location_id,\
											CASE WHEN person_id = owner_id THEN 'owner' \
											WHEN person_id = manager_id THEN 'manager' \
											END AS relation_type, \
											(first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as person_name \
										FROM locations, person \
										WHERE person_id = owner_id or person_id = manager_id", [location_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    	res.json({location: location_info[0], 
	    		categories: categories_list,
	    		agentes: agentes_list, 
	    		ganaderos: ganaderos_list
	    	});
	    }
	  });
	});
});

/* GET location categories where :id matches location_id */
router.get('/location/:id/categories', function(req, res, next){
	var location_id = req.params.id;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT category.category_id, category.name AS category_name \
									FROM location \
									INNER JOIN location_category ON location.location_id = location_category.location_id \
									INNER JOIN category ON location_category.category_id = category.category_id \
									WHERE location_id = $1', 
									[location_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json({location_categories: result.rows});
	    }
	  });
	});
});

/* GET Ganaderos List data 
 * Responds with first 20 ganaderos, alphabetically ordered 
 */
router.get('/list_ganaderos', function(req, res, next) {
	var ganaderos_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// ganaderos
	  client.query("SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
									FROM person \
									WHERE person.status != $1 AND person_id NOT IN (SELECT person_id FROM users) \
									ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
									LIMIT 20", [-1], function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    }
	  });

	  // locations ganadero is associated with
	  client.query('WITH ganaderos AS (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
										FROM person \
										WHERE person.status != $1 AND person_id NOT IN (SELECT person_id FROM users) \
										ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
										LIMIT 20) \
									SELECT person_id, location_id, location.name AS location_name \
									FROM ganaderos INNER JOIN location ON (person_id = owner_id OR person_id = manager_id)', [-1], function(err, result){
			//call `done()` to release the client back to the pool
			done();
			if(err) {
	      return console.error('error running query', err);
	    } else {
	    	locations_list = result.rows;
	    	// response
				res.json({ganaderos: ganaderos_list, locations: locations_list});
	    }
		});	
	});
});

/* GET Cuestionarios List data 
 * Responds with 10 cuestionarios, 
 * alphabetically ordered by name
 */
router.get('/list_cuestionarios', function(req, res, next) {
	var cuestionarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT flowchart_id, name AS flowchart_name, version, creator_id, username \
									FROM flowchart \
									INNER JOIN users ON user_id = creator_id \
									WHERE flowchart.status != $1 \
						 			ORDER BY flowchart_name \
						 			LIMIT 20', [-1], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	cuestionarios_list = result.rows;
	    	res.json({cuestionarios: cuestionarios_list});
	    }
	  });
	});
});

/* GET Cuestionarios List data 
 * Responds with 10 cuestionarios, 
 * alphabetically ordered by name
 */
router.get('/list_tomar_cuestionarios', function(req, res, next) {
	var cuestionarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT flowchart_id, name AS flowchart_name, version, creator_id, username \
									FROM flowchart \
									INNER JOIN users ON user_id = creator_id \
									WHERE flowchart.status = $1 \
						 			ORDER BY flowchart_name \
						 			LIMIT 20', [1], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	cuestionarios_list = result.rows;
	    	res.json({cuestionarios: cuestionarios_list});
	    }
	  });
	});
});

/* GET Usuarios List data 
 * Responds with first 20 usuarios, alphabetically ordered 
 */
router.get('/list_usuarios', function(req, res, next) {
	var usuarios_list, specialties_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query('SELECT user_id, email, first_name, middle_initial, last_name1, last_name2, phone_number, username, type \
									FROM person INNER JOIN users ON users.person_id=person.person_id \
									WHERE users.status != $1 \
									ORDER BY username \
									LIMIT 20', [-1],  function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    }
	  });

		// get user specialties
		client.query('WITH usuarios AS (SELECT user_id, username \
								  	FROM users \
								  	WHERE users.status != $1 \
								  	ORDER BY username ASC \
								  	LIMIT 20) \
									SELECT usuarios.user_id, us.spec_id, spec.name \
									FROM usuarios \
									LEFT JOIN users_specialization AS us ON us.user_id = usuarios.user_id \
									LEFT JOIN specialization AS spec ON us.spec_id = spec.spec_id', [-1],  function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				specialties_list = result.rows;
			}
		});

	  // get locations associated with users
	  client.query('WITH usuarios AS (SELECT user_id, username \
								  	FROM users \
								  	WHERE users.status != $1 \
								  	ORDER BY username ASC \
								  	LIMIT 20) \
								  SELECT user_id, location_id, location.name AS location_name \
								  FROM usuarios \
								  INNER JOIN location ON user_id = agent_id', [-1],  function(err, result){
	  	//call `done()` to release the client back to the pool
	  	done();
	  	if(err) {

	  	} else {
	  		locations_list = result.rows;
	    	res.json({usuarios : usuarios_list, user_specialties: specialties_list, locations : locations_list});
	  	}
	  });
	});
});

/* GET Localizaciones List data 
 * Responds with first 20 localizaciones, 
 * alphabetically ordered by location_name
 */
router.get('/list_localizaciones', function(req, res, next) {
	var localizaciones_list, categories_list, agentes_list, ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// query for location data
	  client.query('SELECT location.location_id, location.name AS location_name, location.address_id, license, address_line1, address_line2, city, zipcode \
									FROM location INNER JOIN address ON location.address_id = address.address_id \
									WHERE location.status != $1 \
									ORDER BY location_name \
									LIMIT 20;',[-1], function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	localizaciones_list = result.rows;
	    }
	  });
		// query for location categories
		client.query('WITH locations AS (SELECT location_id, location.name AS location_name, agent_id \
										FROM location \
										WHERE location.status != $1 \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, locations.location_name, lc.category_id, cat.name \
									FROM locations \
									LEFT JOIN location_category AS lc ON lc.location_id = locations.location_id \
									LEFT JOIN category AS cat ON lc.category_id = cat.category_id',[-1], function(err, result){
			if(err) {
				return console.error('error running query', err);
			} else {
				categories_list = result.rows;
			}
		});
	  // query for associated agentes
	  client.query('WITH locations AS (SELECT location.location_id, location.name AS location_name, agent_id \
										FROM location \
										WHERE location.status != $1 \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, agent_id, username \
									FROM locations,users \
									WHERE user_id = agent_id;', [-1], function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	agentes_list = result.rows;
	    }
	  });
	  // query for associated ganaderos
	  client.query("WITH locations AS (SELECT location.location_id, location.name AS location_name, owner_id, manager_id \
										FROM location \
										WHERE location.status != $1 \
										ORDER BY location_name \
										LIMIT 20) \
										SELECT person_id, locations.location_id,\
											CASE WHEN person_id = owner_id THEN 'owner' \
											WHEN person_id = manager_id THEN 'manager' \
											END AS relation_type, \
											(first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as person_name \
										FROM locations, person \
										WHERE person_id = owner_id or person_id = manager_id",[-1], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    	res.json({localizaciones : localizaciones_list, 
	    		location_categories: categories_list, 
	    		agentes : agentes_list, ganaderos :
	    		ganaderos_list
	    	});
	    }
	  });
	});
});

/* GET Reportes List data 
 * Responds with 20 reportes, 
 * alphabetically ordered by location_name
 */
router.get('/list_reportes', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		var query_config = {};
 		if(user_type == 'admin' || user_type == 'specialist') {
 			// get first 20 reports regardles of creator
 			query_config = {
				text: "SELECT report_id, report.creator_id, users.username, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date, report.location_id, report.name as report_name, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name \
								FROM report INNER JOIN location ON report.location_id = location.location_id \
								INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
								INNER JOIN users ON report.creator_id = user_id \
								WHERE report.status != $1 \
					 			ORDER BY report_name ASC \
								LIMIT 20",
				values: [-1]
			}
 		} else {
 			// get first 20 reports created by this user
			query_config = {
				text: "SELECT report_id, report.creator_id, users.username, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date, report.location_id, report.name as report_name, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name \
								FROM report INNER JOIN location ON report.location_id = location.location_id \
								INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
								INNER JOIN users ON report.creator_id = user_id \
								WHERE report.creator_id = $1 \
								WHERE report.status != $1 \
					 			ORDER BY report_name ASC \
								LIMIT 20",
				values: [user_id, -1]
			};
	 	}
 		// get reports 
 		client.query(query_config, function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json({ reports: result.rows});
			}
		});
 	});
});

/* GET Citas List data 
 * Responds with first 20 citas, 
 * ordered by date and time
 */
router.get('/list_citas', function(req, res, next) {
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
	 				text: "SELECT appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location.location_id, location.name AS location_name, appointments.report_id, report.name AS report_name, appointments.maker_id, username \
									FROM appointments INNER JOIN report ON appointments.report_id = report.report_id \
									LEFT JOIN users ON user_id = maker_id \
									INNER JOIN location ON report.location_id = location.location_id \
									WHERE appointments.status != $1 \
									ORDER BY location.name ASC, date ASC, time ASC \
									LIMIT 20",
					values: [-1]
	 			}
	 		} else {
	 			//get first 20 citas created by this user
	 			query_config = {
	 				text: "SELECT appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location.location_id, location.name AS location_name, appointments.report_id, report.name AS report_name, appointments.maker_id, username \
									FROM appointments INNER JOIN report ON appointments.report_id = report.report_id \
									LEFT JOIN users ON user_id = maker_id \
									INNER JOIN location ON report.location_id = location.location_id \
									WHERE appointments.maker_id = $1 AND appointments.status != $2 \
									ORDER BY location.name ASC, date ASC, time ASC \
									LIMIT 20",
					values: [user_id, -1]
	 			}
	 		}
	 		client.query(query_config, function(err, result) {
		  	//call `done()` to release the client back to the pool
		  	done();
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		res.json({citas: result.rows});
		  	}
		  });
	 	});
	}
});

/* GET Dispositivos List data 
 * Responds with first 20 dispositivos, 
 * ordered by device name
 */
router.get('/list_dispositivos', function(req, res, next) {
	var dispositivos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// get devices and their assigned user (if any)
	  client.query("SELECT device_id, devices.name as device_name, id_number, to_char(latest_sync, 'DD/MM/YYYY @ HH12:MI PM') AS last_sync, devices.user_id as assigned_user, username \
									FROM devices LEFT JOIN users ON devices.user_id = users.user_id \
									ORDER BY devices.name ASC \
									LIMIT 20", function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	dispositivos_list = result.rows;
	    	res.json({dispositivos: dispositivos_list});
	    }
	  });
	});
});

/* GET categories list data 
 * Responds with all categories
 * ordered by name
 */
router.get('/list_categories', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// get categories
	  client.query("SELECT * FROM category ORDER BY name", function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json({categories: result.rows});
	    }
	  });
	});
});

/* GET specialties list data 
 * Responds with all specializations, 
 * ordered by name
 */
router.get('/list_specialties', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// get specialties
	  client.query("SELECT * FROM specialization ORDER BY name", function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json({specialties: result.rows});
	    }
	  });
	});
});

module.exports = router;
