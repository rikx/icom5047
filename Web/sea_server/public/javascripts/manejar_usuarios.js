$(document).ready(function(){
  // initial population of usuarios list
  populate_usuarios();
  // usuarios list
  $usuarios_list = $('#usuarios_list');
  $locations_list = $('#usuario_locations');
  
  // store data for 10 initial usuarios
  var usuarios_array =  JSON.parse($usuarios_list.attr('data-usuarios'));
  var locations_array = JSON.parse($usuarios_list.attr('data-locations'));

  // initial info panel population
  populate_info_panel(usuarios_array[0]);

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

  /* POSTs new usuario */
  $('#btn_submit').on('click', function(){

  });

  /* Open edit panel */
  $usuarios_list.on('click', 'tr td button.btn_edit_usuario', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains usuario id
    var usuario_id = $(this).attr('data-id');
    var arrayPosition = usuarios_array.map(function(arrayItem) { return arrayItem.user_id; }).indexOf(usuario_id);
    var this_usuario = usuarios_array[arrayPosition];
    
    $('#usuario_name').attr("value", this_usuario.first_name);
    $('#usuario_lastname_paternal').attr("value", this_usuario.last_name1);
    $('#usuario_lastname_maternal').attr("value", this_usuario.last_name2);
    $('#usuario_email').attr("value", this_usuario.email);
    $('#usuario_telefono').attr("value", this_usuario.phone_number);
  });

  /* PUTs edited ganadero information */
  $('#btn_edit').on('click', function(){

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
    $('#info_panel_heading').text($this_usuario.first_name + " " + $this_usuario.last_name1 + " " + $this_usuario.last_name2);
    if($this_usuario.middle_initial == null) {
      $('#usuario_info_name').text($this_usuario.first_name);
    } else {
      $('#usuario_info_name').text($this_usuario.first_name + " " + $this_usuario.middle_initial);
    }
    $('#usuario_info_lastname_paternal').text($this_usuario.last_name1);
    $('#usuario_info_lastname_maternal').text($this_usuario.last_name2);
    $('#usuario_info_contact').text($this_usuario.email + " " + $this_usuario.phone_number);

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
    $.getJSON('http://localhost:3000/users/admin/list_usuarios', function(data) {
      usuarios_array = data.usuarios;
      locations_array = data.locations;
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