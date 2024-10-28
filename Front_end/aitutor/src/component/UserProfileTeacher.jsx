import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; 
import { Row, Col, Card } from 'antd';
import '../styles/UserProfile.css'; 
import { getTeacherById } from '../api';

const { Meta } = Card;

const UserProfileTeacher = () => {
  const navigate = useNavigate(); 


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
        // const response = await getStudentById(parseInt(localStorage.getItem('userId')));
        // console.log(2);
        const response = await getTeacherById(parseInt(localStorage.getItem('Id')));
        // console.log(123);
        // console.log(parseInt(localStorage.getItem('userId')));
        // console.log(1);
        // console.log(response.data);
        const userData = response.data;
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
    navigate('/changeprofileteacher'); 
  };

  return (
    <div className="user-profile-container">
      <Row gutter={[16, 16]}>
        <Col span={8}>
          <img src={user.avatar} alt="User Avatar" className="user-avatar" />
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

export default UserProfileTeacher;
