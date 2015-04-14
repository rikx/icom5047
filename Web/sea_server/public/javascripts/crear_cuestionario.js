$(document).ready(function(){
	$preguntas_list = $('#preguntas_list');
	// stores current elements in the jsPlumb container
	var elements_array = [];

	//disable add question button initially
	$('#btn_add_question').prop('disabled', true);

	// Return to admin page button
	$('#btn_home').on('click', function(){
		window.location.href = '/users/admin';
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
});

  function populate_elements_list(){
		// add new element to elements list
		var table_content = '';

		$.each(elements_array, function(i){
			table_content += "<tr><td><a class='list-group-item ";
	    // if initial list item, set to active
	    if(i==0) {
	    	table_content +=  'active ';
	    }
	    table_content += "show_info_elemento' href='#', data-id='"+this.id+"'>"+this.type+' '+this.id+': '+this.name+"</a></td></tr>";
	});

		$('#preguntas_list').html(table_content);
	}
 	// JS PLUMB create code

 	$('#btn_add_question').on('click', function(){
 		AddQuestion();
 	});

 	$('#btn_add_recommendation').on('click', function(){
 		AddRecommendation();
 	});

 	$('#btn_submit').on('click', function(){
 		saveGraph();
 	});

 	$('#btn_load').on('click', function(){
 		loadGraph();
 	});

 	var j =0;
 	var array = [];
 	var arrayConnection = [];
 	var trigger = "yes";
 	jsPlumb.Defaults.Container = $('#container_plumbjs');

 	$('#list_question_type').on('click', 'li a', function(e){
     // prevents link from firing
     e.preventDefault();
     $('#btn_question_type_text').text($(this).text()+' ');
     $('#btn_question_type').val($(this).attr('data-usario-type'));
     var questionType = $('#btn_question_type_text').text($(this).text()+' ').text();
     if(questionType == 'Tipo de Pregunta'){
     	$('#btn_add_question').prop('disabled', true);
     }else
     {
     	$('#btn_add_question').prop('disabled', false);

     }

 });

 	function AddQuestion() {

 		jsPlumb.ready(function() {

 			var questionType = $('#btn_question_type_text').text();
 			var newState = $('<div>').attr('id', 'state' + j).addClass('item_question');
 			newState.addClass(questionType);
 			var title = $('<div>').addClass('title_question');
 			// 
 			var stateNameContainer = $('<div>').attr('data-state-id', 'state' + j);
 			var stateName = $('<input>').attr('type', 'text');

			// store element id as data attribute
			stateName.attr('data-id', 'state' + j);
			var title_id = $('<p>').text('Pregunta ' + j);
			// append element id text to title
			title.append(title_id);
			// put stateName input field into stateNameContainer div, 
			// and then append this div to title
			title.append(stateNameContainer.append(stateName));

			var connect = $('<div>').addClass('connect_question');
			newState.append(title);
			newState.append(connect);

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
				stop: function(event) {
					if ($(event.target).find('select').length == 0) {
					//saveState(event.target);
					array.push(newState);
					   // create element object
					   var this_element = {
					   	id: newState.attr('id'),
					   	name: $('#'+newState.attr('id')).attr('data-state-name'),
					   	type: questionType,
					   	left: newState.position().left,
					   	top: newState.position().top
					   };

			  console.log(this_element.id);
			  console.log(this_element.name);
			  console.log(this_element.left);
			  console.log(this_element.type);
		      // push to elements_array
		      elements_array.push(this_element);
		      // populate elements list with new element
		      populate_elements_list();
		  }
		}
	});

			newState.dblclick(function(e) {
				jsPlumb.detachAllConnections($(this));
				$(this).remove();
				e.stopPropagation();

			}); 

			stateName.keyup(function(e) {
				if (e.keyCode === 13) {
		      //var state = $(this).closest('.item');
		      //state.children('.title').text(this.value);
		      $(this).parent().text(this.value);
					var state_id = $(this).attr('data-id')
					$('#'+state_id).attr('data-state-name', this.value);
		      // // create element object
		      // var this_element = {
		      // 	id: $(this).attr('data-id'),
		      // 	name: this.value,
		      // 	type: 'Pregunta'
		      // };
		      // // push to elements_array
		      // elements_array.push(this_element);
		      // // populate elements list with new element
		      // populate_elements_list();
		  }
		});

			// focuses on input field of the created element
			stateName.focus();

			j++; 
		});
}


