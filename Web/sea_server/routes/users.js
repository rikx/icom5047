var express = require('express');
var router = express.Router();

/* GET users listing. 
 * TODO: make this route redirect to the proper home page
 * TODO: depending on the currently signed in user type
 */
router.get('/', function(req, res, next) {
  res.redirect('/users/admin');
});

/* GET Tomar Cuestionario 
 * Responds with first 20 cuestionarios, 
 * alphabetically ordered by name
 */
router.get('/cuestionarios', function(req, res, next) {
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
	    	res.render('tomar_cuestionarios', { title: 'Cuestionarios', cuestionarios: result.rows});
	    }
	  });
	});
});

/* GET Tomar Cuestionario Metodo Flujo
 * Responds with take survey page for matching survey :id
 */
router.get('/cuestionarios/flow/:id', function(req, res, next) {
	var cuestionario_id = req.params.id;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	  client.query('SELECT flowchart.flowchart_id, flowchart.name AS flowchart_name, item_id, item.label AS question, type AS item_type, option_id, option.label AS answer, next_id \
									FROM flowchart, item, option \
									WHERE flowchart.flowchart_id = $1 AND item_id = first_id AND first_id = parent_id', [cuestionario_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.render('cuestionario_flujo', { title: 'Cuestionario con Flujo', pregunta: result.rows});
	    }
	  });
	});
});

//TODO: add :id or :username after /admin
//TODO: fix it so you cant access it directly (with sesions)
/* GET Admin Home. */
router.get('/admin', function(req, res, next) {
  res.render('admin', { title: 'Admin Home'});
});

/* GET Admin Manejar Cuestionarios
 * Responds with first 10 cuestionarios, 
 * alphabetically ordered by name
 */
router.get('/admin/cuestionarios', function(req, res, next) {
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
	    	res.render('manejar_cuestionarios', { title: 'Manejar Cuestionarios', cuestionarios: result.rows});
	    }
	  });
	});
});

/* GET Admin Crear Cuestionario */
router.get('/admin/cuestionarios/crear', function(req, res, next) {
	res.render('crear_cuestionario', { title: 'Crear Cuestionario'});
});

/* GET Admin Cuestionario */
router.get('/admin/cuestionarios/:id', function(req, res, next) {
	var cuestionario_id = req.params.id;
	res.render('cuestionario', { title: 'Cuestionario'});
});

/* NOT USED YET; WILL REQUIRE MODIFICATION: GET Cuestionarios List data 
 * Responds with 10 cuestionarios, 
 * alphabetically ordered by name
 * for use to fill requested page in pagination
 */
router.get('/admin/list_cuestionarios', function(req, res, next) {
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

/* GET Admin Manejar Ganaderos */
router.get('/admin/ganaderos', function(req, res, next) {
	var ganaderos_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	
	  client.query('SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
									FROM person \
									WHERE person_id NOT IN (SELECT person_id FROM users) \
									ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
									LIMIT 20;', function(err, result) {
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
	    	res.render('manejar_ganaderos', { title: 'Manejar Ganaderos', ganaderos: ganaderos_list, locations: locations_list});
	    }
		});	
	});
});

/* GET Ganaderos List data 
 * Responds with first 10 ganaderos, alphabetically ordered 
 */
router.get('/admin/list_ganaderos', function(req, res, next) {
	var ganaderos_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
	
	  client.query('SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
									FROM person \
									WHERE person_id NOT IN (SELECT person_id FROM users) \
									ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
									LIMIT 20;', function(err, result) {
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

/* POST Admin Manejar Ganaderos
 * Add new ganadero to database
 */
router.post('/admin/ganaderos', function(req, res, next) {
	if(!req.body.hasOwnProperty('ganadero_name') || !req.body.hasOwnProperty('ganadero_apellido1')
			|| !req.body.hasOwnProperty('ganadero_apellido2') || !req.body.hasOwnProperty('ganadero_email')
			|| !req.body.hasOwnProperty('ganadero_telefono') || !req.body.hasOwnProperty('ganadero_m_initial')) {
  	res.statusCode = 400;
  	return res.send('Error: Missing fields for post ganadero.');
	} else {
		var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	    	return console.error('error fetching client from pool', err);
	  	}
	  	// Verify ganadero does not already exist in db
	  	client.query("SELECT email FROM person WHERE email = $1 OR phone_number = $2", [req.body.ganadero_email, req.body.ganadero_telefono], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
		  			res.send({exists: true});
		  		} else {
		  			// Insert new ganadero into db
						client.query("INSERT into person (first_name, middle_initial, last_name1, last_name2, email, phone_number) \
													VALUES ($1, $2, $3, $4, $5, $6)", 
													[req.body.ganadero_name, req.body.ganadero_m_initial, req.body.ganadero_apellido1, req.body.ganadero_apellido2, req.body.ganadero_email, req.body.ganadero_telefono] , function(err, result) {
							//call `done()` to release the client back to the pool
						  done();
						  if(err) {
						    return console.error('error running query', err);
						  } else {
						    res.json(true);
						  }
						});
		  		}
	  		}
	  	});
		});
	}
});


