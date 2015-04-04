$(document).ready(function(){
  // store data for 10 usuarios
  var usuarios_array, locations_array;
  // initial population of usuarios list
  populate_usuarios();
  // usuarios list
  $usuarios_list = $('#usuarios_list');

  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin'
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($usuarios_list);
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($usuarios_list);
  });

  $('#btn_add_usuario').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();


    $('#usuario_name').attr("value", "");
    $('#usuario_lastname_paternal').attr("value", "");
    $('#usuario_lastname_maternal').attr("value", "");
    $('#usuario_email').attr("value", "");
    $('#usuario_telefono').attr("value", "");
    $('#usuario_password').attr("value", "");
  });

  /* Show info panel */
  $usuarios_list.on('click', 'tr td a.show_info_usuario', function(e){
    // prevents link from firing
    e.preventDefault();
    var table_content = '';

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($usuarios_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    var myVar = $this.attr('data-id');
    console.log("hello " + myVar);
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(myVar);
    var thisUserObject = usuarios_array[arrayPosition];
    

    //#info_panel_heading


    $('#info_panel_heading').text(thisUserObject.first_name + " " + thisUserObject.last_name1 + " " + thisUserObject.last_name2);
    if(thisUserObject.middle_initial == null)
    {
      $('#usuario_info_name').text(thisUserObject.first_name);
    }
    else
    {
     $('#usuario_info_name').text(thisUserObject.first_name + " " + thisUserObject.middle_initial);
   }

   $('#usuario_info_lastname_paternal').text(thisUserObject.last_name1);
   $('#usuario_info_lastname_maternal').text(thisUserObject.last_name2);
   $('#usuario_info_contact').text(thisUserObject.email + " " + thisUserObject.phone_number);


   $.each(locations_array, function(i){
    if(myVar == this.user_id){
      console.log(myVar == this.user_id);
      table_content += '<tr>';
      table_content += "<td> ";
      table_content += "" +this.location_name+"</td>";
      table_content += '</tr>';
    }
  });  


   $('#usuario_locations').html(table_content);
});

/* Open edit panel */
$usuarios_list.on('click', 'tr td button.btn_edit_usuario', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();

//change dropdown value tu default
  
    $(".user_type_dropdown:first-child").text("Tipo de Cuenta");
    $(".user_type_dropdown:first-child").val("Tipo de Cuenta");




    // contains ganadero id
    
    var myVar = $(this).attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(myVar);
    var thisUserObject = usuarios_array[arrayPosition];
    

    $('#usuario_name').attr("value", thisUserObject.first_name);
    $('#usuario_lastname_paternal').attr("value", thisUserObject.last_name1);
    $('#usuario_lastname_maternal').attr("value", thisUserObject.last_name2);
    $('#usuario_email').attr("value", thisUserObject.email);
    $('#usuario_telefono').attr("value", thisUserObject.phone_number);

    
    //change dropdown selected value
    $('#user_type li').on('click', function(){
      $(".user_type_dropdown:first-child").text($(this).text());
      $(".user_type_dropdown:first-child").val($(this).text());
    });
    // ajax call for info

  });

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

/* POSTs new ganadero information */
$('#btn_submit').on('click', function(){
      // ajax call to post new ganadero

      // update ganadero list after posting 
      populate_ganaderos();
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

function populate_usuarios(){
  $.getJSON('http://localhost:3000/users/admin/list_usuarios', function(data) {
    usuarios_array = data.usuarios;
    console.log(usuarios_array);
    locations_array = data.locations;
    console.log(locations_array);
    var firstElement = usuarios_array[0];
    //locations_array = data.locations;
    
    //especialidades_array = data.especialidades;

    // contents of usuarios list
    var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.usuarios, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_usuario' href='#', data-id='"+this.user_id+"'>"+this.email+"</a></td>";
        table_content += "<td><button class='btn_edit_usuario btn btn-sm btn-success btn-block' type='button' data-id='"+this.user_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_usuario btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.user_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $usuarios_list.html(table_content);


      $('#info_panel_heading').text(firstElement.first_name + " " + firstElement.last_name1 + " " + firstElement.last_name2);
      if(firstElement.middle_initial == null)
      {
        $('#usuario_info_name').text(firstElement.first_name);
      }
      else
      {
       $('#usuario_info_name').text(firstElement.first_name + " " + firstElement.middle_initial);
     }

     $('#usuario_info_lastname_paternal').text(firstElement.last_name1);
     $('#usuario_info_lastname_maternal').text(firstElement.last_name2);
     $('#usuario_info_contact').text(firstElement.email + " " + firstElement.phone_number);
     table_content = '';




     $.each(locations_array, function(i){
      if(firstElement.user_id == this.user_id){
        console.log(firstElement.user_id);
        console.log(this.user_id);
        table_content += '<tr>';
        table_content += "<td> ";
        table_content += "" +this.location_name+"</td>";
        table_content += '</tr>';
      }
    });  

     
     $('#usuario_locations').html(table_content);

   });
}
});