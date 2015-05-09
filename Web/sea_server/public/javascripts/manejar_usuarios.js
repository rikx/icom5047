$(document).ready(function(){
  // usuarios list
  $usuarios_list = $('#usuarios_list');

  // initial population of usuarios list
  //populate_usuarios();
  
  // store data for 20 initial usuarios
  var usuarios_array = [];
  var specialties_array = [];
  var locations_array = [];
  var all_specialties_array =[];

  var data_usuarios = $usuarios_list.attr('data-usuarios');
  var data_locations = $usuarios_list.attr('data-locations');
  var data_specialties = $usuarios_list.attr('data-specialties');
  var data_all_specialties = $('#specialty_panel').attr('data-all-specialties');

  if(data_usuarios.length >2){
    usuarios_array = JSON.parse($usuarios_list.attr('data-usuarios'));

    if(data_locations.length >2){
      locations_array = JSON.parse($usuarios_list.attr('data-locations'));
    }
    if(data_specialties.length >2){
      specialties_array = JSON.parse($usuarios_list.attr('data-specialties'));
    }
    if(data_all_specialties.length >2){
      all_specialties_array = JSON.parse($('#specialty_panel').attr('data-all-specialties'));
    }

    // initial info panel population
    populate_info_panel(usuarios_array[0]);
  } else {
    $('#info_panel').hide();
  }

   /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compard with ganadero full names or emails
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('username'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.username === localMatch.username;
    },
    local: usuarios_array,
    remote: {
      url: 'http://localhost:3000/usuarios/%QUERY',
      filter: function(list) {
        // populate global arrays with matching results
        usuarios_array = list.usuarios;
  
        // populate list with matching results
        populate_list(usuarios_array);
        return $.map(list.usuarios, function(usuario) { 
          return usuario;
        });
      }
    }
  });

  // kicks off loading and processing of 'local' and 'prefetch'
  search_source.initialize();

  // set typeahead functionality for search bar
  $('.typeahead').typeahead({
    hint: false,
    highlight: true
  },
  {
    name: 'usuarios',
    displayKey: 'username',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(usuario){
        return '<p><strong>Nombre: </strong>'+usuario.username+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').bind('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    populate_list([datum]);
  });

  $("#search_bar").keypress(function (event) {
    if (event.which == 13) {
      var user_input = $('#search_bar').val();
      if(user_input == ''){
        populate_cuestionarios();
        return;
      } else {
        search_source.get(user_input, sync, async);
        $(this).typeahead('close');
        function sync(datums) {
          //console.log('datums from `local`, `prefetch`, and `#add`');
          //console.log(datums);
          populate_list(datums);

        }

        function async(datums) {
          //console.log('datums from `remote`');
          //console.log(datums);
          populate_list(datums);
        }
      }
    }
  });
  /* Search Code End */

  $('#usuario_type').hide();
  $('#btn_edit_specialty').hide();
  $('#add_specialty_panel').hide();

  
  $('#btn_add_specialty').on('click', function(){
    $('#add_specialty_panel').toggle();
    $('#specialty_panel').hide();
  });


  $('#btn_post_new_specialty').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_specialty');
  var form_data = $the_form.serializeArray();
  var new_specialty = ConverToJSON(form_data);


  // ajax call to post new category
  $.ajax({
    url: "http://localhost:3000/users/admin/new_specialty",
    method: "POST",
    data: JSON.stringify(new_specialty),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      if(data.exists){
        //alert("Localización con este número de licensia ya existe");
      } else {
        alert("Especialidad ha sido añadida al sistema.");
         $('#add_specialty_panel').hide();
        // clear add form
        //$the_form[0].reset();
        // update locations list after posting 
        //populate_localizaciones();
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




  $('#btn_home').on('click', function(){
    window.location.href = '/users';
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
    $('#add_specialty_panel').hide();



    //button#btn_add_specialty(class='btn btn-default btn-success', type='button') Agregar Especialidades
    //button#btn_edit_specialty(class='btn btn-default btn-success', type='button') Editar Especialidades


    $('#btn_add_specialty').show();
    $('#btn_edit_specialty').hide();


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

    // set id values of info panel buttons
    $('#btn_edit_panel').attr('data-id', this_usuario.user_id);
    $('#btn_delete').attr('data-id', this_usuario.user_id);
    $('#btn_delete').attr('data-username', this_usuario.username); 
  });

  /* Change user type dropdown selected value */
  $('#list_user_type').on('click', 'li a', function(e){
    // prevents link from firing
    e.preventDefault();
    $('#btn_user_type_text').text($(this).text()+' ');
    $('#btn_user_type').val($(this).attr('data-usario-type'));
    $('#usuario_type').val($(this).attr('data-usario-type'));
    $('#specialty_panel').show();
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
    var form_data = $the_form.serializeArray();
    var new_usuario = ConverToJSON(form_data);
    var user_type = $('#btn_user_type_text').text();
    console.log("User is ");
    console.log($the_form);
    console.log(form_data);
    if(empty_field_check(form_data))
    {
      alert("Uno o mas campos estan vacios");
    }
    else if(new_usuario.usuario_password != new_usuario.usuario_password_confirm)
    {
      alert("Las contraseñas no son iguales.");
    }
    else
    {
    $.ajax({
      url: "http://localhost:3000/users/admin/usuarios",
      method: "POST",
      data: JSON.stringify(new_usuario),
      contentType: "application/json",
      dataType: "json",
      success: function(data) {
        if(data.exists){
          alert("Usuario con este correo electrónico o teléfono ya existe");
        } else {
          alert("Usuario ha sido añadido al sistema.");
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
  }
  });
////////////////////////////////////////////////////////
/* Open edit panel */
 $('#btn_edit_panel').on('click', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();
  $('#specialty_panel').hide();
  $('#btn_add_specialty').hide()
  $('#btn_edit_specialty').show()
  $('#btn_add_specialty').show();
  $('#add_specialty_panel').hide();



    var type = "Test";
    // contains usuario id
    var usuario_id = $(this).attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(usuario_id);
    var this_usuario = usuarios_array[arrayPosition];

    $('#btn_edit').attr('data-id', usuario_id);
    $('#usuario_name').val(this_usuario.first_name);
    $('#usuario_lastname_paternal').val(this_usuario.last_name1);
    $('#usuario_lastname_maternal').val(this_usuario.last_name2);
    $('#usuario_email').val(this_usuario.email);
    $('#usuario_telefono').val(this_usuario.phone_number);
    $('#usuario_middle_initial').val(this_usuario.middle_initial);


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
    $('#usuario_type').val(this_usuario.type);

    if(type == 'Especialista')
    {
      populate_specialties_edit();
      $('#specialty_panel').show();
    }

    populate_specialties_edit();

  });

/* PUTs edited ganadero information */
$('#btn_edit').on('click', function(){
  var usuario_id = $(this).attr('data-id');
  // get form data and conver to json format
  var $the_form = $('#form_manage_usuario');
  var form_data = $the_form.serializeArray();
  var new_usuario = ConverToJSON(form_data);

  if(empty_field_check(form_data))
  {
    alert("Uno o mas campos estan vacios");
  }
  else if(new_usuario.usuario_password != new_usuario.usuario_password_confirm)
  {
    alert("Las contraseñas no son iguales.");
  }
  else
  {
     // ajax call to update ganadero
     $.ajax({
      url: "http://localhost:3000/users/admin/usuarios/" + usuario_id,
      method: "PUT",
      data: JSON.stringify(new_usuario),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        //alert("Informacion de usuario ha sido editada en el sistema.");
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

   //get user id and specialty ids associated to said user id
   var usuario_id =  $('#btn_edit').attr('data-id');
   var checkedSpecialties = [];
   $(':checkbox:checked').each(function(i){
    checkedSpecialties[i] = $(this).val();
  });

////////SECOND AJAX

 //json object for user_specialties
 var specialties = {
  user: usuario_id,
  specialties: checkedSpecialties
};

$.ajax({
  url: "http://localhost:3000/users/admin/user_specialties",
  method: "PUT",
  data: JSON.stringify(specialties),
  contentType: "application/json",
  dataType: "json",

  success: function(data) {

    alert("Especialidades han sido modificadas.");
    console.log("the specialties are");
    console.log(data.users_specialization);
    repopulate_specialties(data.users_specialization);

  },
  error: function( xhr, status, errorThrown ) {
    alert( "Sorry, there was a problem!" );
    console.log( "Error: " + errorThrown );
    console.log( "Status: " + status );
    console.dir( xhr );
  }
});

 $('#edit_panel').hide();
    $('#info_panel').show();
    $('#specialty_panel').show();

}
});

//updates specialties associated to current user
// $('#btn_edit_specialty').on('click', function(){
//   //get user id and specialty ids associated to said user id
//   var usuario_id =  $('#btn_edit').attr('data-id');
//   var checkedSpecialties = [];
//   $(':checkbox:checked').each(function(i){
//     checkedSpecialties[i] = $(this).val();
//   });


//  //json object for user_specialties
//  var specialties = {
//   user: usuario_id,
//   specialties: checkedSpecialties
// };

// $.ajax({
//   url: "http://localhost:3000/users/admin/user_specialties",
//   method: "PUT",
//   data: JSON.stringify(specialties),
//   contentType: "application/json",
//   dataType: "json",

//   success: function(data) {

//     alert("Especialidades han sido modificadas.");

//     },
//     error: function( xhr, status, errorThrown ) {
//       alert( "Sorry, there was a problem!" );
//       console.log( "Error: " + errorThrown );
//       console.log( "Status: " + status );
//       console.dir( xhr );
//     }
//   });
// });


$usuarios_list.on('click', 'tr td a.btn_delete_user', function(e){
    // prevents link from firing
    e.preventDefault();

    // contains ganadero id
    $(this).attr('data-id');

    $.ajax({
      url: "http://localhost:3000/users/admin/user",
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

    if(locations_array.length>0){
      // populate associated locations panel
      var table_content = '';
      $.each(locations_array, function(i){
        if($this_usuario.user_id == this.user_id){
          table_content += '<tr><td>'+this.location_name+'</td></tr>';
        }
      });  
      $('#usuario_locations').html(table_content);
    }


    $('#specialty_panel').show();

    if(specialties_array.length>0){
      populate_specialties_info($this_usuario);
    }
    

    //Categoria de Localizacion

    //$('#especializacion_title').text("Tipo de Especializacion" + " - " + $this_usuario.email)
    
    // set id values of info panel buttons
    $('#btn_edit_panel').attr('data-id', $this_usuario.user_id);
    $('#btn_delete').attr('data-id', $this_usuario.user_id);
    $('#btn_delete').attr('data-username', $this_usuario.username); 
  }

  /* Populate list with first 20 usuarios, organized alphabetically */
  function populate_usuarios(){
    $.getJSON('http://localhost:3000/list_usuarios', function(data) {
      usuarios_array = data.usuarios;
      locations_array = data.locations;
      specialties_array = data.user_specialties;
      populate_list(data.usuarios);

      populate_info_panel(data.usuarios[0]);
    });
  };

    //Delete User
    $('#btn_delete').on('click', function(){
      var user_id = $('#btn_delete').attr("data-id");
      $.ajax({
        url: "http://localhost:3000/users/admin/delete_user/" + user_id,
        method: "PUT",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
          alert("Usuario fue eliminado");
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

  /* Populate list with usuarios_set information */
  function populate_list(usuarios_set){
    // contents of usuarios list
    var table_content = '';

    // for each item in JSON, add table row and cells
    $.each(usuarios_set, function(i){
      table_content += '<tr>';
      table_content += "<td><a class='list-group-item ";
      // if initial list item, set to active
      if(i==0) {
        table_content +=  'active ';
      }
      table_content += "show_info_usuario' href='#', data-id='"+this.user_id+"'>"+this.username+"</a></td>";
      //table_content += "<td><button class='btn_edit_usuario btn btn-sm btn-success btn-block' type='button' data-id='"+this.user_id+"'>Editar</button></td>";
      //table_content += "<td><a class='btn_delete_usuario btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.user_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
      table_content += '</tr>';
    });

    // inject content string into html
    $usuarios_list.html(table_content);

    populate_info_panel(usuarios_set[0]);
  };

function populate_specialties_edit(){

  var user_id =  $('#btn_edit_panel').attr('data-id');
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
     content += "type='checkbox' checked='true' value='" + all_specialties_array[i].spec_id + "'> " + all_specialties_array[i].spec_name + " <br>";
    }
    else
    {
      content += '<input ';
     content += "type='checkbox' value='" + all_specialties_array[i].spec_id + "'> " + all_specialties_array[i].spec_name + " <br>";
    }
    found = false;
  });

  console.log();
  $('#specialist_categories_list_edit').html(content);

}

function populate_specialties_info(usuario){

  var user_id =  usuario.user_id;
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
       content += '<li> ';
      content += "" + all_specialties_array[i].spec_name + "</li>";


    }
    else
    {
     //do nothing
    }
    found = false;
  });

if(content == '')
  content = "<p> Usuario no tiene especialidades asignadas. </p>"
 $('#specialist_categories_list').html(content);
  

}


function repopulate_specialties(specs)
{
var content = '';
$.each(specs, function(i){
      content += '<li> ';
      content += "" + specs[i].spec_name + "</li>";
  });


if(content == '')
  content = "<p> Usuario no tiene especialidades asignadas. </p>"
 $('#specialist_categories_list').html(content);


}



});