/* POST Admin Manejar Dispositivo
 * Add new ganadero to database
 */
router.post('/admin/dispositivos', function(req, res, next) {
	console.log("server executed!!");
	console.log(req.body);
	if(!req.body.hasOwnProperty('dispositivo_name') || !req.body.hasOwnProperty('dispositivo_id_num')) {
  	res.statusCode = 400;
  	return res.send('Error: Missing fields for post ganadero.');
	} else {

	
		var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	    	return console.error('error fetching client from pool', err);
	  	}
	  	// Verify ganadero does not already exist in db
	  	client.query("SELECT * FROM devices WHERE id_number = $1", [req.body.dispositivo_id_num], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
		  			res.send({exists: true});
		  		} else {
		  			// Insert new ganadero into db
						client.query("INSERT into devices (name, id_number, user_id) \
													VALUES ($1, $2, $3)", 
													[req.body.dispositivo_name, req.body.dispositivo_id_num, req.body.dispositivo_id_usuario] , function(err, result) {
							//call `done()` to release the client back to the pool
						  done();
						  if(err) {
						    return console.error('error running query', err);
						  } else {
						    res.json(true);
						  }
						});
		  		}
	  		}
	  	});
		});
	}
});

/* PUT Admin Manejar Ganaderos 
 * Edit ganadero matching :id in database
 */
router.put('/admin/ganaderos/:id', function(req, res, next) {
	var ganadero_id = req.params.id;
	if(!req.body.hasOwnProperty('ganadero_name') || !req.body.hasOwnProperty('ganadero_apellido1')
			|| !req.body.hasOwnProperty('ganadero_apellido2') || !req.body.hasOwnProperty('ganadero_email')
			|| !req.body.hasOwnProperty('ganadero_telefono') || !req.body.hasOwnProperty('ganadero_m_initial')) {
  	res.statusCode = 400;
  	return res.send('Error: Missing fields for put ganadero.');
	} else {
		var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	    	return console.error('error fetching client from pool', err);
	  	}
			// Insert new ganadero into db
			client.query("UPDATE person SET first_name = $1, middle_initial = $2, last_name1 = $3, last_name2 = $4, email = $5, phone_number = $6 \
										WHERE person_id = $7", 
										[req.body.ganadero_name, req.body.ganadero_m_initial, req.body.ganadero_apellido1, req.body.ganadero_apellido2, req.body.ganadero_email, req.body.ganadero_telefono, ganadero_id] , function(err, result) {
				//call `done()` to release the client back to the pool
			  done();
			  if(err) {
			    return console.error('error running query', err);
			  } else {
			    res.json(true);
			  }
			});
		});
	}
});

/* GET Admin Manejar Reportes 
 * Renders page and includes response with first 10 reportes, 
 * alphabetically ordered by location name
 */
router.get('/admin/reportes', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {

		}
		client.query("SELECT report_id, report.creator_id, users.username, report.date_filed, report.location_id, location.name AS location_name, \
										subject_id, (person.first_name||' '||person.last_name1||' '||person.last_name2) AS ganadero_name, \
										report.flowchart_id, flowchart.name AS flowchart_name \
									FROM report, location, person, flowchart, users \
									WHERE report.creator_id = user_id and subject_id = person.person_id and report.location_id = location.location_id \
									ORDER BY location_name ASC \
									LIMIT 20;", function(err, result) {
			done();

			if(err) {
				return console.error('error running query', err);
			} else {
				res.render('manejar_reportes', { title: 'Manejar Reportes', reports: result.rows});
			}
		});
	});
});

/* NOT USED YET; WILL REQUIRE MODIFICATION: GET Admin Reportes List data 
 * Responds with 10 reportes, 
 	* alphabetically ordered by location_name
	* for use to fill requested page in pagination
 */
router.get('/admin/list_reportes', function(req, res, next) {
	var reportes_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query(' \
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

/* GET Admin Ver Reporte */
router.get('/admin/reportes/:id', function(req, res, next) {
	var report_id = req.params.id;
	var reporte_details;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: Falta join con path para determinar recomendacion a desplegar
	  client.query("SELECT report.report_id, report.creator_id, username AS report_creator, report.location_id, location.name AS location_name, report.flowchart_id, flowchart.name AS survey_name, \
									flowchart.version AS survey_version, report.note, report.date_filed AS report_date, appointment_id, appointments.date AS appointment_date, appointments.time AS appointment_time, appointments.purpose AS appointment_purpose \
									FROM report, appointments, users, flowchart, person, location \
									WHERE report.report_id =$1 AND report.report_id = appointments.report_id \
									AND users.user_id = report.creator_id AND report.flowchart_id = flowchart.flowchart_id \
									AND report.location_id = location.location_id", [report_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	reporte_details = result.rows[0];
	    	res.render('reporte', { title: 'Reporte', reporte: reporte_details});
	    }
	  });
	});
});


