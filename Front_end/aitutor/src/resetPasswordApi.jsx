import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    // 'Authorization': `Bearer ${localStorage.getItem('token')}`, 
  },
});

//Reset password
export const resetPassword = (PasswordResetDTO) => {
    return api.post(`/auth/reset-password`, 
        PasswordResetDTO
        );
  };
//getSecurityQuestion
  export const getUserByUsername = (username) => {
    return api.get(`/user/${username}`);
  };

//   export const checkSecurityQuestion = (userId) => {
//     return api.get(`/user/check-security`, { 
//       params: { userId: userId }
//     });
//   };