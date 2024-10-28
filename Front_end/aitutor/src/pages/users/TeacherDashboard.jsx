
import React from 'react';
import StudentGrade from '../../component/StudentGrade'; 
import UserProfileTeacher from '../../component/UserProfileTeacher'; 
import Userlayout from '../../layouts/Userlayout';
import TabSwitcherTeacher from '../../component/TabSwitcherTeacher';
import { Layout } from 'antd';

import '../../styles/TabSwitcher.css'; 
import '../../styles/StudentGrade.css'; 
import '../../styles/TeacherDashboard.css'; 

const { Header, Content } = Layout;

const TeacherDashboard = () => {
  return (
    <Userlayout>
      <div className="teacher-dashboard">
        <div className="left-content">
          <TabSwitcherTeacher
            tabs={[
              { label: 'Courses', content: <div>Manage your courses here.</div> },
            ]}
          />
        </div>
        <div className="right-content">
          <StudentGrade />
        </div>
      </div>
    </Userlayout>
  );
};

export default TeacherDashboard;
