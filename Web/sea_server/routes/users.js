var express = require('express');
var router = express.Router();

/* GET users home redirect
 * redirects to home page of current user session; else redirects to login
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

/* GET Categorias 
 * Responds with categorias, 
 * alphabetically ordered by name
 */

	// WHERE flowchart.flowchart_id = $1', 
	// 						  	[cuestionario_id], function(err, result) {

router.get('/admin/categorias', function(req, res, next) {
 	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (!username) {
  	user_id = req.session.user_id = '';
    username = req.session.username = '';
    user_type = req.session.user_type = '';
    res.redirect('/');
  } else if (user_type != 'admin') {
		res.redirect('/');
	} else {
		var categories_list;
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		client.query('SELECT * FROM category ORDER BY name', function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {
					categories_list = result.rows;
			  	client.query('SELECT * FROM specialization ORDER BY name', function(err, result){
				  	//call `done()` to release the client back to the pool
				  	done();
				  	if(err) {
				  		return console.error('error running query', err);
				  	} else {
				  		res.render('manejar_categorias', { 
				  			title: 'Manejar Categorias', 
				  			categories: categories_list,
				  			specialties: result.rows,
				  			user_type:user_type,
				  			username:username
				  		});
				  	}
				  });
			  }
		  });
	 	});
	}
});

/* GET Tomar Cuestionario 
 * Responds with first 20 cuestionarios, 
 * alphabetically ordered by name
 */
router.get('/cuestionarios', function(req, res, next) {
 	var db = req.db;
 	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

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
	  		res.render('tomar_cuestionarios', { 
	  			title: 'Cuestionarios', 
	  			cuestionarios: result.rows,
	  			username:username,
	  			user_type:user_type
	  		});
	  	}
	  });
 	});
});

/* GET Cuestionario 
 * page does not exist
 */
router.get('/cuestionarios/:id', function(req, res, next) {
	res.redirect('/');
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
 		var query_config;
 		var user_id = req.session.user_id;
 		var user_type = req.session.user_type;

 		if(user_type == 'admin' || user_type == 'specialist'){
 			query_config = {
 				text: 'SELECT location_id, name AS location_name FROM location'
 			}
 		} else if(user_type	== 'agent'){
 			 query_config = {
 				text: "SELECT location_id, name AS location_name FROM location WHERE agent_id = $1",
 				values: [user_id]
 			}
 		}

		// query for all locations data
		client.query(query_config, function(err, result){
				if(err) {
					return console.error('error running query', err);
				} else {
					locations_list = result.rows;
				}
			});
	  // query for flowchart info and first question
	  client.query('SELECT flowchart.flowchart_id, flowchart.name AS flowchart_name, first_id, end_id \
									FROM flowchart \
							  	WHERE flowchart.flowchart_id = $1', 
							  	[cuestionario_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		if(result.rowCount < 1){
					res.redirect('/users'); // redirect since survey does not exist
				} else{
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
	  	}
	  });
	});
});

/* GET Tomar Cuestionario Metodo Abierto
 * Responds with take survey page for matching survey :id
 */
 router.get('/cuestionarios/open/:id', function(req, res, next) {
 	var cuestionario_id = req.params.id;
 	var locations_list;
 	var db = req.db;
 	var answers;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
 		var query_config;
 		var user_id = req.session.user_id;
 		var user_type = req.session.user_type;

 		if(user_type == 'admin' || user_type == 'specialist'){
 			query_config = {
 				text: 'SELECT location_id, name AS location_name FROM location'
 			}
 		} else if(user_type	== 'agent'){
 			query_config = {
 				text: "SELECT location_id, name AS location_name FROM location WHERE agent_id = $1",
 				values: [user_id]
 			}
 		}

		// query for all locations data
		client.query(query_config, function(err, result){
			if(err) {
				return console.error('error running query', err);
			} else {
				locations_list = result.rows;
			}
		});
						  				// insert this_option
						  				client.query('SELECT name, flowchart.flowchart_id, item_id, option.label as answer_label, option_id \
						  					FROM flowchart INNER JOIN item on \
						  					flowchart.flowchart_id = item.flowchart_id \
						  					INNER JOIN option on \
						  					option.parent_id = item.item_id \
 						  					WHERE type != $1 \
						  					AND type != $2 \
						  					AND type != $3 \
						  					AND flowchart.flowchart_id = $4', ['RECOM', 'END', 'START', cuestionario_id],  function(err, result) {
						  						if(err) {
						  							return console.error('error running query', err);
						  						} else {
						  							answers = result.rows;
						  							console.log(answers);
						  						}
						  					});
	  // query for flowchart info and first question
	  client.query('SELECT name, flowchart.flowchart_id, item.label as question_label, item_id, type \
	  	FROM flowchart INNER JOIN item on \
	  	flowchart.flowchart_id = item.flowchart_id \
	  	WHERE type != $1 \
		AND type != $2 \
		AND type != $3 \
		AND flowchart.flowchart_id = $4', 
	  	['RECOM', 'END', 'START', cuestionario_id], function(err, result) {
	  	//call `done()` to release the client back to the pool
	  	done();

	  	if(err) {
	  		return console.error('error running query', err);
	  	} else {
	  		if(result.rowCount < 1){
					res.redirect('/users'); // redirect since survey does not exist
				} else{
					//console.log(result.rows);
					var current_user = {
						user_id: req.session.user_id,
						username: req.session.username
					}
					res.render('cuestionario_open', { 
						title: 'Cuestionario Abierto', 
						flowchart: result.rows, 
						locations: locations_list, 
						current_user: current_user,
						options: answers 
					});
				}
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
  	res.render('agente', { title: 'Agente Home', username: username});
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
  	res.render('admin', { title: 'Admin Home', username: username});
  } else {
  	res.redirect('/users');
  }
});

/* 
 * GET Specialist Home
 */
router.get('/specialist', function(req, res, next) {
	var username = req.session.username;
	var user_type = req.session.user_type;

  if (username != null && user_type == 'specialist') {
  	res.render('agente', { title: 'Especialista Home', username: username});
  } else {
  	res.redirect('/users');
  }
});

/* GET Admin Manejar Cuestionarios
 * Responds with first 20 cuestionarios, 
 * alphabetically ordered by name
 */
router.get('/admin/cuestionarios', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else if (user_type != 'admin') {
		res.redirect('/');
	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		client.query('SELECT flowchart_id, name AS flowchart_name, version, creator_id, username, flowchart.status \
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
		  		res.render('manejar_cuestionarios', { 
		  			title: 'Manejar Cuestionarios', 
		  			cuestionarios: result.rows,
		  			username:username,
		  			user_type:user_type
		  		});
		  	}
		  });
	 	});
	}
});

/* GET Admin Crear Cuestionario */
router.get('/admin/cuestionarios/crear', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else if (user_type != 'admin') {
		res.redirect('/');
	} else {
		var current_user = {
			user_id: user_id,
			username: username
		}
			res.render('crear_cuestionario', { title: 'Crear Cuestionario', user: current_user, user_type: user_type, username:username});
		}
});

/* Get flowchart items and options
 *
 */
