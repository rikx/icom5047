$(document).ready(function(){
  // store data for 10 ganaderos
  var ganaderos_array, locations_array;
  // initial population of ganaderos list
  populate_ganaderos();

  /* Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin'
	});

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
  });

  /* Show info panel */
  $('#ganaderos_list').on('click', 'tr td a.show_info_ganadero', function(e){
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

  /* Add ganadero */
  $('#btn_add_ganadero').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();
  });

    /* POSTs new ganadero information */
  $('#btn_submit').on('click', function(){
      // ajax call to post new ganadero

      // update ganadero list after posting 
      populate_ganaderos();
  });

  /* Open edit panel */
  $('#ganaderos_list').on('click', 'tr td button.btn_edit_ganadero', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains ganadero id
    $(this).attr('data-id');

    // ajax call for info

  });
  
  /* PUTs edited ganadero information */
  $('#btn_edit').on('click', function(){
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

  /* DELETEs ganadero information */
  $('#ganaderos_list').on('click', 'tr td a.btn_delete_ganadero', function(e){
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
  $.getJSON('http://localhost:3000/users/admin/list_ganaderos', function(data) {
    ganaderos_array = data.ganaderos;
    locations_array = data.locations;

    // contents of ganaderos list
    var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.ganaderos, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_ganadero' href='#', data-id='"+this.person_id+"'>"+this.first_name+' '+this.last_name1+' '+this.last_name2+"</a></td>";
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