/* GET Admin Manejar Usuarios 
 * Renders page with first 20 usuarios, alphabetically ordered 
 */
router.get('/admin/usuarios', function(req, res, next) {
	var usuarios_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query('SELECT user_id, email, first_name, middle_initial, last_name1, last_name2, phone_number, person.spec_id, specialization.name AS specialization_name \
									FROM (users natural join person) LEFT OUTER JOIN specialization \
									ON person.spec_id = specialization.spec_id \
									ORDER BY email ASC \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
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
	  		return console.error('error running query', err);
	  	} else {
	  		locations_list = result.rows;
	  		res.render('manejar_usuarios', { title: 'Manejar Usuarios', usuarios : usuarios_list, locations : locations_list});
	  	}
	  });
	});
});

/* GET Usuarios List data 
 * Responds with first 20 usuarios, alphabetically ordered 
 */
router.get('/admin/list_usuarios', function(req, res, next) {
	var usuarios_list, locations_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query('SELECT user_id, email, first_name, middle_initial, last_name1, last_name2, phone_number, person.spec_id, specialization.name AS specialization_name \
									FROM (users natural join person) LEFT OUTER JOIN specialization \
									ON person.spec_id = specialization.spec_id \
									ORDER BY email ASC \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
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
	    	res.json({usuarios : usuarios_list, locations : locations_list});
	  	}
	  });
	});
});

/* GET Admin Manejar Localizaciones */
router.get('/admin/localizaciones', function(req, res, next) {
	var localizaciones_list, agentes_list, ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// query for location data
	  client.query('SELECT location_id, location.name AS location_name, address_id, license, address_line1, address_line2, city, zipcode \
									FROM location natural join address \
									ORDER BY location_name \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	localizaciones_list = result.rows;
	    }
	  });
	  // query for associated agentes
	  client.query('WITH locations AS (SELECT location_id, location.name AS location_name, agent_id, location.name AS location_name \
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
	    	res.render('manejar_localizaciones', { title: 'Manejar Localizaciones', localizaciones: localizaciones_list, agentes: agentes_list, ganaderos: ganaderos_list});
	    }
	  });
	});
});

/* GET Localizaciones List data 
 * Responds with first 10 localizaciones, 
 * alphabetically ordered by location_name
 */
router.get('/admin/list_localizaciones', function(req, res, next) {
	var localizaciones_list, agentes_list, ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// query for location data
	  client.query('SELECT location_id, location.name AS location_name, address_id, license, address_line1, address_line2, city, zipcode \
									FROM location natural join address \
									ORDER BY location_name \
									LIMIT 20;', function(err, result) {
    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	localizaciones_list = result.rows;
	    }
	  });
	  // query for associated agentes
	  client.query('WITH locations AS (SELECT location_id, location.name AS location_name, agent_id, location.name AS location_name \
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
	    	res.json({localizaciones : localizaciones_list, agentes : agentes_list, ganaderos : ganaderos_list});
	    }
	  });
	});
});

/* */
router.post('/admin/localizaciones', function(req, res, next) {
	if(!req.body.hasOwnProperty('localizacion_name') || !req.body.hasOwnProperty('localizacion_license')
			|| !req.body.hasOwnProperty('localizacion_address_line1') || !req.body.hasOwnProperty('localizacion_address_line2')
			|| !req.body.hasOwnProperty('localizacion_address_city') || !req.body.hasOwnProperty('localizacion_address_zipcode')) {
  	res.statusCode = 400;
  	return res.send('Error: Missing fields for post location.');
	} else {
		var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	    	return console.error('error fetching client from pool', err);
	  	}
	  	// Verify location does not already exist in db
	  	client.query("SELECT license FROM location WHERE license = $1", 
	  								[req.body.localizacion_license], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
		  			res.send({exists: true}); 
		  		} else {
		  			// Insert new location into db
						client.query("INSERT into address (address_line1, address_line2, city, zipcode) VALUES ($1, $2, $3, $4) RETURNING address_id", 
													[req.body.localizacion_name, req.body.localizacion_license, req.body.localizacion_address_city, req.body.localizacion_address_zipcode] , function(err, result) {
						  if(err) {
						    return console.error('error running query', err);
						  } else {
						  	var address_id = result.rows[0].address_id;
						  	client.query("INSERT into location (name, license, address_id) VALUES ($1, $2, $3)", 
						  								[req.body.localizacion_name, req.body.localizacion_license, address_id], function(err, result) {
						  		//call `done()` to release the client back to the pool
						  		done();
						  		if(err) {
						  			return console.error('error running query', err);
						  		} else {
						  			res.json(true);
						  		}
						  	});
						  }
						});
		  		}
	  		}
	  	});
		});
	}
});