router.get('/admin/cuestionarios/ver/:id', function(req, res, next){
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else if (user_type != 'admin') {
		res.redirect('/');
	} else {
		var flowchart_id = req.params.id;

		var flowchart_info, items_list, connections_list;

	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		// get flowchart
	 		client.query('SELECT * from flowchart WHERE flowchart_id = $1', [flowchart_id], function(err, result){
				if(err) {
					return console.error('error running query', err);
				} 
				flowchart_info = result.rows[0];
	 		});

	 		// get items
	 		client.query("SELECT flowchart_id, item_id, state_id, state \
										FROM item \
										WHERE flowchart_id = $1", 
	 									[flowchart_id], function(err, result){
				if(err) {
					return console.error('error running query', err);
				} 
				items_list = result.rows;
	 		});
	 		// get connections
	  	client.query("WITH path_targets AS (SELECT flowchart_id, option.label, parent_id, next_id, \
																	CASE \
																	WHEN item_id = next_id THEN state_id \
																	END AS target \
																FROM option \
																INNER JOIN item ON item.item_id = option.next_id \
																WHERE flowchart_id = $1), \
													path_sources AS (SELECT flowchart_id, option.label, parent_id, next_id, \
																	CASE \
																	WHEN item_id = parent_id THEN state_id \
																	END AS source \
																FROM option \
																INNER JOIN item ON item.item_id = option.parent_id \
																WHERE flowchart_id = $1) \
													SELECT * \
													FROM path_sources natural join path_targets", 
	  											[flowchart_id], function(err, result) {
	  		// call done to release client to pool
				done();
				if(err) {
					return console.error('error running query', err);
				}
				connections_list = result.rows
				res.json({
					flowchart: flowchart_info,
					items: items_list,
					connections: connections_list
				});
			});
	 	});
	}
});

/* POST Admin crear cuestionario
 * post new survey
 */
router.post('/admin/cuestionarios/crear', function(req, res, next) {
	console.log(req.body);
	var creator_id = req.session.user_id;

	var flowchart_id;
	var items_array;
	var flowchart_info = req.body.info;
	var flowchart_items = req.body.items;
	var item_options = req.body.options;

 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
  	// Verify flowchart name does not already exist in db
  	client.query("SELECT name FROM flowchart WHERE name = $1 AND version = $2", 
  								[flowchart_info.flowchart_name, flowchart_info.flowchart_version], function(err, result) {
  		if(err) {
  			return console.error('error running query', err);
  		} 
			if(result.rowCount > 0){
				res.send({exists: true});
			} else {
  			// Insert new flowchart into db
  			var the_status;
  			if(flowchart_info.status == 1){
					the_status = 1
  			} else {
  				the_status = 0;
  			}
  			client.query("INSERT into flowchart (name, creator_id, version, status) VALUES ($1, $2, $3, $4) \
											RETURNING flowchart_id", 
											[flowchart_info.flowchart_name, creator_id, flowchart_info.flowchart_version, the_status], function(err, result) {
					if(err) {
						return console.error('error running query', err);
					} else {
						flowchart_id = result.rows[0].flowchart_id;
						
						// Insert items
						var this_item;
		  			for(var i=0; i < flowchart_items.length; i++){
		  				this_item = flowchart_items[i];
		  				// insert this_item
							client.query('INSERT into item (flowchart_id, state_id, label, type, state, connect_id) VALUES ($1, $2, $3, $4, $5, $6) \
														RETURNING item_id, state_id', 
														[flowchart_id, this_item.id, this_item.name, this_item.type, this_item.state, this_item.c_id], function(err, result) {
								if(err) {
									return console.error('error running query', err);
								} else {
									var new_item = result.rows[0].item_id;
									var state_id = result.rows[0].state_id;

									// if new item is start item
									if(flowchart_info.first_id == state_id){
										// edit first_id of flowchart
										client.query('UPDATE flowchart SET first_id = $1 WHERE flowchart_id = $2', 
																	[new_item, flowchart_id] , function(err, result){
											if(err) {
												return console.error('error running query', err);
											} else {
											}
										});
									}
									// if new item is end item
									if(flowchart_info.end_id == state_id){
										// edit end_id of flowchart
										client.query('UPDATE flowchart SET end_id = $1 WHERE flowchart_id = $2', 
																	[new_item, flowchart_id] , function(err, result){
											if(err) {
												return console.error('error running query', err);
											} else {
											}
										});
									}
								}
							});
		  			}
						
						// Get all items associated with this flowchart
						client.query('SELECT item_id, state_id, connect_id FROM item WHERE flowchart_id = $1', 
													[flowchart_id], function(err, result){
							if(err) {
								return console.error('error running query', err);
							} else {
								items_array = result.rows;
								
								// Insert options
								var this_option;
				  			for(var j=0; j < item_options.length; j++){
				  				this_option = item_options[j];
				  				
				  				// find matching source and target
				  				var this_source, this_target;
				  				items_array.forEach(function(item){
				  					if(item.connect_id == this_option.source){
				  						this_source = item.item_id;
				  					} else if(item.state_id == this_option.target){
				  						this_target = item.item_id;
				  					}
				  				});

				  				// insert this_option
		  						client.query('INSERT into option (parent_id, next_id, label) VALUES ($1, $2, $3)', 
																[this_source, this_target, this_option.label], function(err, result) {
										if(err) {
											return console.error('error running query', err);
										} else {
										}
									});
				  			}
							}
						});
  					//call `done()` to release the client back to the pool
						done();
						res.send({redirect: '/users'});
		  			//res.json(true);
					}
				});
  		}
	  });
  });
});

/* GET Admin View/Edit Cuestionario 
 *
 */
router.get('/admin/cuestionarios/:id', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else if (user_type != 'admin') {
		res.redirect('/');
	} else {
		var flowchart_id = req.params.id;

		var flowchart_info, items_list, connections_list;

	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		// get flowchart
	 		client.query('SELECT * from flowchart WHERE flowchart_id = $1', [flowchart_id], function(err, result){
				if(err) {
					return console.error('error running query', err);
				}
				if(result.rowCount < 1){
					res.redirect('/users'); // Redirect since cuestionario does not exist
				} else { 
					flowchart_info = result.rows[0];
				}
	 		});
	 		// get items
	 		client.query("SELECT flowchart_id, item_id, label AS name, type, state_id AS id, state, connect_id \
										FROM item \
										WHERE flowchart_id = $1", 
	 									[flowchart_id], function(err, result){
				if(err) {
					return console.error('error running query', err);
				} 
				items_list = result.rows;
	 		});
	 		// get connections
	  	client.query("WITH path_targets AS (SELECT flowchart_id, option.label, parent_id, next_id, \
																	CASE \
																	WHEN item_id = next_id THEN state_id \
																	END AS target \
																FROM option \
																INNER JOIN item ON item.item_id = option.next_id \
																WHERE flowchart_id = $1), \
													path_sources AS (SELECT flowchart_id, option.label, parent_id, next_id, \
																	CASE \
																	WHEN item_id = parent_id THEN connect_id \
																	END AS source \
																FROM option \
																INNER JOIN item ON item.item_id = option.parent_id \
																WHERE flowchart_id = $2) \
													SELECT * \
													FROM path_sources natural join path_targets", 
	  											[flowchart_id, flowchart_id], function(err, result) {
	  		// call done to release client to pool
				done();
				if(err) {
					return console.error('error running query', err);
				}
				connections_list = result.rows;
				var current_user = {
	  			user_id: user_id,
	  			username: username
	  		}

				res.render('cuestionario', { 
					title: 'Cuestionario',
					flowchart: flowchart_info,
					items: items_list,
					connections: connections_list,
					user: current_user
				});
			});
	 	});
	}
});

/* GET Admin Manejar Ganaderos
 * renders manejar ganaderos page with first 20 ganaderos and their associated information
 */
