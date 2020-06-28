//jshint esversion:6
require('dotenv').config();
const express = require("express");
const bodyParser = require("body-parser");
const ejs = require("ejs");
const mongoose = require("mongoose");
const session = require('express-session');
//const MongoStore = require('connect-mongo')(session);
const MemoryStore = require('memorystore')(session)
const passport = require('passport');
const passportLocalMongoose = require('passport-local-mongoose');
const GoogleStrategy = require('passport-google-oauth20').Strategy;
const findOrCreate = require('mongoose-findorcreate');
const path = require('path');
const multer = require('multer');
const GridFsStorage = require('multer-gridfs-storage');
const Grid = require('gridfs-stream');
const methodOverride = require('method-override');
const serveStatic = require('serve-static');


const app = express();

app.get("/", function(req, res)){
    res.render("home/index")
}

app.get("/admin", function(req, red)){
    res.render("admin/index")
}