import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CoursesCardTeacher from './CoursesCardTeacher';
import { Button} from 'antd';
import '../styles/TabSwitcherTeacher.css';




const TabSwitcherTeacher = () => {

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('course');
  const handleCreateClick = () => {
    navigate('/CreateCourse'); 
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
        <Button className="create-button" onClick={handleCreateClick}>
          Create Courese
        </Button>
      </div>
      <div className="tab-content">
        {activeTab === 'course' && <CoursesCardTeacher />}
      </div>
    </div>
  );
};

export default TabSwitcherTeacher;
