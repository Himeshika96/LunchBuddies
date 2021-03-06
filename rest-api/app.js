/* 
Who's Hungry android application
Authors - IT16067134 & IT16058910
CTSE pair project
Rest API
*/

const express = require('express');
const app = express();
var bodyParser = require('body-parser');

const Routes = require('./MainRoutes');

var Cors = require('cors');

app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());
app.use(Cors());
app.use('/',Routes);

app.listen(21731, '192.168.1.103', function (err) {
    if(err){
        console.log(err);
        process.exit(-1);
    }
    console.log('Server listening on port 21731');
})
