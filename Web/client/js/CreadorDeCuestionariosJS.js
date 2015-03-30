var j =0;
var array = [];
var arrayConnection = [];
var trigger = "yes";
jsPlumb.Defaults.Container = $('#container_plumbjs');


 
function AddQuestion() {

	jsPlumb.ready(function() {
		var newState = $('<div>').attr('id', 'state' + j).addClass('item_question');
		var title = $('<div>').addClass('title_question');
		var stateName = $('<input>').attr('type', 'text');
		title.append(stateName);
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
			containment: 'parent',
			stop: function(event) {
				if ($(event.target).find('select').length == 0) {
					//saveState(event.target);
					array.push(newState);
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
  }
});

		stateName.focus();
		j++; 
	})
}


function AddRecommendation() {

	jsPlumb.ready(function() {
		var newState = $('<div>').attr('id', 'state' + j).addClass('item_rec');
		var title = $('<div>').addClass('title_rec');
		var stateName = $('<input>').attr('type', 'text');
		title.append(stateName);
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
  }
});

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

console.log(info.connection.getOverlays());
info.connection.addOverlay([ "Arrow", { width:30, height:30, location: [0.1, 0.1], id:"arrow" }]); 
info.connection.addOverlay([ "Label", { location: [-.25, 1.1], id:"label", label:"foo"}]); 
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

	var length = array.length;
	var length2 = arrayConnection.length/2;
	for (k = 0; k <= length - 1; k++) { 

		$('#container_plumbjs').append(array.pop());

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

    $('#container_plumbjs').scroll(
                function(){
                    jsPlumb.repaintEverything();
                }
            )

