$(document).ready(function(){
  // dispositivos list
  $dispositivos_list = $('#dispositivos_list');

  // store data for initial 20 dispositivos
  var dispositivos_array =  JSON.parse($dispositivos_list.attr('data-dispositivos'));

  // initial population of dispositivos list
  populate_list(dispositivos_array)
  // initial info panel population
  populate_info_panel(dispositivos_array[0]);

  // hide input that contains user chosen from dropdown
  $('#dispositivo_usuario').hide();

  /* Search Code start */
  // constructs the suggestion engine
  var search_source = new Bloodhound({
    // user input is tokenized and compard with ganadero full names or emails
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('id_number', 'username', 'device_name'),
    queryTokenizer: Bloodhound.tokenizers.whitespace, 
    limit: 10,
    dupDetector: function(remoteMatch, localMatch) {
      return remoteMatch.device_id === localMatch.device_id;
    },
    local: dispositivos_array,
    remote: {
      url: 'http://localhost:3000/dispositivos/%QUERY',
      filter: function(list) {
        // populate global array with matching results
        dispositivos_array = list.devices;
        // populate list with matching results
        populate_list(list.devices);
        return $.map(list.devices, function(device) { 
          return device;
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
    name: 'devices',
    displayKey: 'id_number',
    source: search_source.ttAdapter(),
    templates: {
      suggestion: function(device){
        return '<p><strong>Dispositivo: </strong>'+device.device_name+'</p><p><strong># de identificación: </strong>'+device.id_number+'</p>';
      }
    }
  });

  // search bar input select event listener
  $('#search_bar').bind('typeahead:selected', function(obj, datum, name) {
    // populate list with selected search result
    populate_list({datum});
  });
  /* Search Code End */

  /* Return home */
  $('#btn_home').on('click', function(){
  	window.location.href = '/users'
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($dispositivos_list);
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($dispositivos_list);
  });

  /* Open info panel */
  $dispositivos_list.on('click', 'tr td a.show_info_dispositivo', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($dispositivos_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains dispositivo id
    var dispositivo_id = $this.attr('data-id');
    var arrayPosition = dispositivos_array.map(function(arrayItem) { return arrayItem.device_id; }).indexOf(dispositivo_id);
    var this_dispositivo = dispositivos_array[arrayPosition];
    
    // populate info panel with this_dispositivo
    populate_info_panel(this_dispositivo);
  });

  /* Add dispositivo */
  $('#btn_add_dispositivo').on('click', function(){
    $('#btn_edit, #heading_edit').hide();
    $('#btn_submit, #heading_create').show();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // clear add form
    $('#form_manage_dispositivo')[0].reset();
  });


  /* POSTs new dispositivo information */
  $('#btn_submit').on('click', function(){
    var $the_form = $('#form_manage_dispositivo');
    var form_data = $the_form.serializeArray();
    var new_dispositivo = ConverToJSON(form_data);

    // ajax call to post new device
    $.ajax({
      url: "http://localhost:3000/users/admin/dispositivos",
      method: "POST",
      data: JSON.stringify(new_dispositivo),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        if(data.exists){
          alert("Dispositivo con este numero de identificacion ya existe");
        } else {
          alert("Dispositivo ha sido añadido al sistema.");
          // clear add form
          $the_form[0].reset();
        }
         // update dispositivos list after posting 
         populate_dispositivos();
         $('#edit_panel').hide();
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
  $dispositivos_list.on('click', 'tr td button.btn_edit_dispositivo', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains dispositivo id
    var dispositivo_id = $(this).attr('data-id');
    var arrayPosition = dispositivos_array.map(function(arrayItem) { return arrayItem.device_id; }).indexOf(dispositivo_id);
    var this_dispositivo = dispositivos_array[arrayPosition];

    $('#btn_edit').attr('data-id', dispositivo_id);
    $('#dispositivo_name').val(this_dispositivo.device_name);
    $('#dispositivo_id_num').val(this_dispositivo.id_number);
    $('#dispositivo_usuario').val(this_dispositivo.username);
  });

  /* PUTs edited dispositivo information */
  $('#btn_edit').on('click', function(){
    var dispositivo_id = $(this).attr('data-id');
    // get form data and conver to json format
    var $the_form = $('#form_manage_dispositivo');
    var form_data = $the_form.serializeArray();
    var new_dispositivo = ConverToJSON(form_data);

    // ajax call to update device
    $.ajax({
      url: "http://localhost:3000/users/admin/dispositivos/" + dispositivo_id,
      method: "PUT",
      data: JSON.stringify(new_dispositivo),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        alert("Informacion de ganadero ha sido editada en el sistema.");
        // update ganadero list after posting 
        populate_dispositivos();
        $('#edit_panel').hide();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });

  /* Change asigned agente dropdown selected value */
  $('#list_dropdown_agentes').on('click', 'li a', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#btn_dropdown_agentes_text').text($(this).text()+' ');
    //$('#btn_dropdown_agentes').val($(this).attr('data-id'));
    $('#dispositivo_usuario').val($(this).attr('data-id'));
  });

  /* Populate info panel with $this_dispositivo info */
  function populate_info_panel($this_dispositivo) {
    $('#info_panel_heading').text($this_dispositivo.device_name);
    $('#dispositivo_info_name').text($this_dispositivo.device_name);
    $('#dispositivo_info_id_num').text($this_dispositivo.id_number);
    $('#dispositivo_info_usuario').text($this_dispositivo.username);
    $('#dispositivo_info_last_sync').text($this_dispositivo.last_sync);
  }

  /* Populate list with first 20 ganaderos, ordered by assigned user */
  function populate_dispositivos() {
  	$.getJSON('http://localhost:3000/list_dispositivos', function(data) {
  		dispositivos_array = data.dispositivos;

      populate_list(data.dispositivos);
    });
  };

  /* Populate list with devices_set information */
  function populate_list(devices_set){
    // contents of dispositivos list
    var table_content = '';

    // device_id, devices.name as device_name, latest_sync, devices.user_id as assigned_user, username
    // for each item in JSON, add table row and cells
    $.each(devices_set, function(i){
      table_content += '<tr>';
      table_content += "<td><a class='list-group-item ";

      // if initial list item, set to active
      if(i==0) {
        table_content +=  'active ';
      }
      table_content += "show_info_dispositivo' href='#', data-id='"+this.device_id+"'>"+this.device_name+"</a></td>";
      table_content += '<td><center>'+this.last_sync+'</center></td>';
      table_content += "<td><button class='btn_edit_dispositivo btn btn-sm btn-success btn-block' type='button' data-id='"+this.device_id+"'>Editar</button></td>";
      table_content += "<td><a class='btn_delete_dispositivo btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.device_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
      table_content += '</tr>';
    });  

    // inject content string into html
    $dispositivos_list.html(table_content);
    // close current info panel 
    $('#btn_close_info_panel').trigger('click');
  };
});