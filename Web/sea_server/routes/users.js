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

module.exports = router;
