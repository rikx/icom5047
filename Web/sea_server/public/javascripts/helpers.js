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

	// verifies input value is in array and returns boolean result
	function valid_input(user_input, array) {
		for(var i=0; i < array.length; i++){
			if(user_input == array[i].location_name){
				return true;
			}
		}
		return false;
	}

	// converts date yyyy-mm-dd to spanish Month Day, Year format
	// if get_time is true create {date, time} object and return it
	function get_date_time(date, want_time){
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


		if(!want_time) {
			return spanish_date;
		} else {
			var the_time = get_time(the_date);
			return {date : spanish_date, time : the_time};
		}
	};

	//
	function get_time(date){
		var time = '';

		var hours = date.getHours();
		var minutes = date.getMinutes();
		if(hours > 12) {
			time += hours-12 + ':';
			time += date.getMinutes();
			time += ' PM';
		} else {
			if (hours == 0) {
				time += '12:';
			} else {
				time += hours + ':';
			}

			if (minutes < 10)
				time += '0';
			time += minutes;
			time += ' AM';
		}
		return time;
	};

	// given a collection of html elements
	// injects formated date html into each element, 
	// using its data-date contents as the date to be formated
	function format_dates(elements){
		$.each(elements, function(){
			var date = $(this).attr('data-date');
			var time = $(this).attr('data-time');
			$(this).html(get_date_time(date, false)+' at '+time);
		});
	};