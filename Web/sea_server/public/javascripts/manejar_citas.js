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

  /* Populate info panel with $this_cita information */
  function populate_info_panel($this_cita){
    $('#cita_info_location').text($this_cita.location_name);
    $('#cita_info_date').text(get_date_time($this_cita.date, false));   
    $('#cita_info_hour').text($this_cita.time);
    $('#cita_info_purpose').text($this_cita.purpose);
    $('#cita_info_agent').text($this_cita.username);   
    $('#cita_info_report').html("<a href='/users/admin/reportes/" + $this_cita.report_id + "'> Reporte " + $this_cita.report_id + "</a>");
  }

  /* */
  function populate_citas(){
    $.getJSON('http://localhost:3000/users/admin/list_citas', function(data) {
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