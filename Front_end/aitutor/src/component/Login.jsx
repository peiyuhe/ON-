import React, { useState } from 'react';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input, message } from 'antd';
import { useNavigate } from 'react-router-dom'; 
import '../styles/StudentPanel.css'; 
import { login,getUserByUsername } from '../loginapi'; 
import PopupSignupComponent from './PopupSignup';

const LoginComponent = () => {
  const [loading, setLoading] = useState(false); 
  const [errorMessage, setErrorMessage] = useState(''); 
  const navigate = useNavigate(); 

  const onFinish = async (values) => {
    console.log('Received values of form: ', values);
    
    setLoading(true);
    setErrorMessage(''); 
    
    try {
      // console.log(1);
      const response = await login(values.username, values.password); 
      console.log(1);
      console.log(response.data);

      if (response.status === 200 && response.data.token) {

        const { token, student, teacher } = response.data; 
        if(teacher || student){
          const storedName = student ? student.user.username : teacher.user.username;
          const storeduserId = student ? student.studentId: teacher.teacherId;
          const storedRole = student ? student.user.role: teacher.user.role;
          const storeAvatar = student ? student.user.avatar: teacher.user.avatar;
          console.log(1);
          localStorage.setItem('token', token); 
          localStorage.setItem('role', storedRole);   
          localStorage.setItem('username', storedName);
          localStorage.setItem('Id', storeduserId);
          localStorage.setItem('avatar', storeAvatar);
          message.success('Login successful! Redirecting to homepage...');
          // console.log(student.user.role);
          navigate('/homepage');
        }
        else{
          localStorage.setItem('token', token); 
          navigate('/admin');
        }

        

      } else {
        throw new Error('Login failed');
      }
    } catch (error) {
      console.error('Error during login:', error);
      setErrorMessage('Invalid username or password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ textAlign: 'center' }}> 
      <h1 style={{ marginBottom: '20px' }}>Login</h1>
      
      <Form
        name="login"
        initialValues={{ remember: true }}
        style={{ maxWidth: 360, margin: '0 auto' }}  
        onFinish={onFinish} 
      >
        <Form.Item
          name="username"
          rules={[{ required: true, message: 'Please input your Username!' }]}
        >
          <Input prefix={<UserOutlined />} placeholder="Username" />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[{ required: true, message: 'Please input your Password!' }]}
        >
          <Input prefix={<LockOutlined />} type="password" placeholder="Password" />
        </Form.Item>

        <Form.Item>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Form.Item name="remember" valuePropName="checked" noStyle>
              <Checkbox>Remember me</Checkbox>
            </Form.Item>
            <PopupSignupComponent />
          </div>
        </Form.Item>

        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

        <Form.Item>
          <Button block type="primary" htmlType="submit" loading={loading}>
            Log in
          </Button>
        </Form.Item>

        <Form.Item>
          <p>Or, has no account now?</p>
        </Form.Item>
      </Form>
    </div>
  );
};

export default LoginComponent;
