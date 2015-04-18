$(document).ready(function(){
	// store current element's information and its children
	var element_family;
	var answered_questions = []; 
	// answered questions list
  $answered_list = $('#answered_questions');
  //
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
		// validate location input

		// if not valid 
		if(false){
			alert('La localización escrita no existe. Por favor seleccione una localización valida');
		} else {
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
		}
	});

	/* Saves answer for current question, gets next questionand updates the interface */
	$('#btn_next_question').on('click', function(){
		var the_question = $('#next_question_question');
		var the_answer = $("input[name='answer_radios']:checked");

		var question_answer_pair = {
			flowchart_id: $('#flowchart').attr('data-flowchart-id'),
			item_id: the_question.attr('data-question-id'),
			question: the_question.text(),
			option_id: the_answer.attr('data-answer-id'),
			answer: the_answer.val()
		};
		// push new question-answer pair to array
		answered_questions.push(question_answer_pair);
		// 
		update_answered_questions();
		var next_question = the_answer.attr('data-next-id');
		update_next_question(next_question);
	});

	/* Save Survey progress */
	$('#btn_save_progress').on('click', function(){
		// ajax post

		// redirect to user home
		window.location.href = '/users';
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
			var next_content_answers = '';
			$.each(data.question_family, function(i){
				// push to global array 

				if(this.item_type != "RECOM") {
					if(i==0) {
						$question_panel_question.html(this.question);
						$question_panel_question.attr('data-question-id', this.item_id);
					}
					next_content_answers += "<div class='radio'><label><input id='answer"+i+"' name='answer_radios' type='radio' value='"+this.answer+"' data-answer-id='"+this.option_id+"' data-next-id='"+this.next_id+"'></input>";
					next_content_answers += this.answer+"</label></div>";
				} else {
					$('#btn_save_progress, #btn_next_question').hide();
					$('#btn_end_survey').show();
					$('#panel_title_next').html('Recommendaciones:');

					$question_panel_question.hide();
					next_content_answers += "<p class='lead'>"+this.question+"</p>";
					$question_panel_answers.html(next_content_answers);
					return;
				}
			});
			$question_panel_answers.html(next_content_answers);
		});
	}
});