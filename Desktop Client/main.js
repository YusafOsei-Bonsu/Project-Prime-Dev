// Importing dependencies for Electron
const {app, BrowserWindow} = require('electron');
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
const electronEjs = require('electron-ejs');

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let window;

// Initialising the EJS parser
let ejs = new electronEjs({"key": "my value"}, {});

function createWindow () {
  // Create the browser window.
  window = new BrowserWindow({width: 800, height: 600})

  // and load the index.html of the app.
  window.loadFile('src/upload.html');
  
  //AJAX request server for existing files
  var xhr = new XMLHttpRequest();
  xhr.open('GET', "http://100.76.164.19:3003/files", true);
  xhr.send();
 
  xhr.onreadystatechange = processRequest;

  function processRequest(e) {
    if (xhr.readyState == 4 && xhr.status == 200) {
        var response = JSON.parse(xhr.responseText);
    }
  }

  // Open the DevTools.
  window.webContents.openDevTools()

  // Emitted when the window is closed.
  window.on('closed', () => {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    window = null
  })
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow)

// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (window === null) {
    createWindow()
  }
});

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
