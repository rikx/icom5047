$(document).ready(function(){
  // localizaciones list
  $localizaciones_list = $('#localizaciones_list');

  // initial population of localizaciones list
  populate_localizaciones();
  populate_dropdowns();

  // store data for initial 20 locations
  var localizaciones_array = JSON.parse($localizaciones_list.attr('data-localizaciones'));
  var agentes_array =  JSON.parse($localizaciones_list.attr('data-agentes'));
  var ganaderos_array =  JSON.parse($localizaciones_list.attr('data-ganaderos'));

  // initial info panel population
  populate_info_panel(localizaciones_array[0]);

  /* Button: Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($localizaciones_list);
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($localizaciones_list);
  });

  /* Close add panel */
  $('#btn_close_add_panel').on('click', function(){
    $('#add_associates_panel').hide();
    $('#add_agente_dropdown_panel').hide(); 
    $('#add_ganaderos_dropdown_panel').hide();
    remove_active_class($localizaciones_list);
  });

  /* Edit associates */
  $localizaciones_list.on('click', 'tr td button.btn_add_associates', function(e){
    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_associates_panel').show();

    // contains localizacion id
    var the_location = $(this).attr('data-id');
    var assigned_ganaderos = ganaderos_array.filter(filter_by_id);

    // gives an array of indexes in ganaderos_array where the ganadero is associated to location_id
    function filter_by_id(person) {
      if(person.location_id == the_location){
        return true;
      } else {
        return false;
      }
    }

    // Populate Associated Ganaderos panel
    var owner_content = '';
    var manager_content = '';
    $.each(assigned_ganaderos, function(i){
      var the_content = '';
      the_content += '<span>'+this.person_name+' </span>';
      the_content += "<div class='btn-group' role='group'><a class='btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-edit'></i></a>";
      the_content += "<a class='btn btn-sm btn-danger' data-toggle='tooltip' type='button href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-minus'></i></a></div>";
      if(this.relation_type == 'owner') {
        owner_content = the_content;
      }
      if(this.relation_type == 'manager') {
        manager_content = the_content;
      }
    });
    // if location has no associated owner and/or manager
    if(owner_content == ''){
      owner_content += "<a id='btn_add_associate_ganadero' class='btn btn-sm btn-success' data-toggle='tooltip' type='button href='#'><i class='glyphicon glyphicon-plus'></i></a>";
    }
    if(manager_content == ''){
      manager_content += "<a id='btn_add_associate_ganadero' class='btn btn-sm btn-success' data-toggle='tooltip' type='button href='#'><i class='glyphicon glyphicon-plus'></i></a>";
    }
    // inject content stirngs into html
    $('#add_associates_owner').html(owner_content);
    $('#add_associates_manager').html(manager_content);

    // Populate Associated Agentes panel
    var agentes_content = '';
    var assigned_agentes = agentes_array.filter(filter_by_id);
    $.each(assigned_agentes, function(i){
      agentes_content += '<tr><td><span>'+this.username+' </span>';
      agentes_content += "<div class='btn-group' role='group'><a class='btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-edit'></i></a>";
      agentes_content += "<a class='btn btn-sm btn-danger' data-toggle='tooltip' type='button href='#' data-id='"+this.person_id+"'><i class='glyphicon glyphicon-minus'></i></a></div></td></tr>";
    });
    // Add table row with adding associated agent button
      agentes_content += '<tr><td><strong>Agente Nuevo </strong>';
      agentes_content += "<a id='btn_add_associate_agente' class='btn btn-sm btn-success' data-toggle='tooltip' type='button href='#'><i class='glyphicon glyphicon-plus'></i></a></td></tr>";
    // inject content stirngs into html
    $('#associated_agentes').html(agentes_content);
  });


  /* Add associated ganadero */
  $('#associated_ganaderos').on('click', 'tr td a#btn_add_associate_ganadero', function(e){
    // prevents link from firing
    e.preventDefault();
    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_agente_dropdown_panel').hide(); 
    $('#add_ganaderos_dropdown_panel').show();
  });

  /* */
  $('#btn_add_ganadero').on('click', function(){
    
    // ajax POST call to assign ganadero as owner or manager of location
  });

  /* Change add associated ganadero dropdown selected value */
  $('#list_associated_ganadero').on('click', 'li a', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#btn_associated_ganadero_text').text($(this).text()+' ');
    $('#btn_associated_ganadero').val($(this).attr('data-id'));
  });


  /* Add associated agente  */
  $('#associated_agentes').on('click', 'tr td a#btn_add_associate_agente', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_agente_dropdown_panel').show(); 
    $('#add_ganaderos_dropdown_panel').hide();
  });

  /* */
  $('#btn_add_agente').on('click', function(){
    
    //ajax POST call to assign a user to a location 
  });

  /* Change add associated agente dropdown selected value */
  $('#list_associated_agente').on('click', 'li a', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#btn_associated_agente_text').text($(this).text()+' ');
    $('#btn_associated_agente').val($(this).attr('data-id'));
  });

  /* Open info panel */
  $localizaciones_list.on('click', 'tr td a.show_info_localizacion', function(e){
    // prevents link from firing
    e.preventDefault();
    $('#info_panel_heading').show();
    $('#localizacion_info').show();
    $('#edit_associates_heading').hide();
    $('#add_associates_panel').hide();
    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($localizaciones_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains location id
    var location_id = $this.attr('data-id');
    var arrayPosition = localizaciones_array.map(function(arrayItem) { return arrayItem.location_id; }).indexOf(location_id);
    var this_location = localizaciones_array[arrayPosition];

    // Populate info panel with selected item's information
    populate_info_panel(this_location);
 });

/* Add localizacion */
$('#btn_add_localizacion').on('click', function(){
  $('#btn_edit, #heading_edit').hide();
  $('#btn_submit, #heading_create').show();
  $('#edit_panel').show();
  $('#info_panel').hide();
  $('#add_associates_panel').hide();

  // clear add form
  $('#form_manage_location')[0].reset();
});

/* POSTs new location information */
$('#btn_submit').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_manage_location');
  var form_data = $the_form.serializeArray();
  var new_location = ConverToJSON(form_data);

  // ajax call to post new location
  $.ajax({
    url: "http://localhost:3000/users/admin/localizaciones",
    method: "POST",
    data: JSON.stringify(new_location),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      if(data.exists){
        alert("Localización con este número de licensia ya existe");
      } else {
        alert("Localización ha sido añadido al sistema.");
        // clear add form
        $the_form[0].reset();
        // update locations list after posting 
        populate_localizaciones();
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

/* Open edit location panel */
$localizaciones_list.on('click', 'tr td button.btn_edit_localizacion', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();
  $('#add_associates_panel').hide();

  // contains location id
  var location_id = $(this).attr('data-id');
  var arrayPosition = localizaciones_array.map(function(arrayItem) { return arrayItem.location_id; }).indexOf(location_id);
  var this_location = localizaciones_array[arrayPosition];

  $('#btn_edit').attr('data-id', location_id);
  $('#localizacion_name').val(this_location.location_name);
  $('#localizacion_license').val(this_location.license);
  $('#localizacion_address_line1').val(this_location.address_line1);
  $('#localizacion_address_line2').val(this_location.address_line2);
  $('#localizacion_address_city').val(this_location.city);
  $('#localizacion_address_zipcode').val(this_location.zipcode);

  //change dropdown selected value
  $('#user_type li').on('click', function(){
    $(".user_type_dropdown:first-child").text($(this).text());
    $(".user_type_dropdown:first-child").val($(this).text());
  });
});

/* PUTs edited location information */
$('#btn_edit').on('click', function(){
  var location_id = $(this).attr('data-id');
  // get form data and conver to json format
  var $the_form = $('#form_manage_location');
  var form_data = $the_form.serializeArray();
  var new_location = ConverToJSON(form_data);

  // ajax call to update location
  $.ajax({
    url: "http://localhost:3000/users/admin/localizaciones/" + location_id,
    method: "PUT",
    data: JSON.stringify(new_location),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      alert("Localización ha sido editada en el sistema.");
      // update locations list after posting 
      populate_localizaciones();
     },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });
});

/* Populates info panel with $this_location's information */
function populate_info_panel($this_location){
  $('#info_panel_heading').text($this_location.location_name);
  $('#localizacion_info_name').text($this_location.location_name);
  $('#localizacion_info_license').text($this_location.license);
  if($this_location.address_line2 == null) {
    $('#localizacion_info_address').text($this_location.address_line1);
  } else {
    $('#localizacion_info_address').html('<p>'+$this_location.address_line1+'</p><p>'+$this_location.address_line2+'</p>');
  }
  $('#localizacion_info_ciudad').text($this_location.city);
  $('#localizacion_info_zipcode').text($this_location.zipcode);

  // populate local array with ganaderos associated to this location
  var selectedGanaderos = [];
  $.each(ganaderos_array, function(i){
    if($this_location.location_id == ganaderos_array[i].location_id){
      selectedGanaderos.push(ganaderos_array[i]);
    }
  });  

  // populate ganaderos table according to relation_type
  if(selectedGanaderos.length > 0){
    for (j = 0; j < selectedGanaderos.length; j++) { 
      if(selectedGanaderos[j].relation_type == "owner") {
        $('#owner').html(selectedGanaderos[j].person_name);
      } else {
        $('#manager').html(selectedGanaderos[j].person_name);
      }
    }
  } else {
    // array is empty so ganaderos populate with placeholders
    $('#owner').html('Dueño no asignado');
    $('#manager').html('Gerente no asignado');
  }

  // populate agentes table if there is one assigned
  var agent_found = false;
  var table_content = '';
  $.each(agentes_array, function(i){
    if($this_location.location_id == agentes_array[i].location_id){
      agent_found = true;
      table_content += '<tr><td>'+agentes_array[i].username+'</td></tr>';
    }
  }); 
  // checks if agent is found
  if(agent_found){
    $('#localizacion_agentes').html(table_content);
  } else {
    $('#localizacion_agentes').html('<tr><td>Agente no asignado</td></tr>');
  }  
};

/* */
function populate_localizaciones(){
  $.getJSON('http://localhost:3000/list_localizaciones', function(data) {
    $('#edit_associates_heading').hide();
    $('#localizacion_associates').hide();
    localizaciones_array = data.localizaciones;
    agentes_array = data.agentes;
    ganaderos_array = data.ganaderos;

      // contents of localizaciones list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.localizaciones, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_localizacion' href='#', data-id='"+this.location_id+"'>"+this.location_name+"</a></td>";
        table_content += "<td><button class='btn_add_associates btn btn-sm btn-success btn-block' type='button', data-id='"+this.location_id+"'>Editar Personas Asociadas</button></td>";
        table_content += "<td><button class='btn_edit_localizacion btn btn-sm btn-success btn-block' type='button' data-id='"+this.location_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_localizacion btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.location_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $localizaciones_list.html(table_content);

      // populate info panel with first location's information
      //populate_info_panel(localizaciones_array[0]);
    });
  };

  /* Populates dropdowns with all ganaderos and users in system */
  function populate_dropdowns(){
    var list_content = '';
    // ajax call to GET all ganaderos and usuarios
    $.getJSON('http://localhost:3000/modify_location_dropdowns', function(data) {
      // populate ganaderos dropdown
      $.each(data.ganaderos, function(i){
        list_content += "<li><a role='menuitem' tabindex='-1' href='#' data-id='"+this.person_id+"'>"+this.person_name+'</a></li>';
      });
      // popuplate dropdown list with ganaderos
      $('#list_associated_ganadero').html(list_content);

      // populate users dropdown
      list_content = '';
      $.each(data.usuarios, function(i){
        list_content += "<li><a role='menuitem' tabindex='-1' href='#' data-id='"+this.user_id+"'>"+this.username+'</a></li>';
      });
      // popuplate dropdown list with users
      $('#list_associated_agente').html(list_content);
    });
  }
});