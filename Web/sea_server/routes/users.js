var express = require('express');
var router = express.Router();

/* GET users listing. 
 *
 */
 router.get('/', function(req, res, next) {
 	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (!username) {
  	user_id = req.session.user_id = '';
    username = req.session.username = '';
    user_type = req.session.user_type = '';
    res.redirect('/');
  } else {
  	res.redirect('/users/'+user_type);
  }
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
 	var locations_list;
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// query for all locations data
		client.query("SELECT location_id, name AS location_name \
			FROM location", function(err, result){
				if(err) {
					return console.error('error running query', err);
				} else {
					locations_list = result.rows;
				}
			});
	  // query for flowchart info and first question
	  client.query('SELECT flowchart.flowchart_id, flowchart.name AS flowchart_name, first_id \
									FROM flowchart \
							  	WHERE flowchart.flowchart_id = $1', 
							  	[cuestionario_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		var current_user = {
	  			user_id: req.session.user_id,
	  			username: req.session.username
	  		}
	  		res.render('cuestionario_flujo', { 
	  			title: 'Cuestionario con Flujo', 
	  			flowchart: result.rows[0], 
	  			locations: locations_list, 
	  			current_user: current_user 
	  		});
	  	}
	  });
	});
});

/* 
 * GET Agente Home
 */
router.get('/agent', function(req, res, next) {
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (username != null && user_type == 'agent') {
  	res.render('agente', { title: 'Agente Home'});
  } else {
  	res.redirect('/users');
  }
});

/* 
 * GET Admin Home
 */
router.get('/admin', function(req, res, next) {
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (username != null && user_type == 'admin') {
  	res.render('admin', { title: 'Admin Home'});
  } else {
  	res.redirect('/users');
  }
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

/* POST Admin Manejar Localizaciones
 * Add new ganadero to database
 */
 router.post('/admin/new_category', function(req, res, next) {
 	console.log("category success");
 	if(!req.body.hasOwnProperty('new_category')) {
 		res.statusCode = 400;
 	return res.send('Error: Missing fields for post ganadero.');
 } else {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
	  	// Verify ganadero does not already exist in db
	  	client.query("SELECT * FROM category WHERE name = $1", [req.body.new_category], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
	  				res.send({exists: true});
	  			} else {
		  			// Insert new ganadero into db
		  			client.query("INSERT into category (name) \
		  				VALUES ($1)", 
		  				[req.body.new_category] , function(err, result) {
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
 			return console.error('error fetching client from pool', err);
 		}
 		client.query("SELECT report_id, report.creator_id, users.username, report.date_filed, report.location_id, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name \
									FROM report INNER JOIN location ON report.location_id = location.location_id \
									INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
									INNER JOIN users ON report.creator_id = user_id \
									ORDER BY location_name ASC \
									LIMIT 20;", function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.render('manejar_reportes', { 
					title: 'Manejar Reportes', 
					reports: result.rows});
			}
		});
 	});
 });

/* GET User Ver Reporte 
 *
 */
 router.get('/reportes/:id', function(req, res, next) {
 	var report_id = req.params.id;
 	var report_details, survey_details;
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
 		// report basic info
		client.query("SELECT report.report_id, report.creator_id, username AS report_creator, report.location_id, location.name AS location_name, report.flowchart_id, flowchart.name AS survey_name, \
										flowchart.version AS survey_version, report.note, report.date_filed AS report_date, appointment_id, appointments.date AS appointment_date, appointments.time AS appointment_time, appointments.purpose AS appointment_purpose \
									FROM report \
									LEFT JOIN appointments ON report.report_id = appointments.report_id \
									INNER JOIN users ON users.user_id = report.creator_id \
									INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
									INNER JOIN location ON report.location_id = location.location_id \
									WHERE report.report_id = $1", [report_id], function(err, result) {
	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		report_details = result.rows[0];
	  	}
	  });
		// get answered survey details
		// SELECT distinct to avoid item duplicates
		client.query('SELECT item.label as question, option.label as answer, path.data as path_data, type \
									FROM path INNER JOIN option ON path.option_id = option.option_id \
									INNER JOIN item ON item.item_id = option.parent_id \
									WHERE path.report_id = $1', [report_id], function(err, result) {
			//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		survey_details = result.rows; 
	  		var current_user = {
	  			user_id: req.session.user_id,
	  			username: req.session.username
	  		}
	  		res.render('reporte', { 
	  			title: 'Reporte ' + report_id,
	  			reporte: report_details,
	  			survey: survey_details,
	  			current_user: current_user
	  		});
	  	}
		})
	});
});

