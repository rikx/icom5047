	function ConverToJSON(formData){
		var result = {};
		$.each(formData, 
			function(i, o){
				result[o.name] = o.value;
		});
		return result;
	}

  // remove active from previous clicked list item inside 'list' table
  function remove_active_class(list){
    $(list).find('tr td a.active').removeClass('active');
  }

  // logout current session
 	function logout_helper(){

	}

	function populate_ganaderos(){
    $.getJSON('http://localhost:3000/ganaderos', function(data) {
    	return data.ganaderos;
    });
  }

  function populate_usuarios(){
    $.getJSON('http://localhost:3000/usuarios', function(data) {
    	return data.usuarios;
    });
  }
