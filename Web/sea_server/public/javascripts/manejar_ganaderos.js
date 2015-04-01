$(document).ready(function(){
  $('#edit_panel').hide();

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

  /* Button: Close edit panel */
  $('#btn_close_edit_panel').click(function(){
    $('#edit_panel').hide();
  });

  /* Button: Close info panel */
  $('#btn_close_info_panel').click(function(){
    $('#info_panel').hide();
  });

  /* Button: Add ganadero */
  $('#btn_add_ganadero').click(function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();
  });

    /* Click: Show info panel */
  $('#ganaderos_list tr td a').click(function(){
    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous and add active to current clicked ganadero
    $('#ganaderos_list tr td a.active').removeClass('active');
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }
    
    // contains ganadero id
    $this.attr('data-id');

    // ajax call for info
  });

  /* Button: Open edit panel */
  $('.btn_edit_ganadero').click(function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains ganadero id
    $(this).attr('data-id');

    // ajax call for info
  });
  
  $('.btn_delete_ganadero').click(function(){

    // contains ganadero id
    $(this).attr('data-id');

  });

});