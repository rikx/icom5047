jsPlumb.ready(function() {
	$preguntas_list = $('#preguntas_list');
	// stores current elements in the jsPlumb container
	var elements_array = [];
	var connections_array = [];

 	var j =0; // item id
 	var trigger = true;

  var data_username = $('#header_page').attr('data-username');
  var data_user_type = $('#header_page').attr('data-type');


  //  if(data_user_type == 'admin')
  // {
  //     $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Administrador'+ " - " +data_username+"</small> </h2>");
  // }
  // else if(data_user_type == 'agent')
  // {
  //   $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Agente'+ " - " +data_username+"</small> </h2>");
  // }
  // else
  // {
  //   $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Especialista'+ " - " +data_username+"</small> </h2>");
  // }


	jsPlumb.setContainer($('#container_plumbjs'));
	$('#container_plumbjs').resizable();

	// Return to admin page button
	$('#btn_home').on('click', function(){
		window.location.href = '/users';
	});

/*  // Close info panel
  $('#btn_close_info_panel').on('click', function(){
  	$('#info_panel').hide();
  	remove_active_class($preguntas_list);
  });*/

  $('#btn_edit_item').on('click', function(){
		$('#info_panel').hide();
		$('#edit_panel').show();

	  // contains element id
  	var item_id = $(this).attr('data-id');
  	var arrayPosition = elements_array.map(function(arrayItem) { return arrayItem.id; }).indexOf(item_id);
    var this_element = elements_array[arrayPosition];
		
		$('#item_label').val(this_element.name);

		//edit_answers_list
/*		if(this_element.type == 'MULTI' || this_element.type == 'OPEN'){
			$('#edit_possible_answers').show();
			var answers_content = '';
			$.each(connections_array, function(i){
				if(this_element.id == this.source){
					answers_content += "<input class='form-group' data-source-id='"+this.source+"' data-target-id='"+this.target+"' type='text' value='"+this.label+"'>"; 
				}
			});
			$('#edit_answers_list').html(answers_content);
		}*/
  });

	// Close info panel
  $('#btn_close_edit_panel').on('click', function(){
		//$('#info_panel').show();
		$('#edit_panel').hide();
		$('#edit_possible_answers').hide();

		remove_active_class($preguntas_list);
  });

  $('#btn_edit').on('click', function(){
	  // contains element id
  	var item_id = $(this).attr('data-id');
  	var arrayPosition = elements_array.map(function(arrayItem) { return arrayItem.id; }).indexOf(item_id);
    var this_element = elements_array[arrayPosition];

    var new_text = $('#item_label').val();
    // update element in ui
    $('#content_'+this_element.id).children('.has_text').html(new_text);
		$('#'+this_element.id).attr('data-state-name', new_text);

    // update element in array
    this_element.name = new_text;
    this_element.state = $('#'+this_element.id).prop('outerHTML');
    replace(this_element, elements_array);


    //update connection in array and ui

/*    $.each($('#edit_answers_list'), function(){
	    var this_connection;
	    for(var x = 0; x < connections_array.length; x++){
	    	if(connections_array[x].source == this.attr('data-source-id') && connections_array[x].target == this.attr('data-target-id')){
	    		this_connection = connections_array[x];
	    		$('#'+this.attr('data-source-id')+'-'+this.attr('data-target-id')).text(new_answer_text);
	    	}	
	    }
	    
	    this_connection.label = new_answer_text;
	    replace(this_connection, elements_array);
    });*/

    // refresh list
    populate_elements_list()

		$('#edit_panel').hide();
  });

  $('#btn_delete_item').on('click', function(){
    var this_id = $(this).attr('data-id');
    var source_id = $(this).attr('source-id');

    var $this_element = $('#'+this_id);
/*		// connections
		var source_conns = jsPlumb.getConnections({source: this_id});
		console.log(source_conns)
		for (var i = 0; i < source_conns.length; i++)
			jsPlumb.detach(source_conns[i]);

		var target_conns = jsPlumb.getConnections({target: this_id});
		console.log(target_conns)
		for (var i = 0; i < target_conns.length; i++)
			jsPlumb.detach(target_conns[i]);

		// endpoints
		deleteEmptyEndpoints(this_id);

		// dom element
   	$this_element.remove(); */

   	// alternate delete
   	//jsPlumb.detachAllConnections(this_id);
		//jsPlumb.removeAllEndpoints(this_id);
		//jsPlumb.detach(this_id);
		jsPlumb.remove(this_id);
    
    // find connections where this element is a source or target and 
    // delete them
    connections_array=connections_array.filter(isConnectionEndpoint);
    function isConnectionEndpoint(connection){
      if(connection.source == source_id){
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
    // repopulate elements list
    $('#info_panel').hide();
    populate_elements_list();
  });

	/* Open info panel */
  $preguntas_list.on('click', 'tr td a.show_info_elemento', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#info_panel').show();
    $('#edit_panel').hide();
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

    // set id values of info panel buttons
    $('#btn_edit_item').attr('data-id', this_element.id);
    $('#btn_delete_item').attr('data-id', this_element.id);
    $('#btn_delete_item').attr('source-id', this_element.connect_id);
    $('#btn_edit').attr('data-id', this_element.id);
	});

  /* Focus on list element matching clicked element and show info panel */
  $("#container_plumbjs").on('click', "div div p.item_title", function(){
    var this_id = $(this).attr('data-id');
    var $list_element = $preguntas_list.find("[data-id='"+this_id+"']");
    
    $list_element.focus().trigger('click');
  });

  /* Populate's info panel with $this_element's information */
  function populate_info_panel(this_element){
  	$('#pregunta_info_name').html(this_element.name);
  	$('#pregunta_info_type').html(convert_type(this_element.type));
  	
  	if(this_element.type == 'MULTI' || this_element.type == 'CONDITIONAL'){
	  	var answers_content = '';
	  	$.each(connections_array, function(i){
	  		if(this_element.connect_id == this.source){
	  			var label_text;
	  			if(this_element.type == 'CONDITIONAL'){
	  				//console.log(this.label.substring(0,2))
	  				switch(this.label.substring(0,2)){
	  					case 'lt':
	  						label_text = 'Menor que ' +this.label.substring(2);
	  						break;
	  					case 'gt':
	  						label_text = 'Mayor que ' +this.label.substring(2);
	  						break;
	  					case 'eq':
	  						label_text = 'Igual que ' +this.label.substring(2);
	  						break;
	  					case 'ne':
	  						label_text = 'No igual a ' +this.label.substring(2);
	  						break;
	  					case 'le':
	  						label_text = 'Menor o igual que ' +this.label.substring(2);
	  						break;
	  					case 'ge':
	  						label_text = 'Mayor o igual que ' +this.label.substring(2);
	  						break;
  						case 'ra':
  							label_text = 'En rango '+this.label.substring(2);
  							break;
  						case '!r':
  							label_text = 'Afuera del rango';
  							break;
	  					default:
	  						label_text = 'ERROR: ha escrito condicion incorrectamente';
		  			}
		  		} else {
						label_text = this.label;
					}
					answers_content += "<li class='list-group-item'>"+label_text+"</li>"; 
				}
	  	});
	  	$('#pregunta_info_responses').html(answers_content);
	  	$('#possible_answers').show();
	  } else {
	  	$('#possible_answers').hide();
	  }

	  // set id values of info panel buttons
    $('#btn_edit_item').attr('data-id', this_element.id);
    $('#btn_delete_item').attr('data-id', this_element.id);
    $('#btn_delete_item').attr('source-id', this_element.connect_id);
    $('#btn_edit').attr('data-id', this_element.id);
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
	    table_content += "show_info_elemento' href='#', data-id='"+this.id+"'>Elemento "+this.id.substring(5)+': '+this.name+"</a></td></tr>";
		});

		$('#preguntas_list').html(table_content);
		
		if(elements_array.length >0){
			populate_info_panel(elements_array[0]);
		}
	}

 	// JS PLUMB create code

 	$('#btn_add_question').on('click', function(){
 		$('#flowchart_info').show();
 		$('#item_list').show();
 		$('#info_panel').show();
 		var item_type = $('#btn_item_type').attr('data-item-type');
		// does not execute when selecting a start or an end element if it already exists
		if((item_type == 'START' && $('#start_item').hasClass('disabled')) 
				|| (item_type == 'END' && $('#end_item').hasClass('disabled'))){
			// do nothing
		} else {
 			add_item();
 		}
 	});

 	/* check if labels for an element's connections have the same name */
 	function check_equal_labels(element, the_label){
		for(var j = 0; j< connections_array.length; j++){
			if(element.connect_id == connections_array[j].source){
				if(the_label == connections_array[j].label){
					return false;
				}
			}
		}
		return true;
 	}

 	/* gets first and end item ids from elements_array, if they exist */
 	function check_for_endpoints(){
 		var first_item = -1
 			, end_item = -1;
 		for(var i=0; i<elements_array.length; i++){
 			if(elements_array[i].type == 'START'){
 				for(var x=0; x<connections_array.length; x++){
 					if(elements_array[i].connect_id == connections_array[x].source){
 						first_item = connections_array[x].target; // 'START' item's target
 					}	
 				}
 			} else if(elements_array[i].type == 'END'){
/* 				for(var x=0; x<connections_array.length; x++){
 					if(elements_array[i].id == connections_array[x].target){
 						end_item = connections_array[x].source;
 					}	
 				}*/
 				end_item = elements_array[i].id; // 'END' item
 			}
 		}
 		return {first_id: first_item, end_id: end_item};
 	}

  /* Check item type == 'START' is a source in only one connection
   * all items where type != 'END' || 'START' are a source in at least one connection
   * and item type == 'END' is a target in at least one connection
   * not used when user just saves a flowchart under construction
   */
  function check_item_connections(){
  	var start_count = 0;
  	var end_count = 0;
  	var valid_item_count = 0;
  	var multi_count = 0;
  	var this_element;
  	
  	for(var i = 0; i < elements_array.length; i++){
  		this_element = elements_array[i];
  		multi_count = 0;
  		for(var j = 0; j < connections_array.length; j++){
  			if(this_element.type == 'START'){
  				if(this_element.connect_id == connections_array[j].source){
	  				start_count++;
	  			}
  			} else if(this_element.type == 'END'){
  				if(this_element.id == connections_array[j].target){
  					end_count++;
  				}
  			} else {
  				if(this_element.connect_id == connections_array[j].source){
  					if(this_element.type == 'MULTI' || this_element.type == 'CONDITIONAL'){
  						if(multi_count < 2){
  							if(multi_count < 1){
									valid_item_count++;
								}
								multi_count++;
  						}
  					} else {
  						valid_item_count++;
  					}
	  			}
  			}
  		}
  		console.log('Item type and multi count');
  		console.log(this_element.type);
  		console.log(multi_count);
  		if((this_element.type == 'CONDITIONAL' && multi_count !=2) || (this_element.type == 'MULTI' && multi_count <2) ){
  			return false;
  		}
  	}
  	console.log(valid_item_count + ' - '+elements_array.length);
	  if(start_count == 1 && end_count > 0 && (valid_item_count==elements_array.length-2)){
			return true;
		} else {
			return false;
		}
  }

  /* */
  function check_conditionals(){
    var this_element;
    var element_conns = [];
    for(var i = 0; i < elements_array.length; i++){
      this_element = elements_array[i];
      if(this_element.type == 'CONDITIONAL'){
        for(var j = 0; j < connections_array.length; j++){
          if(this_element.connect_id == connections_array[j].source){
            element_conns.push(connections_array[j].label); 
          }
        }
        var first_cond = element_conns[0].substring(0,2);
        var second_cond = element_conns[1].substring(0,2);
        var first_number, second_number, temp;
        if(first_cond == 'ra' && second_cond == '!r'){
          var commma_index = element_conns[0].indexOf(',');
          temp = first_cond.substring(3,commma_index);
          first_number = parseFloat(temp, '10');
          temp = first_cond.substring(commma_index+1,element_conns[0].length-1);
          second_number = parseFloat(temp, '10');
          if(first_number == second_number){
            return false;
          }
        } else if(first_cond == '!r' && second_cond == 'ra'){
          var commma_index = element_conns[1].indexOf(',');
          temp = first_cond.substring(3,commma_index);
          first_number = parseFloat(temp, '10');
          temp = first_cond.substring(commma_index+1,element_conns[1].length-1);
          second_number = parseFloat(temp, '10');
          if(first_number == second_number){
            return false;
          }
        } else {
          first_number = parseFloat(element_conns[0].substring(2), '10');
          second_number = parseFloat(element_conns[1].substring(2), '10');
          console.log(element_conns);
          console.log(element_conns[0].substring(2));
          console.log(first_cond + ', ' + second_cond + ', '+ first_number + ', '+ second_number);
          if(first_number != second_number){
            return false;
          }
        }

        // if lt other must be ge
        if(first_cond == 'lt' && second_cond != 'ge'){
          return false;
        }
        // if gt other must be le
        if(first_cond == 'gt' && second_cond != 'le'){
          return false;
        }
        // if eq other must be ne
        if(first_cond == 'eq' && second_cond != 'ne'){
          return false;
        }
        // if lt other must be ge
        if(first_cond == 'ge' && second_cond != 'lt'){
          return false;
        }
        // if lt other must be ge
        if(first_cond == 'le' && second_cond != 'gt'){
          return false;
        }
        // if lt other must be ge
        if(first_cond == 'ne' && second_cond != 'eq'){
          return false;
        }
        // if ra other must be !ra
        if(first_cond == 'ra' && second_cond != '!r'){
          return false;
        }
        // if !ra other must be ra
        if(first_cond == '!r' && second_cond != 'ra'){
          return false;
        }
      }
    }
    return true;
  }

  /* POSTs new flowchart information */
  $('#btn_submit').on('click', function(){
  	var end_points = check_for_endpoints();
  	console.log('Elements:');
  	console.log(elements_array);
  	console.log('Connections:');
  	console.log(connections_array);

		// get form data and conver to json format
		var $the_form = $('#form_create_flowchart');
		var form_data = $the_form.serializeArray();
		var new_flowchart = ConverToJSON(form_data);
  	// checks created flowchart has a first item
  	if(!empty_field_check(form_data) && ($('input[name=ready_radios]:checked').length > 0)){
  		// check that endpoints are valid
	    if(end_points.first_id != -1 && end_points.end_id != -1){
        if(($('#container_plumbjs').find("textarea")).length == 0){
  	    	if(check_item_connections()){
  	    		if(check_conditionals()){
  						// add missing flowchart fields
  						new_flowchart.first_id = end_points.first_id;
  						new_flowchart.end_id = end_points.end_id;
  						new_flowchart.status = $("input[name=ready_radios]:checked").val();

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
  	    		} else {
  	    			alert('Condicionales no estan escritos correctamente.');
  	    		}
  	    	} else{
            alert('Verifique que todas las preguntas y recomendaciones tienen una conección hacia otro elemento. Las preguntas de selección multiple necesitan por lo menos 2 conecciones y las conditionales exactamente 2 conecciones.');
          }
        } else {
          alert("Verifique que todos los elementos tienen nombre");
	    	}
	    } else {
	    	alert('Cuestionario no tiene elemento inicial o final, o estos tienen conecciones existentes.');
	    }
	  } else {
	  	alert('Ingrese nombre y version de cuestionario e indique si el cuestionario está listo para uso.');
	  }
  });

 	//jsPlumb.Defaults.Container = $('#container_plumbjs');
	
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

  // convert type text
  function convert_type(type_text){
    switch(type_text){
      case 'MULTI':
        return 'MULTIPLE';
      case 'RECOM':
        return 'RECOMENDACIÓN';
      case 'OPEN':
        return 'ABIERTA';
      case 'CONDITIONAL':
        return 'COMPARACIÓN';
      case 'START':
        return 'INICIO';
      case 'END':
        return 'FIN';
    }
  }

 	function add_item() {
 		jsPlumb.ready(function() {
 			var itemType = $('#btn_item_type').attr('data-item-type');
 			var newState = $('<div>').attr('id', 'state' + j);
 			var title = $('<div>').addClass('title_question');
 			var title_id = $("<p class='item_title' data-id='state"+j+"'>");
 			var has_input = false;

 			// store item type in state
 			newState.attr('data-state-type', itemType);
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
					switch(itemType){
						case 'OPEN':
							newState.addClass('item_abierta');	
							break;
						case 'MULTI':
							newState.addClass('item_multi');	
							break;
						case 'CONDITIONAL':
							newState.addClass('item_conditional');	
							break;
						case 'RECOM':
							newState.addClass('item_recom');
							break;
					}
	 				// Set item identifier text
					title_id.html('<strong>Elemento ' + j+"</strong>");
					title_id.append('<p><strong>['+convert_type(itemType)+']</strong></p>');

					// add input for item text content
					has_input = true;
	 				var stateNameContainer = $('<div>').attr('data-state-id', 'state' + j);
	 				stateNameContainer.attr('id', 'content_state' + j);
	 				
	 				var stateName;
	 				//if(itemType == 'RECOM' || itemType == 'OPEN'){
	 					stateName = $('<textarea>').attr('type', 'text').attr('rows', '3');;
	 				//} 
/*	 				else {
	 					stateName = $('<input>').attr('type', 'text');
					}*/

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
			connect.attr('id', 'connect' + j);
			newState.attr('data-connect-id', 'connect' + j);

			newState.append(connect);

			//add new state item to container
			$('#container_plumbjs').append(newState);

			if(itemType != 'START'){
				jsPlumb.makeTarget(newState, {
					anchor: 'Continuous',
					endpoint:'Blank'
				});
			}

			if(itemType != 'END'){
				jsPlumb.makeSource(connect, {
					parent: newState,
					anchor: 'Continuous',
					connector: 'Flowchart',
					endpoint:'Blank'
				});				
			}
   		
			// create item object
			var new_name = $('#'+newState.attr('id')).attr('data-state-name');
			if(new_name == undefined){
				new_name = 'Elemento sin texto';
			}
   		// initial add to the global array
      var state_html = $('#'+newState.attr('id')).clone().removeClass('ui-draggable ui-draggable-dragging ui-droppable _jsPlumb_endpoint_anchor _jsPlumb_connected');
			state_html.children('.connect_question').removeClass('ui-draggable ui-draggable-dragging ui-droppable _jsPlumb_endpoint_anchor _jsPlumb_connected');
      console.log(state_html.prop('outerHTML'));
      var new_item = {
				id: newState.attr('id'),
				connect_id: newState.attr('data-connect-id'),
				name: new_name,
				type: itemType,
				state: state_html.prop('outerHTML')
			};

			if(containsObject(new_item, elements_array)) {
			 	replace(new_item, elements_array);
			} else {
			 	elements_array.push(new_item);
			}
			populate_elements_list();

			jsPlumb.draggable(newState, {
				//containment: 'parent',
				resizable: false,
				drag: function(){
					jsPlumb.repaintEverything();
				},
				stop: function(event) {
					if ($(event.target).find('select').length == 0) {

						// create item object
						var this_name = $('#'+newState.attr('id')).attr('data-state-name');
						if(this_name == undefined){
							this_name = 'Elemento sin texto'
						}

            var state_html = $('#'+newState.attr('id')).clone().removeClass('ui-draggable ui-draggable-dragging ui-droppable _jsPlumb_endpoint_anchor _jsPlumb_connected');
						state_html.children('.connect_question').removeClass('ui-draggable ui-draggable-dragging ui-droppable _jsPlumb_endpoint_anchor _jsPlumb_connected');
            console.log(state_html.prop('outerHTML'))
            var this_item = {
							id: newState.attr('id'),
							connect_id: $('#'+newState.attr('id')).attr('data-connect-id'),
							name: this_name,
							type: itemType,
							state: state_html.prop('outerHTML')
						};

						// check if item is already in array and updates it
					  if(containsObject(this_item, elements_array)) {
					   	replace(this_item, elements_array);
					  } else {
					   	elements_array.push(this_item);
						}
						// populate elements list with new item
						//populate_elements_list();
					}
					jsPlumb.repaintEverything();
				}
			});


/*			newState.dblclick(function(e) {
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
				// repopulate elements list
				$('#info_panel').hide();
				populate_elements_list();

			}); */

			if(itemType != 'START' && itemType != 'END'){
				stateName.keyup(function(e) {
					if (e.keyCode === 13) {
			      //var state = $(this).closest('.item');
			      //state.children('.title').text(this.value);

			      $(this).parent().html("<p class='has_text'>"+this.value+'</p>');
			      var state_id = $(this).attr('data-id');
			      $('#'+state_id).attr('data-state-name', this.value);

			      // repaint it do to the resize
			      jsPlumb.repaintEverything();
						// create item object
            var state_html = $('#'+newState.attr('id')).clone().removeClass('ui-draggable ui-draggable-dragging ui-droppable _jsPlumb_endpoint_anchor _jsPlumb_connected');
						state_html.children('.connect_question').removeClass('ui-draggable ui-draggable-dragging ui-droppable _jsPlumb_endpoint_anchor _jsPlumb_connected');
            console.log(state_html.prop('outerHTML'))
            var this_item = {
							id: newState.attr('id'),
							connect_id: $('#'+newState.attr('id')).attr('data-connect-id'),
							name: $('#'+newState.attr('id')).attr('data-state-name'),
							type: itemType,
							state: state_html.prop('outerHTML')
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


	jsPlumb.bind("beforeDrop", function(connection) {
		if(trigger){
			var state_number = connection.sourceId.substring(7);
			var source_id = "state"+state_number;
			var target_id = connection.targetId;

			//check if source and target are the same
			if(source_id == target_id){
				return false;
			}

			// get source item type
			for(var y = 0; y<elements_array.length; y++){
				this_item = elements_array[y];
				if(this_item.id == source_id){
					source_type = this_item.type;
				} else if(this_item.id == target_id){
					target_type = this_item.type;
				}
			}

			//checks if source and target are recommendations
			if(source_type == 'RECOM' && target_type == 'RECOM'){
				return false;
			}

			//if target is 'END' sources must be 'RECOM'
			if(target_type == 'END' && source_type != 'RECOM'){
				return false;
			}

			// check source and target pair does not exist
			var source_is_source = 0;

			for(var z = 0; z<connections_array.length; z++){
				if('connect'+state_number == connections_array[z].source && target_id == connections_array[z].target){
					return false;
				}
				if('connect'+state_number == connections_array[z].source){
					source_is_source++;
				}
			}

			if(source_type == 'OPEN' && source_is_source == 1){
				return false;
			}

			if(source_type == 'RECOM' && source_is_source == 1){
				return false;
			}

			var start_count = 0;
			// if source_type == 'START' check its not connected to an 'END' type
			if(source_type == 'START'){
				// dont allow start element to connect to end
				if(target_type == 'RECOM' || target_type == 'END'){
					return false;
				}
				for(var z = 0; z<connections_array.length; z++){
					if('connect'+state_number == connections_array[z].source){
						start_count++;
					} 
				}
				// dont allow 'START' to have more than 1 connection;
				if(start_count >= 1){
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
	});

	jsPlumb.bind("connection", function(info, originalEvent) {
		//jsPlumb.ready(function() {
			info.connection.addOverlay( [ "Arrow", { width:20, length:20, location:1, id:"arrow" } ]);
			info.connection.setPaintStyle( {lineWidth:10,strokeStyle:'rgb(204,255,204)'});
			
			if(trigger){
				var this_connection, source_type, target_type, mylabel;
				var state_number = info.sourceId.substring(7);
				var source_id = "state"+state_number;
				var target_id = info.targetId;

				for(var y = 0; y<elements_array.length; y++){
					this_item = elements_array[y];
					if(this_item.id == source_id){
						source_type = this_item.type;
						console.log('source type:' +source_type);
					} else if(this_item.id == target_id){
						target_type = this_item.type;
						console.log('target type:' +target_type);
					}
				}
				if(source_type == 'START'){
					mylabel = 'con-inicio';
				} else if(source_type == 'OPEN'){
					mylabel = 'con-abierta';
				} else if(target_type == 'END' && (source_type == 'OPEN' || source_type == 'RECOM')){
					mylabel = 'con-fin';
				} else if(source_type == 'RECOM'){
					mylabel = 'con-recom';
				} else {
					mylabel = prompt("Escriba la posible respuesta a la pregunta.");
          while(mylabel == null){
            mylabel = prompt("Si le dio al botón de cancel por error, escriba el texto y presione 'OK'. Si cometío un error presione 'OK' y luego borre el elemento.");
          }		
				}

				info.connection.addOverlay(["Label", { label: mylabel, location:0.5, id: 'connect'+state_number+'-'+target_id} ]);

				this_connection = {
					source: info.sourceId,
					target: info.targetId,
					label: mylabel
				};

				connections_array.push(this_connection);
			} else if(!trigger){
				var state_number = info.sourceId.substring(7);
				var source_id = "state"+state_number;
				var target_id = info.targetId;
				console.log('Creating connection: connect'+state_number+ ' to '+target_id);
/*
				for(z = 0; z<lines_array.length; z++){
					this_connection = lines_array[z];
					if(this_connection.source == source_id && this_connection.target == target_id){
						mylabel = this_connection.label;
						info.connection.addOverlay(["Label", { label: mylabel, location:0.5, id: "connLabel"} ]);
					}
				}	*/
			}

			populate_elements_list();
		//});
	});

	$('#container_plumbjs').on("scroll", function(){
		jsPlumb.repaintEverything();
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
