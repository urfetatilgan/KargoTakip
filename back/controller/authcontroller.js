const axios = require("axios");
const fs = require('fs').promises;
const { generateConfig } = require("../utils");
const nodemailer = require("nodemailer");
const CONSTANTS = require("../constant");
const Cargo = require('../models/Cargo');
const {OAuth2Client} = require('google-auth-library');
const path = require('path');
const process = require('process');
const {authenticate} = require('@google-cloud/local-auth');
const {google} = require('googleapis');
require("dotenv").config();
const SCOPES = ['https://www.googleapis.com/auth/gmail.readonly'];
const TOKEN_PATH = path.join(process.cwd(), 'token.json')
const oAuthclient = new OAuth2Client(
  process.env.CLIENT_ID,
  process.env.CLIENT_SECRET,
  process.env.REDIRECT_URI
  );

let auth =null;
// oAuth2Client.setCredentials({ refresh_token: process.env.REFRESH_TOKEN });

async function loadSavedCredentialsIfExist() {
    try {
      const content = await fs.readFile(TOKEN_PATH);
      const credentials = JSON.parse(content);
      return credentials;
    } catch (err) {
      return null;
    }
  }

async function getId(req,res){
  try {
    const{idToken,authCode}= req.body
    async function verify() {
      const ticket = await oAuthclient.verifyIdToken({
          idToken: idToken,
          audience: process.env.CLIENT_ID,  // Specify the CLIENT_ID of the app that accesses the backend
          // Or, if multiple clients access the backend:
          //[CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]
    });
    if(ticket!=null){
        // let newClient= await loadSavedCredentialsIfExist()
        // if(newClient){
        //     return newClient
        // }
        const payload = ticket.getPayload();
        // client = await authenticate({
        //     scopes: SCOPES,
        //     keyfilePath: CREDENTIALS_PATH,
        // });
        const userid = payload['sub'];

        const credentials = await loadSavedCredentialsIfExist()
        const { tokens } = await oAuthclient.getToken(authCode);

        if(tokens.refresh_token!=null){
          let savedTokens= JSON.stringify({
            refresh_token: tokens.refresh_token,
            access_token:tokens.access_token,
            user_email:payload.email
          })
          await fs.writeFile(TOKEN_PATH, savedTokens);
        }
        else{
          let savedTokens= JSON.stringify({
            refresh_token:credentials.refresh_token,
            access_token:tokens.access_token,
            user_email:payload.email
          })
          await fs.writeFile(TOKEN_PATH, savedTokens);
        }
        
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

async function getUser(req, res) {
    try {
      const content = await fs.readFile(TOKEN_PATH);
      const credentials = JSON.parse(content);
      const url = "https://gmail.googleapis.com/gmail/v1/users/"+credentials.user_email+"/messages";
      const  token  = credentials.access_token;
      const config = generateConfig(url, token);
      const response = await axios(config);
      res.json(response.data);
    } catch (error) {
      console.log(error);
      res.send(error);
    }
  }

async function insertCargo(req, res) {
    try {
      const cargo = await Cargo.create(
        req.body
      );
      res.status(200).send()
    } catch (error) {
      console.log(error);
      res.send(error);
    }
  }

  async function updateCargo(req, res) {
    try {
      const cargo = await Cargo.update(
        req.body
      );
      res.status(200).send()
    } catch (error) {
      console.log(error);
      res.send(error);
    }
  }  

async function getCargos(req,res){
  try {

    const cargos = await Cargo.findAll();
    if(cargos===null){
      console.log("yapamadın")
    }
    res.json(cargos).status(200).send();
  } catch (error) {
    console.log(error);
    res.status(400).send(error);
  }


}     
async function getMails(req, res) {
  try {
    const content = await fs.readFile(TOKEN_PATH);
    const credentials = JSON.parse(content);
    const url = "https://gmail.googleapis.com/gmail/v1/users/"+credentials.user_email+"/messages?q=Trendyol";
    const  token  = credentials.access_token;
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
  getId,
  getMails,
  getCargos,
  insertCargo
};