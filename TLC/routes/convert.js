var express = require('express');
var router = express.Router();

module.exports = 
// middleware that is specific to this router
	router.use(function (req, res, next) {
		console.log('[ Date ] : ', Date.now());
		next();
	});
	// define the home page route
	router.get('/', function(req, res) {
		res.send('TLC index');
		res.render('/convert/index.html');
	});
	// define the about route
	router.get('../admin', function(req, res) {
		res.send('ADMIN index');
		res.render('/admin/index.html');
	});



/*
function(app)
{
     app.get('/',function(req,res){
        res.render('indexNew.jsp')
     });     
		
     app.get('/admin',function(req,res){
        res.render('indexNew.jsp');
     });
	 app.get('/board',function(req,res){
        res.render('about.jsp');
     });
	 app.get('/contact',function(req,res){
        res.render('about.jsp');
     });
	 app.get('/convert',function(req,res){
        res.render('about.jsp');
     });
	 app.get('/intro',function(req,res){
        res.render('about.jsp');
     });
	 app.get('/login',function(req,res){
        res.render('about.jsp');
     });
	 app.get('/logout',function(req,res){
       res.render('about.jsp');
     });
	 app.get('/member',function(req,res){
       res.render('about.jsp');
     });
	 app.get('/register',function(req,res){
       res.render('about.jsp');
     });
	 */
