jsPlumb.ready(function() {
	$preguntas_list = $('#preguntas_list');
	// stores current elements in the jsPlumb container
	var elements_array = new Array();
	var connections_array = new Array();

	// Return to admin page button
	$('#btn_home').on('click', function(){
		window.location.href = '/users';
	});

  // Close info panel
  $('#btn_close_info_panel').on('click', function(){
  	$('#info_panel').hide();
  	remove_active_class($preguntas_list);
  });

  /* Open info panel */
  $preguntas_list.on('click', 'tr td a.show_info_elemento', function(e){
    // prevents link from firing
    e.preventDefault();

    // remove active from previous clicked list item
    remove_active_class($preguntas_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
    	$this.addClass('active');
    }

    // contains item id
    var item_id = $this.attr('data-id');
    var arrayPosition = elements_array.map(function(arrayItem) { return arrayItem.id; }).indexOf(item_id);
    var this_element = elements_array[arrayPosition];

    // populate info panel with this_ganadero info
    populate_info_panel(this_element);
	});

  /* Populate's info panel with $this_element's information */
  function populate_info_panel(this_element){
  	$('#pregunta_info_name').html(this_element.name);
  	$('#pregunta_info_type').html(this_element.type);
  	
  	if(this_element.type != 'START' && this_element.type != 'END'){
	  	var answers_content = '';
	  	$.each(connections_array, function(i){
	  		if(this_element.id == this.source){
	  			answers_content += "<li class='list-group-item'>"+this.label+"</li>"; 
	  		}
	  	});
	  	$('#pregunta_info_responses').html(answers_content);
	  	$('#possible_answers').show();
	  } else {
	  	$('#possible_answers').hide();
	  }
  }

  /* Check all items are a source in at least one connection 
   * for use when user submits a finished flowchart
   * not used when user just saves a flowchart under construction
   */
  function check_item_connections(){
  	var has_child = false;
  	for(var i = 0; i < elements_array.length; i++){
  		for(var j = 0; j < connections_array.length; j++){
  			if(elements_array[i].id == connections_array[j].source){
  				has_child = true;
  			}
  		}
  		if(!has_child){
  			console.log('item '+elements_array[i].id+' necesita una connecion');
  		} else {
  			has_child = false;
  		}
  	}
  }

  /* */
  function populate_elements_list(){
		// add new element to elements list
		var table_content = '';

		$.each(elements_array, function(i){
			table_content += "<tr><td><a class='list-group-item ";
	    // if initial list item, set to active
	    if(i==0) {
	    	table_content +=  'active ';
	    	populate_info_panel(this);
	    }
	    table_content += "show_info_elemento' href='#', data-id='"+this.id+"'>"+this.id+': '+this.name+"</a></td></tr>";
		});

		$('#preguntas_list').html(table_content);
	}

 	// JS PLUMB create code

 	$('#btn_add_question').on('click', function(){
 		var item_type = $('#btn_item_type').attr('data-item-type');
		// does not execute when selecting a start or an end element if it already exists
		if((item_type == 'START' && $('#start_item').hasClass('disabled')) 
				|| (item_type == 'END' && $('#end_item').hasClass('disabled'))){
			// do nothing
		} else {
 			add_item();
 		}
 	});


 	/* gets first and end item ids from elements_array, if they exist */
 	function check_for_endpoints(){
 		var first_item = -1
 			, end_item = -1;
 		for(var i=0; i<elements_array.length; i++){
 			if(elements_array[i].type == 'START'){
 				for(var x=0; x<connections_array.length; x++){
 					if(elements_array[i].id == connections_array[x].source){
 						first_item = connections_array[x].target;
 					}	
 				}
 			} else if(elements_array[i].type == 'END'){
 				end_item = elements_array[i].id;
 			}
 		}
 		return {first_id: first_item, end_id: end_item};
 	}

  /* POSTs new flowchart information */
  $('#btn_submit').on('click', function(){
  	var end_points = check_for_endpoints();

  	// checks created flowchart has a first item
  	if(end_points.first_id != -1){
	    // get form data and conver to json format
	    var $the_form = $('#form_create_flowchart');
	    var form_data = $the_form.serializeArray();
	    var new_flowchart = ConverToJSON(form_data);

	    // add missing flowchart fields
	    new_flowchart.first_id = end_points.first_id;
	    new_flowchart.end_id = end_points.end_id;
	    console.log(new_flowchart);
	    // ajax call to post new ganadero
	    $.ajax({
	      url: "http://localhost:3000/users/admin/cuestionarios/crear",
	      method: "POST",
	      data: JSON.stringify({
	      	info: new_flowchart,
	      	items: elements_array,
	      	options: connections_array
	      }),
	      contentType: "application/json",
	      dataType: "json",

	      success: function(data) {
	        if(data.exists){
	          alert("Cuestionario con este nombre y versión ya existe.");
	        } else {
	          alert("Cuestionario ha sido añadido al sistema.");
	         	if(typeof data.redirect == 'string') {
					    window.location.replace(window.location.protocol + "//" + window.location.host + data.redirect);
					  }
	        }
	      },
	      error: function( xhr, status, errorThrown ) {
	        alert( "Sorry, there was a problem!" );
	        console.log( "Error: " + errorThrown );
	        console.log( "Status: " + status );
	        console.dir( xhr );
	      }
	    });
	  }
  });


 	var j =0; // item id
 	var array = [];
 	var arrayConnection = [];
 	var trigger = "yes";
 	jsPlumb.Defaults.Container = $('#container_plumbjs');

 	$('#list_question_type').on('click', 'li a', function(e){
		// prevents link from firing
		e.preventDefault();

		var item_type = $(this).attr('data-item-type');
		// does not execute when selecting a start or an end element if it already exists
		if((item_type == 'START' && $('#start_item').hasClass('disabled')) 
			|| (item_type == 'END' && $('#end_item').hasClass('disabled'))){
			// do nothing
		} else {
			$('#btn_item_type_text').text($(this).text()+' ');
			$('#btn_item_type').attr('data-item-type', item_type);
			// enable add question button
			$('#btn_add_question').prop('disabled', false);
		}
	});

 	function add_item() {
 		jsPlumb.ready(function() {
 			var itemType = $('#btn_item_type').attr('data-item-type');
 			var newState = $('<div>').attr('id', 'state' + j);
 			var title = $('<div>').addClass('title_question');
 			var title_id = $('<p>');
 			var has_input = false;

 			// add css and title fitting item type
 			if(itemType == 'START'){
				newState.addClass('item_end_point');
				newState.attr('data-state-name', 'INICIO');
				$('#start_item').addClass('disabled');
				title_id.text('INICIO');
 			} else if(itemType == 'END'){
 				newState.addClass('item_end_point');
 				newState.attr('data-state-name', 'FIN');
 				$('#end_item').addClass('disabled');
 				title_id.text('FIN');
 			} else {
 				if(itemType == 'RECOM'){
					newState.addClass('item_recom');
 				} else {
 					newState.addClass('item_question');	
 				}
 				// Set item identifier text
				title_id.text('Elemento ' + j);
				// add input for item text content
				has_input = true;
 				var stateNameContainer = $('<div>').attr('data-state-id', 'state' + j);
 				var stateName = $('<input>').attr('type', 'text');

 				// store element id as data attribute
				stateName.attr('data-id', 'state' + j);
 			}
 			
 			// append title_id to state item
			title.append(title_id);

			// if has_input
		 	// put stateName input field into stateNameContainer div, 
			// and then append this div to title
 			if(has_input){
				title.append(stateNameContainer.append(stateName));
 			}

 			// append title to state item
			newState.append(title);

			// append connect node to state item
			var connect = $('<div>').addClass('connect_question');
			newState.append(connect);

			//add new state item to container
			$('#container_plumbjs').append(newState);
			
			jsPlumb.makeTarget(newState, {
				anchor: 'Continuous',
				endpoint:'Blank'
			});
			jsPlumb.makeSource(connect, {
				parent: newState,
				anchor: 'Continuous',
				connector: 'Flowchart',
				endpoint:'Blank'
			});   

			jsPlumb.draggable(newState, {
				containment: 'parent',
				stop: function(event) {
					if ($(event.target).find('select').length == 0) {
						//saveState(event.target);
						array.push(newState);
						// create item object
						var this_item = {
							id: newState.attr('id'),
							name: $('#'+newState.attr('id')).attr('data-state-name'),
							type: itemType,
							left: newState.position().left,
							top: newState.position().top
						};

						// check if item is already in array and updates it
					  if(containsObject(this_item, elements_array)) {
					   	replace(this_item, elements_array);
					  } else {
					   	elements_array.push(this_item);
						}
						// populate elements list with new item
						populate_elements_list();
					}
				}
			});


			newState.dblclick(function(e) {
				var this_id = $(this).attr('id');

				jsPlumb.detachAllConnections($(this));
				$(this).remove();
				e.stopPropagation();	
				
				// find connections where this element is a source or target and 
				// delete them
				connections_array=connections_array.filter(isConnectionEndpoint);
				function isConnectionEndpoint(connection){
					if(connection.source == this_id){
						return false; 
					} else if(connection.target == this_id){
						return false; 
					} else {
						return true;
					}
				}
				// find element in array and delete it
				var this_item;
				for(var d=0; d<elements_array.length;d++){
					this_item = elements_array[d];
					if(this_item.id == this_id){
						elements_array.splice(d,1);
						if(this_item.type=='START'){
							$('#start_item').removeClass('disabled');
						} else if(this_item.type=='END'){
							$('#end_item').removeClass('disabled');
						}
						//return;
					}
				}
			}); 

			if(itemType != 'START' && itemType != 'END'){
				stateName.keyup(function(e) {
					if (e.keyCode === 13) {
			      //var state = $(this).closest('.item');
			      //state.children('.title').text(this.value);
			      $(this).parent().text(this.value);
			      var state_id = $(this).attr('data-id');
			      $('#'+state_id).attr('data-state-name', this.value);

						// create item object
						var this_item = {
							id: newState.attr('id'),
							name: $('#'+newState.attr('id')).attr('data-state-name'),
							type: itemType,
							left: newState.position().left,
							top: newState.position().top
						};

						// check if item is already in array and updates it
					  if(containsObject(this_item, elements_array)) {
					   	replace(this_item, elements_array);
					  } else {
					   	elements_array.push(this_item);
						}
			      // populate elements list with new element
			      populate_elements_list();
				  }
				});
				// focuses on input field of the created element
				stateName.focus();
			}

			// increase state id variable
			j++; 
		});
	}


	$('#connect_item').dblclick(function(e) {
		e.stopPropagation();
	}); 


	jsPlumb.bind("connection", function(info, originalEvent) {
		jsPlumb.ready(function() {
			var this_connection;
			info.connection.addOverlay( [ "Arrow", { width:20, length:20, location:1, id:"arrow" } ]);
			info.connection.setPaintStyle( {lineWidth:10,strokeStyle:'rgb(204,255,204)'});
			if(trigger == "yes"){
				this_connection = {
					source: info.sourceId,
					target: info.targetId,
					label: 'label'
				};

				var mylabel = prompt("Por favor, escriba la respuesta a la pregunta.");
				info.connection.addOverlay(["Label", { label: mylabel, location:0.5, id: "connLabel"} ]);
				this_connection.label = mylabel;
				connections_array.push(this_connection);
			}
		});
	});

	$('#container_plumbjs').scroll(function(){
		jsPlumb.ready(function() {
			jsPlumb.repaintEverything();
		});
	});

	function containsObject(obj, list) {

		var i;
		for (i = 0; i < list.length; i++) {
			if (list[i].id === obj.id) {
				return true;
			}
		}

		return false;
	}
	function replace(obj, list) {
		var i;
		for (i = 0; i < list.length; i++) {
			if (list[i].id === obj.id) {
				list[i] = obj;
			}
		}
	}
});
