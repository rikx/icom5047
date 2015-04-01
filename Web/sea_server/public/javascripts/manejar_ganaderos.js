$(document).ready(function(){
  //$('#info_panel').hide();
  $('#btn_edit').hide();

  /* Button: Return home */
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

  /* Button: Close info panel */
  $('#btn_close_info_panel').click(function(){
    $('#info_panel').hide();
  });

  /* Button: Add ganadero */
  $('#btn_add_ganadero').click(function(){
    $('#btn_edit').hide();
    $('#btn_submit').show();
    $('#info_panel').show();
  });

  /* Button: Open info panel */
  $('.btn_edit_ganadero').click(function(){
    $('#btn_edit').show();
    $('#btn_submit').hide();
    $('#info_panel').show();

    $(this).attr('data-id');
    //ajax call for info
  });
  
  $('.btn_delete_ganadero').click(function(){

    $(this).attr('data-id');

  });

});