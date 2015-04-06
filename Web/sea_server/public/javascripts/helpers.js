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

	//converts date yyyy-mm-dd to Month Day, Year format
	function convert_date(date){
		var the_date = new Date(date);
		var spanish_date = '';
		//month
		switch(the_date.getMonth()){
			case 0:
				spanish_date += 'Enero ';
				break;
			case 1:
				spanish_date += 'Febrero ';
				break;
			case 2:
				spanish_date += 'Marzo ';
				break;
			case 3:
				spanish_date += 'Abril ';
				break;
			case 4:
				spanish_date += 'Mayo ';
				break;
			case 5:
				spanish_date += 'Junio ';
				break;
			case 6:
				spanish_date += 'Julio ';
				break;
			case 7:
				spanish_date += 'Agosto ';
				break;
			case 8:
				spanish_date += 'Septiembre ';
				break;
			case 9:
				spanish_date += 'Octubre ';
				break;
			case 10:
				spanish_date += 'Noviembre ';
				break;
			case 11:
				spanish_date += 'Diciembre ';
				break;
		};
		// set day
		spanish_date += the_date.getDate();
		spanish_date += ', '
		// set year
		spanish_date += the_date.getFullYear();
		return spanish_date;
	};

	//
	function populate_ganaderos(){
    $.getJSON('http://localhost:3000/ganaderos', function(data) {
    	return data.ganaderos;
    });
  }

  //
  function populate_usuarios(){
    $.getJSON('http://localhost:3000/usuarios', function(data) {
    	return data.usuarios;
    });
  }
