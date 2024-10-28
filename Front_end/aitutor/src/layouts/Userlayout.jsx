import React, { useState,useEffect } from 'react';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import AppFooter from '../component/AppFooter';
import AppHeader from '../component/AppHeader';
import UserProfile from '../component/UserProfile';
import UserProfileTeacher from '../component/UserProfileTeacher';
const { Header, Content, Footer } = Layout;

const Userlayout = ({ children }) => {
    const [role, setRole] = useState("");
    useEffect(() => {
        const storedRole = localStorage.getItem('role');
        setRole(storedRole);
    }, []);
    return (
        <Layout>
            <Header style={{
                display: 'flex',

                alignItems: 'center',
                background: '#E9F7FF',
                height: '150px',
                padding: '0 20px',
            }}
            >
                {role === 'STUDENT' && <UserProfile />}
                {role === 'TEACHER' && <UserProfileTeacher />}
            </Header>
            <Content>
                {children}
            </Content>
        </Layout>

    );
};
export default Userlayout;