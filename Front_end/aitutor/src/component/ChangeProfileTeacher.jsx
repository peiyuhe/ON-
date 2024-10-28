import React, { useState, useEffect } from 'react';
import { Form, Input, Button, DatePicker, Select, Upload, Avatar, message, Modal } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { getTeacherById, updateTeacher, checkSecurityQuestion, setSecurityQuestion, updateSecurityQuestion, uploadAvatar } from '../api';
import moment from 'moment';
import '../styles/ChangeProfile.css';
import ChangeAvatar from './ChangeAvatar';

const { Option } = Select;

const ChangeProfileTeacher = () => {
  const [form] = Form.useForm();
  const [userData, setUserData] = useState({});
  const [loading, setLoading] = useState(false);
  const [hasSecurityQuestion, setHasSecurityQuestion] = useState(false);
  const [securityQuestionData, setSecurityQuestionData] = useState({ question: '', answer: '' });
  const [avatarModalVisible, setAvatarModalVisible] = useState(false); 
  const teacherId = parseInt(localStorage.getItem('Id'));
  const [selectedAvatar, setSelectedAvatar] = useState(null);
  const [avatarPreview, setAvatarPreview] = useState(null);
  const baseUrl = 'http://localhost:8080';
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await getTeacherById(teacherId);
        const fetchedData = response.data;
        // console.log(fetchedData);

       
        form.setFieldsValue({
          ...fetchedData,
          birth_day: fetchedData.birthDay ? moment(fetchedData.birthDay) : null,
          updated_at: fetchedData.updated_at ? moment(fetchedData.updated_at).format('YYYY-MM-DD') : '',
          security_question: fetchedData.securityQuestion,
          security_answer: fetchedData.securityAnswer,
        });
        setUserData(fetchedData);
        
        setHasSecurityQuestion(fetchedData.securityQuestion !== null);
      
        // if (securityResponse.data.hasSecurityQuestion) {
        //   setSecurityQuestionData({
        //     question: securityResponse.data.securityQuestion,
        //     answer: securityResponse.data.securityAnswer
        //   });

        //   form.setFieldsValue({
        //     security_question: securityResponse.data.securityQuestion,
        //     security_answer: securityResponse.data.securityAnswer,
        //   });
        // }

      } catch (error) {
        message.error('Failed to load user data or security question status');
      }
    };
    fetchUserData();
  }, [teacherId, form]);


  const handleAvatarUpload = ({ file }) => {
    setSelectedAvatar(file);
    const previewURL = URL.createObjectURL(file); 
    setAvatarPreview(previewURL);
  };

  const handleConfirmAvatar = async () => {
    if (!selectedAvatar) {
      message.error('Please select an avatar to upload.');
      return;
    }

    
    const avatarURL = URL.createObjectURL(selectedAvatar);
    setUserData((prevData) => ({ ...prevData, avatar: avatarURL }));
    console.log(userData.userId);
    console.log(selectedAvatar);
    await uploadAvatar(userData.userId, selectedAvatar);
    message.success('Avatar uploaded successfully!');
    setAvatarModalVisible(false);
  };

  const handleFinish = async (values) => {
    setLoading(true);
    try {
      await updateTeacher(
        teacherId,
        {
          birthDay: values.birthDay,
          email: values.email,
          facility: values.facility,
          gender: values.gender,
          phone: values.phone,
          username: values.username,
          avatar: values.avatar,

        }
      );

      const securitySetupDTO = {
        username: values.username,
        securityQuestion: values.security_question,
        securityAnswer: values.security_answer
      };

      if (hasSecurityQuestion) {
        await updateSecurityQuestion(teacherId, securitySetupDTO);
      } else {
        await setSecurityQuestion(securitySetupDTO);
      }

      message.success('Profile and security question updated successfully!');
    } catch (error) {
      message.error('Failed to update profile or security question');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="profile-container">
      <h2>Update Your Profile</h2>
      <Form
        form={form}
        layout="vertical"
        onFinish={handleFinish}
      >
        <Form layout="vertical">
          <Form.Item label="Avatar" name="avatar">
            <div onClick={() => setAvatarModalVisible(true)}>
              <Avatar size={80} src={`${baseUrl}${userData.avatar}`} />
            </div>
          </Form.Item>
        </Form>

        <Form.Item
          label="Birthday"
          name="birth_day"
        >
          <DatePicker
            style={{ width: '100%' }}
            format="YYYY-MM-DD"
          />
        </Form.Item>

        <Form.Item
          label="Email"
          name="email"
          rules={[{  type: 'email', message: 'Please enter a valid email!' }]}
        >
          <Input placeholder="Enter your email" />
        </Form.Item>

        <Form.Item label="Gender" name="gender" >
          <Select placeholder="Select your gender">
            <Option value="male">Male</Option>
            <Option value="female">Female</Option>
            <Option value="other">Other</Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Phone"
          name="phone"
          rules={[{  message: 'Please enter your phone number!' }]}
        >
          <Input placeholder="Enter your phone number" />
        </Form.Item>

        <Form.Item label="Username" name="username">
          <Input placeholder="Enter your username" />
        </Form.Item>

        <Form.Item label="Facility" name="facility">
          <Input placeholder="Enter your facility" />
        </Form.Item>


        <Form.Item label="Security Question" name="security_question" rules={[{ required: !hasSecurityQuestion}]}>
          <Input placeholder={userData.securityQuestion} />
        </Form.Item>

        <Form.Item label="Security Answer" name="security_answer" rules={[{ required: !hasSecurityQuestion}]}>
          <Input placeholder={userData.securityAnswer} />
        </Form.Item>



        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading} block>
            Save Changes
          </Button>
        </Form.Item>
      </Form>
      <Modal
        title="Upload New Avatar"
        visible={avatarModalVisible}
        onOk={handleConfirmAvatar}
        onCancel={() => setAvatarModalVisible(false)}
        okText="Confirm"
        cancelText="Cancel"
      >
        <Avatar size={80} src={avatarPreview || userData.avatar} style={{ marginBottom: 10 }} /> 
        <Upload
          listType="picture"
          maxCount={1}
          beforeUpload={() => false} 
          onChange={handleAvatarUpload}
        >
          <Button icon={<UploadOutlined />}>Select Avatar</Button>
        </Upload>
      </Modal>
    </div>
  );
};

export default ChangeProfileTeacher;
