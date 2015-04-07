$(document).ready(function(){
	// cuestionarios list
  $reportes_list = $('#reportes_list');
	// store data for initial 10 cuestionarios
  var reportes_array = $reportes_list.attr('data-reportes');

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});

});