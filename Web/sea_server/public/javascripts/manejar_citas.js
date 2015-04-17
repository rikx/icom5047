$(document).ready(function(){
  // citas list
  $citas_list = $('#citas_list');
  // format dates in initial cita list population
  format_dates($('.show_date_cita'));
  // store data for initial 20 citas
  var citas_array = JSON.parse($citas_list.attr('data-citas'));
  // initial info panel population
  populate_info_panel(citas_array[0]);

  /* Button: Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    remove_active_class($citas_list);
  });

  /* Close info panel */
  $('#btn_close_info_panel').on('click', function(){
    $('#info_panel').hide();
    remove_active_class($citas_list);
  });

  /* Open info panel */
  $citas_list.on('click', 'tr td a.show_info_cita', function(e){
    // preveLnts link from firing
    e.preventDefault();

    $('#location_panel').hide();
    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous list item 
    remove_active_class($citas_list);
    // add active to current clicked list item
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains cita id
    var cita_id = $this.attr('data-id');
    var arrayPosition = citas_array.map(function(arrayItem) { return arrayItem.appointment_id; }).indexOf(cita_id);
    var this_cita = citas_array[arrayPosition];

    // populate info panel with this_cita
    populate_info_panel(this_cita);
  });

  /* Open edit panel */
  $citas_list.on('click', 'tr td button.btn_edit_cita', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    var cita_id = $(this).attr('data-id');
    var arrayPosition = citas_array.map(function(arrayItem) { return arrayItem.appointment_id; }).indexOf(cita_id);
    var this_cita = citas_array[arrayPosition];

    //date and time logic
    var dateTimeObject = new Date(this_cita.date + " " + this_cita.time);
    var day = ("0" + dateTimeObject.getDate()).slice(-2);
    var month = ("0" + (dateTimeObject.getMonth() + 1)).slice(-2);
    var hours = dateTimeObject.getHours();
    var minutes = dateTimeObject.getMinutes();
    var seconds = dateTimeObject.getSeconds();
    var theDay = dateTimeObject.getFullYear()+"-"+(month)+"-"+(day);
    var theTime = ('0' + hours).slice(-2) + ":" + ('0' + minutes).slice(-2) + ":" + ('0' + seconds).slice(-2);
    
    $('#btn_edit').attr('data-id', cita_id);
    $('#cita_proposito').val(this_cita.purpose);
    $('#cita_date').val(theDay);
    $('#cita_time').val(theTime);
  });

  /* PUTs edited ganadero information */
  $('#btn_edit').on('click', function(){

  });

  /* Close location panel */
  $('#btn_close_location_panel').on('click', function(){
    $('#location_panel').hide();
  });

  /* Open location panel */
  $('#cita_info').on('click', 'tr td a.show_location_info', function(e){
    // preveLnts link from firing
    e.preventDefault();

    $('#location_panel').show();
    // contains location id
    var location_id = $(this).attr('data-location-id');
    $.getJSON('http://localhost:3000/location/'+location_id, function(data){
      // populate location panel with this_location
      populate_location_panel(data.location, data.ganaderos, data.agentes);
    });
  });

  /* Populates info panel with $this_location's information */
  function populate_location_panel($this_location, location_ganaderos, location_agentes){
    $('#location_panel_heading').text($this_location.location_name);
    $('#localizacion_info_name').text($this_location.location_name);
    $('#localizacion_info_category').text($this_location.location_category);
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
    $.each(location_ganaderos, function(i){
      if($this_location.location_id == location_ganaderos[i].location_id){
        selectedGanaderos.push(location_ganaderos[i]);
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
    $.each(location_agentes, function(i){
      if($this_location.location_id == location_agentes[i].location_id){
        agent_found = true;
        table_content += '<tr><td>'+location_agentes[i].username+'</td></tr>';
      }
    }); 
    // checks if agent is found
    if(agent_found){
      $('#localizacion_agentes').html(table_content);
    } else {
      $('#localizacion_agentes').html('<tr><td>Agente no asignado</td></tr>');
    }  
  };

  /* Populate info panel with $this_cita information */
  function populate_info_panel($this_cita){
    $('#cita_info_location').html("<a class='show_location_info' href='#' data-location-id='"+$this_cita.location_id+"'>"+$this_cita.location_name+"</a>");
    $('#cita_info_date').text(get_date_time($this_cita.date, false));   
    $('#cita_info_hour').text($this_cita.time);
    $('#cita_info_purpose').text($this_cita.purpose);
    $('#cita_info_agent').text($this_cita.username);   
    $('#cita_info_report').html("<a href='/users/admin/reportes/" + $this_cita.report_id + "'> Reporte " + $this_cita.report_id + "</a>");
  }

  /* */
  function populate_citas(){
    $.getJSON('http://localhost:3000/list_citas', function(data) {
      citas_array = data.citas;

      // contents of localizaciones list
      var table_content = '';

      // for each item in JSON, add table row and cells
      $.each(data.citas, function(i){
        table_content += '<tr>';
        table_content += "<td><a class='list-group-item ";
        // if initial list item, set to active
        if(i==0) {
          table_content +=  'active ';
        }
        table_content += "show_info_cita' href='#', data-id='"+this.appointment_id+"'>"+this.location_name+"</a></td>";
        table_content += '<td><center>'+get_date_time(this.date, false)+' at '+this.time+'</center></td>';
        table_content += "<td><button class='btn_edit_cita btn btn-sm btn-success btn-block' type='button' data-id='"+this.appointment_id+"'>Editar</button></td>";
        table_content += "<td><a class='btn_delete_cita btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+this.appointment_id+"'><i class='glyphicon glyphicon-trash'></i></a></td>";
        table_content += '</tr>';
      });  

      // inject content string into html
      $citas_list.html(table_content);
    });
  };
});