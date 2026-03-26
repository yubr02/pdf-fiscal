const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  saveFile: (payload) => ipcRenderer.invoke('save-file', payload)
});
