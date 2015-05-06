jsPlumb.ready(function() {
 
  jsPlumb.setContainer($('#container_jsplumb'));
 	
 	// variable to track # of states
  var i = 0;
 
  $('#container_jsplumb').dblclick(function(e) {
    var newState = $('<div>').attr('id', 'state' + i).addClass('item');
    
    var title = $('<div>').addClass('title').text('State ' + i);
    var connect = $('<div>').addClass('connect');
    
/*    newState.css({
      'top': e.pageY,
      'left': e.pageX
    });*/
    
    newState.append(title);
    newState.append(connect);
    
    $('#container_jsplumb').append(newState);
 
    jsPlumb.makeTarget(newState, {
      anchor: 'Continuous'
    });
    
    jsPlumb.makeSource(connect, {
      parent: newState,
      anchor: 'Continuous'
    });

		jsPlumb.draggable(newState, {
		  containment: 'parent'
		});
		 
		newState.dblclick(function(e) {
		  jsPlumb.detachAllConnections($(this));
		  $(this).remove();
		  e.stopPropagation();
		});
    
    // increase id number to ensure uniqueness
    i++;   
  });  
});

function loadGraphTest(){
  jsPlumb.ready(function() {
  var length = elements_array.length;
  var length2 = connections_array.length;
  var targetlol;
  var sourcelol;
  var element;
  var questionType;
  var questionNumber;
  var newState;
  var title;
  var stateNameContainer;
  var stateName;
  var title_id;
  var connect;
  var str;
  var temp;
  var k;
  var i;
  
  for (k = 0; k <= length - 1; k++) { 
    element = elements_array[k];
    console.log(k);
    questionType = element.type;
    str = element.id;
    console.log(str);
    questionNumber = str.substring(5);

    newState = $('<div>').attr('id', element.id).addClass('item_question');
    newState.addClass(questionType);
    title = $('<div>').addClass('title_question');
    stateNameContainer = $('<div>').attr('data-state-id', element.id);
    stateName = $('<p>').text(element.name);
      // store element id as data attribute
      console.log("element id is " + element.id);
      stateName.attr('data-id', element.id);
      title_id = $('<p>').text('Pregunta ' + questionNumber);
      // append element id text to title
      title.append(title_id);
      // put stateName input field into stateNameContainer div, 
      // and then append this div to title
      title.append(stateNameContainer.append(stateName));
      connect = $('<div>').addClass('connect_question');
      newState.css({
        'top': element.top,
        'left': element.left
      });
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
        containment: 'parent'
      });
      newState.dblclick(function(e) {
        jsPlumb.detachAllConnections($(this));
        $(this).remove();
        e.stopPropagation();
      });   

    }

    trigger = "no";
    alert("conections array");
    console.log('conecitons array length: '+connections_array.length );
    console.log(connections_array);
    for (a = 0; a <= connections_array.length - 1; a++) {
      sourcelol = connections_array[a].source;
      targetlol = connections_array[a].target;
      
      console.log(sourcelol + " and " +targetlol);
      jsPlumb.connect({
        source: sourcelol, 
        target:targetlol
      });
      alert("hello world");

    }
    trigger = "yes";
  
});
  
}

$(document).ready(function(){
  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users';
	});

  $('#btn_test').on('click', function(){
    loadGraphTest();
  });

  /* Function to get the latest_item_id from query results
   * to use in any new items added to the flowchart
   * latest_item_id++ will be used as the starting point for new items 
   */
  function get_latest_id(){

  }

  /* Check all items are a source in at least one connection 
   * for use when user submits a finished flowchart
   * not used when user just saves a flowchart under construction
   */
  function check_item_connections(){

  }
});
