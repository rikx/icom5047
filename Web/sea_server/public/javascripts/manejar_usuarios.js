$(document).ready(function(){

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});
  
  /* Show info panel */
  $('#usuarios_list').on('click', 'tr td a.show_info_usuario', function(e){
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

	/* Open edit panel */
  $('#usuarios_list').on('click', 'tr td button.btn_edit_usuario', function(){
    $('#btn_edit, #heading_edit').show();
    $('#btn_submit, #heading_create').hide();
    $('#edit_panel').show();
    $('#info_panel').hide();

    // contains ganadero id
    $(this).attr('data-id');

    // ajax call for info

  });
});