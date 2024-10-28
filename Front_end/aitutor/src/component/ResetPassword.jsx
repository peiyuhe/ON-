
import React, { useEffect, useState } from 'react';
import { Button, Form, Input, Checkbox, message, Typography } from 'antd';
import { resetPassword, getUserByUsername } from '../resetPasswordApi';

const { Text } = Typography;

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

const ResetPasswordComponent = ({ setFormRef }) => {
  const [form] = Form.useForm();
  const [securityQuestion, setSecurityQuestion] = useState('');

  useEffect(() => {
    setFormRef(form);
  }, [form, setFormRef]);

  const onFinish = async (values) => {
    const userName = values.username;
    

    const PasswordResetDTO = {
      username: userName,
      newPassword: values.newPassword,
      confirmPassword: values.confirm,
      securityAnswer: values.answer,
    };

    try {

      const response = await getUserByUsername(userName);
      if (response.data && response.data.securityQuestion) {
        setSecurityQuestion(response.data.securityQuestion); 
      } else {
        message.error('Failed to retrieve security question.');
      }

      // Call API to reset password
      const resetResponse = await resetPassword(PasswordResetDTO);
      console.log(resetResponse.data);
      if (resetResponse.data === "User not found") {
        message.error('User not found!');
      } else if (resetResponse.data === "Incorrect security answer") {
        message.error('Incorrect security answer!');
      } else if (resetResponse.data === "Passwords do not match") {
        message.error('Passwords do not match!');
      } else {
        message.success('Password reset successful!');
      }

    } catch (error) {
      message.error('Password reset failed. Please try again.');
      console.error('Error during password reset:', error);
    }
  };

  return (
    <Form
      {...formItemLayout}
      form={form}
      name="resetPassword"
      onFinish={onFinish}
      style={{ maxWidth: 800 }}
      scrollToFirstError
    >
      <Form.Item
        name="username"
        label="Username"
        rules={[{ required: true, message: 'Please input your username!', whitespace: true }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        name="newPassword"
        label="New Password"
        rules={[{ required: true, message: 'Please input your new password!' }]}
        hasFeedback
      >
        <Input.Password />
      </Form.Item>

      <Form.Item
        name="confirm"
        label="Confirm Password"
        dependencies={['newPassword']}
        hasFeedback
        rules={[
          {
            required: true,
            message: 'Please confirm your password!',
          },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('newPassword') === value) {
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
        label="Security Question"
      >
        <Text>{securityQuestion || 'No security question'}</Text>
      </Form.Item>

      <Form.Item
        name="answer"
        label="Answer"
        rules={[{ required: true, message: 'Please input the answer to your personal question!' }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        name="agreement"
        valuePropName="checked"
        rules={[{ validator: (_, value) => value ? Promise.resolve() : Promise.reject(new Error('You must accept the agreement!')) }]}
        {...tailFormItemLayout}
      >
        <Checkbox>
          I have read the <a href="">agreement</a>
        </Checkbox>
      </Form.Item>
    </Form>
  );
};

export default ResetPasswordComponent;
