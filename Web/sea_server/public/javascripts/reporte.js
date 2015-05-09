$(document).ready(function(){
  // Ramon code
  var titleText = $('<input>').attr('type', 'text');
	$('#btn_submit_notes').hide();
  $('#title_panel').hide();

  //double click title to display title edit form
  $('#title').dblclick(function(e) {
    $('#title_panel').show();
    $('#title').hide();
  }); 

  // user_data is of the currently signed in user
  var user_data = JSON.parse($('#info_current_user').attr('data-user'));
  var report_data = JSON.parse($('#info_report').attr('data-report'));
  var survey_data = JSON.parse($('#answered_survey').attr('data-survey'));

  // stores appointment data locally if it exists
  var appointment_data = $('#info_panel').attr('data-appointment');
  if(appointment_data != undefined) {
    appointment_data = JSON.parse($('#info_panel').attr('data-appointment'));
  }

  /* Button: Return home */
  $('#btn_home').on('click', function(){
  	window.location.href = '/users';
  });

  /* Button: Return to Reportes */
  $('#btn_reportes').on('click', function(){
    window.location.href = '/users/reportes'; 
  });

  /* Button: Return to Citas */
  $('#btn_citas').on('click', function(){
    window.location.href = '/users/citas'; 
  });

  /* Close edit panel */
  $('#btn_close_edit_panel').on('click', function(){
    $('#edit_panel').hide();
    $('#info_panel').show();
  });

  /* Edit note */
  $('#btn_notes').on('click', function(){
  	var currentText = $('#notas').text();
  	$('#btn_notes').hide();
  	$('#btn_submit_notes').show();
  	$('#notas').html("<textarea id='note_text' rows='10' cols='40'>" + currentText + "</textarea>");
  });

  /* PUTs edited note */
  $('#btn_submit_notes').on('click', function(){
  	$('#btn_notes').show();
  	$('#btn_submit_notes').hide();

    var notes = {

     report_id: report_data.report_id,
     note_text: $('#note_text').val()
   };

    // ajax call to update location
    $.ajax({
    	url: "http://localhost:3000/users/reports/note",
    	method: "PUT",
    	data: JSON.stringify(notes),
    	contentType: "application/json",
    	dataType: "json",

    	success: function(data) {
    		alert("Se han agregado las notas exitosamente.");
    		$('#notas').html($('#note_text').val());
      },
      error: function( xhr, status, errorThrown ) {
       alert( "Sorry, there was a problem!" );
       console.log( "Error: " + errorThrown );
       console.log( "Status: " + status );
       console.dir( xhr );
     }
   });
  });

  /* Add appointment */
  $('#btn_add_appointment').on('click', function(){
    $('#info_panel, #btn_put_appointment').hide();
    $('#edit_panel').show();
  });

  /* POSTs new appointment */
  $('#btn_submit_appointment').on('click', function(){
    var $the_form = $('#form_post_appointment');
    var form_data = $the_form.serializeArray();
    var new_appointment = ConverToJSON(form_data);

    // ajax call to post new appointment
    $.ajax({
      url: "http://localhost:3000/users/reports/appointment/" + report_data.report_id + "/" + user_data.user_id,
      method: "POST",
      data: JSON.stringify(new_appointment),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        if(data.exists){
          alert("Este reporte ya tiene una cita.");
        } else {
          alert("Se ha agregado una nueva cita.");
          appointment_data = data.appointment;
          appointment_data.maker = user_data.username;

          var citas_content = '';

          citas_content += "<p><strong>Creador de cita: </strong><span id='appointment_maker'>"+appointment_data.maker+"</span></p>";
          citas_content += "<p><strong>Fecha: </strong><span id='appointment_date'>"+appointment_data.date+"</span></p>";
          citas_content += "<p><strong>Hora: </strong><span id='appointment_time'>"+appointment_data.time+"</span></p>";
          citas_content += "<p><strong> Propósito: </strong><span id='appointment_purpose'>"+appointment_data.purpose+"</span></p>";
          //citas_content += "<a id='btn_edit_appointment' class='btn btn-sm btn-success' data-toggle='tooltip' type='button' href='#' data-id='"+appointment_data.appointment_id+"'><i class='glyphicon glyphicon-edit'></i></a>";

          // inject html string
          $('#info_panel').html(citas_content);

          // hide post form; display info panel
          $('#edit_panel').hide();
          $('#info_panel').show();
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

  /* Edit appointment */
  $('#info_panel').on('click', 'a#btn_edit_appointment', function(e){
    // prevents link from firing
    e.preventDefault();

    $('#info_panel, #btn_submit_appointment').hide();
    $('#edit_panel').show();

    // date formatting
    var cita_date = appointment_data.date;
    var cita_day = cita_date.slice(0,2);
    var cita_month = cita_date.slice(3,5);
    var cita_year = cita_date.slice(6,10)

    cita_date = cita_year+"-"+cita_month+"-"+cita_day;
    
    // time formatting
    var cita_time = appointment_data.time;
    var cita_hour = cita_time.slice(0,2);
    var cita_minutes = cita_time.slice(3,5)
    var cita_meridian = cita_time.slice(6,8);
    // if time is pm, convert to military time
    if(cita_meridian == 'PM'){
      cita_hour = parseInt(cita_hour) + 12;
    } 
    cita_time = cita_hour+':'+cita_minutes;

    // set edit forms with existing values
    $('#cita_date').val(cita_date);
    $('#cita_time').val(cita_time);
    $('#cita_purpose').val(appointment_data.purpose);
  });

  /* PUTs edited cita information */
/*  $('#btn_put_appointment').on('click', function(){
    var appointment_id = $(this).attr('data-id');
    // get form data and conver to json format
    var $the_form = $('#form_post_appointment');
    var form_data = $the_form.serializeArray();
    var edited_appointment = ConverToJSON(form_data);

    // ajax call to update ganadero
    $.ajax({
      url: "http://localhost:3000/users/admin/citas/" + appointment_id,
      method: "PUT",
      data: JSON.stringify(edited_appointment),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        alert("Informacion de cita ha sido editada en el sistema.");
        // update cita info
        appointment_data = data.appointment;

        // hide post form; display info panel
        $('#edit_panel').hide();
        $('#info_panel').show();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });*/

  $('#btn_submit_title').on('click', function(){
    var $the_form = $('#form_title');
    var form_data = $the_form.serializeArray();
    var new_title = ConverToJSON(form_data);
    var title = new_title.report_title;

    // ajax call to update location
    $.ajax({
      url: "http://localhost:3000/users/reports/new_title/" + report_data.report_id,
      method: "PUT",
      data: JSON.stringify(new_title),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
      $('#title_panel').hide();
      $('#title').html(title);
      $('#title').show();
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });

});

