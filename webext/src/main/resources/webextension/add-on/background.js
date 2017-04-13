/*
On startup, connect to the "java_echo_test" app.
*/
var port = browser.runtime.connectNative("java_echo_test");

/*
 * Listen for messages from the app.
 */
port.onMessage.addListener((response) => {
  console.log("Received: " + response);
});

/*
 * On a click on the browser action, send the app a message.
 */
browser.browserAction.onClicked.addListener(() => {
  console.log("Sending HelloWorld!");
  port.postMessage("HelloWorld!");
});