$(document).ready(function(){

  /* Button: Return home */
	$('#btn_home').on('click', function(){
    window.location.href = '/users/admin';
	});

	/* Button: Return to Reportes */
	$('#btn_reportes').on('click', function(){
    window.location.href = '/users/admin/reportes';
	});
});