const axios = require("axios");
const { generateConfig } = require("../utils");
const nodemailer = require("nodemailer");
const CONSTANTS = require("../constant");
const { google } = require("googleapis");

require("dotenv").config();

const oAuth2Client = new google.auth.OAuth2(
  process.env.CLIENT_ID,
  process.env.CLIENT_SECRET,
  process.env.REDIRECT_URL
);

oAuth2Client.setCredentials({ refresh_token: process.env.REFRESH_TOKEN });

async function sendMail(req, res) {
  try {
  } catch (error) {
    console.log(error);
    res.send(error);
  }
}

async function getId(req,res){
  try {
    const{idToken}= req.body
    const {OAuth2Client} = require('google-auth-library');
    const client = new OAuth2Client(process.env.CLIENT_ID);
    console.log(req.body)
    async function verify() {
      const ticket = await client.verifyIdToken({
          idToken: idToken,
          audience: process.env.CLIENT_ID,  // Specify the CLIENT_ID of the app that accesses the backend
          // Or, if multiple clients access the backend:
          //[CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]
    });
    if(ticket!=null){
      const payload = ticket.getPayload();
      const userid = payload['sub'];
      res.status(200).send()
    }
      
      // If request specified a G Suite domain:
      console.log(userid);
      // const domain = payload['hd'];
    }
  verify().catch(console.error);
  } catch (error) {
    
  }
}

async function getUser(req, res) {
    try {
      const url = `https://gmail.googleapis.com/gmail/v1/users/${req.params.email}/profile`;
      const { token } = await oAuth2Client.getAccessToken();
      const config = generateConfig(url, token);
      const response = await axios(config);
      res.json(response.data);
    } catch (error) {
      console.log(error);
      res.send(error);
    }
  }

async function getDrafts(req, res) {
  try {
  } catch (error) {
    console.log(error);
    return error;
  }
}

async function readMail(req, res) {
  try {
  } catch (error) {
    res.send(error);
  }
}

module.exports = {
  getUser,
  sendMail,
  getDrafts,
  readMail,
  getId
};