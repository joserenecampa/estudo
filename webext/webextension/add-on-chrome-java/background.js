/*
On startup, connect to the "ping_pong" app.
*/
var port = chrome.runtime.connectNative("java_ping_pong");

/*
Listen for messages from the app.
*/
port.onMessage.addListener(function(response) {
  console.log("Received: " + response);
  alert("Received from Java: " + response);
});

port.onDisconnect.addListener(function() {
  console.log("Disconnected");
  alert("Java Disconnected")
});

/*
On a click on the browser action, send the app a message.
*/
chrome.browserAction.onClicked.addListener(function() {
  console.log("Sending:  ping");
  port.postMessage("ping");
});