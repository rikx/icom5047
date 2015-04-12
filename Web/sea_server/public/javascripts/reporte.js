$(document).ready(function(){

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users';
	});

	/* Button: Return to Reportes */
	$('#btn_reportes').on('click', function(){
    window.location.href = '/users/admin/reportes'; //TODO CHANGE depending on account type
	});
});