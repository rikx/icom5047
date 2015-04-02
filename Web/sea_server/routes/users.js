var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

//TODO: add :id or :username after /admin
//TODO: fix it so you cant access it directly (with sesions)
/* GET Admin Home. */
router.get('/admin', function(req, res, next) {
  res.render('admin', { title: 'Admin Home'});
});

/* GET Admin Crear Cuestionario */
router.get('/admin/crear-cuestionario', function(req, res, next) {
	res.render('crear-cuestionario', { title: 'Crear Cuestionario'});
});

/* GET Admin Manejar Cuestionarios */
router.get('/admin/cuestionarios', function(req, res, next) {
	res.render('manejar_cuestionarios', { title: 'Manejar Cuestionarios'});
});

/* GET Admin Manejar Ganaderos */
router.get('/admin/ganaderos', function(req, res, next) {
	res.render('manejar_ganaderos', { title: 'Manejar Ganaderos'});
});

/* GET Admin Manejar Reportes */
router.get('/admin/reportes', function(req, res, next) {
	res.render('manejar_reportes', { title: 'Manejar Reportes'});
});

/* GET Admin Manejar Usuarios */
router.get('/admin/usuarios', function(req, res, next) {
	res.render('manejar_usuarios', { title: 'Manejar Usuarios'});
});

/* GET Admin Manejar Localizaciones */
router.get('/admin/localizaciones', function(req, res, next) {
	res.render('manejar_localizaciones', { title: 'Manejar Localizaciones'});
});

/* GET Admin Manejar Citas */
router.get('/admin/citas', function(req, res, next) {
	res.render('manejar_citas', { title: 'Manejar Citas'});
});

/* GET Admin Manejar Dispositivos */
router.get('/admin/dispositivos', function(req, res, next) {
	res.render('manejar_dispositivos', { title: 'Manejar Dispositivos'});
});


module.exports = router;
