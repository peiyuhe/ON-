import React from 'react';

import TabSwitcherStudent from '../../component/TabSwitcherStudent';
import AIAssistant from '../../component/AIAssistant'; 
import UserProfile from '../../component/UserProfile';
import Userlayout from '../../layouts/Userlayout';
import { Breadcrumb, Layout, Menu, theme } from 'antd';

import '../../styles/TabSwitcher.css'; 
import '../../styles/AIAssistant.css'; 
import '../../styles/StudentDashboard.css';
import '../../styles/UserProfile.css';

const { Header, Content, Footer } = Layout;
const StudentDashboard = () => {
    return (
        <Userlayout>
            <div className="student-dashboard">
                <div className="left-content">
                    <TabSwitcherStudent />
                </div>
                <div className="right-content">
                    <AIAssistant />
                </div>
            </div>
        </Userlayout>
    )
}
export default StudentDashboard;