/* GET Admin Manejar Usuarios 
 * Renders page with first 20 usuarios, alphabetically ordered 
 */
 router.get('/admin/usuarios', function(req, res, next) {
 	var usuarios_list, specialties_list, locations_list, all_specialties;
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

		// get all specialties
		client.query('SELECT spec_id, name as spec_name \
									FROM specialization', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				all_specialties = result.rows;
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
	  		return console.error('error running query', err);
	  	} else {
	  		locations_list = result.rows;
	  		res.render('manejar_usuarios', {
	  		 title: 'Manejar Usuarios', 
	  		 usuarios : usuarios_list, 
	  		 user_specialties: specialties_list, 
	  		 locations : locations_list, 
	  		 specialties: all_specialties
	  		});
	  	}
	  });
	});
});


/* POST Admin Manejar Usuario
 * Add new user to database
 */
 router.post('/admin/usuarios', function(req, res, next) {
 	if(!req.body.hasOwnProperty('usuario_email') || !req.body.hasOwnProperty('usuario_name') 
 		|| !req.body.hasOwnProperty('usuario_lastname_maternal') 
 		|| !req.body.hasOwnProperty('usuario_lastname_paternal') 
 		|| !req.body.hasOwnProperty('usuario_password') 
 		|| !req.body.hasOwnProperty('usuario_password_confirm') 
 		|| !req.body.hasOwnProperty('usuario_telefono') 
 		|| !req.body.hasOwnProperty('usuario_type')) {
 		res.statusCode = 400;
 	return res.send('Error: Missing fields for post user.');
 } else {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
	  	// Verify user does not already exist in db
	  	client.query("SELECT * FROM person WHERE person_id = $1", [req.body.person_id], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
	  				res.send({exists: true});
	  			} else {

		  			// Insert new person into db
		  			client.query("INSERT into person (first_name, last_name1, last_name2, email, phone_number) \
		  				VALUES ($1, $2, $3, $4, $5) RETURNING person_id", 
		  				[req.body.usuario_name, req.body.usuario_lastname_paternal, req.body.usuario_lastname_maternal, req.body.usuario_email, req.body.usuario_telefono] , function(err, result) {
							//call `done()` to release the client back to the pool
							done();
							if(err) {
								return console.error('error running query', err);
							} else {
		  			// Insert new user into db
		  			var person_id = result.rows[0].person_id;
		  			console.log(person_id);
		  			client.query("INSERT into users (person_id, type, username) \
		  				VALUES ($1, $2, $3)", 
		  				[person_id, req.body.usuario_type, req.body.usuario_email] , function(err, result) {
							//call `done()` to release the client back to the pool
							done();
							if(err) {
								return console.error('error running query', err);
							} else {
								console.log("done");
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


/* PUT Admin Manejar Usuarios 
 * Edit information of user matching :id
 */
 router.put('/admin/usuarios/:id', function(req, res, next) {
 	var user_id = req.params.id;
 	if(!req.body.hasOwnProperty('usuario_email') || !req.body.hasOwnProperty('usuario_name') 
 		|| !req.body.hasOwnProperty('usuario_lastname_maternal') 
 		|| !req.body.hasOwnProperty('usuario_lastname_paternal') 
 		|| !req.body.hasOwnProperty('usuario_password') 
 		|| !req.body.hasOwnProperty('usuario_password_confirm') 
 		|| !req.body.hasOwnProperty('usuario_telefono') 
 		|| !req.body.hasOwnProperty('usuario_type')) {
 		res.statusCode = 400;
 	return res.send('Error: Missing fields for put user.');
 } else {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
			// Edit usuario in db
			client.query("UPDATE users SET type = $1, username = $2 \
				WHERE user_id = $3", 
				[req.body.usuario_type, req.body.usuario_email, user_id], function(err, result) {
					if(err) {
						return console.error('error running query', err);
					} else {
						client.query("UPDATE person \
							SET first_name = $1, last_name1 = $2, last_name2 = $4, email = $5, phone_number = $6 \
							FROM users \
							WHERE user_id = $3 and users.person_id = person.person_id",
							[req.body.usuario_name, req.body.usuario_lastname_paternal, user_id, req.body.usuario_lastname_maternal, req.body.usuario_email,req.body.usuario_telefono], function(err, result) {
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

/* GET Admin Manejar Localizaciones 
 *
 */
 router.get('/admin/localizaciones', function(req, res, next) {
 	var localizaciones_list, categories_list, all_categories, agentes_list, ganaderos_list;
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
		// get all categories
		client.query('SELECT category_id, name as category_name FROM category', function(err, result) {
			if(err) {
				return console.error('error running query', err);
			} else {
				all_categories = result.rows;
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
	  	FROM location \
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
	  		res.render('manejar_localizaciones', { 
	  			title: 'Manejar Localizaciones', 
	  			localizaciones: localizaciones_list, 
	  			location_categories: categories_list, 
	  			agentes: agentes_list, 
	  			ganaderos: ganaderos_list, 
	  			categorias: all_categories
	  		});
	  	}
	  });
	});
});

/* POST ADMIN Manejar Localizaciones
 *
 */
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

 		client.query("SELECT appointment_id, to_char(date, 'YYYY-MM-DD') AS date, to_char(appointments.time, 'HH24:MI:SS') AS time, purpose, location_id, location.name AS location_name, report_id, agent_id, username \
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

 /* POST cita (done through report page)
  *
  */ 
router.post('/reports/citas', function(req, res, next) {

});

/* PUT Admin Manejar Citas 
 * Edit cita matching :id in database
 */
 router.put('/admin/citas/:id', function(req, res, next) {
 	var cita_id = req.params.id;
 	if(!req.body.hasOwnProperty('cita_date') || !req.body.hasOwnProperty('cita_time')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for edit appointment.');
 	} else {
 		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
 			if(err) {
 				return console.error('error fetching client from pool', err);
 			}
			// Edit cita in db
			client.query("UPDATE appointments SET date = $1, time = $2, purpose = $3 \
										WHERE appointment_id = $4", 
										[req.body.cita_date, req.body.cita_time, req.body.cita_proposito, cita_id] , function(err, result) {
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
		// to populate dispositivo list
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

	  // to populate dispositivo dropdown list
	  client.query('SELECT person_id, email, first_name, middle_initial, last_name1, last_name2, phone_number \
	  	FROM (users natural join person) \
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

/* POST Admin Manejar Dispositivo
 * Add new dispositivo to database
 */
 router.post('/admin/dispositivos', function(req, res, next) {
 	if(!req.body.hasOwnProperty('dispositivo_name') || !req.body.hasOwnProperty('dispositivo_id_num')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for post device.');
 	} else {
 		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
 			if(err) {
 				return console.error('error fetching client from pool', err);
 			}
	  	// Verify dispositivo does not already exist in db
	  	client.query("SELECT * FROM devices WHERE id_number = $1", [req.body.dispositivo_id_num], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
	  				res.send({exists: true});
	  			} else {
		  			// Insert new dispositivo into db
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

/* PUT Admin Manejar Dispositivos 
 * Edit dispositivo matching :id in database
 */
 router.put('/admin/dispositivos/:id', function(req, res, next) {
 	var dispositivo_id = req.params.id;
 	if(!req.body.hasOwnProperty('dispositivo_name') || !req.body.hasOwnProperty('dispositivo_id_num')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for put device.');
 	} else {
 		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
 			if(err) {
 				return console.error('error fetching client from pool', err);
 			}
			// Edit dispositivo in db
			client.query("UPDATE devices SET name = $1, id_number = $2 \
				WHERE device_id = $3", 
				[req.body.dispositivo_name, req.body.dispositivo_id_num, dispositivo_id] , function(err, result) {
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

module.exports = router;
