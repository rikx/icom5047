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
		  client.query('SELECT user_id, username, type FROM users WHERE username=$1', [req.body.input_username], function(err, result) {
		  	//call `done()` to release the client back to the pool
		    done();
		    if(err) {
		      return console.error('error running query', err);
		    }
		    if(result.rowCount == 0){
					res.send({user_found: false});
  			} else {
  				var user = result.rows[0];
  				var user_id = req.session.user_id = user.user_id;
  				var username = req.session.username = user.username;
				  var user_type = req.session.user_type = user.type;
				  res.send({redirect: '/users/'+user_type});
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
 		|| !req.body.hasOwnProperty('take_survey_date')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for post report.');
	} else {
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
			if(err) {
		  	return console.error('error fetching client from pool', err);
			}
			// insert new report
		  client.query('INSERT into report (creator_id, location_id, flowchart_id, date_filed) \
										VALUES ($1, $2, $3, $4) \
										RETURNING report_id', 
										[req.body.take_survey_user_id, req.body.take_survey_location_id, req.body.take_survey_id, req.body.take_survey_date], function(err, result) {
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
 	if(!req.body.hasOwnProperty('report_id') || !req.body.hasOwnProperty('option_id') ) {
 		return res.send('Error: Missing fields for post path.');
 	} else {
		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
			if(err) {
		  	return console.error('error fetching client from pool', err);
			}
			// insert new report
		  client.query('INSERT into path (report_id, option_id, data) \
										VALUES ($1, $2, $3)', 
										[req.body.report_id, req.body.option_id, req.body.user_input], function(err, result) {
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

/* GET all ganaderos and users
 * For use in manejar localizaciones dropdowns
 */
router.get('/modify_location_dropdowns', function(req, res, next) {
	var ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// gets all users
	  client.query('SELECT user_id, username \
									FROM users \
									ORDER BY username ASC', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    }
	  });
	  // gets all ganaderos
	  client.query("SELECT person_id, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
									FROM person \
									WHERE person_id NOT IN (SELECT person_id FROM users) \
									ORDER BY person_name ASC;", function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    	res.json({ganaderos : ganaderos_list, usuarios : usuarios_list});
	    }
	  });
	});
});

// SEARCH FUNCTIONS START
/* GET ganaderos search
 * returns ganaderos matching :user_input and their associated information 
 */
router.get('/ganaderos/:user_input', function(req, res, next) {
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
									WHERE person_name like '%"+req.params.user_input+"%'", function(err, result) {
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
									FROM ganaderos, location \
									WHERE person_name like '%"+req.params.user_input+"%' AND (person_id = owner_id OR person_id = manager_id)", function(err, result){
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

/* 
 * GET usuarios 
 * return returns users matching :user_input and their associated information 
 */
router.get('/usuarios', function(req, res, next) {
	var usuarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT user_id, username \
									FROM users \
									ORDER BY username ASC', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    	res.json({usuarios : usuarios_list});
	    }
	  });
	});
});

/* GET all locations
 * returns locations matching :user_input and their associated information 
 */
