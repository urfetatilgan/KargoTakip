const axios = require("axios");
const { generateConfig } = require("../utils");
const nodemailer = require("nodemailer");
const CONSTANTS = require("../constant");
const { google } = require("googleapis");

const {OAuth2Client} = require('google-auth-library');
require("dotenv").config();

const oAuthclient = new OAuth2Client(
  process.env.CLIENT_ID,
  process.env.CLIENT_SECRET,
  process.env.REDIRECT_URI
  );

// oAuth2Client.setCredentials({ refresh_token: process.env.REFRESH_TOKEN });

async function sendMail(req, res) {
  try {
  } catch (error) {
    console.log(error);
    res.send(error);
  }
}

async function getId(req,res){
  try {
    const{idToken,authCode}= req.body
    const { tokens } = await oAuthclient.getToken(authCode);
    let rawdata = await fs.readFile('token.json');
    let Token = JSON.parse(rawdata);
    if(tokens.refresh_token!=null){
      process.env.REFRESH_TOKEN=tokens.refresh_token
    }
    tokens.access_token
    async function verify() {
      const ticket = await oAuthclient.verifyIdToken({
          idToken: idToken,
          audience: process.env.CLIENT_ID,  // Specify the CLIENT_ID of the app that accesses the backend
          // Or, if multiple clients access the backend:
          //[CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]
    });
    if(ticket!=null){
      const payload = ticket.getPayload();
      CONSTANTS.auth.user=payload.email
      const userid = payload['sub'];
      res.status(200).send()
    }
      // If request specified a G Suite domain:
      // const domain = payload['hd'];
    }
  verify().catch(console.error);
  } catch (error) {
    console.log(error);
  }
}

// async function getUser(req, res) {
//     try {
//       const url = `https://people.googleapis.com/v1/people/me?personFields=names`;
//       const  token  = process.env.ACCESS_TOKEN
//       console.log(token.scopes);
//       const config = generateConfig(url, token);
//       const response = await axios(config);
//       res.json(response.data);
//     } catch (error) {
//       console.log(error);
//       res.send(error);
//     }
//   }

async function getUser(req, res) {
  try {
    const url = `https://gmail.googleapis.com/gmail/v1/users/${req.params.email}/profile`;
    const content = await fs.readFile(TOKEN_PATH);
    const credentials = JSON.parse(content);
    const { token } = credentials.access_token;
    const config = generateConfig(url, token);
    const response = await axios(config);
    res.json(response.data);
  } catch (error) {
    console.log(error);
    res.send(error);
  }
}  


module.exports = {
  getUser,
  sendMail,
  getId
};