router.get('/ganaderos', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else {
	 	var ganaderos_list, locations_list;
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		var query_config1, query_config2;
	 		if(user_type == 'admin' || user_type=='specialist'){
	 			query_config1 = {
	 				text: "SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
								FROM person \
								WHERE person.status != $1 AND person_id NOT IN (SELECT person_id FROM users) \
								ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
								LIMIT 20",
	 				values: [-1]
	 			}
	 			query_config2 = {
	 				text: 'WITH ganaderos AS (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
						 			FROM person \
						 			WHERE person.status != $1 AND person_id NOT IN (SELECT person_id FROM users) \
						 			ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
						 			LIMIT 20), \
								owners AS (SELECT person_id AS owner_id, location_id AS owner_location, location.name AS location_owner_name \
									FROM ganaderos INNER JOIN location ON ganaderos.person_id = location.owner_id), \
								managers AS(SELECT person_id AS manager_id, location_id AS manager_location, location.name AS location_manager_name \
									FROM ganaderos INNER JOIN location ON ganaderos.person_id = location.manager_id) \
						 		SELECT * \
						 		FROM owners FULL OUTER JOIN managers ON owners.owner_location = managers.manager_location',
					values: [-1]
	 			}
	 		} else if(user_type == 'agent'){
	 			query_config1 = {
	 				text: "WITH ganaderos AS (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
						 			FROM person \
						 			WHERE person.status != $1 AND person_id NOT IN (SELECT person_id FROM users) \
						 			ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
						 			LIMIT 20) \
								SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name  \
								FROM ganaderos INNER JOIN location ON ganaderos.person_id = location.owner_id \
								WHERE agent_id = $2\
								UNION \
								SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number, (first_name || ' ' || last_name1 || ' ' || last_name2) as person_name \
								FROM ganaderos INNER JOIN location ON ganaderos.person_id = location.manager_id \
								WHERE agent_id = $2",
	 				values: [-1, user_id]
	 			}
	 			query_config2 = {
	 				text: "WITH ganaderos AS (SELECT person_id, first_name, middle_initial, last_name1, last_name2, email, phone_number \
						 			FROM person \
						 			WHERE person.status != $1 AND person_id NOT IN (SELECT person_id FROM users) \
						 			ORDER BY first_name ASC, last_name1 ASC, last_name2 ASC \
						 			LIMIT 20), \
								owners AS (SELECT person_id AS owner_id, location_id AS owner_location, location.name AS location_owner_name \
									FROM ganaderos INNER JOIN location ON ganaderos.person_id = location.owner_id \
									WHERE agent_id = $2), \
								managers AS(SELECT person_id AS manager_id, location_id AS manager_location, location.name AS location_manager_name \
									FROM ganaderos INNER JOIN location ON ganaderos.person_id = location.manager_id \
									WHERE agent_id = $2) \
						 		SELECT * \
						 		FROM owners FULL OUTER JOIN managers ON owners.owner_location = managers.manager_location",
					values: [-1, user_id]
	 			}
	 		}
	 		// get ganaderos
	 		client.query(query_config1, function(err, result) {
	 				if(err) {
	 					return console.error('error running query', err);
	 				} else {
	 					ganaderos_list = result.rows;
	 				}
	 			});
	 		// get associated locations
	 		client.query(query_config2, function(err, result){
				//call `done()` to release the client back to the pool
				done();
				if(err) {
					return console.error('error running query', err);
				} else {
					locations_list = result.rows;	  		
					var current_user = {
		  			user_id: user_id,
		  			username: username,
		  			user_type: user_type
		  		}

					res.render('manejar_ganaderos', { 
						title: 'Manejar Ganaderos', 
						ganaderos: ganaderos_list, 
						locations: locations_list,
						user: current_user,
						user_type: user_type,
						username: username 
					});
				}
			});	
	 	});
	}
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
	  	//client.query("SELECT email FROM person WHERE email = $1 OR phone_number = $2", 
	  								//[req.body.ganadero_email, req.body.ganadero_telefono], function(err, result) {
	  		// if(err) {
	  		// 	return console.error('error running query', err);
	  		// } else //{
	  			//if(result.rowCount > 0){
	  				//res.send({exists: true});
	  			//} //else {
		  			// Insert new ganadero row
		  			client.query("INSERT into person (first_name, middle_initial, last_name1, last_name2, email, phone_number) VALUES ($1, $2, $3, $4, $5, $6)", 
		  										[req.body.ganadero_name, req.body.ganadero_m_initial, req.body.ganadero_apellido1, req.body.ganadero_apellido2, req.body.ganadero_email, req.body.ganadero_telefono] , function(err, result) {
							//call `done()` to release the client back to the pool
							done();
							if(err) {
								return console.error('error running query', err);
							} else {
								res.json(true);
							}
						});
		  		//}
		  	//}
		 // });
	  });
	}
});

/* PUT Admin User Specialties
 *
 */
