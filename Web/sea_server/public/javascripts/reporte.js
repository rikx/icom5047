$(document).ready(function(){
	// answered questions list

	var titleText = $('<input>').attr('type', 'text');
	$answered_survey_panel = $('#answered_survey');
	$('#btn_submit_notes').hide();
	$('#btn_submit_appointment').hide();
	$('#form_post_appointment').hide();
  $('#title_panel').hide();


  //double click title to display title edit form
  $('#title').dblclick(function(e) {
    $('#title_panel').show();
    $('#title').hide();
  }); 


  // user_data is of the currently signed in user
  var user_data = JSON.parse($('#info_current_user').attr('data-user'));
  var report_data = JSON.parse($('#info_report').attr('data-report'));
  var survey_data = JSON.parse($answered_survey_panel.attr('data-survey'));

  // checks if there is an appointment
  var appointment_data = $('#citas').attr('data-appointment');
  if(appointment_data != undefined) {
    appointment_data = JSON.parse($('#citas').attr('data-appointment'));
  }


  /* Button: Return home */
  $('#btn_home').on('click', function(){
  	window.location.href = '/users';
  });

  /* Button: Return to Reportes */
  $('#btn_reportes').on('click', function(){
    window.location.href = '/users/reportes'; //TODO CHANGE manejar reportes so this works
  });


  $('#btn_notes').on('click', function(){
  	var currentText = $('#notas').text();
  	$('#btn_notes').hide();
  	$('#btn_submit_notes').show();
  	$('#notas').html("<textarea id='note_text' rows='10' cols='40'>" + currentText + "</textarea>");
  });

  $('#btn_submit_notes').on('click', function(){
  	$('#btn_notes').show();
  	$('#btn_submit_notes').hide();

    var notes = {

     report_id: report_data.report_id,
     note_text: $('#note_text').val()
   };

    // ajax call to update location
    $.ajax({
    	url: "http://localhost:3000/users/admin/note_edit",
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

  $('#btn_appointment').on('click', function(){
    $('#no_appointment_text').hide();
    $('#form_post_appointment').show();
    $('#btn_appointment').hide();
    $('#btn_submit_appointment').show();
    
    // if there is apointment data, display it in the edit fields
    if(appointment_data != undefined){
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
    }
  });

  $('#btn_submit_appointment').on('click', function(){
    var $the_form = $('#form_post_appointment');
    var form_data = $the_form.serializeArray();
    var new_appointment = ConverToJSON(form_data);

    // ajax call to update location
    $.ajax({
      url: "http://localhost:3000/users/appointment/" + report_data.report_id + "/" + user_data.user_id,
      method: "POST",
      data: JSON.stringify(new_appointment),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
        alert("Se ha agregado una nueva cita.");
        var this_appointment = data.appointment;

        var citas_content = '';

        citas_content += "<p><strong>Creador de cita: </strong><span id='appointment_maker'>"+user_data.username+"</span></p>";
        citas_content += "<p><strong>Fecha: </strong><span id='appointment_date'>"+this_appointment.date+"</span></p>";
        citas_content += "<p><strong>Hora: </strong><span id='appointment_time'>"+this_appointment.time+"</span></p>";
        citas_content += "<p><strong> Propósito: </strong><span id='appointment_purpose'>"+this_appointment.purpose+"</span></p>";

        // hide post forms
        $('#btn_submit_appointment').hide();
        $('#form_post_appointment').hide();
        $('#btn_appointment').show();

        // inject html string
        $('#citas').html(citas_content);

        // for appointment update
/*        // set cita information in panel
        $('#appointment_date').val(this_appointment.date);
        $('#appointment_time').val(this_appointment.time);
        $('#appointment_purpose').val(this_appointment.purpose);
        $('#appointment_maker').val(user_data.username);*/
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
  });

  $('#btn_submit_title').on('click', function(){
    var $the_form = $('#form_title');
    var form_data = $the_form.serializeArray();
    var new_title = ConverToJSON(form_data);
    var title = new_title.report_title;

    // ajax call to update location
    $.ajax({
      url: "http://localhost:3000/users/admin/new_title/" + report_data.report_id,
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

