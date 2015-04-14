$(document).ready(function(){
	// store current element's information and its children
	var element_family;

	var answered_questions = []; 
	// answered questions list
  $answered_list = $('#answered_questions');
  //
  $question_panel_question = $('#next_question');
  $question_panel_answers = $('#next_question_answers');

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
		var next_question = the_answer.attr('data-next-id');
		update_next_question(next_question);
	});

	/* */
	$('#btn_save_progress').on('click', function(){

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
	function update_next_question(next_question) {
		$.getJSON('http://localhost:3000/element/'+next_question, function(data) {
			var next_content_answers = '';
			$.each(data.question_family, function(i){
				if(i!=0) {
					$question_panel_question.html(this.question);
					$question_panel_question.attr('data-question-id', this.item_id);
				} else {
					next_content_answers += "<div class='radio'><label><input id='answer"+i+"' name='answer_radios' value='"+this.answer+"' data-answer-id='"+this.option_id+"' data-next-id='"+this.next_id+"'></input>";
					next_content_answers += this.answer+"</label></div>";
				}
			});
			$question_panel_answers.html(next_content_answers);
		});
	}
});