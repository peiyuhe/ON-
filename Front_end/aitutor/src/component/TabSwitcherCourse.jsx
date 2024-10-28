import React, { useState } from 'react';
import CoursesCard from './CoursesCard';
import LearningMaterialCard from './LearningMterialcard';
import AssignmentCard from './AssignmentCard';
import '../styles/TabSwitcher.css';

const TabSwitcherCourse = (studentId) => {
  const [activeTab, setActiveTab] = useState('materials');
  console.log("Received Student ID in TabSwitcherCourse:", studentId);

  return (
    <div className="tab-switcher">
      <div className="tab-buttons">
        <button
          className={`tab-button ${activeTab === 'materials' ? 'active' : ''}`}
          onClick={() => setActiveTab('materials')}
        >
          materials
        </button>
        <button
          className={`tab-button ${activeTab === 'Assignment' ? 'active' : ''}`}
          onClick={() => setActiveTab('Assignment')}
        >
          Assignment
        </button>
      </div>
      <div className="tab-content">
        {activeTab === 'materials' && <LearningMaterialCard />}
        {activeTab === 'Assignment' && <AssignmentCard studentId={studentId}/>}
      </div>
    </div>
  );
};

export default TabSwitcherCourse;
