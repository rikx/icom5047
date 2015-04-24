$(document).ready(function(){
	// answered questions list
  $answered_survey_panel = $('#answered_survey');

  var survey_data = JSON.parse($answered_survey_panel.attr('data-survey'));

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users';
	});

	/* Button: Return to Reportes */
	$('#btn_reportes').on('click', function(){
    window.location.href = '/users/reportes'; //TODO CHANGE manejar reportes so this works
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