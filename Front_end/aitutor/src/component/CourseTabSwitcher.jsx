import React, { useState } from 'react';
import LearningMaterialCard from './LearningMaterialCard';
import AssignmentCard from './AssignmentCard';
import '../styles/TabSwitcher.css';

const CourseTabSwitcher = () => {
  const [activeTab, setActiveTab] = useState('materials');

  return (
    <div className="tab-switcher">

      <div className="tab-buttons">
        <button
          className={`tab-button ${activeTab === 'materials' ? 'active' : ''}`}
          onClick={() => setActiveTab('materials')}
        >
          Learning Materials
        </button>
        <button
          className={`tab-button ${activeTab === 'exercise' ? 'active' : ''}`}
          onClick={() => setActiveTab('exercise')}
        >
          Assignment
        </button>
      </div>


      <div className="tab-content">
        {activeTab === 'materials' && <LearningMaterialCard />}
        {activeTab === 'exercise' && <AssignmentCard />}
      </div>
    </div>
  );
};

export default CourseTabSwitcher;
