$(document).ready(function(){
	$('#btn_home').click(function(){
    $.ajax({
        url: "http://localhost:3000/users/get_admin",
        method: "GET",
     
        success: function( data ) {
          if(typeof data.redirect == 'string') {
            window.location.replace(window.location.protocol + "//" + window.location.host + data.redirect);
          }
        },
     
        // Code to run if the request fails; the raw request and
        // status codes are passed to the function
        error: function( xhr, status, errorThrown ) {
            alert( "Sorry, there was a problem!" );
            console.log( "Error: " + errorThrown );
            console.log( "Status: " + status );
            console.dir( xhr );
        }/*,
     
        // Code to run regardless of success or failure
        complete: function( xhr, status ) {
            alert( "The request is complete!" );
        }*/
    });
	});
});