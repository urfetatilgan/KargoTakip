'use strict';
const { Sequelize, DataTypes,Model } = require('sequelize');
const sequelize = new Sequelize(process.env.DB_NAME, process.env.DB_USERNAME,process.env.DB_PASSWORD, {
  host: process.env.DB_HOST,
  port:5432,
  dialect: 'postgres',
  sync:true
});

const Cargo = sequelize.define('Cargo',{
    // id: DataTypes.DataTypes.INTEGER,
    cargo_id:{
      type:DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey:true
    },

    cargo_name: DataTypes.STRING,
    cargo_no:DataTypes.STRING,
    cargo_date: {
      type: DataTypes.DATE,
      allowNull: false,
    },
    cargo_status:{
      type:DataTypes.STRING
    }
  },{
    sequelize,
    modelName: 'Cargo',
    tableName:'cargos',
    timestamps:false,
    
  })
 
  
    
  Cargo.sync()
  // (async () => {
  //   await sequelize.sync({ force: true });
  //   // Code here
  // })();
  console.log(Cargo === sequelize.models.Cargo);

module.exports =Cargo

