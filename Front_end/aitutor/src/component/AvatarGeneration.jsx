import React, { useState } from 'react';
import { Card, Button, Modal, Input, message } from 'antd';
import Userlayout from '../layouts/Userlayout';
import '../styles/StudentPanel.css'; 
import { generateTemporaryAvatar, updateStudent, updateTeacher } from '../api';
import UserProfile from './UserProfile';


const userData = {
  name: "ikun",
  role: "Student",
  materialsDownloaded: 9,
  exercisesSubmitted: 12,
  avatar: null,
};

const GenerateAvatar = () => {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [avatarDescription, setAvatarDescription] = useState('');
  const [errors, setErrors] = useState('');
  const [generatedAvatar, setGeneratedAvatar] = useState(null);
  const [isGenerated, setIsGenerated] = useState(false);

  
  const showModal = async () => {
    if (!avatarDescription.trim()) {
      setErrors("Please provide a description for the avatar!");
      return;
    }
    setErrors('');
    setIsModalVisible(true);

    try {
      const response = await generateTemporaryAvatar(avatarDescription);
      setGeneratedAvatar(response.data);
      console.log(response.data)
      setIsGenerated(true);
    } catch (error) {
      message.error("Failed to generate avatar. Please try again.");
      console.error(error); 
    }
  };

  
  const handleCancel = () => {
    setIsModalVisible(false);
    setIsGenerated(false);
    setGeneratedAvatar(null); 
  };

 
  const handleRegenerate = async () => {
    try {
      const response = await generateTemporaryAvatar(avatarDescription);
      setGeneratedAvatar(response.data);
      message.success("Avatar regenerated successfully!");
    } catch (error) {
      message.error("Failed to regenerate avatar. Please try again.");
    }
  };


  const handleChoose = async() => {
    localStorage.setItem('avatar', generatedAvatar);
    message.success("Avatar chosen successfully!");
    const role = localStorage.getItem('role');
    if(role === 'STUDENT'){
      await updateStudent(localStorage.getItem("Id"), {
        avatar: generatedAvatar
      })
    }
    else{
      await updateTeacher(localStorage.getItem("Id"), {
        avatar: generatedAvatar
      })
    }
    setIsModalVisible(false);
  };

  const handleInputChange = (e) => {
    setAvatarDescription(e.target.value);
    if (e.target.value.trim()) {
      setErrors('');
    }
  };

  return (
    <Userlayout>
      <Card className="generate-avatar-section">
        <h3>Generate Your Personalized Avatar</h3>
        <Input.TextArea
          placeholder="Describe your avatar (e.g., 'A cool anime character with blue hair and glasses')"
          rows={4}
          value={avatarDescription}
          onChange={handleInputChange}
        />
        {errors && <p className="error-text" style={{ color: 'red' }}>{errors}</p>}
        <Button type="primary" onClick={showModal} style={{ marginTop: '10px' }}>
          Generate Avatar
        </Button>
      </Card>

      <Modal
        title="AI Generated Avatar"
        visible={isModalVisible}
        onCancel={handleCancel}
        footer={null}
        width={500}
      >
        <div style={{ textAlign: 'center' }}>
          {isGenerated && generatedAvatar ? (
            <img src={generatedAvatar} alt="Generated Avatar" style={{ width: '150px', height: '150px' }} />
          ) : (
            <p>Generating...</p>
          )}
          <div style={{ marginTop: '20px' }}>
            <Button type="primary" onClick={handleRegenerate} style={{ marginRight: '10px' }}>
              Regenerate
            </Button>
            <Button type="primary" onClick={handleChoose}>
              Choose
            </Button>
          </div>
        </div>
      </Modal>
    </Userlayout>
    // <div className="generate-avatar-page">

    // </div>
  );
};

export default GenerateAvatar;

