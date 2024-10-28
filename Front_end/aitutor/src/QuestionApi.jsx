import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Authorization': `Bearer ${localStorage.getItem('token')}`, 
  },
});


export const postQuestion = (courseId, studentId, question) => { 
    return api.post('/forums/post-question', null, { 
      params: { 
        studentId: 1,
        courseId: 1,
        question: question 
      } })};

    // export const postQuestion = (question) => { 
    // return api.post('/forums/post-question', null, { 
    //     params: { 
    //     question: question 
    //     } })};

    // export const getSecurityQuestion = ()
    