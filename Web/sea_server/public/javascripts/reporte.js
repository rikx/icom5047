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

  console.log(user_data);
  console.log(report_data);
  console.log(survey_data);


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
    //var currentText = $('#notas').text();
    $('#no_appointment_text').hide();
    $('#form_post_appointment').show();
    $('#btn_appointment').hide();
    $('#btn_submit_appointment').show();
    //$('#notas').html("<textarea id='note_text' rows='10' cols='40'>" + currentText + "</textarea>");
  });

  $('#btn_submit_appointment').on('click', function(){
    var $the_form = $('#form_post_appointment');
    var form_data = $the_form.serializeArray();
    var new_appointment = ConverToJSON(form_data);

  // ajax call to update location
  $.ajax({
    url: "http://localhost:3000/users/admin/new_appointment/" + report_data.report_id + "/" + user_data.user_id,
    method: "POST",
    data: JSON.stringify(new_appointment),
    contentType: "application/json",
    dataType: "json",

    success: function(data) {
      alert("Se ha agregado una nueva cita.");
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
    console.log(new_title);
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

  function populate_survey(){
/*		if locals.survey
						ol
							each item in survey
								li
									strong
										= item.question
									br
									em
										= item.answer
					else
						center
					button(class='btn btn-sm btn-success') Continuar Entrevista*/
				}
			});

