$(document).ready(function(){
	$('#btn_manejar_cuestionarios').click(function(){
		window.location.href = '/users/admin/cuestionarios'
	});

	$('#btn_manejar_ganaderos').click(function(){
		window.location.href = '/users/admin/ganaderos'
	});

	$('#btn_manejar_reportes').click(function(){
		window.location.href = '/users/admin/reportes'
	});

	$('#btn_manejar_cuentas').click(function(){
		window.location.href = '/users/admin/cuentas'
	});

	$('#btn_manejar_localizaciones').click(function(){
		window.location.href = '/users/admin/localizaciones'
	});

	$('#btn_manejar_citas').click(function(){
		window.location.href = '/users/admin/citas'
	});

	$('#btn_manejar_dispositivos').click(function(){
		window.location.href = '/users/admin/dispositivos'
	});
});