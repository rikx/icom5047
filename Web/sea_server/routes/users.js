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
		// TODO: modify query to also give you account type
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

	  client.query('SELECT person_id, location_id, location.name \
									FROM person, location \
									WHERE person_id = owner_id or person_id = manager_id', function(err, result){
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

/* GET Ganaderos List data 
 * Responds with first 10 ganaderos, alphabetically ordered 
 */
router.get('/admin/list_usuarios', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
		// TODO: modify query to also give you account type
	  client.query('SELECT DISTINCT ON (person_id, first_name, last_name1, last_name2) \
	  								person_id, first_name, middle_initial, last_name1, last_name2, email \
									FROM person, location \
									WHERE person_id = owner_id OR person_id = manager_id \
									ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
									LIMIT 10;', function(err, result) {
	  	//call `done()` to release the client back to the pool
	    done();

    	if(err) {
	      return console.error('error running query', err);
	    } else {
	    	res.json(result.rows);
	    }

	  });
	});
});

/* GET Admin Manejar Localizaciones */
router.get('/admin/localizaciones', function(req, res, next) {
	res.render('manejar_localizaciones', { title: 'Manejar Localizaciones'});
});

/* GET Admin Manejar Citas */
router.get('/admin/citas', function(req, res, next) {
	res.render('manejar_citas', { title: 'Manejar Citas'});
});

/* GET Admin Manejar Dispositivos */
router.get('/admin/dispositivos', function(req, res, next) {
	res.render('manejar_dispositivos', { title: 'Manejar Dispositivos'});
});


module.exports = router;
