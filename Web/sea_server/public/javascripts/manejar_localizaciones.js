$(document).ready(function(){
  // localizaciones list
  $localizaciones_list = $('#localizaciones_list');

  // initial population of localizaciones list
  populate_localizaciones();
  populate_dropdowns();

  // store data for initial 20 locations
  var localizaciones_array = JSON.parse($localizaciones_list.attr('data-localizaciones'));
  var categorias_array = JSON.parse($localizaciones_list.attr('data-categorias'));
  var agentes_array =  JSON.parse($localizaciones_list.attr('data-agentes'));
  var ganaderos_array =  JSON.parse($localizaciones_list.attr('data-ganaderos'));
  var all_categorias_array = JSON.parse($('#categoria_panel').attr('data-all-categorias'));

  // initial info panel population
  populate_info_panel(localizaciones_array[0]);

  $('#add_categoria_panel').hide();
  $('#btn_edit_categories').hide();


  /* Button: Add Categories */
  $('#btn_add_categories').on('click', function(){
    $('#categoria_panel').hide();
    $('#add_categoria_panel').show();



  });


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


//updates categories associated to current location
$('#btn_edit_categories').on('click', function(){

  //get location id and category ids associated to said location id
  var location_id =  $('#btn_edit').attr('data-id');
  var checkedCategories = [];
  $(':checkbox:checked').each(function(i){
    checkedCategories[i] = $(this).val();
  });

 //json object for category_location
 var category_location = {
  location: location_id,
  categories: checkedCategories,
};

console.log("category location is ");
console.log(category_location);


$.ajax({
  url: "http://localhost:3000/users/admin/category_location",
  method: "PUT",
  data: JSON.stringify(category_location),
  contentType: "application/json",
  dataType: "json",

  success: function(data) {

    alert("server fucntion executed succesful");

    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });


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
    $('#btn_add_categories').show();
    $('#btn_edit_categories').hide();
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

/* POSTs new category information */
$('#btn_post_new_category').on('click', function(){
  // get form data and conver to json format
  var $the_form = $('#form_new_category');
  var form_data = $the_form.serializeArray();
  var new_category = ConverToJSON(form_data);

  console.log("Posting New Category.New category is ");
  console.log(new_category);


  // ajax call to post new category
  $.ajax({
    url: "http://localhost:3000/users/admin/new_category",
    method: "POST",
    data: JSON.stringify(new_category),
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
  $('#btn_add_categories').hide();
  $('#btn_edit_categories').show();
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
  populate_categories_edit();
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
    categorias_array = data.location_categories;

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


  function populate_categories_info(variable){

    var location_id =  variable;
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
     content += '<input ';
     content += "type='checkbox' checked='true' value='" + all_categorias_array[i].category_id + "'> " + all_categorias_array[i].category_name + " <br>";
   }
   else
   {
     content += '<input ';
     content += "type='checkbox' value='" + all_categorias_array[i].category_id + "'> " + all_categorias_array[i].category_name + " <br>";
   }
   found = false;
 });

  
  $('#categories_list').html(content);

}

});