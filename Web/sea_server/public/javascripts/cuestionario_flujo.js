$(document).ready(function(){
	// store current element's information and its children
	var element_family;
	var locations_array;
	var answered_questions = []; 
	// answered questions list
  $answered_list = $('#answered_questions');
  //
  $question_panel_question = $('#next_question_question');
  $question_panel_answers = $('#next_question_answers');

  // set up basic information panel
  survey_start();

  /* TEST for location search */
  $('.typeahead').typeahead({
  	highlight: true
  },
  {
  	name: 'locations',
  	source: locations_array
  });

	/* */
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

	});

	/* Save Survey and redirect to report page */
	$('#btn_end_survey').on('click', function(){

	});

	/* */
	function survey_start() {
		var start_date = get_date_time(new Date(), true)
		// date and time when survey started 
		$('#take_survey_date').text(start_date.date + " at " + start_date.time)
		// current logged in user
  	$('#take_survey_user').html()

  	// get locations
  	$.getJSON('http://localhost:3000/locations', function(data) {
  		locations_array = data.locations;
  	})
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