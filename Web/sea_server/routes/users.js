var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

//TODO: fix it so you cant access it directly
/* GET Admin Home. */
router.get('/admin', function(req, res, next) {
  res.render('admin', { title: 'Admin Home'});
});

//TODO: add :id between /admin/ganaderos
//TODO: fix it so you cant access it directly
router.get('/admin/get_ganaderos', function(req, res, next) {
	 res.send({redirect: '/users/admin/ganaderos'});
});

/* GET Manejar Ganaderos */
router.get('/admin/ganaderos', function(req, res, next) {
	res.render('manejar_ganaderos', { title: 'Manejar Ganaderos Home'});
});

module.exports = router;
