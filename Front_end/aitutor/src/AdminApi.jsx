import axios from 'axios';

// 设置 API 基础 URL
const API_BASE_URL = 'http://localhost:8080';

// 创建 Axios 实例，包含全局配置
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`, // 使用 Bearer token
  },
});

// 管理员相关 API

// 创建管理员
export const createAdmin = (username, password) => {
    return api.post('/admins/create', { 
      username, 
      password 
    });
  };
  
  // 根据 ID 获取管理员信息
  export const getAdminById = (adminId) => {
    return api.get(`/admins/${adminId}`);
  };
  
  // 更新管理员信息
  export const updateAdmin = (adminId, username, password) => {
    return api.put(`/admins/update/${adminId}`, {
      username,
      password
    });
  };
  
  // 删除管理员
  export const deleteAdmin = (adminId) => {
    return api.delete(`/admins/delete/${adminId}`);
  };
  
  //获取所有用户信息
  export const getAllUser = () =>{
    return api.get('/admins')
  };
// 获取所有老师
export const getAllTeachers = () => {
  return api.get('/teachers/get/all');
};

// 获取所有学生
export const getAllStudents = () => {
  return api.get('/students/get/all');
};