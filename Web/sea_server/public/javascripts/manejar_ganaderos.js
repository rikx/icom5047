$(document).ready(function(){
  // store data for 10 ganaderos
  var ganaderos_array;
  // initial population of ganaderos list
  populate_ganaderos();

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
  $('#ganaderos_list tr td a').click(function(e){
    // prevents link from firing
    e.preventDefault();

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

  /* Click: POSTs new ganadero information */
  $('#btn_submit').click(function(){
      // ajax call to post new ganadero

      // update ganadero list after posting 
      populate_ganaderos();
  });
  
  /* Click: PUTs edited ganadero information */
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

  /* Click: DELETEs ganadero information */
  $('.btn_delete_ganadero').click(function(e){
    // prevents link from firing
    e.preventDefault();

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
  $.getJSON('http://localhost:3000/users/admin/get_ganaderos', function(data) {
    ganaderos_array = data;

    // contents of ganaderos list
    var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data, function(){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item active show_info_ganadero' href='#', data-id='"+this.person_id+"'>"+this.first_name+' '+this.last_name1+"</a></td>";
        table_content += "<td><button class='btn_edit_ganadero btn btn-sm btn-success btn-block' type='button' data-id='"+this.person_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $('#ganaderos_list').html(table_content);
  });
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