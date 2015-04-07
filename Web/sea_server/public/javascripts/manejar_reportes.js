$(document).ready(function(){
	// reportes list
  $reportes_list = $('#reportes_list');
	
	// store data for initial 10 reports
	var reportes_data = $reportes_list.attr('data-reports');
	var reportes_array = JSON.parse(reportes_data);

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});

});