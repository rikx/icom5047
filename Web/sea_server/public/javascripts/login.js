$(document).ready(function() {      
	/* POSTs login user */
	$('#btn-login').on('click', function(){
	  // get form data and conver to json format
	  var $the_form = $('#form-login');
	  var form_data = $the_form.serializeArray();
	  var new_user = ConverToJSON(form_data);

	  // ajax call to post login
	  $.ajax({
	    url: "/login",
	    method: "POST",
	    data: JSON.stringify(new_user),
	    contentType: "application/json",
	    dataType: "json",

	    success: function(data) {
	    	if((typeof data.user_found == 'boolean' && data.user_found == false) || (typeof data.pass_found == 'boolean' && data.pass_found == false)){
	    		//alert('Usuario "'+new_user.input_username+'" no existe en el sistema. Verifique que el nombre de usuario sea correcto.');
	    		alert("Verifique que el nombre de usuario y contraseña han sido escritas correctamente");
	    	} else {
		    	if(typeof data.redirect == 'string') {
				    window.location.replace(window.location.protocol + "//" + window.location.host + data.redirect);
				  }
	    	}
/*	    	
	    	if(typeof data.pass_found == 'boolean' && data.pass_found == false){
	    		alert('Contraseña incorrecta.');
	    	} */
	    },
	    error: function( xhr, status, errorThrown ) {
	      alert( "Sorry, there was a problem!" );
	      console.log( "Error: " + errorThrown );
	      console.log( "Status: " + status );
	      console.dir( xhr );
	    }
	  });
	});
});