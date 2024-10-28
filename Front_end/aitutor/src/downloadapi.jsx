import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_BASE_URL,
  responseType: 'blob',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`, 
  },
});

// GET request to download a file by filename
export const downloadFileByFilename = (filePath) => {
    // This function triggers the download of a file using the filename
    // It sends a GET request to download the specified file
    return api.get(`/files/download`, {
      params: { filePath: filePath }
    });
  };