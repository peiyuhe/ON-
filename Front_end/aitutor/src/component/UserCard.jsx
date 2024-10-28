import React from 'react';
import '../styles/UserCard.css'; 


const UserCard = ({ username, role, password, avatar, onDelete, user_Id }) => {
  return (
    <div className="user-card">
      <img src={avatar} alt="User Avatar" className="user-avatar" />
      <div className="user-details">
        {/* <p>
          <strong>UserID:</strong> {user_Id}
        </p> */}
        <p>
          <strong>Username:</strong> {username}
        </p>
        <p>
          <strong>Role:</strong> {role}
        </p>
      </div>
      <div className="user-actions">
        <button className="delete-button" onClick={() => onDelete(user_Id)}>Delete</button>
      </div>
    </div>
  );
};

export default UserCard;
