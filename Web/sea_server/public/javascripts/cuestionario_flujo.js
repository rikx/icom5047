$(document).ready(function(){
	// store current element's information and its children
	var element_family;

	var answered_questions = []; 
	// answered questions list
  $answered_list = $('#answered_questions');
  //
  $question_panel = $('#next_question');

	/* */
	$('#btn_next_question').on('click', function(){
		var the_question = $('#question');
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

		var next_question_info = get_next_element(the_answer.attr('data-next-id'));
		update_next_question(next_question_info);
	});

	/* */
	$('#btn_save_progress').on('click', function(){

	});

	/* */
	function get_next_element(id) {
		$.getJSON('http://localhost:3000/element/'+id, function(data) {
    	return data.element_family;
    });
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
	function update_next_question(question_family) {
		var next_content = '';
		$.each(question_family, function(){
			next_content += "<p id='question'>"+this.question+"</p>";
			next_content += "<div class='radio'><label></label></div>";
		});
		$question_panel.html(answered_content);
	}
});