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

