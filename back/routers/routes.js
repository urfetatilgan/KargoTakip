const express = require('express');
const controllers=require('../controller/MailController');
const authcontrollers=require('../controller/authcontroller');
const router = express.Router();

router.get('/mail/user',authcontrollers.getUser)
router.get('/mail/send',controllers.sendMail);
router.post('/mail/user/tokenId',authcontrollers.getId)
router.get('/mail/user/Trendyol',authcontrollers.getMailsTrendyol)
router.get('/cargos',authcontrollers.getCargos)
router.post('/cargo/add',authcontrollers.insertCargo)


module.exports = router;