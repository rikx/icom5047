$(document).ready(function(){
  // store data for initial 10 localizaciones
  var localizaciones_array, agentes_array, ganaderos_array;
  // initial population of localizaciones list
  populate_localizaciones();

  // localizaciones list
  $localizaciones_list = $('#localizaciones_list');

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


  /* Add ganadero */
  $('#btn_add_localizacion').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();
    $('#add_associates_panel').hide();
  });



  /* */
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

    var list_content = '';

    // ajax call to GET all ganaderos
    $.getJSON('http://localhost:3000/ganaderos', function(data) {
      $.each(data.ganaderos, function(i){
        list_content += "<li><a role='menuitem' tabindex='-1' href='#' data-id='"+this.person_id+"'>"+this.person_name+'</a></li>';
      });

      // popuplate dropdown list with ganaderos
      $('#list_associated_ganadero').html(list_content);
    });
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
  
    var list_content = '';

    // ajax call to GET all usuarios
    $.getJSON('http://localhost:3000/usuarios', function(data) {
      $.each(data.usuarios, function(i){
        list_content += "<li><a role='menuitem' tabindex='-1' href='#' data-id='"+this.user_id+"'>"+this.username+'</a></li>';
      });

      // popuplate dropdown list with ganaderos
      $('#list_associated_agente').html(list_content);
    });
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
    $('#info_panel_heading').show();
    $('#localizacion_info').show();
    $('#edit_associates_heading').hide();
    $('#add_associates_panel').hide();
    e.preventDefault();
    var table_content = '';
    

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($localizaciones_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }


    var selectedGanaderos = [];
    // contains ganadero id
    var myVar = $this.attr('data-id');
    var arrayPosition = localizaciones_array.map(function(arrayItem) { return arrayItem.location_id; }).indexOf(myVar);
    var thisUserObject = localizaciones_array[arrayPosition];
    

    // Populate info panel with selected item's information
    $('#info_panel_heading').text(thisUserObject.location_name);
    $('#localizacion_info_name').text(thisUserObject.location_name);
    $('#localizacion_info_license').text(thisUserObject.license);

    if(thisUserObject.address_line2 == null) {
      $('#localizacion_info_address').text(thisUserObject.address_line1);
    } else {
      $('#localizacion_info_address').text(thisUserObject.address_line1 + " " + thisUserObject.address_line2);
    }
    $('#localizacion_info_ciudad').text(thisUserObject.city);
    $('#localizacion_info_zipcode').text(thisUserObject.zipcode);

    $.each(ganaderos_array, function(i){
      if(myVar == ganaderos_array[i].location_id){
        selectedGanaderos.push(ganaderos_array[i]);
      }
    });  

    //populate ganaderos table according to relation_type
    for (j = 0; j < selectedGanaderos.length; j++) { 
    if(selectedGanaderos[j].relation_type == "owner") {
      //$("#localizacion_ganaderos td:contains('Ganadero 1')").html(selectedGanaderos[j].person_name);
      $('#owner').html(selectedGanaderos[j].person_name);
    } else {
       //$("#localizacion_ganaderos td:contains('Ganadero 2')").html(selectedGanaderos[j].person_name);
       $('#manager').html(selectedGanaderos[j].person_name);
     }
    }

    $.each(agentes_array, function(i){
      if(myVar == agentes_array[i].location_id){
        table_content += '<tr>';
        table_content += "<td> ";
        table_content += "" +agentes_array[i].username+"</td>";
        table_content += '</tr>';
      }
    }); 
    $('#localizacion_agentes').html(table_content);
 });

$localizaciones_list.on('click', 'tr td button.btn_edit_localizacion', function(){
  $('#btn_edit, #heading_edit').show();
  $('#btn_submit, #heading_create').hide();
  $('#edit_panel').show();
  $('#info_panel').hide();
  $('#add_associates_panel').hide();

  // contains localizacion id
  var myVar = $(this).attr('data-id');
  var arrayPosition = localizaciones_array.map(function(arrayItem) { return arrayItem.location_id; }).indexOf(myVar);
  var thisUserObject = localizaciones_array[arrayPosition];


  $('#localizacion_name').attr("value", thisUserObject.location_name);
  $('#localizacion_license').attr("value", thisUserObject.license);
  $('#localizacion_address_line1').attr("value", thisUserObject.address_line1);
  $('#localizacion_address_line2').attr("value", thisUserObject.address_line2);
  $('#localizacion_address_city').attr("value", thisUserObject.city);
  $('#localizacion_address_zipcode').attr("value", thisUserObject.zipcode);



  //change dropdown selected value
  $('#user_type li').on('click', function(){
    $(".user_type_dropdown:first-child").text($(this).text());
    $(".user_type_dropdown:first-child").val($(this).text());
  });
});


function populate_localizaciones(){
  $.getJSON('http://localhost:3000/users/admin/list_localizaciones', function(data) {
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
    });
  };
});