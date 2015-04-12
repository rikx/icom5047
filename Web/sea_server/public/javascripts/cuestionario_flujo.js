$(document).ready(function(){
	// store current element's information and its children
	var element_family; 

	function get_next_element() {
		$.getJSON('http://localhost:3000/element/:id', function(data) {
    	element_family = data.element_family;
    });
	}

});