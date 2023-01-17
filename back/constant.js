require("dotenv").config();

const auth = {
  type: "OAuth2",
  user: "enesurfet@gmail.com",
  clientId: process.env.CLIENT_ID,
  clientSecret: process.env.CLIENT_SECRET,
  refreshToken: process.env.REFRESH_TOKEN,
};

const mailoptions = {
  from: "Siddhant &lt;enesurfet@gmail.com>",
  to: "enesurfet@gmail.com",
  subject: "Gmail API NodeJS",
};

module.exports = {
  auth,
  mailoptions,
};