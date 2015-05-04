var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
//var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var pg = require('pg')
  , session = require('express-session')
  , pgSession = require('connect-pg-simple')(session);

var routes = require('./routes/index');
var users = require('./routes/users');
var synchronization = require('./routes/synchronization');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(favicon(__dirname + '/public/images/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
//app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Make our db accessible to our router
app.use(function(req,res,next){

  // TODO: change default pool size later
  //var conString = "postgres://postgres:RENeR2015DB@136.145.116.231:5432/SEA";
  var conString = {
    user: 'postgres',
    password: 'RENeR2015DB',
    database: 'SEA',
    host: 'localhost',
    port: 5432
  }
  req.db = pg;
  req.conString = conString;
 //req.conString = encodeURIComponent(conString); //to fix a problem with node-postgress not liking # characters
  next();
});

//TODO: finish session options
/*app.use(session({
  store: new (require('connect-pg-simple')(session))(),
  secret: 'Ramon2Enrique0Nelson1Ricardo5icom5047',
  resave: false,
  saveUninitialized: true,
  cookie: { maxAge: 30 * 24 * 60 * 60 * 1000 } // 30 days
}));*/
app.use(session({
  store: new pgSession({
    pg : pg,
    conString : 'postgres://postgres:RENeR2015DB@localhost:5432/SEA'
  }),
  secret: 'Ramon2Enrique0Nelson1Ricardo5icom5047',
  resave: false,
  saveUninitialized: true,
  cookie: { maxAge: 30 * 24 * 60 * 60 * 1000 } // 30 days
}));

app.use('/', routes);
app.use('/users', users);
app.use('/synchronization', synchronization);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});

module.exports = app;
