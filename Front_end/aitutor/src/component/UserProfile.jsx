
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';  
import { Row, Col, Card } from 'antd';
import '../styles/UserProfile.css';
import { getStudentById } from '../api';

const { Meta } = Card;

const UserProfile = () => {
  const navigate = useNavigate(); 
  const baseUrl = 'http://localhost:8080';

  const [user, setUser] = useState({
    username: '',
    role: '',
    avatar: ''
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);


  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        // console.log(1);
        const response = await getStudentById(parseInt(localStorage.getItem('Id'))); 
        
        // console.log(userId);
        // console.log(2);
        const userData = response.data;
        console.log(response.data);
        setUser({
          username: userData.username,
          role: userData.role,
          avatar: userData.avatar 
        });
        setLoading(false);
      } catch (err) {
        setError('Failed to load user profile');
        setLoading(false);
      }
    };

    fetchUserProfile();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;


  const handleEditClick = () => {
    navigate('/changeprofilestudent'); 
  };

  return (
    <div className="user-profile-container">
      <Row gutter={[16, 16]}>
        <Col span={8}>
          {/* <img src={user.avatar || 'https://via.placeholder.com/50'} alt="User Avatar" className="user-avatar" /> */}
          <img src={`${user.avatar}` || 'https://via.placeholder.com/50'} alt="User Avatar" className="user-avatar" />
        </Col>
        <Col span={8}>
          <div className="user-details">
            <h3 className="user-name">{user.username}</h3>
            <p className="user-role">{user.role}</p>
          </div>
        </Col>

        <Col span={8}>
          <button className="edit-button" onClick={handleEditClick}>Edit</button>
        </Col>
      </Row>
    </div>
  );
};

export default UserProfile;
