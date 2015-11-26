var express = require('express');
var router = express.Router();
var carService = require('../services/car-service');


router.get('/', function(req, res, next) {
	res.render('search_cars');
});

router.post('/car_results', function(req, res, next) {
	var carMake = req.body.car_make;
	console.log(req.body.car_make);
	if (carMake === "bmw") {
		res.render('car_search/car_makes/bmw_index');
	} else if (carMake === "mercedes_benz") {
		res.render('car_search/car_makes/mb_index');
	} else if (carMake === "porsche") {
		res.render('car_search/car_makes/porsche_index');
	} else if (carMake === "maserati") {
		res.render('car_search/car_makes/maserati_index');
	} else {
		res.render('/index');
	}
});

module.exports = router;