import React, { useState } from 'react';
import { Button, Checkbox, Form, Input, Select, message } from 'antd';
import { registerUser } from '../loginapi'; 
import '../styles/StudentPanel.css'; 

const { Option } = Select;

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 10 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 16 },
  },
};

const tailFormItemLayout = {
  wrapperCol: {
    xs: { span: 24, offset: 0 },
    sm: { span: 16, offset: 8 },
  },
};

const SignupComponent = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false); 


  const onFinish = async (values) => {
    
    setLoading(true);

    try {

      const response = await registerUser({
        username: values.username, 
        password: values.password, 
        role: values.role,         
      });


      if (response.status === 201) {
        message.success('Registration successful!');
        form.resetFields();
      } else {
        throw new Error('Registration failed');
      }
    } catch (error) {
      console.error('Error during registration:', error);
      message.error('Registration failed, please try again!'); 
    } finally {
      setLoading(false); 
    }
  };

  return (
    <div style={{ textAlign: 'center' }}>

      <h1 style={{ marginBottom: '20px' }}>Sign Up</h1>

      <Form
        {...formItemLayout}
        form={form}
        name="register"
        onFinish={onFinish}
        style={{ maxWidth: 800 }}
        scrollToFirstError
      >

        <Form.Item
          name="username"
          label="Username"
          rules={[
            {
              required: true,
              message: 'Please input your username!',
              whitespace: true,
            },
          ]}
        >
          <Input />
        </Form.Item>


        <Form.Item
          name="password"
          label="Password"
          rules={[
            {
              required: true,
              message: 'Please input your password!',
            },
          ]}
          hasFeedback
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="confirm"
          label="Confirm Password"
          dependencies={['password']}
          hasFeedback
          rules={[
            {
              required: true,
              message: 'Please confirm your password!',
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('password') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error('The passwords do not match!'));
              },
            }),
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="role"
          label="Role"
          rules={[
            {
              required: true,
              message: 'Please select your role!',
            },
          ]}
        >
          <Select placeholder="select your role">
            <Option value="STUDENT">Student</Option>
            <Option value="TEACHER">Teacher</Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="agreement"
          valuePropName="checked"
          rules={[
            {
              validator: (_, value) =>
                value ? Promise.resolve() : Promise.reject(new Error('Should accept agreement')),
            },
          ]}
          {...tailFormItemLayout}
        >
          <Checkbox>
            I have read the <a href="">agreement</a>
          </Checkbox>
        </Form.Item>

        <Form.Item {...tailFormItemLayout}>
          <Button type="primary" htmlType="submit" loading={loading}>
            Sign up
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default SignupComponent;