router.get('/locations', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// query for all locations data
		//WHERE name like '%"+req.query.key+"%'"
		client.query("SELECT location_id, name AS location_name \
									FROM location", function(err, result){
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json({locations: result.rows});
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
									FROM item, option \
									WHERE item_id = $1 AND item_id = parent_id', [element_id], function(err, result) {
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
	var location_info, agentes_list, ganaderos_list;
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
	    	res.json({location: location_info[0], agentes: agentes_list, ganaderos: ganaderos_list});
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
	
	  client.query("SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
									FROM person \
									WHERE person_id NOT IN (SELECT person_id FROM users) \
									ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
									LIMIT 20", function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    }
	  });

	  client.query('WITH ganaderos AS (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
										FROM person \
										WHERE person_id NOT IN (SELECT person_id FROM users) \
										ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
										LIMIT 20) \
									SELECT person_id, location_id, location.name AS location_name \
									FROM ganaderos, location \
									WHERE person_id = owner_id OR person_id = manager_id;', function(err, result){
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

/* NOT USED YET; WILL REQUIRE MODIFICATION: GET Cuestionarios List data 
 * Responds with 10 cuestionarios, 
 * alphabetically ordered by name
 * for use to fill requested page in pagination
 */
router.get('/list_cuestionarios', function(req, res, next) {
	var cuestionarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT flowchart_id, name AS flowchart_name, version, creator_id, username \
									FROM flowchart, users \
									WHERE user_id = creator_id \
									ORDER BY flowchart_name \
									LIMIT 20;', function(err, result) {
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
									FROM (users natural join person) \
									ORDER BY email ASC \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    }
	  });

		// get user specialties
		client.query('WITH usuarios AS (SELECT user_id, email \
								  	FROM users natural join person \
								  	ORDER BY email ASC \
								  	LIMIT 20) \
									SELECT usuarios.user_id, email, us.spec_id, spec.name \
									FROM usuarios \
									LEFT JOIN users_specialization AS us ON us.user_id = usuarios.user_id \
									LEFT JOIN specialization AS spec ON us.spec_id = spec.spec_id ', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				specialties_list = result.rows;
			}
		});

	  // get locations associated with users
	  client.query('WITH usuarios AS (SELECT user_id, email \
										FROM users natural join person \
										ORDER BY email ASC \
										LIMIT 20) \
									SELECT user_id, location_id, location.name AS location_name \
									FROM usuarios, location \
									WHERE user_id = agent_id', function(err, result){
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
									ORDER BY location_name \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	localizaciones_list = result.rows;
	    }
	  });
		// query for location categories
		client.query('WITH locations AS (SELECT location_id, location.name AS location_name, agent_id \
										FROM location \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, locations.location_name, lc.category_id, cat.name \
									FROM locations \
									LEFT JOIN location_category AS lc ON lc.location_id = locations.location_id \
									LEFT JOIN category AS cat ON lc.category_id = cat.category_id', function(err, result){
			if(err) {
				return console.error('error running query', err);
			} else {
				categories_list = result.rows;
			}
		});
	  // query for associated agentes
	  client.query('WITH locations AS (SELECT location_id, location.name AS location_name, agent_id \
										FROM location natural join address \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT location_id, agent_id, username \
									FROM locations,users \
									WHERE user_id = agent_id;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	agentes_list = result.rows;
	    }
	  });
	  // query for associated ganaderos
	  client.query("WITH locations AS (SELECT location_id, location.name AS location_name, owner_id, manager_id \
										FROM location natural join address \
										ORDER BY location_name \
										LIMIT 20) \
										SELECT person_id, location_id,\
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
	    	res.json({localizaciones : localizaciones_list, location_categories: categories_list, agentes : agentes_list, ganaderos : ganaderos_list});
	    }
	  });
	});
});

/* NOT USED YET; WILL REQUIRE MODIFICATION: GET Admin Reportes List data 
 * Responds with 20 reportes, 
 	* alphabetically ordered by location_name
	* for use to fill requested page in pagination
 */
router.get('/list_reportes', function(req, res, next) {
	var reportes_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query('SELECT report_id, report.creator_id, users.username, report.date_filed, report.location_id, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name \
									FROM report INNER JOIN location ON report.location_id = location.location_id \
									INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
									INNER JOIN users ON report.creator_id = user_id \
									ORDER BY location_name ASC \
									LIMIT 20;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	reportes_list = result.rows;
	    	res.json({reportes : reportes_list});
	    }
	  });
	});
});

/* NOT USED YET; WILL REQUIRE MODIFICATION: GET Citas List data 
 * Responds with first 20 citas, 
 * ordered by date and time
 * for use to fill requested page in pagination
 */
router.get('/list_citas', function(req, res, next) {
	var citas_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		
	  client.query("SELECT appointment_id, to_char(date, 'YYYY-MM-DD') AS date, to_char(appointments.time, 'HH24:MI:SS') AS time, purpose, location_id, location.name AS location_name, report_id, agent_id, username \
									FROM (appointments natural join report natural join location), users \
									WHERE user_id = agent_id \
									ORDER BY date ASC, time ASC \
									LIMIT 20;", function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	citas_list = result.rows;
	    	res.json({citas: citas_list});
	    }
	  });
	});
});

/* GET Dispositivos List data 
 * Responds with first 20 dispositivos, 
 * ordered by assigned person
 */
router.get('/list_dispositivos', function(req, res, next) {
	var dispositivos_list, usuarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		
	  client.query('SELECT device_id, devices.name as device_name, latest_sync, devices.user_id as assigned_user, username \
									FROM devices natural join users \
									ORDER BY username ASC \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	dispositivos_list = result.rows;
	    }
	  });

	  // to populate usuario dropdown list
	  client.query('SELECT person_id, email, first_name, middle_initial, last_name1, last_name2, phone_number \
									FROM (users natural join person) \
									ORDER BY email ASC', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    	res.json({dispositivos: dispositivos_list, usuarios: usuarios_list});
	    }
	  });
	});
});

module.exports = router;
