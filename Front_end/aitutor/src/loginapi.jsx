import axios from 'axios';

// 设置 API 基础 URL
const API_BASE_URL = 'http://localhost:8080';
// 创建 Axios 实例，包含全局配置
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
    },
  });
  
  // 用户认证相关 API
  
  // 登录
  export const login = (username, password) => {
    return api.post('/auth/login', {
      username,
      password,
    });
  };
  
  // 注册
  export const registerUser = (userData) => {
    return api.post('/auth/register', userData);
  };

