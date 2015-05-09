jsPlumb.ready(function() {
  $preguntas_list = $('#preguntas_list');
  // stores current elements in the jsPlumb container
  var elements_array;
  var connections_array;

  var state_count = 0;

  // store data for flowchart
  var user_info = JSON.parse($('#flowchart_jumbotron').attr('data-user'));
  var data_items = $preguntas_list.attr('data-items');
  var data_connections = $preguntas_list.attr('data-connections');
  if(data_items.length >2){
    elements_array = JSON.parse($preguntas_list.attr('data-items'));
    if(data_connections.length >2){
      connections_array = JSON.parse($preguntas_list.attr('data-connections'));
    }
    
    // initial info panel population
    populate_info_panel(elements_array[0]);
    recreate_graph(elements_array, connections_array)
  } else {
    $('#info_panel').hide();
  }
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
    
    if(this_element.type == 'MULTI' || this_element.type == 'CONDITIONAL'){
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
            first_item = connections_array[x].target; // 'START' item's target
          } 
        }
      } else if(elements_array[i].type == 'END'){
/*        for(var x=0; x<connections_array.length; x++){
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
          if(this_element.id == connections_array[j].source){
            start_count++;
          }
        } else if(this_element.type == 'END'){
          if(this_element.id == connections_array[j].target){
            end_count++;
          }
        } else {
          if(this_element.id == connections_array[j].source){
            if(this_element.type == 'MULTI' || this_element.type == 'CONDITIONAL'){
              if(multi_count < 1){
                valid_item_count++;
                multi_count++;
              }
            } else {
              valid_item_count++;
            }
          }
        }
      }
    }
    console.log(valid_item_count + ' - '+elements_array.length);
    if(start_count == 1 && end_count > 0 && (valid_item_count==elements_array.length-2)){
      return true;
    } else {
      return false;
    }
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
    if(!empty_field_check(form_data)){
      // check 
      if(end_points.first_id != -1 && end_points.end_id != -1){
        if(check_item_connections()){
          // add missing flowchart fields
          new_flowchart.first_id = end_points.first_id;
          new_flowchart.end_id = end_points.end_id;
          // ajax call to post new ganadero
          $.ajax({
            url: "http://localhost:3000/users/admin/cuestionarios/crear",
            method: "POST",
            data: JSON.stringify({
              info: new_flowchart,
              items: states_array,
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
          alert('Verifique que todas preguntas y recomendaciones tienen una conección hacia otro elemento');
        }
      } else {
        alert('Cuestionario no tiene preguntas o recomendaciones');
      }
    } else {
      alert('Ingrese nombre y version de cuestionario');
    }
  });


  var j =0; // item id
  var trigger = true;
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
   
      jsPlumb.draggable(newState, {
        containment: 'parent',
        stop: function(event) {
          if ($(event.target).find('select').length == 0) {

            // create item object
            var this_name = $('#'+newState.attr('id')).attr('data-state-name');
            if(this_name == undefined){
              this_name = 'Elemento sin título'
            }
            var this_item = {
              id: newState.attr('id'),
              name: this_name,
              type: itemType,
              left: newState.position().left,
              top: newState.position().top
            };

            var state_item = {
              id: newState.attr('id'),
              type: itemType,
              state: $('#'+newState.attr('id')).prop('outerHTML')
            }

            // check if item is already in array and updates it
            if(containsObject(this_item, elements_array)) {
              replace(this_item, elements_array);
              replace(state_item, states_array);
            } else {
              elements_array.push(this_item);
              states_array.push(state_item);
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
        // populate elements list with new element
        populate_elements_list();
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

            var state_item = {
              id: newState.attr('id'),
              type: itemType,
              state: $('#'+newState.attr('id')).prop('outerHTML')
            }

            // check if item is already in array and updates it
            if(containsObject(this_item, elements_array)) {
              replace(this_item, elements_array);
              replace(state_item, states_array);
            } else {
              elements_array.push(this_item);
              states_array.push(state_item);
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
      var source_id = connection.sourceId;
      var target_id = connection.targetId;

      // get source item type
      for(var y = 0; y<elements_array.length; y++){
        this_item = elements_array[y];
        if(this_item.id == source_id){
          source_type = this_item.type;
        } else if(this_item.id == target_id){
          target_type = this_item.type;
        }
      }
      var start_count = 0;
      // if source_type == 'START' check its not connected to an 'END' type
      if(source_type == 'START'){
        // dont allow start element to connect to end
        if(target_type == 'END'){
          return false;
        } 
        for(var z = 0; z<connections_array.length; z++){
          if(source_id == connections_array[z].source){
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
        var source_id = info.sourceId;
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
          mylabel = 'con-start';
        } else if(source_type == 'OPEN'){
          mylabel = 'con-open';
        } else if(target_type == 'END' && (source_type == 'OPEN' || source_type == 'RECOM')){
          mylabel = 'con-end';
        } else {
          mylabel = prompt("Por favor, escriba la respuesta a la pregunta.");
          info.connection.addOverlay(["Label", { label: mylabel, location:0.5, id: "connLabel"} ]);
        }
        
        this_connection = {
          source: info.sourceId,
          target: info.targetId,
          label: mylabel
        };

        connections_array.push(this_connection);
        lines_array.push(this_connection);
      } else if(!trigger){
        //var this_connection, mylabel;
        var source_id = info.sourceId;
        var target_id = info.targetId;
        console.log('Creating connection: '+source_id+ ' to '+target_id);
/*
        for(z = 0; z<lines_array.length; z++){
          this_connection = lines_array[z];
          if(this_connection.source == source_id && this_connection.target == target_id){
            mylabel = this_connection.label;
            info.connection.addOverlay(["Label", { label: mylabel, location:0.5, id: "connLabel"} ]);
          }
        } */
      }
    //});
  });

  $('#container_plumbjs').scroll(function(){
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

  // test recreation
  $('#btn_recreate').on('click', function(){
    recreate_graph(elements_array, connections_array);
  });
  // recreate graph
  function recreate_graph(elements, connections){
    jsPlumb.ready(function() {
      // loop through elements to create them in the DOM
      var this_id;
      var thing;
      for(var i=0; i<elements.length; i++){
        var theState = $.parseHTML(elements[i].state);
        state_count = elements[i].state_id.substring(5) + 1;
        
        console.log(elements[i].state_id);

        $('#container_plumbjs').append(theState);
        console.log($('#' + elements[i].state_id));
        thing = $('#' + elements[i].state_id).children('.connect_question');
        $('#' + elements[i].state_id).removeClass('ui-draggable ui-draggable-dragging'); //

        jsPlumb.draggable('' + elements[i].state_id, {
          //containment: 'parent'
        });

/*        jsPlumb.makeTarget('' + elements[i].state_id, {
          anchor: 'Continuos',
          connector: 'Flowchart'
        });

        jsPlumb.makeSource(thing, {
          parent: '' + elements[i].state_id,
          anchor: 'Continuos',
          connector: 'Flowchart',
          endpoint: 'Blank'
        });*/

/*        console.log(elements[i]);
        this_id = elements[i].id;
        var itemType, newState, title, title_id, has_input, stateName, stateNameContainer, connect;
        itemType = elements[i].type;
        newState = $('<div>').attr('id', this_id);
        title = $('<div>').addClass('title_question');
        title_id = $('<p>');
        has_input = false;

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
            title_id.text('Elemento ' + this_id.substring(5));
            // add input for item text content
            has_input = true;
            stateNameContainer = $('<div>').attr('data-state-id', this_id);
            stateName = $('<p>').text(elements[i].name);

            // store element id as data attribute
            stateName.attr('data-id', this_id);
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
        connect = $('<div>').addClass('connect_question');
        newState.append(connect);

        newState.css({
          'top': elements[i].top,
          'left': elements[i].left
        });

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
     
        jsPlumb.draggable(newState, {
          containment: 'parent',
          stop: function(event) {
            if ($(event.target).find('select').length == 0) {

              // create item object
              var this_name = $('#'+newState.attr('id')).attr('data-state-name');
              if(this_name == undefined){
                this_name = 'Elemento sin título'
              }
              var this_item = {
                id: newState.attr('id'),
                name: this_name,
                type: itemType,
                left: newState.position().left,
                top: newState.position().top
              };

              // check if item is already in array and updates it
              if(containsObject(this_item, elements_array)) {
                replace(this_item, elements_array);
                replace(this_item, states_array);
              } else {
                elements_array.push(this_item);
                states_array.push(this_item);
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
          // populate elements list with new element
          populate_elements_list();
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
                replace(this_item, states_array);
              } else {
                elements_array.push(this_item);
                states_array.push(this_item);
              }
              // populate elements list with new element
              populate_elements_list();
            }
          });
          // focuses on input field of the created element
          stateName.focus();
        }*/
      } // end of for loop for elements

      console.log('States:');
      console.log(elements);
      console.log('Connections to add: ');
      console.log(connections);
      // loop through connections to connect elements in the DOM
      var source_item, target_item, this_element, this_connection;
      trigger = false;
      console.log(connections.length);
      for(var count = 0; count < connections.length; count++){
        this_connection = connections[count];
        for(var x = 0; x < elements.length; x++){
          this_element = elements[x];
          if(this_element.state_id == this_connection.source){
            //source_item = document.getElementById(this_element.id);
            source_item = this_element.state_id;
            console.log('Calling connect on source: '+this_element.state_id);
          } else if(this_element.state_id   == this_connection.target){
            //target_item = document.getElementById(this_element.id);
            target_item = this_element.state_id;
            console.log('Calling connect on target: '+this_element.state_id);
          }
        }
        var common = {
          anchors: ['Continuous', 'Continuous'],
          endpoint:'Blank'
        };
        jsPlumb.connect({
          source: source_item, 
          target: target_item,
          anchors: ['Continuous', 'Continuous'],
          connector: 'Flowchart',
          endpoint:'Blank',
          overlays: [["Label", { label: this_connection.label, location:0.5, id: "connLabel"+count} ]]
        });
      }
      trigger = true;
    });
  }
  $('#btn_repopulate').on('click', function(){
    var flowchart_id = $(this).attr('data-id');
    $.getJSON('http://localhost:3000/users/admin/cuestionarios/ver/'+flowchart_id, function(data) {
      states_array = data.items;
      connections_array = data.connections;
      recreate_graph(states_array,connections_array);
    });
  });
});
