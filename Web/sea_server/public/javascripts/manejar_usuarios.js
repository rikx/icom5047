$(document).ready(function(){

  /* Button: Return home */
	$('#btn_home').click(function(){
    window.location.href = '/users/admin'
	});
  
  /* Click: Show info panel */
  $('#usuarios_list tr td a').click(function(e){
    // prevents link from firing
    e.preventDefault();

    $('#edit_panel').hide();
    $('#info_panel').show();

    // remove active from previous and add active to current clicked ganadero
    $('#usuarios_list tr td a.active').removeClass('active');
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active');
    }

    // contains ganadero id
    $this.attr('data-id');

    // ajax call for info
  });

	/* Button: Open edit panel */
  $('.btn_edit_usuario').click(function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains ganadero id
    $(this).attr('data-id');

    // ajax call for info

  });
});