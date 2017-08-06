/*
On startup, connect to the "ping_pong" app.
*/
var port = browser.runtime.connectNative("java_ping_pong");

/*
Listen for messages from the app.
*/
port.onMessage.addListener((response) => {
  console.log("Received: " + response);
  alert("Received from Java: " + response);
});

port.onDisconnect.addListener(() => {
  console.log("Disconnected");
  alert("Java Disconnected")
});

/*
On a click on the browser action, send the app a message.
*/
browser.browserAction.onClicked.addListener(() => {
  console.log("Sending: ping");
  port.postMessage("ping");
});
