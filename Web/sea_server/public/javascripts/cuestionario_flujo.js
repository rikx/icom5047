$(document).ready(function(){
	// store question-answer pairs of answered questions
	var answered_questions = []; 
	// answered questions list
  $answered_list = $('#answered_questions');
  
  $question_panel_question = $('#next_question_question');
  $question_panel_answers = $('#next_question_answers');

  var locations_array = JSON.parse($('#scrollable-dropdown-menu').attr('data-locations'));

	/* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users';
  });

  // set up basic information panel
  survey_start();

  /* TEST: location search */
  $('.typeahead').typeahead({
  	hint: false,
  	highlight: true
  },
  {
  	name: 'locations',
	  displayKey: 'value',
	  source: substringMatcher(locations_array)
  });

  // this probably goes in helper
  function substringMatcher(strs) {
	  return function findMatches(q, cb) {
	    var matches, substrRegex;
	 
	    // an array that will be populated with substring matches
	    matches = [];
	 
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	 
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.location_name)) {
	        // the typeahead jQuery plugin expects suggestions to a
	        // JavaScript object, refer to typeahead docs for more info
	        matches.push({ value: str.location_name });
	      }
	    });
	 
	    cb(matches);
	  };
	};
	/* TEST END: location search */

	/* Saves pre-survey information and displays first questions */
	$('#take_survey_start').on('click', function(){
		var location_input = $('#take_survey_location').val();
		// validate location input 
		if(valid_input(location_input, locations_array)){
			// disable location input 
			$('#take_survey_location').attr('disabled', true);

			// get form data and conver to json format
	    var $the_form = $('#form_survey_flow');
	    var form_data = $the_form.serializeArray();
	    var new_cuestionario = ConverToJSON(form_data);

	    // ajax call to post new ganadero
	/*    $.ajax({
	      url: "http://localhost:3000/cuestionarios/start",
	      method: "POST",
	      data: JSON.stringify(new_cuestionario),
	      contentType: "application/json",
	      dataType: "json",

	      success: function(data) {
	      	alert("Cuestionario ha sido comenzado.");
	      },
	      error: function( xhr, status, errorThrown ) {
	        alert( "Sorry, there was a problem!" );
	        console.log( "Error: " + errorThrown );
	        console.log( "Status: " + status );
	        console.dir( xhr );
	      }
	    });*/

			$('#row_answered, #row_current, #row_buttons').show();
			$('#take_survey_start, #row_home').attr('disabled', true).hide();
		} else {
			alert('La localización escrita no existe. Por favor seleccione una localización valida');
		}
	});

	/* Saves answer for current question, gets next questionand updates the interface */
	$('#btn_next_question').on('click', function(){
		// get question and answer input information
		var the_question = $('#next_question_question');
		var the_answer;
		switch(the_question.attr('data-question-type')){
			case 'BOOLEAN':
			case 'MULTI':
				the_answer = $("input[name='answer_radios']:checked");
				break;
			case 'OPEN':
				the_answer = $('#answer_open_text');
				break;
			case 'CONDITIONAL':
				// TODO finish how conditionals work
				break;
			case 'RECOM': 
				the_answer = the_question.text();
				break;
		}

		// check if user selected or wrote an answer
		var answer_value = the_answer.val();
		if(answer_value == null || answer_value == '') {
			alert("Por favor seleccione una contestacion o escriba en el espacio proveido.");
		} else {
			// create new question-answer pair to post in db
			var question_answer_pair = {
				flowchart_id: $('#flowchart').attr('data-flowchart-id'),
				item_id: the_question.attr('data-question-id'),
				question: the_question.text(),
				option_id: the_answer.attr('data-answer-id'),
				answer: answer_value
			};

			// ajax post

			// push new question-answer pair to array
			answered_questions.push(question_answer_pair);
			// update answered questions list
			update_answered_questions();
			// get next question info
			update_next_question(the_answer.attr('data-next-id'));
		}
	});

	/* Save (Abort) Survey progress */
	$('#btn_save_progress').on('click', function(){
		// ajax post
			//ajax success
			var save = prompt('Guardar progreso?');
			if(save){
				// redirect to user home
				window.location.href = '/users';
		}
	});

	/* Save Survey and redirect to report page */
	$('#btn_end_survey').on('click', function(){
		// ajax post

			// if ajax successful
			// redirect to report page using returned id
			//var report_id = data.report_id;
			//window.location.href = '/users/reports/'+report_id;
	});

	/* */
	function survey_start() {
		var start_date = get_date_time(new Date(), true)
		// date and time when survey started 
		$('#take_survey_date').text(start_date.date + " at " + start_date.time);
		// current logged in user
  	$('#take_survey_user').html();
  }

	/* update answered questions */
	function update_answered_questions() {
		var answered_content = '';
		$.each(answered_questions, function(){
			answered_content += "<li><strong>"+this.question+"</strong><br>";
			answered_content += "<em>"+this.answer+"</em></li>";
		});
		$answered_list.html(answered_content);
	}

	/* */
	function update_next_question(next_question) {
		$('#panel_title_first').hide();
		$('#panel_title_next').show();

		$.getJSON('http://localhost:3000/element/'+next_question, function(data) {
			var this_question = data.question_family[0];
			// populate question heading with question
			$question_panel_question.html(this_question.question);
			$question_panel_question.attr('data-question-id', this_question.item_id);
			$question_panel_question.attr('data-question-type', this_question.item_type);
			//
			var next_content_answers = '';
			switch(this_question.item_type){
				case 'BOOLEAN':
				case 'MULTI':
					$.each(data.question_family, function(i){
						next_content_answers += "<div class='radio'><label><input id='answer"+i+"' name='answer_radios' type='radio' value='"+this.answer+"' data-answer-id='"+this.option_id+"' data-next-id='"+this.next_id+"'></input>";
						next_content_answers += this.answer+"</label></div>";
					});
					break;
				case 'OPEN':
					next_content_answers += "<textarea id='answer_open_text' name='answer_open_text' data-answer-id='"+this_question.option_id+"' data-next-id='"+this_question.next_id+"'></textarea>";
					break;
				case 'CONDITIONAL':
					// TODO finish how conditionals work
					next_content_answers += "<input name='answer_conditional_text' type='text' data-answer-id='' data-next-id=''></input>";
					break;
				case 'RECOM': {
					$('#btn_save_progress, #btn_next_question').hide();
					$('#btn_end_survey').show();
					$('#panel_title_next').html('Recommendaciones:');

					$question_panel_question.hide();
					next_content_answers += "<p class='lead'>"+this_question.question+"</p>"; // probably modify query so, in case item_type is recom, instead of question call it recommendation
					$question_panel_answers.html(next_content_answers);
					return;
				}
			}
			// inject html with answer input(s)
			$question_panel_answers.html(next_content_answers);
		});
	}
});