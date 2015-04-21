$(document).ready(function(){
  // usuarios list
  $usuarios_list = $('#usuarios_list');

  // initial population of usuarios list
  populate_usuarios();
  
  // store data for 20 initial usuarios
  var usuarios_array =  JSON.parse($usuarios_list.attr('data-usuarios'));
  var specialties_array = JSON.parse($usuarios_list.attr('data-specialties'));
  var locations_array = JSON.parse($usuarios_list.attr('data-locations'));
  var all_specialties_array = JSON.parse($('#specialty_panel').attr('data-all-specialties'));

  // initial info panel population
  populate_info_panel(usuarios_array[0]);

  $('#usuario_type').hide();
  $('#specialty_panel').hide();


  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
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

  /* Show info panel */
  $usuarios_list.on('click', 'tr td a.show_info_usuario', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($usuarios_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains usuario id
    var usuario_id = $this.attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(usuario_id);
    var this_usuario = usuarios_array[arrayPosition];
    
    // populate info panel with this_usuario
    populate_info_panel(this_usuario);
  });

  /* Change user type dropdown selected value */
  $('#list_user_type').on('click', 'li a', function(e){
    // prevents link from firing
    e.preventDefault();
    $('#btn_user_type_text').text($(this).text()+' ');
    $('#btn_user_type').val($(this).attr('data-usario-type'));
    $('#usuario_type').val( $('#btn_user_type_text').text().toLowerCase());
    $('#specialty_panel').show();
    console.log(specialties_array);
    populate_specialties();

  });

  /* Open add panel */
  $('#btn_add_usuario').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // clear add form
    $('#form_manage_usuario')[0].reset();
  });

  /* POSTs new usuario information */
  $('#btn_submit').on('click', function(){
    // get form data and conver to json format
    var $the_form = $('#form_manage_usuario');
    console.log($the_form);
    var form_data = $the_form.serializeArray();
    var new_usuario = ConverToJSON(form_data);
    var user_type = $('#btn_user_type_text').text();
    console.log("The new user is : ");
    console.log(new_usuario);
    console.log(user_type);

    // ajax call to post new ganadero
    $.ajax({
      url: "http://localhost:3000/users/admin/usuarios",
      method: "POST",
      data: JSON.stringify(new_usuario),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        if(data.exists){
          alert("Ganadero con este correo electrónico o teléfono ya existe");
        } else {
          alert("Ganadero ha sido añadido al sistema.");
          // clear add form
          $the_form[0].reset();
        }
        // update ganadero list after posting 
        populate_usuarios();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });

/* Open edit panel */
$usuarios_list.on('click', 'tr td button.btn_edit_usuario', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();
  $('#specialty_panel').hide();

    var type = "Test";
    // contains usuario id
    var usuario_id = $(this).attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(usuario_id);
    var this_usuario = usuarios_array[arrayPosition];
    console.log("The ususario is " );
    console.log(this_usuario);
    $('#btn_edit').attr('data-id', usuario_id);
    $('#usuario_name').val(this_usuario.first_name);
    $('#usuario_lastname_paternal').val(this_usuario.last_name1);
    $('#usuario_lastname_maternal').val(this_usuario.last_name2);
    $('#usuario_email').val(this_usuario.email);
    $('#usuario_telefono').val(this_usuario.phone_number);


    
    if(this_usuario.type == 'agent')
    {
      type = 'Agente';
    } 
    else if (this_usuario.type == 'specialist')
    {
      type = 'Especialista';
    } 
    else 
    {
      type = 'Admin';
    }

    $('#btn_user_type_text').text(type);

    if(type == 'Especialista')
    {
      populate_specialties();
      $('#specialty_panel').show();
    }



  });

/* PUTs edited ganadero information */
$('#btn_edit').on('click', function(){

  var usuario_id = $(this).attr('data-id');
  alert(usuario_id);
  // get form data and conver to json format
  var $the_form = $('#form_manage_usuario');
  var form_data = $the_form.serializeArray();
  var new_usuario = ConverToJSON(form_data);
  console.log(new_usuario);


  // ajax call to update ganadero
  $.ajax({
    url: "http://localhost:3000/users/admin/usuarios/" + usuario_id,
    method: "PUT",
    data: JSON.stringify(new_usuario),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      alert("Informacion de usuario ha sido editada en el sistema.");
      // update ganadero list after posting 
      populate_usuarios();
    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });
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

/* Populates info panel with $this_usuario information */
function populate_info_panel($this_usuario){
    // populate basic information panel
    var type;
    $('#info_panel_heading').text($this_usuario.first_name + " " + $this_usuario.last_name1 + " " + $this_usuario.last_name2);
    if($this_usuario.middle_initial == null) {
      $('#usuario_info_name').text($this_usuario.first_name);
    } else {
      $('#usuario_info_name').text($this_usuario.first_name + " " + $this_usuario.middle_initial);
    }
    $('#usuario_info_lastname_paternal').text($this_usuario.last_name1);
    $('#usuario_info_lastname_maternal').text($this_usuario.last_name2);
    $('#usuario_info_contact').text($this_usuario.email + " " + $this_usuario.phone_number);


    if($this_usuario.type == 'agent')
    {
      type = 'Agente';
    } 
    else if ($this_usuario.type == 'specialist')
    {
      type = 'Especialista';
    } 
    else 
    {
      type = 'Administrador';
    }

    $('#usuario_info_type').text(type);

    // populate associated locations panel
    var table_content = '';
    $.each(locations_array, function(i){
      if($this_usuario.user_id == this.user_id){
        table_content += '<tr><td>'+this.location_name+'</td></tr>';
      }
    });  
    $('#usuario_locations').html(table_content);
  }

  /* */
  function populate_usuarios(){
    $.getJSON('http://localhost:3000/list_usuarios', function(data) {
      usuarios_array = data.usuarios;
      locations_array = data.locations;
      specialties_array = data.user_specialties;

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
    });
}

function populate_specialties(){

  var user_id =  $('#btn_edit').attr('data-id');
  var matches = [];
  //find matches
  $.each(specialties_array, function(i){
    if(user_id == specialties_array[i].user_id){      
     matches.push(specialties_array[i]);
   }
 }); 

  var content = '';
  var found = false;
  var i = 0;

  //solid algorithm
  $.each(all_specialties_array, function(i){
    //for each specialty, check if it is present in the matches array
    //if so then check property "checked" as true
    $.each(matches, function(j){
      if(matches[j].spec_id == all_specialties_array[i].spec_id)
      {
        found = true;
      }
      else
      {
            //do nothing
          }
        });
    if(found)
    {
      content += '<input ';
      content += "type='checkbox' checked='true'> " + all_specialties_array[i].spec_name + " <br>";
    }
    else
    {
      content += '<input ';
      content += "type='checkbox'> " + all_specialties_array[i].spec_name + " <br>";
    }
    found = false;
  });


  $('#specialist_categories_list').html(content);

}

});