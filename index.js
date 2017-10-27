'use strict';

process.env.DEBUG = 'actions-on-google:*';
const App = require('actions-on-google').DialogflowApp;
const functions = require('firebase-functions');


// a. the action name from the make_name Dialogflow intent
const NAME_ACTION = 'input.ordersandwich';
// b. the parameters that are parsed from the make_name intent
const TYPE_ARGUMENT = 'type';
const SAUCE_ARGUMENT = 'sauce';
const BREAD_ARGUMENT = 'bread';
const NUMSANDWICH_ARGUMENT = 'numSandwich';


exports.sandwichNoobBot = functions.https.onRequest((request, response) => {
  const app = new App({request, response});
  console.log('Request headers: ' + JSON.stringify(request.headers));
  console.log('Request body: ' + JSON.stringify(request.body));


// c. The function that generates the silly name
  function orderSandwich (app) {
    let type = app.getArgument(TYPE_ARGUMENT);
    let sauce = app.getArgument(SAUCE_ARGUMENT);
    let bread = app.getArgument(BREAD_ARGUMENT);
    let numSandwich = app.getArgument(NUMSANDWICH_ARGUMENT);
    app.tell('Alright, your order is ' +
      numSandwich + ' ' + type + ' sandwich with ' + sauce + ' sauce ' + ' and '
       + bread + ' bread! I hope you like it');
  }
  // d. build an action map, which maps intent names to functions
  let actionMap = new Map();
  actionMap.set(NAME_ACTION, orderSandwich);


app.handleRequest(actionMap);
});