router.put('/admin/user_specialties', function(req, res, next) {
  if(!req.body.hasOwnProperty('specialties') || !req.body.hasOwnProperty('user') ) {
	  res.statusCode = 400;
	  return res.send('Error: Missing fields for put user specialties.');
	} else {
		var user_specialties_array, included;

	  var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	  		return console.error('error fetching client from pool', err);
	  	}
	    // Get specialties (if any) associated to user
	    client.query("SELECT * FROM users_specialization WHERE user_id = $1", 
	    							[req.body.user], function(err, result) {
	    	if(err) {
	      	return console.error('error running query', err);
	      } else {
		     	// if user has specialties assocciated to it
		      if(result.rowCount > 0){
		        user_specialties_array = result.rows;

		        /*
		         * Code for addding to user_specialties
		         */
		        // loop through checkmarked specialties
		        for(i = 0 ; i < req.body.specialties.length; i++) {
		        	// loop through specialties associated to user
		        	for(j = 0; j < user_specialties_array.length; j++) {
		        		// if match is found then specialty is already associated with user
		        		if(req.body.specialties[i] == user_specialties_array[j].spec_id) {
									included = true;
		       			}
		       		}
							// if included then specialty is already associated with user; else, add association
							if(!included) {
								// add association
								client.query("INSERT into users_specialization (user_id, spec_id) VALUES ($1, $2)", 
															[req.body.user,req.body.specialties[i]] , function(err, result) {
									//call `done()` to release the client back to the pool
									done();
									if(err) {
										return console.error('error running query', err);
									} else {

									}
								});
							}
							// reset included value
							included = false;
		      	} //end for loop 1

		      	/* 
		      	 * Code for removing from user_specialties 
		      	 */
		    	  // loop through specialties associated to user
		      	for(j = 0; j < user_specialties_array.length; j++) {
		      		// loop through checkmarked specialties
		       		for(i = 0 ; i < req.body.specialties.length; i++) {
		       			// if match is found then specialty is already associated with user
		        		if(req.body.specialties[i] == user_specialties_array[j].spec_id) {
		       				included = true;
		       			}
		       		}
		       		// if included then specialty remained checkmarked; else, remove association
		       		if(!included) {
		       			// remove association
								client.query("DELETE FROM users_specialization WHERE user_id =$1 AND spec_id = $2", 
															[req.body.user,user_specialties_array[j].spec_id] , function(err, result) {
									//call `done()` to release the client back to the pool
									done();
									if(err) {
										return console.error('error running query', err);
									} else {
										// do nothing
									}
								});
		       		}
		       		// reset included value
							included = false;
		      	}
		      } else {
		      	// user has no specialties so add the ones that are checkmarked
		      	for(i = 0 ; i < req.body.specialties.length; i++){
							client.query("INSERT into users_specialization (user_id, spec_id) VALUES ($1, $2)", 
														[req.body.user,req.body.specialties[i]] , function(err, result) {
								//call `done()` to release the client back to the pool
								done();
								if(err) {
									return console.error('error running query', err);
								} else {
									// do nothing
								}
		      		});
						}
		      }
		    	// user_specialties associations were succesfully updated
		    	client.query('SELECT specialization.spec_id, specialization.name AS spec_name \
												FROM users \
												INNER JOIN users_specialization ON users.user_id = users_specialization.user_id \
												INNER JOIN specialization ON users_specialization.spec_id = specialization.spec_id \
												WHERE users.user_id = $1 ORDER BY specialization.name', 
												[req.body.user], function(err, result) {
				  	//call `done()` to release the client back to the pool
				    done();
			    	if(err) {
				      return console.error('error running query', err);
				    } else {
				    	res.json({users_specialization: result.rows});
				    }
				  });
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
			// Edit ganadero
			client.query("UPDATE person SET first_name = $1, middle_initial = $2, last_name1 = $3, last_name2 = $4, email = $5, phone_number = $6 WHERE person_id = $7", 
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

/* PUT Admin Manejar Ganaderos 
 * Edit ganadero matching :id in database
 */
 router.put('/admin/edit_category/:id', function(req, res, next) {
 	console.log("edit category");
 	if(!req.body.hasOwnProperty('category_name')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for put categories.');
 	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// Edit category
			client.query("UPDATE category SET name = $1 WHERE category_id = $2", 
										[req.body.category_name, req.params.id] , function(err, result) {
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

/* PUT/DELETE categories
 */
 router.delete('/admin/delete_category/:id', function(req, res, next) {
 	console.log("delete category");
 		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
 			if(err) {
 				return console.error('error fetching client from pool', err);
 			}
			// Edit category
			client.query("DELETE FROM category WHERE category_id = $1", 
				[req.params.id] , function(err, result) {
				//call `done()` to release the client back to the pool
				done();
				if(err) {
					return console.error('error running query', err);
				} else {
					//console.log(result.rows);
					//res.json({"categories": result.rows});
					res.json(true);

			

				}
			});
		});
 	
 });

 /* PUT/DELETE specialties
 */
 router.delete('/admin/delete_specialty/:id', function(req, res, next) {
 	console.log("delete specialty");
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
			// Edit category
			client.query("DELETE FROM specialization WHERE spec_id = $1", 
				[req.params.id] , function(err, result) {
				//call `done()` to release the client back to the pool
				done();
				if(err) {
					return console.error('error running query', err);
				} else {
					//console.log(result.rows);
					//res.json({"categories": result.rows});
					res.json(true);



				}
			});
		});
 	
 });




 /* PUT Admin Manejar Ganaderos 
 * Edit ganadero matching :id in database
 */
 router.put('/admin/edit_specialty/:id', function(req, res, next) {
 	console.log("edit spec");
 	if(!req.body.hasOwnProperty('specialty_name')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for put categories.');
 	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// Edit category
			client.query("UPDATE specialization SET name = $1 WHERE spec_id = $2", 
										[req.body.specialty_name, req.params.id] , function(err, result) {
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

/* GET User Manejar Reportes
 * Renders page and includes response with first 20 reportes, 
 * alphabetically ordered by location name
 */
router.get('/reportes', function(req, res, next) {
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
	 			// get first 20 reports regardles of creator
	 			query_config = {
					text: "SELECT report_id, report.creator_id, users.username, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date, report.location_id, report.name as report_name, report.status, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name, flowchart.version \
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
					text: "SELECT report_id, report.creator_id, users.username, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date, report.location_id, report.name as report_name, report.status, location.name AS location_name, report.flowchart_id, flowchart.name AS flowchart_name, flowchart.version \
									FROM report INNER JOIN location ON report.location_id = location.location_id \
									INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
									INNER JOIN users ON report.creator_id = user_id \
									WHERE report.creator_id = $1 AND report.status != $2 \
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
					var current_user = {
		  			user_id: user_id,
		  			username: username,
		  			user_type: user_type
		  		}
					res.render('manejar_reportes', { 
						title: 'Manejar Reportes', 
						reports: result.rows,
						user: current_user,
						username:username,
						user_type:user_type
					});
				}
			});
	 	});
  }
});

/* GET User Ver Reporte 
 * TODO make queries depend on current signed in user
 */
router.get('/reportes/:id', function(req, res, next) {
 	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else {
	 	var report_id = req.params.id;
	 	var report_details, survey_details, appointment_details;
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		// get report basic info
			client.query("SELECT report.report_id, report.creator_id, username AS report_creator, report.location_id, report.name as report_name, report.status, location.name AS location_name, report.flowchart_id, flowchart.name AS survey_name, \
											flowchart.version AS survey_version, report.note, to_char(report.date_filed, 'DD/MM/YYYY') AS report_date \
										FROM report \
										INNER JOIN users ON users.user_id = report.creator_id \
										INNER JOIN flowchart ON report.flowchart_id = flowchart.flowchart_id \
										INNER JOIN location ON report.location_id = location.location_id \
										WHERE report.report_id = $1", [report_id], function(err, result) {
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		if(result.rowCount < 1){
						res.redirect('/users'); // redirect since report does not exist
					} else {
						if((user_type == 'agent' && result.rows[0].creator_id == user_id) || user_type == 'admin' || user_type == 'specialist'){
							report_details = result.rows[0];
						} else {
							res.redirect('/users'); // redirect since user does not have access to this report
						}
		  		}
		  	}
		  });
		  // get appointment details
		  client.query("SELECT appointment_id, to_char(appointments.date, 'DD/MM/YYYY' ) AS date, to_char(appointments.time, 'HH12:MI AM') AS time, appointments.purpose, username AS maker \
										FROM appointments \
										INNER JOIN report ON report.report_id = appointments.report_id \
										INNER JOIN users ON appointments.maker_id = user_id \
										WHERE report.report_id = $1 AND appointments.status != $2", [report_id, -1], function(err, result){
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		appointment_details = result.rows[0];
		  	}
		  });
			// get answered survey details
			// TODO: need SELECT distinct to avoid item duplicates?
			client.query('SELECT item.label as question, option.label as answer, path.data as path_data, type \
										FROM path INNER JOIN option ON path.option_id = option.option_id \
										INNER JOIN item ON item.item_id = option.parent_id \
										WHERE path.report_id = $1 \
										ORDER BY sequence', [report_id], function(err, result) {
				//call `done()` to release the client back to the pool
		  	done();

		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		survey_details = result.rows; 
		  		var current_user = {
		  			user_id: user_id,
		  			username: username
		  		}
		  		res.render('reporte', { 
		  			title: 'Reporte ' + report_id,
		  			reporte: report_details,
		  			survey: survey_details,
		  			appointment: appointment_details,
		  			current_user: current_user
		  		});
		  	}
			})
		});
	}
});

/* PUT Report Title
 * 
 */
router.put('/reports/new_title/:id', function(req, res, next) {
 	var report_id = req.params.id;
 	if(!req.body.hasOwnProperty('report_title')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for put report title.');
	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// Edit note in report
			client.query("UPDATE report SET name = $1 WHERE report_id = $2", 
										[req.body.report_title, report_id], function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {
				 res.json(true);
				}
			});
		});
	}
});

/* PUT Report Note
 * 
 */