/* PUT Admin Manejar Localizaciones 
 * Edit information of location matching :id
 */
router.put('/admin/localizaciones/:id', function(req, res, next) {
	var location_id = req.params.id;
	if(!req.body.hasOwnProperty('localizacion_name') || !req.body.hasOwnProperty('localizacion_license')
			|| !req.body.hasOwnProperty('localizacion_address_line1') || !req.body.hasOwnProperty('localizacion_address_line2')
			|| !req.body.hasOwnProperty('localizacion_address_city') || !req.body.hasOwnProperty('localizacion_address_zipcode')) {
  	res.statusCode = 400;
  	return res.send('Error: Missing fields for put location.');
	} else {
		var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	    	return console.error('error fetching client from pool', err);
	  	}
			// Insert new ganadero into db
			client.query("UPDATE location SET name = $1, license = $2 \
										WHERE location_id = $3", 
										[req.body.localizacion_name, req.body.localizacion_license, location_id], function(err, result) {
			  if(err) {
			    return console.error('error running query', err);
			  } else {
			  	client.query("UPDATE address \
												SET address_line1 = $1, address_line2 = $2, city = $3, zipcode = $4 \
												FROM location \
												WHERE location_id = $5 and location.address_id = address.address_id",
												[req.body.localizacion_address_line1, req.body.localizacion_address_line2, req.body.localizacion_address_city, 
												req.body.localizacion_address_zipcode, location_id], function(err, result) {
						//call `done()` to release the client back to the pool
						done();
					  if(err) {
					    return console.error('error running query', err);
					  } else {
					  	res.json(true);
					  }
			  	});   
			  }
			});
		});
	}
});

/* PUT Admin Manejar Localizaciones Associates
 * Edit associates of location matching :id
 */
router.put('/admin/localizaciones/:id/associates', function(req, res, next) {
	var location_id = req.params.id;
});

/* GET Admin Manejar Citas */
router.get('/admin/citas', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		
	  client.query("SELECT appointment_id, to_char(date, 'YYYY, MM, DD') AS date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location_id, location.name AS location_name, report_id, agent_id, username \
									FROM (appointments natural join report natural join location), users \
									WHERE user_id = agent_id \
									ORDER BY date ASC, time ASC \
									LIMIT 10;", function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.render('manejar_citas', { title: 'Manejar Citas', citas: result.rows});
	    }
	  });
	})
});

/* NOT USED YET; WILL REQUIRE MODIFICATION: GET Citas List data 
 * Responds with first 10 citas, 
 * ordered by date and time
 * for use to fill requested page in pagination
 */
router.get('/admin/list_citas', function(req, res, next) {
	var citas_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		
	  client.query("SELECT appointment_id, appointments.date, to_char(appointments.time, 'HH12:MI AM') AS time, purpose, location_id, location.name AS location_name, report_id, agent_id, username \
									FROM (appointments natural join report natural join location), users \
									WHERE user_id = agent_id \
									ORDER BY date ASC, time ASC \
									LIMIT 10;", function(err, result) {
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

/* GET Admin Manejar Dispositivos
 *
 */
router.get('/admin/dispositivos', function(req, res, next) {
	var dispositivos_list, usuarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// to populate devices list
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
	  client.query('SELECT person_id, email, first_name, middle_initial, last_name1, last_name2, phone_number, person.spec_id, specialization.name AS specialization_name \
									FROM (users natural join person) LEFT OUTER JOIN specialization \
									ON person.spec_id = specialization.spec_id \
									ORDER BY email ASC', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	usuarios_list = result.rows;
	    	res.render('manejar_dispositivos', { title: 'Manejar Dispositivos', dispositivos: dispositivos_list, usuarios: usuarios_list});
	    }
	  });
	});
});

/* GET Dispositivos List data 
 * Responds with first 20 dispositivos, 
 * ordered by assigned person
 */
router.get('/admin/list_dispositivos', function(req, res, next) {
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
	  client.query('SELECT person_id, email, first_name, middle_initial, last_name1, last_name2, phone_number, person.spec_id, specialization.name AS specialization_name \
									FROM (users natural join person) LEFT OUTER JOIN specialization \
									ON person.spec_id = specialization.spec_id \
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
