var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

//TODO: add :id or :username after /admin
//TODO: fix it so you cant access it directly (with sesions)
/* GET Admin Home. */
router.get('/admin', function(req, res, next) {
  res.render('admin', { title: 'Admin Home'});
});

/* GET Admin Crear Cuestionario */
router.get('/admin/cuestionario/crear', function(req, res, next) {
	res.render('crear_cuestionario', { title: 'Crear Cuestionario'});
});

/* GET Admin Manejar Cuestionarios */
router.get('/admin/cuestionarios', function(req, res, next) {
	res.render('manejar_cuestionarios', { title: 'Manejar Cuestionarios'});
});

/* GET Admin Cuestionario */
router.get('/admin/cuestionarios/:id', function(req, res, next) {
	var cuestionario_id = req.params.id;
	res.render('cuestionario', { title: 'Cuestionario'});
});

/* GET Cuestionarios List data 
 * Responds with first 10 cuestionarios, alphabetically ordered 
 */
router.get('/admin/list_cuestionarios', function(req, res, next) {
	var cuestionarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query('SELECT flowchart_id, name AS flowchart_name, version, creator_id, username \
									FROM flowchart, users \
									WHERE user_id = creator_id \
									LIMIT 10;', function(err, result) {
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
	res.render('manejar_ganaderos', { title: 'Manejar Ganaderos'});
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
	
	  client.query('SELECT DISTINCT ON (person_id, first_name, last_name1, last_name2) \
	  								person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
									FROM person, location \
									WHERE person_id = owner_id OR person_id = manager_id \
									ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
									LIMIT 10;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	ganaderos_list = result.rows;
	    }
	  });

	  client.query('WITH ganaderos AS (SELECT DISTINCT ON (person_id, first_name, last_name1, last_name2) \
											person_id, first_name, last_name1, last_name2 \
										FROM person, location \
										WHERE person_id = owner_id OR person_id = manager_id \
										ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
										LIMIT 10) \
									SELECT person_id, location_id, location.name AS location_name \
									FROM ganaderos, location \
									WHERE person_id = owner_id OR person_id = manager_id;', function(err, result){
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

/* GET Admin Manejar Reportes */
router.get('/admin/reportes', function(req, res, next) {
	res.render('manejar_reportes', { title: 'Manejar Reportes'});
});

/* GET Admin Reportes List data 
 * Responds with first 10 reportes, alphabetically ordered 
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
									LIMIT 10;', function(err, result) {
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
	  client.query('', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	//reporte_details = result.rows;
	    	reporte_details = {report_id : report_id, username : 'Test User Please Ignore', date_filed : '05-12-2015', note : 'Lorem ipsum dolor sit amet, ne quo dictas iracundia, vivendum elaboraret contentiones sit cu. No omnium argumentum has, eos ea laoreet perfecto elaboraret. Nonumy mnesarchum an vix. Nam ei munere praesent referrentur, ei exerci soleat torquatos nec. Est et minimum voluptatum, has ad nulla homero recteque, et nam alii eleifend hendrerit.\
\
Nominavi hendrerit ex mea, iudico sententiae inciderint pro ea, nec ex dictas virtute albucius. Lorem everti mandamus no vel. Ut habemus menandri instructior nam. Virtute principes in ius. Cum id probo admodum, pro et facer meliore singulis, ne aeque aeterno salutandi vix. In mea iudico decore forensibus, sit et movet iuvaret, sapientem consetetur est an.\
\
Sit alia intellegat at, in ferri rebum est. Maluisset honestatis mei ei. Lorem melius an vim, admodum deserunt sit id. Suas detraxit vis in, ullum error iisque his ex. Vel eius mollis albucius ad, quot eius et duo.\
', recomendation : 'Lorem ipsum dolor sit amet, feugiat nulla commodo, luctus elit euismod. Neque nunc, lectus mi tellus, neque primis erat, eget euismod convallis. Justo laoreet id neque eget leo placerat, nulla id, viverra amet laoreet litora lorem, mollis elit volutpat vel turpis sed laborum, massa consectetuer facilisis. Donec vehicula egestas purus nulla ut est. Accumsan risus massa commodo vivamus placerat sit, ut felis turpis consectetuer, per litora lectus diam scelerisque libero, ante ac at nulla. Pellentesque fermentum nunc eros mattis.'}
	    	res.render('reporte', { title: 'Reporte', reporte: reporte_details});
	    }
	  });
	});
});


/* GET Admin Manejar Usuarios */
router.get('/admin/usuarios', function(req, res, next) {
	res.render('manejar_usuarios', { title: 'Manejar Usuarios'});
});

/* GET Usuarios List data 
 * Responds with first 10 usuarios, alphabetically ordered 
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
									LIMIT 10;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

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
										LIMIT 10) \
									SELECT user_id, location_id, location.name AS location_name \
									FROM usuarios, location \
									WHERE user_id = agent_id', function(err, result){
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
	res.render('manejar_localizaciones', { title: 'Manejar Localizaciones'});
});

/* GET Localizaciones List data 
 * Responds with first 10 localizaciones, alphabetically ordered 
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
									LIMIT 10;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

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
										LIMIT 10) \
									SELECT location_id, agent_id, username \
									FROM locations,users \
									WHERE user_id = agent_id;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

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
										LIMIT 10) \
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

/* GET Admin Manejar Citas */
router.get('/admin/citas', function(req, res, next) {
	res.render('manejar_citas', { title: 'Manejar Citas'});
});

/* GET Citas List data 
 * Responds with first 10 citas, ordered by date and time
 */
router.get('/admin/list_citas', function(req, res, next) {
	var citas_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		
	  client.query('', function(err, result) {
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

/* GET Admin Manejar Dispositivos */
router.get('/admin/dispositivos', function(req, res, next) {
	res.render('manejar_dispositivos', { title: 'Manejar Dispositivos'});
});

/* GET Citas List data 
 * Responds with first 10 dispositivos, ordered by assigned person
 */
router.get('/admin/list_dispositivos', function(req, res, next) {
	var dispositivos_list, usuarios_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		
	  client.query('SELECT device_id, devices.name as device_name, latest_sync, devices.user_id as assigned_user, username \
									FROM devices natural join users', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

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
