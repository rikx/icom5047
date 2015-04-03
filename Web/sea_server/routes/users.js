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
router.get('/admin/crear-cuestionario', function(req, res, next) {
	res.render('crear-cuestionario', { title: 'Crear Cuestionario'});
});

/* GET Admin Manejar Cuestionarios */
router.get('/admin/cuestionarios', function(req, res, next) {
	res.render('manejar_cuestionarios', { title: 'Manejar Cuestionarios'});
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
	  client.query(' \
									LIMIT 10;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	cuestionarios_list = result.rows;
	    	res.json({cuestionarios: cuestionrios_list});
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
	var localizaciones_list, agents_list, ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// query for location data
	  client.query('', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	localizaciones_list = result.rows;
	    }
	  });
	  // query for associated agentes
	  client.query('', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	agents_list = result.rows;
	    }
	  });
	  // query for associated ganaderos
	  client.query('', function(err, result) {
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
