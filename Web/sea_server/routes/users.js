var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});


/* GET Admin Home redirect */
router.get('/get_admin', function(req, res, next) {
  res.send({redirect: '/users/admin'});
});

//TODO: add :id or :username after /admin
//TODO: fix it so you cant access it directly (with sesions)
/* GET Admin Home. */
router.get('/admin', function(req, res, next) {
  res.render('admin', { title: 'Admin Home'});
});

//TODO: add :id or :username between /admin/ganaderos
//TODO: fix it so you cant access it directly (with sessions)
/* GET Admin Manejar Ganaderos redirect */
router.get('/admin/get_ganaderos', function(req, res, next) {
	 res.send({redirect: '/users/admin/ganaderos'});
});

/* GET Admin Manejar Ganaderos */
router.get('/admin/ganaderos', function(req, res, next) {
	res.render('manejar_ganaderos', { title: 'Manejar Ganaderos Home'});
});

module.exports = router;
