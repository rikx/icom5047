$(document).ready(function(){

	$('#btn_tomar_cuestionario').click(function(){
		window.location.href = '/users/cuestionarios' // TODO change this route so its for taking surveys e.g. /users/cuestionarios/tomar
	});

	$('#btn_manejar_ganaderos').click(function(){
		window.location.href = '/users/ganaderos'
	});

	$('#btn_manejar_localizaciones').click(function(){
		window.location.href = '/users/localizaciones'
	});

	$('#btn_manejar_reportes').click(function(){
		window.location.href = '/users/reportes'
	});

	$('#btn_manejar_citas').click(function(){
		window.location.href = '/users/citas'
	});

	//logout function
	$('#btn_logout').click(function(){
		logout_helper();
	});
});