router.put('/reports/note', function(req, res, next) {
 	console.log("notes edit");
 	if(!req.body.hasOwnProperty('report_id') || !req.body.hasOwnProperty('note_text')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for put report note.');
	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// Edit report note
			client.query("UPDATE report SET note = $1 WHERE report_id = $2", 
										[req.body.note_text, req.body.report_id] , function(err, result) {
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

/* POST Report Appointment 
 * 
 */
router.post('/reports/appointment/:id/:uid', function(req, res, next) {
  var report_id = req.params.id;
  var maker = req.params.uid;

	if(!req.body.hasOwnProperty('cita_date') || !req.body.hasOwnProperty('cita_time') || !req.body.hasOwnProperty('cita_purpose') ) {
		res.statusCode = 400;
		return res.send('Error: Missing fields for post report appointment.');
	} else {
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
			if(err) {
				return console.error('error fetching client from pool', err);
			}
			// Verify appointment does not already exist in db
			client.query("SELECT appointment_id FROM appointments WHERE report_id = $1", [report_id], function(err, result) {
		 		if(err) {
		  		return console.error('error running query', err);
		 		} else {
			 		if(result.rowCount > 0){
			   		res.send({exists: true});
		  		} else {
			   		// Insert new appointment into db
				   	client.query("INSERT into appointments (date, time, purpose, report_id, maker_id, status) \
				    							VALUES ($1, $2, $3, $4, $5, $6) \
				    							RETURNING appointment_id, to_char(date, 'DD/MM/YYYY') AS date, to_char(time, 'HH12:MI AM') AS time, purpose", 
				    							[req.body.cita_date, req.body.cita_time, req.body.cita_purpose, report_id, maker, 1] , function(err, result) {
					   	//call `done()` to release the client back to the pool
					  	done();
					  	if(err) {
					   		return console.error('error running query', err);
					   	} else {
					   		var the_result = result.rows[0];
					   		var new_appointment = {
					   			appointment_id: the_result.appointment_id,
					   			date: the_result.date,
					   			time: the_result.time,
					   			purpose: the_result.purpose
					   		};
								res.json({appointment: new_appointment});
				   		}
		  			});
					}
	 			}
			});
		});
	}
});

/* POST survey answer
 *
 */
router.post('/cuestionario/open/submit', function(req, res, next) {
 	// if(!req.body.hasOwnProperty('report_id') || !req.body.hasOwnProperty('option_id') 
 	// 	||!req.body.hasOwnProperty('has_data') || !req.body.hasOwnProperty('sequence')) {
 	// 	return res.send('Error: Missing fields for post path.');
 	// } else {
 		console.log("Open Method Server Execution");
 		console.log(req.body.answer);

		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
			if(err) {
		  	return console.error('error fetching client from pool', err);
			}
			var query_config;
			if(req.body.answer.has_data == true){
				query_config = {
					text: "INSERT into path (report_id, option_id, data, sequence) VALUES ($1, $2, $3, $4)",
					values: [req.body.answer.report_id, req.body.answer.option_id, req.body.answer.data, req.body.answer.sequence]
				}
			} else {
				query_config = {
					text: "INSERT into path (report_id, option_id, sequence) VALUES ($1, $2, $3)",
					values: [req.body.answer.report_id, req.body.answer.option_id, req.body.answer.sequence]
				}
			}
			// insert new report
		  client.query(query_config, function(err, result) {
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
 	
});

/* GET Admin Manejar Usuarios 
 * Renders page with first 20 usuarios, alphabetically ordered 
 */
router.get('/admin/usuarios', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else if (user_type != 'admin') {
		res.redirect('/');
	} else {
	 	var usuarios_list, specialties_list, locations_list, all_specialties;
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

			// get all specialties
			client.query('SELECT spec_id, name as spec_name FROM specialization ORDER BY name', function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {
					all_specialties = result.rows;
				}
			});

			// get specialties associated with users
			client.query('WITH usuarios AS (SELECT user_id, username \
									  	FROM users \
									  	WHERE users.status != $1 \
									  	ORDER BY username ASC \
									  	LIMIT 20) \
										SELECT usuarios.user_id, us.spec_id, spec.name \
										FROM usuarios \
										LEFT JOIN users_specialization AS us ON us.user_id = usuarios.user_id \
										LEFT JOIN specialization AS spec ON us.spec_id = spec.spec_id', [-1], function(err, result) {
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
		  		return console.error('error running query', err);
		  	} else {
		  		locations_list = result.rows;

		  		console.log(usuarios_list)
		  		console.log(specialties_list)
		  		console.log(locations_list)
		  		console.log(all_specialties)
		  		res.render('manejar_usuarios', {
		  		 title: 'Manejar Usuarios', 
		  		 usuarios : usuarios_list, 
		  		 user_specialties: specialties_list, 
		  		 locations : locations_list, 
		  		 specialties: all_specialties,
		  		 user_type: user_type,
		  		 username : username
		  		});
		  	}
		  });
		});
	}
});


/* POST Admin Manejar Usuario
 * Add new user to database
 */
