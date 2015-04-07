$(document).ready(function(){
  // store data for 10 localizaciones
  var localizaciones_array;
  var agentes_array;
  var ganaderos_array;
  // initial population of localizaciones list
  populate_localizaciones();
  // localizaciones list
  $localizaciones_list = $('#localizaciones_list');

  /* Button: Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
  });


  /* Add ganadero */
  $('#btn_add_localizacion').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();
  });

  /* */
  $localizaciones_list.on('click', 'tr td button.btn_add_associates', function(e){
    $('#edit_panel').hide();
    $('#info_panel').hide();
    $('#add_assosiates_panel').show();
  });

  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($localizaciones_list);
  });

  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($localizaciones_list);
  });

  $localizaciones_list.on('click', 'tr td a.show_info_localizacion', function(e){

    $('#info_panel_heading').show();
    $('#localizacion_info').show();
    $('#edit_associates_heading').hide();
    $('#localizacion_associates').hide();
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

  // contains ganadero id

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

// RAMON READ: at the end of each of this functions you need to 
// RAMON READ: create an object that contains the id(s) of the ganadero(s)
// RAMON READ: you are assigning as owner and/or manager to this location
var ganaderos = populate_ganaderos();
$('#btn_add_ganadero').on('click', function(){
  
  // ajax POST call to assign ganadero as owner or manager of location
});

// RAMON READ: at the end of each of this functions you need to 
// RAMON READ: create an object that contains the id of the usuario
// RAMON READ: you are assigning to this location
var usuarios = populate_usuarios();
$('#btn_add_usuario').on('click', function(){
  
  //ajax POST call to assign a user to a location 
});


function populate_localizaciones(){
  $.getJSON('http://localhost:3000/users/admin/list_localizaciones', function(data) {
    $('#edit_associates_heading').hide();
    $('#localizacion_associates').hide();
    localizaciones_array = data.localizaciones;
    agentes_array = data.agentes;
    ganaderos_array = data.ganaderos;
    console.log(data.agentes);
    console.log(data.localizaciones);
    console.log(data.ganaderos);

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
        table_content += "<td><button class='btn_add_associates btn btn-sm btn-success btn-block' type='button', data-id='"+this.location_id+"'>Agregar Ganadero/Agente</button></td>";
        table_content += "<td><button class='btn_edit_localizacion btn btn-sm btn-success btn-block' type='button' data-id='"+this.location_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_localizacion btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.location_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $localizaciones_list.html(table_content);
    });
  };
});