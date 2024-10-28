
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import { Button} from 'antd';
import CoursesCardStudent from './CoursesCardStudent';
import '../styles/TabSwitcherStudent.css';

const TabSwitcherStudent = () => {

  const [activeTab, setActiveTab] = useState('course');
  const navigate = useNavigate(); 

  const handleEnrollClick = () => {
    navigate('/enrollment'); 
  };

  return (
    <div className="tab-switcher">
     
      <div className="tab-buttons">
        <button
          className={`tab-button ${activeTab === 'course' ? 'active' : ''}`}
          onClick={() => setActiveTab('course')}
        >
          Course
        </button>
        <Button className="enroll-button" onClick={handleEnrollClick}>
          Enroll
        </Button>
      </div>
     
      <div className="tab-content">
        {activeTab === 'course' && <CoursesCardStudent />}
      </div>
    </div>
  );
};

export default TabSwitcherStudent;
