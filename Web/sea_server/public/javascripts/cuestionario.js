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

$(document).ready(function(){
  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users';
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
