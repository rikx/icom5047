$(document).ready(function(){
	$('#btn_crear_cuestionario').click(function(){
		window.location.href = '/users/admin/cuestionarios/crear';
	});

	$('#btn_manejar_cuestionarios').click(function(){
		window.location.href = '/users/admin/cuestionarios';
	});

	$('#btn_tomar_cuestionario').click(function(){
		window.location.href = '/users/cuestionarios';
	});

	$('#btn_manejar_ganaderos').click(function(){
		window.location.href = '/users/ganaderos';
	});

	$('#btn_manejar_usuarios').click(function(){
		window.location.href = '/users/admin/usuarios';
	});

	$('#btn_manejar_localizaciones').click(function(){
		window.location.href = '/users/localizaciones';
	});

	$('#btn_manejar_dispositivos').click(function(){
		window.location.href = '/users/admin/dispositivos';
	});

	$('#btn_manejar_reportes').click(function(){
		window.location.href = '/users/reportes';
	});

	$('#btn_manejar_citas').click(function(){
		window.location.href = '/users/citas';
	});

	$('#btn_manejar_categorias').click(function(){
		window.location.href = '/users/admin/categorias';
	});


	//logout function
	$('#btn_logout').click(function(){
		logout_helper();
	});
});