function AddLastNode() {

	jsPlumb.ready(function() {
		var newState = $('<div>').attr('id', 'state' + j).addClass('item_rec');
		var title = $('<div>').addClass('title_rec');
		var stateNameContainer = $('<div>');
		var stateName = $('<input>').attr('type', 'text');

		// store element id as data attribute
		stateName.attr('data-id', j);
		var title_id = $('<p>').text('Recomendación ' + j);
		// append element id text to title
		title.append(title_id);
		// put stateName input field into stateNameContainer div, 
		// and then append this div to title
		title.append(stateNameContainer.append(stateName));

		var connect = $('<div>').addClass('connect_rec');
		newState.append(title);
		newState.append(connect);


		

		$('#container_plumbjs').append(newState);


		jsPlumb.makeTarget(newState, {
			anchor: 'Continuous'
			
		});

		jsPlumb.makeSource(connect, {
			parent: newState,
			anchor: 'Continuous',
			connector: 'StateMachine',
			paintStyle:{ strokeStyle:"blue"}
			

		});   

		jsPlumb.draggable(newState, {
			containment: 'parent'
		});


		newState.dblclick(function(e) {
			jsPlumb.detachAllConnections($(this));
			$(this).remove();
			e.stopPropagation();

		});   

		stateName.keyup(function(e) {
			if (e.keyCode === 13) {
	      //var state = $(this).closest('.item');
	      //state.children('.title').text(this.value);
	      $(this).parent().text(this.value);

	    	// create element object
	    	var this_element = {
	    		id: $(this).attr('data-id'),
	    		name: this.value,
	    		type: 'Recomendación'
	    	};
	      // push to elements_array
	      elements_array.push(this_element);
	      // populate elements list with new element
	      populate_elements_list();
	  }
	});

		// focuses on input field of the created element
		stateName.focus();
		j++; 
	})
}

function AddRecommendation() {

	jsPlumb.ready(function() {
		var newState = $('<div>').attr('id', 'state' + j).addClass('item_rec');
		var title = $('<div>').addClass('title_rec');
		var stateNameContainer = $('<div>');
		var stateName = $('<input>').attr('type', 'text');

		// store element id as data attribute
		stateName.attr('data-id', j);
		var title_id = $('<p>').text('Recomendación ' + j);
		// append element id text to title
		title.append(title_id);
		// put stateName input field into stateNameContainer div, 
		// and then append this div to title
		title.append(stateNameContainer.append(stateName));

		var connect = $('<div>').addClass('connect_rec');
		newState.append(title);
		newState.append(connect);


		

		$('#container_plumbjs').append(newState);


		jsPlumb.makeTarget(newState, {
			anchor: 'Continuous'
			
		});

		jsPlumb.makeSource(connect, {
			parent: newState,
			anchor: 'Continuous',
			connector: 'StateMachine',
			paintStyle:{ strokeStyle:"blue"}
			

		});   

		jsPlumb.draggable(newState, {
			containment: 'parent'
		});


		newState.dblclick(function(e) {
			jsPlumb.detachAllConnections($(this));
			$(this).remove();
			e.stopPropagation();

		});   

		stateName.keyup(function(e) {
			if (e.keyCode === 13) {
	      //var state = $(this).closest('.item');
	      //state.children('.title').text(this.value);
	      $(this).parent().text(this.value);

	    	// create element object
	    	var this_element = {
	    		id: $(this).attr('data-id'),
	    		name: this.value,
	    		type: 'Recomendación'
	    	};
	      // push to elements_array
	      elements_array.push(this_element);
	      // populate elements list with new element
	      populate_elements_list();
	  }
	});

		// focuses on input field of the created element
		stateName.focus();
		j++; 
	})
}


$('#connect_item').dblclick(function(e) {
	alert("hello world");
	e.stopPropagation();

}); 


jsPlumb.bind("connection", function(info, originalEvent) {

	if(trigger == "yes"){
//info.connection.setLabel("FOO");
arrayConnection.push(info.sourceId);
arrayConnection.push(info.targetId);
var mylabel = prompt("Por favor, escriba la respuesta a la pregunta.");
console.log(info.connection.getOverlays());
info.connection.addOverlay([ "Arrow", { width:30, height:30, location: [0.1, 0.1], id:"arrow" }]); 
info.connection.addOverlay([ "Label", { id:"label", label:mylabel, borderWidth:20}]); 
info.connection.setPaintStyle({lineWidth:10,strokeStyle:'rgb(0,225,0)'});
console.log(info.connection.getOverlays());
}
});


function saveGraph()
{
	console.log(array);
}

function loadGraph()
{
	var targetlol;
	var sourcelol;
	var state;

	var length = array.length;
	var length2 = arrayConnection.length/2;
	for (k = 0; k <= length - 1; k++) { 

		state = array.pop();


		//EED TO FIND A WAY TO GET "connect" object back to make it a source
		jsPlumb.draggable(state, {
			containment: 'parent'
		});
		state.dblclick(function(e) {
			jsPlumb.detachAllConnections($(this));
			$(this).remove();
			e.stopPropagation();

		}); 
		jsPlumb.makeTarget(state, {
			anchor: 'Continuous',
			endpoint:'Blank'
		});
		$('#container_plumbjs').append(state);


	}


	trigger = "no";


	for (a = 0; a <= length2 - 1; a++) { 

		targetlol = arrayConnection.pop();
		sourcelol = arrayConnection.pop();
		var temp = jsPlumb.connect({source: sourcelol, target:targetlol});
		temp.addOverlay([ "Arrow", { width:30, height:30, location: [0.1, 0.1], id:"arrow" }]); 
		temp.addOverlay([ "Label", { location: [-.25, 1.1], id:"label", label:"foo"}]); 

	}

	trigger = "yes";

}

function checkGraph(){


	console.log(arrayConnection);

}

$('#container_plumbjs').scroll(function(){
	jsPlumb.repaintEverything();
});

});