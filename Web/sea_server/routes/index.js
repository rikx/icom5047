var express = require('express');
var router = express.Router();

/* GET home (login) page. */
router.get('/', function(req, res, next) {
	res.render('index', { title: 'Servicio De Extension Agricola',});
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

/* 
 * GET ganaderos 
 */
router.get('/ganaderos', function(req, res, next) {
	var ganaderos_list;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
	  	return console.error('error fetching client from pool', err);
		}
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
	    	res.json({ganaderos : ganaderos_list});
	    }
	  });
	});
});

/* This query returns all persons that are not associated with a location
//SELECT person_id, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
//								FROM person \
//									WHERE person_id NOT IN ( \
//										SELECT person_id \
//										FROM person, location \
//										WHERE person_id = owner_id or person_id = manager_id)

/* 
 * GET usuarios 
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

/* POST login */
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
		  client.query('SELECT user_id, username FROM person natural join users WHERE username=$1', [req.body.input_username], function(err, result) {
		  	//call `done()` to release the client back to the pool
		    done();
		    if(err) {
		      return console.error('error running query', err);
		    }
		    if(result.rowCount == 0){
  				// not found
  				client.end();
  				res.statusCode = 404;
					res.send("User not found.");
  			} else {
  					//TODO: password authentication
  					switch(get_user_type(result.rows)) {
  						case 'admin': {

  							//TODO: figure out the problem with the address still saying /users/admin isntead of just /admin
  							res.location('admin');
  							res.redirect('/users/admin');
  							break;
  						}
  						case 'agent': {
  							res.location("agent");
  							res.redirect('/users/agent');
  							break;
  						}
  						case 'specialist': {
  							res.location("specialist");
  							res.redirect('/users/specialist');
  							break;
  						}
						}
  			}
		  });
	  });
	}
});

// TODO: this will have to be some account_type parameter in the final version
function get_user_type(query_result){
	var id = query_result[0].user_id; 
	if (id == 1) {
		return 'admin';
	};
	if (id == 2) {
		return 'agent';
	};
	if (id == 3) {
		return 'specialist';
	};
}

module.exports = router;
