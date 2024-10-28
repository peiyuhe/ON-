import React from 'react';
import '../styles/AIAssistant.css'; 
import { useNavigate } from 'react-router-dom';
import { FaRegFileAlt, FaRegCalendarAlt, FaSmile, FaComments } from 'react-icons/fa'; 

const AIAssistant = () => {
  const nav = useNavigate()
  return (
    <div className="ai-assistant-container">
      <h3 className="ai-title">AI Assistant</h3>
      <div className="ai-option">
        <FaRegFileAlt className="ai-icon" />
        <span onClick={() => nav('/reportgeneration')}>Learning Report Generation</span>
      </div>
      <div className="ai-option">
        <FaRegCalendarAlt className="ai-icon" />
        <span onClick={() => nav('/plangeneration')}>Study Plan</span>
      </div>
      <div className="ai-option">
        <FaSmile className="ai-icon" />
        <span onClick={() => nav('/generateavatar')}>Avatars Generation</span>
      </div>
      <div className="ai-option">
        <FaComments className="ai-icon" />
        <span onClick={() => nav('/studentonlineforum')}>Online Forum</span>
      </div>
    </div>
  );
};

export default AIAssistant;
