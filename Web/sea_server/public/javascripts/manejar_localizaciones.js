$(document).ready(function(){
  // localizaciones list
  $localizaciones_list = $('#localizaciones_list');

  // initial population of localizaciones list
  //populate_localizaciones();
  $('#localizacion_license_old').hide();

  // store data for initial 20 locations
  var localizaciones_array = [];
  var categorias_array = [];
  var agents_array = [];
  var ganaderos_array = [];
  var all_categorias_array = [];

  var data_locations = $localizaciones_list.attr('data-localizaciones');
  var data_categories = $localizaciones_list.attr('data-categorias');
  var data_agents = $localizaciones_list.attr('data-agentes');
  var data_ganaderos =  $localizaciones_list.attr('data-ganaderos');
  var data_all_categories = $('#categoria_panel').attr('data-all-categorias');
  if(data_locations.length >2){
    localizaciones_array = JSON.parse($localizaciones_list.attr('data-localizaciones'));
    if(data_categories.length >2){
      categorias_array = JSON.parse($localizaciones_list.attr('data-categorias'));
    }
    if(data_agents.length >2){
      agents_array = JSON.parse($localizaciones_list.attr('data-agentes'));
    }
    if(data_ganaderos.length >2){
      ganaderos_array = JSON.parse($localizaciones_list.attr('data-ganaderos'));
    }
    if(data_all_categories.length >2){
      all_categorias_array = JSON.parse($localizaciones_list.attr('data-all-categorias'));
    }
  } else {
    $('#info_panel').hide();
  }



  var user_info = JSON.parse($localizaciones_list.attr('data-user'));
  var data_username = $localizaciones_list.attr('data-username');
  var data_user_type = $localizaciones_list.attr('data-type');
  //var data_username = $localizaciones_list.attr('data-user');
  //var data_user_type = $localizaciones_list.attr('data-type');
  var all_ganaderos = [];
  var all_agentes = [];
 
  //$('#currently_signed_in').text("Usuario actual: " + user_info.username);
 // $('#current_type').text("Tipo de cuenta: " + user_info.user_type);

  if(data_user_type == 'admin')
  {
      $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Administrador'+ " - " +data_username+"</small> </h2>");
  }
  else if(data_user_type == 'agent')
  {
    $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Agente'+ " - " +data_username+"</small> </h2>");
  }
  else
  {
    $('#header_page').html("<h2> Servicio de Extensión Agrícola <br> <small>"+'Especialista'+ " - " +data_username+"</small> </h2>");
  }

  // initial info panel population
  if(localizaciones_array.length > 0)
  populate_info_panel(localizaciones_array[0]);


  /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compared with location names or licenses
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('location_name', 'license'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.location_id === localMatch.location_id;
    },
    local: localizaciones_array,
    remote: {
      url: '/localizaciones/%QUERY',
      filter: function(list) {
        // populate global arrays with matching results

          localizaciones_array = list.locations;
          categorias_array = list.location_categories;
          agents_array = list.agents;
          ganaderos_array = list.ganaderos;
          // populate list with matching results
          populate_list(localizaciones_array);


        return $.map(list.locations, function(location) { 
          return location;
        });
      }
    }
  });

  // kicks off loading and processing of 'local' and 'prefetch'
  search_source.initialize();

  // set typeahead functionality for search bar
  $('#search_bar').typeahead({
    hint: false,
    highlight: true
  },
  {
    name: 'localizaciones',
    displayKey: 'location_name',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(location){
        return '<p><strong>Nombre: </strong>'+location.location_name+'</p>' + '<p><strong>Licencia: </strong>'+location.license+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').bind('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    localizaciones_array = [datum]
    populate_list([datum]);
  });

  $("#search_bar").keypress(function (event) {
    if (event.which == 13) {
      var user_input = $('#search_bar').val();
      if(user_input == ''){
        populate_localizaciones();
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

  $('#search_bar').on('input', function() { 
    populate_list(localizaciones_array);
  });
  /* Search Code End */

  $('#add_categoria_panel').hide();
  $('#btn_edit_categories').hide();


  /* Button: Add Categories */
  $('#btn_add_categories').on('click', function(){
    $('#categoria_panel').hide();
    $('#add_categoria_panel').toggle();
  });

  /* Button: Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users';
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($localizaciones_list);
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    //remove_active_class($localizaciones_list);
    $('#info_panel').show();

  });

  /* Close add panel */
  $('#btn_close_add_panel').on('click', function(){
    $('#add_associates_panel').hide();
    $('#add_agente_dropdown_panel').hide(); 
    $('#add_ganaderos_dropdown_panel').hide();
    remove_active_class($localizaciones_list);
  });

  /* Edit associates */
  $('#btn_add_associates').on('click', function(){
    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_associates_panel').show();

    // contains localizacion id
    var the_location = $(this).attr('data-id');
    $('#add_associates_panel').attr('data-location-id', the_location);

    // gives an array of indexes in ganaderos_array where the ganadero is associated to location_id
    function filter_by_id(person) {
      if(person.owner_location == the_location || person.manager_location == the_location){
        return true;
      } else {
        return false;
      }
    }

    function filter_agent_by_id(agent) {
      if(agent.location_id == the_location){
        return true;
      } else {
        return false;
      }
    }
    
    // Populate Associated Ganaderos panel
    var owner_content = '';
    var manager_content = '';
    var assigned_ganaderos = ganaderos_array.filter(filter_by_id);
    console.log("assigned ganaderos");
    console.log(assigned_ganaderos);
    console.log(ganaderos_array);
    $.each(assigned_ganaderos, function(i){
      var the_content = '';
      var relation_content; 
      if(this.relation_owner != null) {
        owner_content += "<a class='btn_change_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.owner_id+"' data-relation-type='"+this.relation_owner+"'><i class='glyphicon glyphicon-edit'></i></a>";
        owner_content += "<a class='btn_remove_ganadero btn btn-sm btn-danger' data-toggle='tooltip' type='button' href='#' data-id='"+this.owner_id+"' data-relation-type='"+this.relation_owner+"'><i class='glyphicon glyphicon-minus'></i></a>";
        owner_content += "<span>"+this.owner_name+"</span>"
      }
      if(this.relation_manager != null) {
        manager_content += "<a class='btn_change_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.manager_id+"' data-relation-type='"+this.relation_manager+"'><i class='glyphicon glyphicon-edit'></i></a>";
        manager_content += "<a class='btn_remove_ganadero btn btn-sm btn-danger' data-toggle='tooltip' type='button' href='#' data-id='"+this.manager_id+"' data-relation-type='"+this.relation_manager+"'><i class='glyphicon glyphicon-minus'></i></a>";
        manager_content += "<span>"+this.manager_name+"</span>";
      }
    });
    // if location has no associated owner and/or manager
    if(owner_content == ''){
      owner_content += "<a class='btn_add_associate_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button href='#' data-relation-type='owner'><i class='glyphicon glyphicon-plus'></i></a>";
    }
    if(manager_content == ''){
      manager_content += "<a class='btn_add_associate_ganadero btn btn-sm btn-success' data-toggle='tooltip' type='button href='#' data-relation-type='manager'><i class='glyphicon glyphicon-plus'></i></a>";
    }
    // inject content strings into html
    $('#add_associates_owner').html(owner_content);
    $('#add_associates_manager').html(manager_content);

    // Populate Associated Agent panel
    var agent_content = '';
    var assigned_agents = agents_array.filter(filter_agent_by_id);
    $.each(assigned_agents, function(i){
      agent_content += '<tr><td>';
      agent_content += "<a class='btn_change_agent btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.user_id+"'><i class='glyphicon glyphicon-edit'></i></a>";
      agent_content += "<a class='btn_remove_agent btn btn-sm btn-danger' data-toggle='tooltip' type='button' href='#' data-id='"+this.user_id+"'><i class='glyphicon glyphicon-minus'></i></a>";
      agent_content += '<span>'+this.username+'</span>';
      agent_content += "</td></tr>";
    });
    // Add table row with adding associated agent button
    if(agent_content == '') {
      agent_content += '<tr><td>';
      agent_content += "<a class='btn_add_associate_agent btn btn-sm btn-success' data-toggle='tooltip' type='button href='#'><i class='glyphicon glyphicon-plus'></i></a>";
      agent_content += '<strong>Agente Nuevo </strong>';
      agent_content += '</td></tr>';
    }
    // inject content strings into html
    $('#associated_agentes').html(agent_content);
  });

  /* Add associated ganadero */
  $('#associated_ganaderos').on('click', 'tr td a.btn_add_associate_ganadero, tr td a.btn_change_ganadero', function(e){
    // prevents link from firing
    e.preventDefault();
    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_agent_dropdown_panel').hide(); 
    $('#add_ganaderos_dropdown_panel').show();

    // set data attribute in input form for relation type to be changed
    $('#change_ganadero').attr('data-relation-type', $(this).attr('data-relation-type'));
  });

  /* Configuration for change associated ganadero input */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compard with ganadero full names or emails
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('person_name', 'email'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 6,
    remote: {
      url: '/all_ganaderos/%QUERY',
      filter: function(list) {
        if(list.ganaderos.length > 0)
          all_ganaderos = list.ganaderos;
        return $.map(list.ganaderos, function(ganadero) { 
          return ganadero;
        });
      }
    }
  });

  // kicks off loading and processing of 'local' and 'prefetch'
  search_source.initialize();

  // set typeahead functionality for search bar
  $('#change_ganadero').typeahead({
    hint: false,
    highlight: true
  },
  {
    name: 'all_ganaderos',
    displayKey: 'person_name',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(ganadero){
        return '<p><strong>Nombre: </strong>'+ganadero.person_name+'</p><p><strong>Email: </strong>'+ganadero.email+'</p>';
      }
    }
  });
  // ganadero name select event listener
  $('#change_ganadero').bind('typeahead:selected', function(obj, datum, name) {
    // add person_id value to input form
    $('#change_ganadero').attr('data-id', datum.person_id);
  });

// verifies input value is in array and returns boolean result
function valid_input_ganaderos(user_input, array) {
  for(var i=0; i < array.length; i++){
    if(user_input == array[i].person_name){
      $('#change_ganadero').attr('data-id', array[i].person_id);
      return true;
    }
  }
  return false;
}

// verifies input value is in array and returns boolean result
function valid_input_agents(user_input, array) {
  console.log(user_input);
  for(var i=0; i < array.length; i++){
    console.log(array[i].username);
    if(user_input == array[i].username){
      $('#change_agent').attr('data-id', array[i].user_id);
      return true;
    }
  }
  return false;
}


/* Ajax PUT call to assign ganadero as owner or manager of location */
$('#btn_submit_ganadero').on('click', function(){
    // disable edit associates button until populate localizaciones is done
    $('#btn_add_associates').addClass('disabled');
    // creat ganadero JSON to submit
    var this_input = $('#change_ganadero');
    var ganadero_association = {
      location_id: $('#add_associates_panel').attr('data-location-id'),
      ganadero_id: this_input.attr('data-id'),
      relation_type: this_input.attr('data-relation-type')
    };

    if(!valid_input_ganaderos(this_input.val(), all_ganaderos))
    {
      alert("El ganadero que escribió no existe.");
    }
    else
    {
      $.ajax({
        url: "/users/admin/associated/ganadero",
        method: "PUT",
        data: JSON.stringify(ganadero_association),
        contentType: "application/json",
        dataType: "json",

        success: function() {
          alert("Ganadero asociado ha sido editado en el sistema.");
          $('#info_panel').show();
        // update locations list after posting 
        populate_localizaciones();
        $('#add_associates_panel').hide();
        $('#add_ganaderos_dropdown_panel').hide();

        // reset typeahead input
        $('#change_ganadero').val('');
        $('#change_ganadero').removeAttr('data-id');
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

  /* Ajax DELETE call to assign ganadero as owner or manager of location */
  $('#associated_ganaderos').on('click', 'tr td a.btn_remove_ganadero', function(){
    
    // disable edit associates button until populate localiaciones is done
    $('#btn_add_associates').addClass('disabled');
    // creat ganadero JSON to submit
    var ganadero_association = {
      location_id: $('#add_associates_panel').attr('data-location-id'),
      ganadero_id: $(this).attr('data-id'),
      relation_type: $(this).attr('data-relation-type')
    };

    $.ajax({
      url: "/users/admin/associated/ganadero",
      method: "DELETE",
      data: JSON.stringify(ganadero_association),
      contentType: "application/json",
      dataType: "json",

      success: function() {
        alert("Asociación de ganadero a ésta localización ha sido eliminada.");
        $('#info_panel').show();
        // update locations list after posting 
        populate_localizaciones();
        $('#add_associates_panel').hide();
        $('#add_ganaderos_dropdown_panel').hide();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });

  /* Add/change associated agente  */
  $('#associated_agentes').on('click', 'tr td a.btn_add_associate_agent, tr td a.btn_change_agent', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_agent_dropdown_panel').show(); 
    $('#add_ganaderos_dropdown_panel').hide();
  });

  /* Configuration for change associated ganadero input */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compard with ganadero full names or emails
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('username'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 6,
    remote: {
      url: '/agents/%QUERY',
      filter: function(list) {
        all_agentes = list.agents;
        return $.map(list.agents, function(agent) { 
          return agent;
        });
      }
    }
  });

  // kicks off loading and processing of 'local' and 'prefetch'
  search_source.initialize();

  // set typeahead functionality for search bar
  $('#change_agent').typeahead({
    hint: false,
    highlight: true
  },
  {
    name: 'all_agents',
    displayKey: 'username',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(agent){
        return '<p><strong>Nombre: </strong>'+agent.username+'</p>';
      }
    }
  });
  // agent name select event listener
  $('#change_agent').bind('typeahead:selected', function(obj, datum, name) {
    // add user_id value to input form
    $('#change_agent').attr('data-id', datum.user_id);
  });

  /* Ajax PUT call to assign an agent to a location  */
  $('#btn_submit_agent').on('click', function(){
    // disable edit associates button until populate localizaciones is done
    $('#btn_add_associates').addClass('disabled');
    // creat agent JSON to submit
    var this_input = $('#change_agent');

    var agent_association = {
      location_id: $('#add_associates_panel').attr('data-location-id'),
      agent_id: this_input.attr('data-id')
    };
    if(!valid_input_agents(this_input.val(), all_agentes))
    {
      alert("El agente que escribió no existe.");
    }
    else
    {
    $.ajax({
      url: "/users/admin/associated/agent",
      method: "PUT",
      data: JSON.stringify(agent_association),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        alert("Agente asociado ha sido editado en el sistema.");
        // update locations list after posting 
        populate_localizaciones();
        $('#add_associates_panel').hide();
        $('#add_agent_dropdown_panel').hide();
        $('#info_panel').show();

        // reset typeahead input
        $('#change_agent').val('');
        $('#change_agent').removeAttr('data-id');
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

  /* Ajax DELETE call to remove an assigned agent from a location  */
  $('#associated_agentes').on('click', 'tr td a.btn_remove_agent', function(){
    // disable edit associates button until populate localizaciones is done
    $('#btn_add_associates').addClass('disabled');
    // creat agent JSON to submit
    var agent_association = {
      location_id: $('#add_associates_panel').attr('data-location-id'),
      agent_id: $(this).attr('data-id')
    };

    $.ajax({
      url: "/users/admin/associated/agent",
      method: "DELETE",
      data: JSON.stringify(agent_association),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        alert("Asociación de agente a ésta localización ha sido eliminada.");
        // update locations list after posting 
        populate_localizaciones();
        $('#add_associates_panel').hide();
        $('#add_agent_dropdown_panel').hide();
        $('#info_panel').show();

      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });

//updates categories associated to current location
$('#btn_edit_categories').on('click', function(){

  //get location id and category ids associated to said location id
  var location_id =  $('#btn_edit').attr('data-id');
  var checkedCategories = [];
  var unCheckedCategories = [];
  $(':checkbox:checked').each(function(i){
    checkedCategories[i] = $(this).val();
  });

$("input:checkbox:not(:checked)").each(function(i){
    unCheckedCategories[i] = $(this).val();
  });
 //json object for category_location
 var category_location = {
  location: location_id,
  categories: checkedCategories,
  unchecked_categories: unCheckedCategories
};

console.log("category location is ");
console.log(category_location);


$.ajax({
  url: "/users/admin/category_location",
  method: "PUT",
  data: JSON.stringify(category_location),
  contentType: "application/json",
  dataType: "json",

  success: function(data) {

    alert("Se han modificado las categorias.");

    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status:active " + status );
      console.dir( xhr );
    }
  });


});

//Delete Localizacion
$('#btn_delete').on('click', function(){
  var location_id = $('#btn_delete').attr("data-id");
  //url: "/users/admin/usuarios/" + usuario_id,
  $.ajax({
    url: "/users/admin/delete_location/" + location_id,
    method: "PUT",
    contentType: "application/json",
    dataType: "json",
    success: function(data) {
      alert("Localización fue eliminada");
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

/* Open info panel */
$localizaciones_list.on('click', 'tr td a.show_info_localizacion', function(e){
    // prevents link from firing
    //table_content += "<td><button class='btn_add_associates btn btn-sm btn-success btn-block' type='button', data-id='"+this.location_id+"'>Editar Personas Asociadas</button></td>";
   //table_content += "<td><button class='btn_edit_localizacion btn btn-sm btn-success btn-block' type='button' data-id='"+this.location_id+"'>Editar</button></td>";
    e.preventDefault();
    $('#btn_add_categories').show();
    $('#btn_edit_categories').hide();
    $('#info_panel_heading').show();
    $('#localizacion_info').show();
    $('#edit_associates_heading').hide();
    $('#add_associates_panel').hide();
    $('#edit_panel').hide();
    $('#info_panel').show();
    $('#add_categoria_panel').hide();
    $('#categoria_panel').show();

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
    console.log("location is is: ");
    console.log(location_id);
    $('#btn_edit_localizacion').attr("data-id", location_id);
    $('#btn_add_associates').attr("data-id", location_id);
    $('#btn_delete').attr("data-id", location_id);
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
  $('#categoria_panel_edit').hide();

  // clear add form
  $('#form_manage_location')[0].reset();
});

/* POSTs new location information */
$('#btn_submit').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_manage_location');
  var form_data = $the_form.serializeArray();
  var new_location = ConverToJSON(form_data);
  if(!new_location.localizacion_name.trim().length > 0)
  {
    alert("Por favor ingrese un nombre.");
  }
  else if(!new_location.localizacion_license.trim().length > 0)
  {
    alert("Por favor ingrese número de licencia.");
  }
  else if(!/^\d{1,10}$/.test(new_location.localizacion_address_zipcode))
  {
    alert('Código postal no puede ser más largo de diez caracteres.');
  }
  else
  {
  $.ajax({
    url: "/users/admin/localizaciones",
    method: "POST",
    data: JSON.stringify(new_location),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      if(data.exists){
        alert("Localización con este número de licencia ya existe");
      } else {
        alert("Localización ha sido añadido al sistema.");
        // clear add form
        $the_form[0].reset();
        // update locations list after posting 
        $('#info_panel').show();
        $('#edit_panel').hide();
        
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
}
});

/* POSTs new category information */
$('#btn_post_new_category').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_category');
  var form_data = $the_form.serializeArray();
  var new_category = ConverToJSON(form_data);
  // ajax call to post new category
  $.ajax({
    url: "/users/admin/new_category",
    method: "POST",
    data: JSON.stringify(new_category),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      if(data.exists){
        alert("Localización con este número de licencia ya existe");
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
$('#btn_edit_localizacion').on('click', function(){
  //$('#btn_add_categories').hide();
  $('#btn_edit_categories').show();
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();
  $('#add_associates_panel').hide();
  $('#add_categoria_panel').hide();
  $('#categoria_panel').show();
  $('#categoria_panel_edit').show();
  // contains location id
  var location_id = $(this).attr('data-id');
  var arrayPosition = localizaciones_array.map(function(arrayItem) { return arrayItem.location_id; }).indexOf(location_id);
  var this_location = localizaciones_array[arrayPosition];
  $('#btn_edit').attr('data-id', location_id);
  $('#localizacion_name').val(this_location.location_name);
  $('#localizacion_license').val(this_location.license);
  $('#localizacion_license_old').val(this_location.license);
  $('#localizacion_address_line1').val(this_location.address_line1);
  $('#localizacion_address_line2').val(this_location.address_line2);
  $('#localizacion_address_city').val(this_location.city);
  $('#localizacion_address_zipcode').val(this_location.zipcode);

  //change dropdown selected value
  $('#user_type li').on('click', function(){
    $(".user_type_dropdown:first-child").text($(this).text());
    $(".user_type_dropdown:first-child").val($(this).text());
  });
  populate_categories_edit();
});

/* PUTs edited location information */
$('#btn_edit').on('click', function(){

  //EDIT LOCATIONS
  var location_id = $(this).attr('data-id');
  // get form data and conver to json format
  var $the_form = $('#form_manage_location');
  var form_data = $the_form.serializeArray();
  var new_location = ConverToJSON(form_data);

   if(!new_location.localizacion_name.trim().length > 0)
  {
    alert("Por favor ingrese un nombre.");
  }
  else if(!new_location.localizacion_license.trim().length > 0)
  {
    alert("Por favor ingrese número de licencia.");
  }
  else if(!/^\d{1,10}$/.test(new_location.localizacion_address_zipcode))
  {
    alert('Código postal no puede ser más largo de diez caracteres.');
  }
  else
  {

  // ajax call to update location
  $.ajax({
    url: "/users/admin/localizaciones/" + location_id,
    method: "PUT",
    data: JSON.stringify(new_location),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {

      if(data.exists)
      {
        alert("Localización con ese número de licensia ya existe en el sistema.");
      }
      else
      {
        alert("Localización ha sido modificada.");
      // update locations list after posting 
      $('#info_panel').show();
      $('#edit_panel').hide();


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
}


//EDIT CATEGORIES
var location_id =  $('#btn_edit').attr('data-id');
var checkedCategories = [];
var unCheckedCategories = [];
$(':checkbox:checked').each(function(i){
  checkedCategories[i] = $(this).val();
});

$("input:checkbox:not(:checked)").each(function(i){
  unCheckedCategories[i] = $(this).val();
});
 //json object for category_location
 var category_location = {
  location: location_id,
  categories: checkedCategories,
  unchecked_categories: unCheckedCategories
};

$.ajax({
  url: "/users/admin/category_location",
  method: "PUT",
  data: JSON.stringify(category_location),
  contentType: "application/json",
  dataType: "json",

  success: function(data) {
    repopulate_categories(data.location_categories);

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

});

/* Populates info panel with $this_location's information */
function populate_info_panel($this_location){
  var missing_owner = false;
  var missing_manager = false;
  console.log($this_location);
  populate_categories_info($this_location.location_id);
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

  $('#btn_edit_localizacion').attr("data-id", $this_location.location_id);
  $('#btn_add_associates').attr("data-id", $this_location.location_id);
  $('#btn_delete').attr("data-id", $this_location.location_id);

  // populate local array with ganaderos associated to this location
  var selectedGanaderos = [];
  $.each(ganaderos_array, function(i){
    if($this_location.location_id == ganaderos_array[i].owner_location || $this_location.location_id == ganaderos_array[i].manager_location){
      selectedGanaderos.push(ganaderos_array[i]);
    }
  });  
  // populate ganaderos table according to relation_type
  if(selectedGanaderos.length > 0){
    for (j = 0; j < selectedGanaderos.length; j++) { 
      if(selectedGanaderos[j].relation_owner != null) {
        $('#owner').html(selectedGanaderos[j].owner_name);
      } 
      if(selectedGanaderos[j].relation_manager != null){
        $('#manager').html(selectedGanaderos[j].manager_name);
      }
    }
  } 
  //else if(selectedGanaderos.length == 1)
 // {
   // missing_manager = selectedGanaderos[0].manager_name;
   // missing_owner = selectedGanaderos[0].owner_name;
   // if(missing_manager == null)
    //   $('#manager').html('Gerente no asignado');

   // if(missing_owner == null)
      // $('#owner').html('Dueño asignado');
 // } 
  else {
    // array is empty so ganaderos populate with placeholders
    $('#owner').html('Dueño no asignado');
    $('#manager').html('Gerente no asignado');
    console.log();
  }

  //if either has manager or owner (at least one of them)
  if(!selectedGanaderos.length == 0)
  {
   missing_manager = selectedGanaderos[0].manager_name;
   missing_owner = selectedGanaderos[0].owner_name;
     if(missing_manager == null)
       $('#manager').html('Gerente no asignado');
    if(missing_owner == null)
       $('#owner').html('Dueño asignado');
   }


  // populate agentes table if there is one assigned
  var agent_found = false;
  var table_content = '';
  $.each(agents_array, function(i){
    if($this_location.location_id == agents_array[i].location_id){
      agent_found = true;
      table_content += '<tr><td>'+agents_array[i].username+'</td></tr>';
    }
  }); 
  // checks if agent is found
  if(agent_found){
    $('#localizacion_agentes').html(table_content);
  } else {
    $('#localizacion_agentes').html('<tr><td>Agente no asignado</td></tr>');
  }
  agent_found = false;  

  console.log("Categorias Panel Title");

  //var currentText = $('#categoria_panel_title').text();
  //$('#categoria_panel_title').text("Categoria de Localizacion")
  //$('#categoria_panel_title').text('');
};

/* Populate list with first 20 locations, organized alphabetically by location_name */
function populate_localizaciones(){
  $.getJSON('/list_localizaciones', function(data) {
    $('#edit_associates_heading').hide();
    $('#localizacion_associates').hide();
    localizaciones_array = data.localizaciones;
    agents_array = data.agentes;
    ganaderos_array = data.ganaderos;
    categorias_array = data.location_categories;

    populate_list(data.localizaciones);
    $('#btn_add_associates').removeClass('disabled');
  });
};

/* Populate list with ganaderos_set information */
function populate_list(locations_set){
  // contents of localizaciones list
  var table_content = '';

  // for each item in JSON, add table row and cells
  $.each(locations_set, function(i){
    table_content += '<tr>';
    table_content += "<td><a class='list-group-item ";
    // if initial list item, set to active
    if(i==0) {
      table_content +=  'active ';
    }
    table_content += "show_info_localizacion' href='#', data-id='"+this.location_id+"'>"+this.location_name+"</a></td>";
   //table_content += "<td><button class='btn_add_associates btn btn-sm btn-success btn-block' type='button', data-id='"+this.location_id+"'>Editar Personas Asociadas</button></td>";
   //table_content += "<td><button class='btn_edit_localizacion btn btn-sm btn-success btn-block' type='button' data-id='"+this.location_id+"'>Editar</button></td>";
   //table_content += "<td><a class='btn_delete_localizacion btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.location_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
    table_content += '</tr>';
  });  
 
  // inject content string into html
  $localizaciones_list.html(table_content);

  // populate info panel with first location's information
  if(locations_set.length > 0)
    populate_info_panel(locations_set[0]);
};

  function populate_categories_info(variable){

    var the_categories = [];
/*    $.ajax({
      url: "/location/" + variable + "/categories/",
      method: "GET",
      contentType: "application/json",
      dataType: "json",

      success: function(data) {

       the_categories = data.location_categories;
       console.log("updated categories");
       console.log(the_categories);

      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });*/

    var location_id =  variable;
    var matches = [];
  //find matches
  $.each(categorias_array, function(i){
    if(location_id == categorias_array[i].location_id){      
     matches.push(categorias_array[i]);
   }
 }); 

  var content = '';
  var found = false;
  var i = 0;
  //solid algorithm
  $.each(all_categorias_array, function(i){
    //for each specialty, check if it is present in the matches array
    //if so then check property "checked" as true
    $.each(matches, function(j){
      if(matches[j].category_id == all_categorias_array[i].category_id)
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
      content += "" + all_categorias_array[i].category_name + "</li>";
    }
    else
    {
      // content += '<input ';
      // content += "type='checkbox'> " + all_categorias_array[i].category_name + " <br>";
    }
    found = false;
  });


  if(content == '')
    content = "<p> Localización no tiene categorías asignadas. </p>";  
  $('#categories_list').html(content);

}


function populate_categories_edit(){

  var location_id =  $('#btn_edit').attr('data-id');
  var matches = [];
  //find matches
  console.log("locations is ");
  console.log(categorias_array);
  console.log("location id is ");
  console.log(location_id);
  console.log("all categories is ");
  console.log(all_categorias_array);
  $.each(categorias_array, function(i){
    if(location_id == categorias_array[i].location_id){      
     matches.push(categorias_array[i]);
   }
 }); 

  console.log("matches is ");
  console.log(matches);

  var content = '';
  var found = false;
  var i = 0;
  //solid algorithm
  $.each(all_categorias_array, function(i){
    //for each specialty, check if it is present in the matches array
    //if so then check property "checked" as true
    $.each(matches, function(j){
      if(matches[j].category_id == all_categorias_array[i].category_id)
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
      console.log("1");
     content += '<input ';
     content += "type='checkbox' checked='true' value='" + all_categorias_array[i].category_id + "'> " + all_categorias_array[i].category_name + " <br>";
   }
   else
   {
    console.log("2");
     content += '<input ';
     content += "type='checkbox' value='" + all_categorias_array[i].category_id + "'> " + all_categorias_array[i].category_name + " <br>";
   }
   found = false;
 });

  console.log(content);



  $('#categories_list_edit').html(content);

}

function repopulate_categories(categories)
{

  var content = ''
  $.each(categories, function(i){
    content += '<li> ';
    content += "" + categories[i].category_name + "</li>";
  });


  if(content == '')
  content = "<p> Localización no tiene categorías asignadas. </p>";


 $('#categories_list').html(content);

}


});