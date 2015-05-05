$(document).ready(function(){
	// store question-answer pairs of answered questions
	var answered_questions = []; 
	// path sequence number
	var sequence_number = 0;
	// answered questions list
  $answered_list = $('#answered_questions');
  
  $question_panel_question = $('#next_question_question');
  $question_panel_answers = $('#next_question_answers');

  var locations_array = JSON.parse($('#scrollable-dropdown-menu').attr('data-locations'));

	/* Return home */
  $('#btn_home').on('click', function(){
    window.location.href = '/users';
  });

  // set up survey start date and time in basic information panel
  var survey_date = new Date();
	var start_date = get_date_time(survey_date, true);
	$('#survey_date').text(start_date.date + " @ " + start_date.time);

	// convert survey_date to 'yyyy-mm-dd format'
	start_date = survey_date.getFullYear() +'-';
	var month = survey_date.getMonth();
	month++; // account for JavaScript months being from 0-11; 
	if(month < 10){
		start_date += '0';
	}
	start_date += month +'-';
	start_date += survey_date.getDate();
	// set formatted date in hidden input field for report creation form
	$('#take_survey_date').attr('value', start_date);

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
	        matches.push({ 
	        	location_id: str.location_id,
	        	value: str.location_name 
	        });
	      }
	    });
	 
	    cb(matches);
	  };
	};
	// location name select event listener
	$('#take_survey_location_name').bind('typeahead:selected', function(obj, datum, name) {
    // add location_id value to input form
    $('#take_survey_location_id').attr('value', datum.location_id);
	});
	/* TEST END: location search */
	
	// verifies input value is in array and returns boolean result
	function valid_input(user_input, array) {
		for(var i=0; i < array.length; i++){
			if(user_input == array[i].location_name){
				return true;
			}
		}
		return false;
	}

	/* Saves pre-survey information and displays first questions */
	$('#take_survey_start').on('click', function(){
		var location_input = $('#take_survey_location_name').val();
		// validate location input 
		if(valid_input(location_input, locations_array)){
			if($('#take_survey_report_name').val() == ''){
				alert('Por favor ingrese un nombre para el reporte que se va a crear.');
				return;
			}
			// disable location input 
			$('#take_survey_location_name').attr('disabled', true);

			// get form data and conver to json format
	    var $the_form = $('#form_survey_flow');
	    var form_data = $the_form.serializeArray();
	    var new_cuestionario = ConverToJSON(form_data);

	    new_cuestionario.report_name = $('#take_survey_report_name').val();
	    // ajax call to post new report
	    $.ajax({
	      url: "http://localhost:3000/report",
	      method: "POST",
	      data: JSON.stringify(new_cuestionario),
	      contentType: "application/json",
	      dataType: "json",

	      success: function(data) {
	      	alert("Cuestionario ha sido comenzado.");
	      	// initialize path sequence number value
	      	sequence_number = 0;
	      	// save new report id
	      	$question_panel_answers.attr('data-report-id', data.report_id);
			    // populate question panel
					update_next_question($question_panel_question.attr('data-question-id'));
					//
					$('#row_answered, #row_current, #row_buttons').show();
					$('#take_survey_start, #row_home').attr('disabled', true).hide();
	      },
	      error: function( xhr, status, errorThrown ) {
	        alert( "Sorry, there was a problem!" );
	        console.log( "Error: " + errorThrown );
	        console.log( "Status: " + status );
	        console.dir( xhr );
	      }
	    });
		} else {
			alert('La localización escrita no existe. Por favor seleccione una localización valida');
		}
	});

	/* Saves answer for current question, gets next question and updates the interface */
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
			case 'CONDITIONAL': {
				// TODO finish how conditionals work
				the_answer = $('#answer_conditional_text');
				regex_conditional(the_answer.val(), the_answer);
				break;
			}
		}

		// check if user selected or wrote an answer
		var answer_value = the_answer.val();
		if(answer_value === null || answer_value === undefined || answer_value === '') {
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
			var new_path = {
				report_id: $question_panel_answers.attr('data-report-id'),
				option_id: the_answer.attr('data-answer-id'),
				user_input: answer_value,
				sequence: sequence_number
			};
	    $.ajax({
	      url: "http://localhost:3000/cuestionario/path",
	      method: "POST",
	      data: JSON.stringify(new_path),
	      contentType: "application/json",
	      dataType: "json",

	      success: function(data) {
	      	// increment path sequence number
	      	sequence_number++;
					// push new question-answer pair to array
					answered_questions.push(question_answer_pair);
					// update answered questions list
					update_answered_questions();
					// get next question info
					update_next_question(the_answer.attr('data-next-id'));
	      },
	      error: function( xhr, status, errorThrown ) {
	        alert( "Sorry, there was a problem!" );
	        console.log( "Error: " + errorThrown );
	        console.log( "Status: " + status );
	        console.dir( xhr );
	      }
	    });
		}
	});

	/* Sets option_id and next_id data attributes to the 
	 * answer field, based on the outcome of the conditional
	 */
	function regex_conditional(user_input, input_field){
		var possible_answers = JSON.parse($question_panel_answers.attr('data-answers'));
		var reg_ex, comp_value;

		$.each(possible_answers, function(){
			// get number to compare with
			//comp_value = this.answer.match(/\d+(\.\d+)?/)[0];

			// less than
			reg_ex = /lt\-?\d+(\.\d+)?/;
			if(reg_ex.test(this.answer)){
				comp_value = this.answer.match(/\-?\d+(\.\d+)?/)[0];
				if(user_input < comp_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
					return;
				}
			}
			// greater than
			reg_ex = /gt\-?\d+(\.\d+)?/;
			if(reg_ex.test(this.answer)){
				comp_value = this.answer.match(/\-?\d+(\.\d+)?/)[0];
				if(user_input > comp_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
					return;
				}
			}
			// equal
			reg_ex = /eq\-?\d+(\.\d+)?/;
			if(reg_ex.test(this.answer)){
				comp_value = this.answer.match(/\-?\d+(\.\d+)?/)[0];
				if(user_input == comp_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
					return;
				}
			}
			// not equal
			reg_ex = /ne\-?\d+(\.\d+)?/;
			if(reg_ex.test(this.answer)){
				comp_value = this.answer.match(/\-?\d+(\.\d+)?/)[0];
				if(user_input != comp_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
					return;
				}
			}
			// less or equal than
			reg_ex = /le\-?\d+(\.\d+)?/;
			if(reg_ex.test(this.answer)){
				comp_value = this.answer.match(/\-?\d+(\.\d+)?/)[0];
				if(user_input <= comp_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
					return;
				}
			}
			// greater or equal to
			reg_ex = /ge\-?\d+(\.\d+)?/;
			if(reg_ex.test(this.answer)){
				comp_value = this.answer.match(/\-?\d+(\.\d+)?/)[0];
				if(user_input >= comp_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
					return;
				}
			}
			// within range
			reg_ex = /ra(\[|\()\-?\d+(\.\d+)?,\-?\d+(\.\d+)?(\]|\))/;
			if(reg_ex.test(this.answer)){
					var commma_index = this.answer.indexOf(',');
					var left_value = this.answer.substring(3,commma_index);
					var right_value = this.answer.substring(commma_index+1,this.answer.length-1);

				if(user_input >= left_value && user_input <= right_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
				} else if(user_input > left_value && user_input <= right_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
				} else if(user_input >= left_value && user_input < right_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
				} else if(user_input > left_value && user_input < right_value){
					input_field.attr('data-answer-id', this.option_id);
					input_field.attr('data-next-id', this.next_id);
				} 
			} else {
				input_field.attr('data-answer-id', this.option_id);
				input_field.attr('data-next-id', this.next_id);
			}
		});
	}

	/* Save (Abort) Survey progress */
	$('#btn_save_progress').on('click', function(){
		var save = confirm('Guardar progreso?');
		if(save){
			// ajax post to abort survey
				//ajax success
				// redirect to user home
				window.location.href = '/users';
		}
	});

	/* Save Survey and redirect to report page */
	$('#btn_end_survey').on('click', function(){
		// ajax post to end survey
		var end_path = {
			report_id: $question_panel_answers.attr('data-report-id'),
			option_id: $question_panel_answers.attr('data-answer-id'),
			user_input: 'RECOM',
			sequence: sequence_number
		};
    $.ajax({
      url: "http://localhost:3000/cuestionario/path",
      method: "POST",
      data: JSON.stringify(end_path),
      contentType: "application/json",
      dataType: "json",

      success: function(data) {
      	alert("Cuestionario terminado.");
				// redirect to report page using returned id
				var report_id = data.report_id;
				window.location.href = '/users/reportes/'+report_id;
      },
      error: function( xhr, status, errorThrown ) {
        alert( "Sorry, there was a problem!" );
        console.log( "Error: " + errorThrown );
        console.log( "Status: " + status );
        console.dir( xhr );
      }
    });
	});

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
	function update_next_question(next_question_id) {
		$('#panel_title_first').hide();
		$('#panel_title_next').show();

		$.getJSON('http://localhost:3000/element/'+next_question_id, function(data) {
			var this_item = data.question_family[0];
			// populate question heading with question
			$question_panel_question.html(this_item.question);
			$question_panel_question.attr('data-question-id', this_item.item_id);
			$question_panel_question.attr('data-question-type', this_item.item_type);
			//
			var next_content_answers = '';
			switch(this_item.item_type){
				case 'BOOLEAN':
				case 'MULTI':
					$.each(data.question_family, function(i){
						next_content_answers += "<div class='radio'><label><input id='answer"+i+"' name='answer_radios' type='radio' value='"+this.answer+"' data-answer-id='"+this.option_id+"' data-next-id='"+this.next_id+"'></input>";
						next_content_answers += this.answer+"</label></div>";
					});
					break;
				case 'OPEN':
					next_content_answers += "<textarea id='answer_open_text' name='answer_open_text' data-answer-id='"+this_item.option_id+"' data-next-id='"+this_item.next_id+"'></textarea>";
					break;
				case 'CONDITIONAL': {
					// TODO finish how conditionals work
					next_content_answers += "<input id='answer_conditional_text' name='answer_conditional_text' type='number' min='1.0' pattern='\d+(\.\d+)?' data-answer-id='' data-next-id=''></input>";
					break;
				}
				case 'RECOM': {
					$('#btn_save_progress, #btn_next_question').hide();
					$('#btn_end_survey').show();
					$('#panel_title_next').html('Recommendaciones:');

					$question_panel_question.hide();
					// probably modify query so, in case item_type is recom, instead of question call it recommendation
					next_content_answers += "<p class='lead' id='answer_recommendation_text' data-answer-id='"+this_item.option_id+"' data-next-id='"+this_item.next_id+"'>"+this_item.question+"</p>";
					// assign option id to data attribute
					$question_panel_answers.attr('data-answer-id', this_item.option_id);
					break;
				}
			}
			// put possible answers in data attribute
			$question_panel_answers.attr('data-answers', JSON.stringify(data.question_family));
			// inject html with answer input(s)
			$question_panel_answers.html(next_content_answers);
		});
	}
});