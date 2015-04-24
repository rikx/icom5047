$(document).ready(function(){
  // ganaderos list
  $ganaderos_list = $('#ganaderos_list');

  // initial population of ganaderos list
  populate_ganaderos();

  // store data for initial 20 ganaderos
  var ganaderos_array =  JSON.parse($ganaderos_list.attr('data-ganaderos'));
  var localizaciones_array = JSON.parse($ganaderos_list.attr('data-localizaciones'));

  // initial info panel population
  populate_info_panel(ganaderos_array[0]);

  /* Search Code start */
  $('#btn_search').on('click', function() {
    // populate list with search results
    populate_list(ganaderos_set, locations_set);
  });

  $('.typeahead').typeahead({
    hint: false,
    highlight: true
  },
  {
    name: 'locations',
    displayKey: 'value',
    source: substringMatcher(locations_array)
  });
  /* Search Code End */

  /* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users'
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($ganaderos_list);
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($ganaderos_list);
  });

  /* Open info panel */
  $ganaderos_list.on('click', 'tr td a.show_info_ganadero', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous clicked list item
    remove_active_class($ganaderos_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    var ganadero_id = $this.attr('data-id');
    var arrayPosition = ganaderos_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(ganadero_id);
    var this_ganadero = ganaderos_array[arrayPosition];

    // populate info panel with this_ganadero info
    populate_info_panel(this_ganadero);
  });

  /* Add ganadero */
  $('#btn_add_ganadero').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // clear add form
    $('#form_manage_ganadero')[0].reset();
  });

  /* POSTs new ganadero information */
  $('#btn_submit').on('click', function(){
    // get form data and conver to json format
    var $the_form = $('#form_manage_ganadero');
    var form_data = $the_form.serializeArray();
    var new_ganadero = ConverToJSON(form_data);

    // ajax call to post new ganadero
    $.ajax({
      url: "http://localhost:3000/users/admin/ganaderos",
      method: "POST",
      data: JSON.stringify(new_ganadero),
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
        populate_ganaderos();
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
  $ganaderos_list.on('click', 'tr td button.btn_edit_ganadero', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

  // contains ganadero id
  var ganadero_id =  $(this).attr('data-id');
  var arrayPosition = ganaderos_array.map(function(arrayItem) { return arrayItem.person_id; }).indexOf(ganadero_id);
  var this_ganadero = ganaderos_array[arrayPosition];

  $('#btn_edit').attr('data-id', ganadero_id);
  $('#ganadero_name').val(this_ganadero.first_name);
  $('#ganadero_m_initial').val(this_ganadero.middle_initial);
  $('#ganadero_apellido1').val(this_ganadero.last_name1);
  $('#ganadero_apellido2').val(this_ganadero.last_name2);
  $('#ganadero_email').val(this_ganadero.email);
  $('#ganadero_telefono').val(this_ganadero.phone_number);
});

  /* PUTs edited ganadero information */
  $('#btn_edit').on('click', function(){
    var ganadero_id = $(this).attr('data-id');
  // get form data and conver to json format
  var $the_form = $('#form_manage_ganadero');
  var form_data = $the_form.serializeArray();
  var new_ganadero = ConverToJSON(form_data);

  // ajax call to update ganadero
  $.ajax({
    url: "http://localhost:3000/users/admin/ganaderos/" + ganadero_id,
    method: "PUT",
    data: JSON.stringify(new_ganadero),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      alert("Informacion de ganadero ha sido editada en el sistema.");
      // update ganadero list after posting 
      populate_ganaderos();
    },
    error: function( xhr, status, errorThrown ) {
      alert( "Sorry, there was a problem!" );
      console.log( "Error: " + errorThrown );
      console.log( "Status: " + status );
      console.dir( xhr );
    }
  });
});

  /* DELETEs ganadero information */
  $ganaderos_list.on('click', 'tr td a.btn_delete_ganadero', function(e){
    // prevents link from firing
    e.preventDefault();

    // contains ganadero id
    $(this).attr('data-id');

    $.ajax({
      url: "http://localhost:3000/users/admin/ganadero",
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

  /* Populates info panel with $this_ganadero information */
  function populate_info_panel($this_ganadero){
    // populate basic information panel
    $('#info_panel_heading').text($this_ganadero.first_name + " " + $this_ganadero.last_name1 + " " + $this_ganadero.last_name2);
    if($this_ganadero.middle_initial == null) {
      $('#ganadero_info_name').text($this_ganadero.first_name);
    } else {
      $('#ganadero_info_name').text($this_ganadero.first_name + " " + $this_ganadero.middle_initial);
    }
    $('#ganadero_info_apellidos').text($this_ganadero.last_name1 + " " + $this_ganadero.last_name2);
    $('#ganadero_info_contact').html('<p>'+$this_ganadero.email+'</p><p>'+$this_ganadero.phone_number+'</p>');
    
   // populate associated locations panel
   var table_content = '';
   $.each(localizaciones_array, function(i){
    if($this_ganadero.person_id == this.person_id){
      table_content += '<tr><td>'+this.location_name+'</td></tr>';
    }
  });  
   $('#ganadero_locations').html(table_content);
 }

  /* */
  function populate_ganaderos(){
    $.getJSON('http://localhost:3000/list_ganaderos', function(data) {
      ganaderos_array = data.ganaderos;
      localizaciones_array = data.locations;

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
      $ganaderos_list.html(table_content);
    });
  };
  
  /* Populate list from search results */
  function populate_list(ganaderos_set, locations_set){
    ganaderos_array = ganaderos_set;
    localizaciones_array = locations_set;

    // contents of ganaderos list
    var table_content = '';

    // for each item in JSON, add table row and cells
    $.each(ganaderos_set, function(i){
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
    $ganaderos_list.html(table_content);
  };
});