router.post('/admin/usuarios', function(req, res, next) {
 	if(!req.body.hasOwnProperty('usuario_email') 
 		|| !req.body.hasOwnProperty('usuario_name') 
 		|| !req.body.hasOwnProperty('usuario_lastname_maternal') 
 		|| !req.body.hasOwnProperty('usuario_lastname_paternal') 
 		|| !req.body.hasOwnProperty('usuario_password')
 		|| !req.body.hasOwnProperty('usuario_telefono') 
 		|| !req.body.hasOwnProperty('usuario_type')
 		|| !req.body.hasOwnProperty('usuario_middle_initial')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for post user.');
	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		var new_username = req.body.usuario_email;
	  		var at_index = new_username.indexOf('@');
	  		new_username = new_username.substring(0, at_index);
	  	// Verify user does not already exist
	  	client.query("SELECT user_id, username FROM users WHERE username = $1", 
	  								[new_username], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0){
	  						  var user_id = result.rows[0].user_id;
	  						  var username = result.rows[0].username;
	  						  // use as username string content before '@' in usuario_email
/*	  						  var new_username = req.body.usuario_email;
	  						  var at_index = new_username.indexOf('@');
	  						  new_username = new_username.substring(0, at_index);*/
	  						  client.query("UPDATE users SET type = $1, username = $2, status = $3 \
	  						  	WHERE user_id = $4", 
	  						  	[req.body.usuario_type, new_username, 1,  user_id], function(err, result) {
	  						  		if(err) {
	  						  			return console.error('error running query', err);
	  						  		} else {
					// edit password 
					client.query("UPDATE users \
						SET passhash = crypt($1, gen_salt('bf'::text)) \
						WHERE user_id = $2", 
						[req.body.usuario_password, user_id] , function(err, result) {
							if(err) {
								return console.error('error running query', err);
							} else {
							// do nothing
						}
					});
					// edit person table
					client.query("UPDATE person \
						SET first_name = $1, last_name1 = $2, last_name2 = $4, email = $5, phone_number = $6, middle_initial = $7 \
						FROM users \
						WHERE user_id = $3 and users.person_id = person.person_id",
						[req.body.usuario_name, req.body.usuario_lastname_paternal, user_id, req.body.usuario_lastname_maternal, req.body.usuario_email,req.body.usuario_telefono, req.body.usuario_middle_initial], function(err, result) {
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
	  			} else {
		  			// Insert new person row
		  			client.query("INSERT into person (first_name, last_name1, last_name2, email, phone_number, middle_initial) VALUES ($1, $2, $3, $4, $5, $6) \
		  										RETURNING person_id", 
		  										[req.body.usuario_name, req.body.usuario_lastname_paternal, req.body.usuario_lastname_maternal, req.body.usuario_email, req.body.usuario_telefono, req.body.usuario_middle_initial] , function(err, result) {
							//call `done()` to release the client back to the pool
							done();
							if(err) {
								return console.error('error running query', err);
							} else {
								var person_id = result.rows[0].person_id;
				  			// Insert new user into db
				  			// use as username string content before '@' in usuario_email
				  			var new_username = req.body.usuario_email;
				  			var at_index = new_username.indexOf('@');
				  			new_username = new_username.substring(0, at_index);
				  			// Insert new user row
				  			client.query("INSERT into users (person_id, type, username) VALUES ($1, $2, $3) \
				  										RETURNING user_id", 
				  										[person_id, req.body.usuario_type, new_username] , function(err, result) {
									if(err) {
										return console.error('error running query', err);
									} else {
										var user_id = result.rows[0].user_id;
										// Update user with password
						  			client.query("UPDATE users \
																	SET passhash = crypt($1, gen_salt('bf'::text)) \
																	WHERE user_id = $2", 
						  										[req.body.usuario_password, user_id] , function(err, result) {
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
 	console.log(req.body.change_password);
 	if(!req.body.hasOwnProperty('usuario_email') 
 		|| !req.body.hasOwnProperty('usuario_name') 
 		|| !req.body.hasOwnProperty('usuario_lastname_maternal') 
 		|| !req.body.hasOwnProperty('usuario_lastname_paternal') 
 		|| !req.body.hasOwnProperty('usuario_password')
 		|| !req.body.hasOwnProperty('usuario_telefono') 
 		|| !req.body.hasOwnProperty('usuario_type')
 		|| !req.body.hasOwnProperty('usuario_middle_initial')) {
 		res.statusCode = 400;
	 	return res.send('Error: Missing fields for put user.');
	} else {
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// edit user table
		  // use as username string content before '@' in usuario_email
			var new_username = req.body.usuario_email;
			var at_index = new_username.indexOf('@');
			new_username = new_username.substring(0, at_index);
			client.query("UPDATE users SET type = $1, username = $2 \
										WHERE user_id = $3", 
										[req.body.usuario_type, new_username, user_id], function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {

				if(req.body.change_password){
					// edit password 
				console.log("changing password");
	  			client.query("UPDATE users \
												SET passhash = crypt($1, gen_salt('bf'::text)) \
												WHERE user_id = $2", 
	  										[req.body.usuario_password, user_id] , function(err, result) {
						if(err) {
							return console.error('error running query', err);
						} else {
							// do nothing
						}
					});
	  		}
					// edit person table
					client.query("UPDATE person \
												SET first_name = $1, last_name1 = $2, last_name2 = $4, email = $5, phone_number = $6, middle_initial = $7 \
												FROM users \
												WHERE user_id = $3 and users.person_id = person.person_id",
												[req.body.usuario_name, req.body.usuario_lastname_paternal, user_id, req.body.usuario_lastname_maternal, req.body.usuario_email,req.body.usuario_telefono, req.body.usuario_middle_initial], function(err, result) {
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

/* POST Admin User Specialties
 * Add new user specialty to database
 */
router.post('/admin/new_specialty', function(req, res, next) {
  if(!req.body.hasOwnProperty('new_specialty')) {
   	res.statusCode = 400;
  	return res.send('Error: Missing fields for post user specialty.');
 	} else {
	  var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	   	if(err) {
	    	return console.error('error fetching client from pool', err);
	   	}
	    // Verify specialty does not already exist in db
	    client.query("SELECT * FROM specialization WHERE name = $1", 
	    							[req.body.new_specialty], function(err, result) {
	     	if(err) {
	      	return console.error('error running query', err);
	     	} else {
	      	if(result.rowCount > 0){
	       		res.send({exists: true});
	      	} else {
	       		// Insert new ganadero into db
	       		client.query("INSERT into specialization (name) VALUES ($1)", 
	        								[req.body.new_specialty] , function(err, result) {
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


/* GET Admin Manejar Localizaciones 
 *
 */
router.get('/localizaciones', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;
	var query_config_1;
	var query_config_2;
	var query_config_3;
	var query_config_4;
	console.log(user_id);
	console.log(user_type);
		console.log("users");


  if (!username) {
  	user_id = req.session.user_id = '';
    username = req.session.username = '';
    user_type = req.session.user_type = '';
    res.redirect('/');
  } else {

  		if(user_type == 'agent')
	{
		query_config_1 = {
			text: 'SELECT location.location_id, location.name AS location_name, location.address_id, license, address_line1, address_line2, city, zipcode \
			FROM location INNER JOIN address ON location.address_id = address.address_id \
			WHERE location.status != $1 AND agent_id =$2 \
			ORDER BY location_name \
			LIMIT 20;',
			values: [-1, user_id]
		}
		query_config_2 = {
			text: 'WITH locations AS (SELECT location_id, location.name AS location_name, agent_id \
										FROM location \
										WHERE location.status != $1 AND agent_id = $2 \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, locations.location_name, lc.category_id, cat.name \
									FROM locations \
									LEFT JOIN location_category AS lc ON lc.location_id = locations.location_id \
									LEFT JOIN category AS cat ON lc.category_id = cat.category_id',
			values: [-1, user_id]
		}

		query_config_3 = {
			text: 'WITH locations AS (SELECT location.location_id, location.name AS location_name, agent_id \
										FROM location \
										WHERE location.status != $1 AND agent_id =$2 \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, agent_id, username \
									FROM locations,users \
									WHERE user_id = agent_id;',
			values: [-1, user_id]
		}

		query_config_4 = {
			text: "WITH locations AS (SELECT location.location_id, location.name AS location_name, owner_id, manager_id \
											FROM location \
											WHERE location.status != $1 AND agent_id = $2 \
											ORDER BY location_name \
											LIMIT 20), \
								owners AS ( \
														SELECT owner_id, (first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as owner_name, locations.location_id AS owner_location, \
														CASE WHEN person_id = owner_id THEN 'owner' \
														END AS relation_owner \
														FROM locations \
														INNER JOIN person ON person_id = owner_id), \
								managers AS ( \
														SELECT manager_id, (first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as manager_name , locations.location_id AS manager_location, \
														CASE WHEN person_id = manager_id THEN 'manager' \
														END AS relation_manager \
														FROM locations \
														INNER JOIN person ON person_id = manager_id) \
								SELECT * \
								FROM owners \
								FULL OUTER JOIN managers ON owners.owner_location = managers.manager_location",
			values: [-1, user_id]
		}
	}
	else if (user_type == 'admin' || user_type == 'specialist')
	{
		query_config_1 = {
	 				text: 'SELECT location.location_id, location.name AS location_name, location.address_id, license, address_line1, address_line2, city, zipcode \
									FROM location INNER JOIN address ON location.address_id = address.address_id \
									WHERE location.status != $1 \
									ORDER BY location_name \
									LIMIT 20;',
					values: [-1]
	 			}

	 	query_config_2 = {
			text: 'WITH locations AS (SELECT location_id, location.name AS location_name, agent_id \
										FROM location \
										WHERE location.status != $1 \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, locations.location_name, lc.category_id, cat.name \
									FROM locations \
									LEFT JOIN location_category AS lc ON lc.location_id = locations.location_id \
									LEFT JOIN category AS cat ON lc.category_id = cat.category_id',
			values: [-1]
		}

		query_config_3 = {
			text: 'WITH locations AS (SELECT location.location_id, location.name AS location_name, agent_id \
										FROM location \
										WHERE location.status != $1 \
										ORDER BY location_name \
										LIMIT 20) \
									SELECT locations.location_id, agent_id, username \
									FROM locations,users \
									WHERE user_id = agent_id;',
			values: [-1]
		}

		query_config_4 = {
			text: "WITH locations AS (SELECT location.location_id, location.name AS location_name, owner_id, manager_id \
											FROM location \
											WHERE location.status != $1 \
											ORDER BY location_name \
											LIMIT 20), \
								owners AS ( \
														SELECT owner_id, (first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as owner_name, locations.location_id AS owner_location, \
														CASE WHEN person_id = owner_id THEN 'owner' \
														END AS relation_owner \
														FROM locations \
														INNER JOIN person ON person_id = owner_id), \
								managers AS ( \
														SELECT manager_id, (first_name || ' ' || last_name1 || ' ' || COALESCE(last_name2, '')) as manager_name , locations.location_id AS manager_location, \
														CASE WHEN person_id = manager_id THEN 'manager' \
														END AS relation_manager \
														FROM locations \
														INNER JOIN person ON person_id = manager_id) \
								SELECT * \
								FROM owners \
								FULL OUTER JOIN managers ON owners.owner_location = managers.manager_location",
			values: [-1]
		}
	}
	 	var localizaciones_list, categories_list, all_categories, agentes_list, ganaderos_list;
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// query for location data
			client.query(query_config_1,  function(err, result) {
					if(err) {
						return console.error('error running query', err);
					} else {
						localizaciones_list = result.rows;
					}
				});
			// get all categories
			client.query('SELECT category_id, name as category_name FROM category ORDER BY name', function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {
					all_categories = result.rows;
				}
			});
			// query for location categories
			client.query(query_config_2,  function(err, result){
				if(err) {
					return console.error('error running query', err);
				} else {
					categories_list = result.rows;
				}
			});

		  // query for associated agentes
		  client.query(query_config_3, function(err, result) {
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		agentes_list = result.rows;
		  	}
		  });

		  // query for associated ganaderos
		  client.query(query_config_4, function(err, result) {
		  	//call `done()` to release the client back to the pool
		  	done();

		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		ganaderos_list = result.rows; 		
					var current_user = {
		  			user_id: user_id,
		  			username: username,
		  			user_type: user_type
		  		}
		  		res.render('manejar_localizaciones', { 
		  			title: 'Manejar Localizaciones', 
		  			localizaciones: localizaciones_list, 
		  			location_categories: categories_list, 
		  			agentes: agentes_list, 
		  			ganaderos: ganaderos_list, 
		  			categorias: all_categories,
		  			user: current_user,
		  			username: username,
		  			user_type: user_type
		  		});
		  	}
		  });
		});
	}
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
	  	client.query("SELECT location_id, license FROM location WHERE license = $1", 
	  								[req.body.localizacion_license], function(err, result) {
  			if(err) {
  				return console.error('error running query', err);
  			} else {
  				if(result.rowCount > 0){
  					var location_id = result.rows[0].location_id;
  					var license = result.rows[0].license;
			client.query("UPDATE location SET name = $1, license = $2, status = $4 \
				WHERE location_id = $3", 
				[req.body.localizacion_name, req.body.localizacion_license, location_id, 1], function(err, result) {
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
  					//res.send({exists: true}); 
  				} else {
		  			// Insert new location 
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

/* PUT Admin Manejar Localizaciones associated ganadero
 * Edit associated ganadero of location matching :id
 */
router.put('/admin/associated/ganadero', function(req, res, next) {
	if(!req.body.hasOwnProperty('location_id') || !req.body.hasOwnProperty('ganadero_id')
				|| !req.body.hasOwnProperty('relation_type')) {
			res.statusCode = 400;
			return res.send('Error: Missing fields for put location associated ganadero.');
	} else {
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		var query_config = {};
	 		if(req.body.relation_type == 'owner'){
	 			console.log("hello world");
	 			query_config = {
	 				text: 'UPDATE location SET owner_id = $1 \
									WHERE location_id = $2',
					values: [req.body.ganadero_id, req.body.location_id]
	 			}
	 		} else if(req.body.relation_type == 'manager'){
	 			console.log("goodbye world");
	 			query_config = {
	 				text: 'UPDATE location SET manager_id = $1 \
									WHERE location_id = $2',
					values: [req.body.ganadero_id, req.body.location_id]
	 			}
	 		}
	 		client.query(query_config, function(err, result) {
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

/* PUT Admin Manejar Localizaciones associated agent
 * Edit associated agent of location matching :id
 */
router.put('/admin/associated/agent', function(req, res, next) {
	if(!req.body.hasOwnProperty('location_id') || !req.body.hasOwnProperty('agent_id')) {
			res.statusCode = 400;
			return res.send('Error: Missing fields for put location associated agent.');
	} else {
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
	 		client.query('UPDATE location SET agent_id = $1 \
										WHERE location_id = $2', [req.body.agent_id, req.body.location_id], function(err, result) {
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

/* POST Admin Manejar Localizaciones
 * Add new ganadero to database
 */
router.post('/admin/new_category', function(req, res, next) {
  if(!req.body.hasOwnProperty('new_category')) {
   	res.statusCode = 400;
  	return res.send('Error: Missing fields for post location category.');
 	} else {
  	var db = req.db;
  	db.connect(req.conString, function(err, client, done) {
   		if(err) {
    		return console.error('error fetching client from pool', err);
   		}
    	// Verify category does not already exist in db
    	client.query("SELECT * FROM category WHERE name = $1", 
    								[req.body.new_category], function(err, result) {
     		if(err) {
      		return console.error('error running query', err);
     		} else {
      		if(result.rowCount > 0){
      			res.send({exists: true});
      		} else {
	       		// Insert new category into db
	       		client.query("INSERT into category (name) VALUES ($1)", 
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


/* PUT Admin Category_Locations
 *
 */
router.put('/admin/category_location', function(req, res, next) {
	if(!req.body.hasOwnProperty('categories') || !req.body.hasOwnProperty('location') ) {
	  res.statusCode = 400;
	  return res.send('Error: Missing fields for post ganadero.');
	} else {
		var location_category_array, included;

	  var db = req.db;
	  db.connect(req.conString, function(err, client, done) {
	  	if(err) {
	  		return console.error('error fetching client from pool', err);
	  	}
	    // Get categories (if any) associated to location
	    client.query("SELECT * FROM location_category WHERE location_id = $1", 
	    							[req.body.location], function(err, result) {
	    	if(err) {
	      	return console.error('error running query', err);
	      } else {
		     	// if location has categories assocciated to it
		      if(result.rowCount > 0){
		        location_category_array = result.rows;

		        /*
		         * Code for addding to location_category
		         */
		        // loop through checkmarked categories
		        for(i = 0 ; i < req.body.categories.length; i++) {
		        	// loop through categories associated to location
		        	for(j = 0; j < location_category_array.length; j++) {
		        		// if match is found then category is already associated with location
		        		if(req.body.categories[i] == location_category_array[j].category_id) {
									included = true;
		       			}
		       		}
							// if included then category is already associated with location; else, add association
							if(!included) {
								// add association
								client.query("INSERT into location_category (location_id, category_id) VALUES ($1, $2)", 
															[req.body.location,req.body.categories[i]] , function(err, result) {
									//call `done()` to release the client back to the pool
									done();
									if(err) {
										return console.error('error running query', err);
									} else {

									}
								});
							}
							// reset included value
							included = false;
		      	} //end for loop 1

		      	/* 
		      	 * Code for removing from location_category 
		      	 */
		    	  // loop through categories associated to location
		      	for(j = 0; j < location_category_array.length; j++) {
		      		// loop through checkmarked categories
		       		for(i = 0 ; i < req.body.categories.length; i++) {
		       			// if match is found then category is already associated with location
		        		if(req.body.categories[i] == location_category_array[j].category_id) {
		       				included = true;
		       			}
		       		}
		       		// if included then category remained checkmarked; else, remove association
		       		if(!included) {
		       			// remove association
								client.query("DELETE FROM location_category WHERE location_id =$1 AND category_id = $2", 
															[req.body.location,location_category_array[j].category_id] , function(err, result) {
									//call `done()` to release the client back to the pool
									//done();
									if(err) {
										return console.error('error running query', err);
									} else {
										// do nothing
									}
								});
		       		}
		       		// reset included value
							included = false;
		      	}
		      } else {
		      	// location has no categories so add the ones the user checkmarked
		      	for(i = 0 ; i < req.body.categories.length; i++){
							client.query("INSERT into location_category (location_id, category_id) VALUES ($1, $2)", 
														[req.body.location,req.body.categories[i]] , function(err, result) {
								//call `done()` to release the client back to the pool
								//done();
								if(err) {
									return console.error('error running query', err);
								} else {
									// do nothing
								}
		      		});
						}
		      }
		    	// location_category associations were succesfully updated
		    	client.query('SELECT category.category_id, category.name AS category_name \
												FROM location \
												INNER JOIN location_category ON location.location_id = location_category.location_id \
												INNER JOIN category ON location_category.category_id = category.category_id \
												WHERE location.location_id = $1 ORDER BY category.name', 
												[req.body.location], function(err, result) {
				  	//call `done()` to release the client back to the pool
				    done();
			    	if(err) {
				      return console.error('error running query', err);
				    } else {
				    	res.json({location_categories: result.rows});
				    }
				  });
		   	}
		  });
	  });
	}
});

/* GET Admin Manejar Citas
 * renders citas page with first 20 citas associated with a user
 */
router.get('/citas', function(req, res, next) {
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
	 			// TODO add in where conditional check with current date
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
		  		var current_user = {
		  			user_id: user_id,
		  			username: username,
		  			user_type: user_type
		  		}
		  		res.render('manejar_citas', { 
		  			title: 'Manejar Citas', 
		  			citas: result.rows,
		  			user: current_user,
		  			username: username,
		  			user_type:user_type
		  		});
		  	}
		  });
	 	});
	}
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
			client.query("UPDATE appointments SET date = $1, time = $2, purpose = $3, maker_id = $4 WHERE appointment_id = $5", 
										[req.body.cita_date, req.body.cita_time, req.body.cita_proposito, req.session.user_id, cita_id] , function(err, result) {
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

/* DELETE Admin Manejar Citas 
 * Delete cita matching :id in database
 */
router.delete('/admin/citas/:id', function(req, res, next) {
 	var cita_id = req.params.id;
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
		if(err) {
			return console.error('error fetching client from pool', err);
		}
		// Edit cita in db
		client.query("UPDATE appointments SET status = $1 WHERE appointment_id = $2", 
									[-1, cita_id] , function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
});

/* Delete Flow chart
 * 
 */
router.put('/admin/delete_flowchart/:id', function(req, res, next) {
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
			if(err) {
				return console.error('error fetching client from pool', err);
			}
		// Edit ganadero
		client.query("UPDATE flowchart SET status = $1 where flowchart_id = $2", 
									[-1,req.params.id] , function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
});


/* Delete Ganadero
 * 
 */
 router.put('/admin/delete_ganadero/:id', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// Edit ganadero
		client.query("UPDATE person SET status = $1 where person_id = $2", 
			[-1,req.params.id] , function(err, result) {
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
 });


/* Delete Report
 * 
 */
router.put('/admin/delete_report/:id', function(req, res, next) {
		console.log("deleting report");
		var db = req.db;
		db.connect(req.conString, function(err, client, done) {
			if(err) {
				return console.error('error fetching client from pool', err);
			}
		// Edit ganadero
		client.query("UPDATE report SET status = $1 where report_id = $2", 
									[-1,req.params.id] , function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
});

/* Delete Report
 * 
 */
 router.put('/admin/delete_user/:id', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// Edit ganadero
		client.query("UPDATE users SET status = $1 where user_id = $2", 
			[-1,req.params.id] , function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
 });

/* Delete Location
 * 
 */
 router.put('/admin/delete_location/:id', function(req, res, next) {
 	var db = req.db;
 	db.connect(req.conString, function(err, client, done) {
 		if(err) {
 			return console.error('error fetching client from pool', err);
 		}
		// Edit ganadero
		client.query("UPDATE location SET status = $1 where location_id = $2", 
			[-1,req.params.id] , function(err, result) {
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
 });

/* GET Admin Manejar Dispositivos
 * renders manejar dispositivos page with first 20 dispositivos 
 * ordered by device name
 */
router.get('/admin/dispositivos', function(req, res, next) {
	var user_id = req.session.user_id;
	var username = req.session.username;
	var user_type = req.session.user_type;

	if (!username) {
		user_id = req.session.user_id = '';
	  username = req.session.username = '';
	  user_type = req.session.user_type = '';
	  res.redirect('/');
	} else if (user_type != 'admin') {
		res.redirect('/');
	} else { 	
	 	var dispositivos_list, usuarios_list;
	 	var db = req.db;
	 	db.connect(req.conString, function(err, client, done) {
	 		if(err) {
	 			return console.error('error fetching client from pool', err);
	 		}
			// to populate dispositivo list
			client.query("SELECT device_id, devices.name as device_name, id_number, to_char(latest_sync, 'DD/MM/YYYY @ HH12:MI PM') AS last_sync, devices.user_id as assigned_user, username \
										FROM devices LEFT JOIN users ON devices.user_id = users.user_id \
										ORDER BY devices.name ASC \
										LIMIT 20", function(err, result) {
				if(err) {
					return console.error('error running query', err);
				} else {
					dispositivos_list = result.rows;
				}
			});

		  // to populate dispositivo dropdown list
		  client.query('SELECT user_id, username FROM users \
								  	ORDER BY username ASC', function(err, result) {
		  	//call `done()` to release the client back to the pool
		  	done();
		  	if(err) {
		  		return console.error('error running query', err);
		  	} else {
		  		usuarios_list = result.rows;
		  		res.render('manejar_dispositivos', { 
		  			title: 'Manejar Dispositivos', 
		  			dispositivos: dispositivos_list, 
		  			usuarios: usuarios_list,
		  			username:username,
		  			user_type:user_type
		  		});
		  	}
		  });
		});
	}
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
	  	client.query("SELECT * FROM devices WHERE id_number = $1", 
	  								[req.body.dispositivo_id_num], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			console.log(result.rowCount);
	  			if(result.rowCount > 0){
	  				res.send({exists: true});
	  			} else {

	  				var query_config = {};
	  				if(req.body.dispositivo_id_usuario.length > 0){
	  					query_config = {
	  						text: "INSERT into devices (name, id_number, user_id) VALUES ($1, $2, $3)",
	  						values: [req.body.dispositivo_name, req.body.dispositivo_id_num, req.body.dispositivo_id_usuario]
	  					}
	  				} else {
	  					query_config = {
	  						text: "INSERT into devices (name, id_number) VALUES ($1, $2)",
	  						values: [req.body.dispositivo_name, req.body.dispositivo_id_num]
	  					}
	  				}

		  			// Insert new dispositivo into db
		  			client.query(query_config, function(err, result) {
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
 	if(!req.body.hasOwnProperty('dispositivo_name') 
 		|| !req.body.hasOwnProperty('dispositivo_id_num')
 		|| !req.body.hasOwnProperty('dispositivo_id_usuario')) {
 		res.statusCode = 400;
 		return res.send('Error: Missing fields for put device.');
 	} else {
 		var db = req.db;
 		db.connect(req.conString, function(err, client, done) {
 			if(err) {
 				return console.error('error fetching client from pool', err);
 			}
	  	// Verify dispositivo does not already exist in db
	  	client.query("SELECT device_id, id_number FROM devices WHERE id_number = $1", 
	  								[req.body.dispositivo_id_num], function(err, result) {
	  		if(err) {
	  			return console.error('error running query', err);
	  		} else {
	  			if(result.rowCount > 0 && result.rows[0].device_id != dispositivo_id){
	  				res.send({exists: true});
	  			} else {
						// Edit dispositivo in db
						client.query("UPDATE devices SET name = $1, id_number = $2, user_id = $3 WHERE device_id = $4", 
													[req.body.dispositivo_name, req.body.dispositivo_id_num, req.body.dispositivo_id_usuario, dispositivo_id] , function(err, result) {
							//call `done()` to release the client back to the pool
							done();
							if(err) {
								return console.error('error running query', err);
							} else {
								console.log(result.rows);
								res.json(true);
			/*					if(false){

								} else {
									res.json(true);
								}*/
							}
						});
					}
				}
			});
		});
 	}
});

/* DELETE Admin Manejar Dispositivos 
 * Delete dispositivo matching :id in database
 */
router.delete('/admin/dispositivos/:id', function(req, res, next) {
	var db = req.db;
	db.connect(req.conString, function(err, client, done) {
	 	if(err) {
			return console.error('error fetching client from pool', err);
		}
		// delete
		client.query('DELETE FROM devices WHERE device_id = $1', 
									[req.params.id],function(err, result){
			//call `done()` to release the client back to the pool
			done();
			if(err) {
				return console.error('error running query', err);
			} else {
				res.json(true);
			}
		});
	});
});

module.exports = router;
