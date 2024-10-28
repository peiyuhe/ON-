
import React from 'react';
import '../styles/StudentGrade.css'; 
import { FaBook, FaChartBar, FaSmile, FaComments } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

const StudentGrade = () => {

  const nav = useNavigate()
  
  return (
    <div className="ai-assistant-container">
      <h3 className="ai-title">Avatar and Forum</h3>
      {/*<div className="ai-option">*/}
      {/*  <FaBook className="ai-icon" />*/}
      {/*  <span onClick={() => nav('/markassignments')}>Mark Assignments</span>*/}
      {/*</div>*/}
      {/*<div className="ai-option">*/}
      {/*  <FaChartBar className="ai-icon" />*/}
      {/*  <span onClick={() => nav('/gradedistribution')}>Grade Distribution Generation</span>*/}
      {/*</div>*/}
      <div className="ai-option">
        <FaSmile className="ai-icon" />
        <span onClick={() => nav('/avatarsgeneration')}>Avatars Generation</span>
      </div>
      <div className="ai-option">
        <FaComments className="ai-icon" />
        <span onClick={() => nav('/onlineforumteacher')}>Online Forum</span>
      </div>
    </div>
  );
};

export default StudentGrade;
