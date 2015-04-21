$(document).ready(function() {

 // if(typeof data.redirect == 'string') {
 //    window.location.replace(window.location.protocol + "//" + window.location.host + data.redirect);
 //  }
        
	/* POSTs new ganadero information */
	$('#btn-login').on('click', function(){
	  // get form data and conver to json format
	  var $the_form = $('#form-login');
	  var form_data = $the_form.serializeArray();
	  var new_user = ConverToJSON(form_data);

	  // ajax call to post login
	  $.ajax({
	    url: "http://localhost:3000/login",
	    method: "POST",
	    data: JSON.stringify(new_user),
	    contentType: "application/json",
	    dataType: "json",

	    success: function(data) {
			 	if(typeof data.redirect == 'string') {
			    window.location.replace(window.location.protocol + "//" + window.location.host + data.redirect);
			  }

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