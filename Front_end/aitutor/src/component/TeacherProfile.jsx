import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/UserProfile.css';

const UserProfile = () => {
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
        const response = await axios.get('http://localhost:8082/user/profile'); 
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

  return (
    <div className="user-profile-container">

      <div className="user-info">
        <img src={user.avatar || 'https://via.placeholder.com/50'} alt="User Avatar" className="user-avatar" />
        <div className="user-details">
          <h3 className="user-name">{user.username}</h3>
          <p className="user-role">{user.role}</p>
        </div>
      </div>
      
 
      <div className="edit-button-container">
        <button className="edit-button">Edit</button>
      </div>
    </div>
  );
};

export default UserProfile;
