import React, { useState, useEffect } from 'react';
import { Form, Input, Button, DatePicker, Select, Upload, Avatar, message, Modal } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { getStudentById, updateStudent, checkSecurityQuestion, setSecurityQuestion, updateSecurityQuestion,uploadAvatar } from '../api'; 
import moment from 'moment';
import '../styles/ChangeProfile.css'; 


const { Option } = Select;

const ChangeProfileStudent = () => {
  const [form] = Form.useForm();
  const [userData, setUserData] = useState({});
  const [loading, setLoading] = useState(false);
  const [hasSecurityQuestion, setHasSecurityQuestion] = useState(false);
  const [securityQuestionData, setSecurityQuestionData] = useState({ question: '', answer: '' });
  const studentId = parseInt(localStorage.getItem('Id'));
  const [avatarModalVisible, setAvatarModalVisible] = useState(false); 
  const [selectedAvatar, setSelectedAvatar] = useState(null); 
  const [avatarPreview, setAvatarPreview] = useState(null);
  const baseUrl = 'http://localhost:8080';
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await getStudentById(studentId);
        const fetchedData = response.data;
        form.setFieldsValue({
          ...fetchedData,
          birth_day: fetchedData.birthDay ? moment(fetchedData.birthDay) : null,
          security_question: fetchedData.securityQuestion,
          security_answer: fetchedData.securityAnswer,
        });
        
        setUserData(fetchedData); 
        // console.log(fetchedData.securityAnswer);
        setHasSecurityQuestion(fetchedData.securityQuestion !== null);
        // console.log(fetchedData.securityQuestion !== null);
        // console.log("this is securitydata");

      } catch (error) {
        message.error('Failed to load user data or security question status');
      }
    };
    fetchUserData();
  }, [studentId, form]);
  

  const handleAvatarUpload = ({ file }) => {
    setSelectedAvatar(file);
    const previewURL = URL.createObjectURL(file);
    setAvatarPreview(previewURL); 
  };


  const handleConfirmAvatar = async() => {
    if (!selectedAvatar) {
      message.error('Please select an avatar to upload.');
      return;
    }

    const avatarURL = URL.createObjectURL(selectedAvatar);
    setUserData((prevData) => ({ ...prevData, avatar: avatarURL })); 
    console.log(userData.user_id);
    await uploadAvatar(userData.user_id, selectedAvatar);
    message.success('Avatar uploaded successfully!');
    setAvatarModalVisible(false); 
  };

  const handleFinish = async (values) => {
    setLoading(true);
    try {
      const formValues = form.getFieldsValue();
      await updateStudent(
        studentId,
        {
          birthDay:formValues.birth_day.format('YYYY-MM-DD') || null,
          email: formValues.email,
          facility: formValues.facility,  
          gender: formValues.gender,
          major: formValues.major,    
          phone: formValues.phone, 
          username: formValues.username,  
          
        }
      );
      
      const securitySetupDTO = {
        username: values.username,
        securityQuestion: values.security_question,
        securityAnswer: values.security_answer
      };
      
      if (hasSecurityQuestion) {
        await updateSecurityQuestion(userData.user_id, securitySetupDTO);
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
            format='YYYY-MM-DD'
            inputReadOnly={true}
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

        <Form.Item label="Major" name="major">
          <Input placeholder="Enter your major" />
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

export default ChangeProfileStudent;
