$(document).ready(function(){

  /* Button: Return home */
	$('#btn_home').click(function(){
    window.location.href = '/users/admin'
	});

  /* Button: Close edit panel */
  $('#btn_close_edit_panel').click(function(){
    $('#edit_panel').hide();
  });

  /* Button: Close info panel */
  $('#btn_close_info_panel').click(function(){
    $('#info_panel').hide();
  });

    /* Click: Show info panel */
  $('#ganaderos_list tr td a').click(function(event){
    // prevents link from firing
    event.preventDefault();

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

    /* Button: Add ganadero */
  $('#btn_add_ganadero').click(function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // ajax call to post new ganadero
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
  
  $('#btn_edit').click(function(){
    //TODO: collect data to submit from edit form

    $.ajax({
      url: "http://localhost:3000/users/admin/ganadero_edit",
      method: "PUT",
   
      success: function( data ) {

      },
   
      // Code to run if the request fails; the raw request and
      // status codes are passed to the function
      error: function( xhr, status, errorThrown ) {
          alert( "Sorry, there was a problem!" );
          console.log( "Error: " + errorThrown );
          console.log( "Status: " + status );
          console.dir( xhr );
      },
   
      // Code to run regardless of success or failure
      complete: function( xhr, status ) {
          alert( "The request is complete!" );
      }
    });
  });

  $('.btn_delete_ganadero').click(function(){

    // contains ganadero id
    $(this).attr('data-id');

    $.ajax({
      url: "http://localhost:3000/users/admin/ganadero_delete",
      method: "DELETE",
   
      success: function( data ) {

      },
   
      // Code to run if the request fails; the raw request and
      // status codes are passed to the function
      error: function( xhr, status, errorThrown ) {
          alert( "Sorry, there was a problem!" );
          console.log( "Error: " + errorThrown );
          console.log( "Status: " + status );
          console.dir( xhr );
      },
   
      // Code to run regardless of success or failure
      complete: function( xhr, status ) {
          alert( "The request is complete!" );
      }
    });

  });

function populate_ganaderos(){
  $.getJSON();
};

function populate_info(){
  $.ajax({
    url: "http://localhost:3000/users/admin/ganadero_show",
    method: "GET",
 
    success: function( data ) {

    },
 
    // Code to run if the request fails; the raw request and
    // status codes are passed to the function
    error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
    },
 
    // Code to run regardless of success or failure
    complete: function( xhr, status ) {
        alert( "The request is complete!" );
    }
  });
};

function populate_locations(){
  $.getJSON();
};

});