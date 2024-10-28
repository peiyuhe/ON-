import React, { useState, useEffect } from 'react';
import { AppstoreOutlined, HomeOutlined, WechatWorkOutlined, UserOutlined } from '@ant-design/icons';
import { Menu } from 'antd';
import { useNavigate } from 'react-router-dom'; 
import logo from '../imge/logo.png';


const createMenuItems = (isDisabled) => [
  {
    label: 'Home',
    key: 'home',
    icon: <HomeOutlined />,
    disabled: isDisabled, 
  },
  {
    label: 'Dashboard',
    key: 'Dashboard',
    icon: <AppstoreOutlined />,
    disabled: isDisabled, 
  },
  {
    label: 'Forum',
    key: 'forum',
    icon: <WechatWorkOutlined />,
    disabled: isDisabled, 
  },
  {
    label: 'Profile',
    key: 'profile',
    icon: <UserOutlined />,
    disabled: isDisabled, 
  }
];

const AppHeader = () => {
  const [current, setCurrent] = useState('home'); 
  const [isLoggedIn, setIsLoggedIn] = useState(false); 
  const [isDisabled, setIsDisabled] = useState(true);
  const navigate = useNavigate(); 

 
  const checkToken = () => {
    const token = localStorage.getItem('token'); 
    const role = localStorage.getItem('role'); 
    if (token && role !=='STUDENT' && role !=='TEACHER') {
      setIsLoggedIn(true); 
      setIsDisabled(true);
    }
    else if (token) {
      setIsLoggedIn(true);  
      setIsDisabled(false);   
    }
    else {
      setIsLoggedIn(false); 
      setIsDisabled(true); 
    }
  };

  
  useEffect(() => {
    
    checkToken();

   
    const interval = setInterval(() => {
      checkToken();
    }, 1000);

    
    return () => clearInterval(interval);
  }, []);

  
  const onClick = (e) => {
    setCurrent(e.key);

    
    if (e.key === 'forum') {
      const role = localStorage.getItem('role'); 
      if (role === 'STUDENT') {
        navigate('/studentonlineforum'); 
      } else if (role === 'TEACHER') {
        navigate('/teacheronlineforum'); 
      } else {
        console.warn('Unknown role or not logged in.');
      }
    }
    
    if (e.key === 'Dashboard') {
      const role = localStorage.getItem('role'); 
      if (role === 'STUDENT') {
        navigate('/StudentDashboard');
      } else if (role === 'TEACHER') {
        navigate('/TeacherDashboard'); 
      } else {
        console.warn('Unknown role or not logged in.');
      }
    }
    if (e.key === 'profile') {
      const role = localStorage.getItem('role'); 
      if (role === 'STUDENT') {
        navigate('/changeprofilestudent');
      } else if (role === 'TEACHER') {
        navigate('/changeprofileteacher');
      } else {
        console.warn('Unknown role or not logged in.');
      }
    }
    if (e.key === 'home') {
      navigate('/homepage');
    }
  };

  
  const handleLogout = () => {
    localStorage.removeItem('token'); 
    localStorage.removeItem('role'); 
    setIsLoggedIn(false); 
    setIsDisabled(true); 
    navigate('/'); 
  };

  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        width: '100%',
        overflow: 'visible',
        whiteSpace: 'nowrap',  
      }}
    >
      
      <div style={{ marginLeft: '40px', marginTop: '30px' }}>
        <img src={logo} alt="My Logo" style={{ height: '60px' }} />
      </div>
  
     
      <div
        style={{
          flexGrow: 1,
          display: 'flex',
          justifyContent: 'flex-end',
          minWidth: '0',  
          
          overflow: 'visible',
        }}
      >
        {/*<Menu*/}
        {/*  onClick={onClick}*/}
        {/*  selectedKeys={[current]}*/}
        {/*  mode="horizontal"*/}
        {/*  style={{ flexShrink: 1, maxWidth: '100%' }}  */}
        {/*  items={createMenuItems(isDisabled)}*/}
        {/*/>*/}
        {/*<Menu*/}
        {/*    onClick={onClick}*/}
        {/*    selectedKeys={[current]}*/}
        {/*    mode="horizontal"*/}
        {/*    style={{ flexShrink: 1, maxWidth: '100%', whiteSpace: 'nowrap' }}*/}
        {/*    items={createMenuItems(isDisabled)}*/}
        {/*    overflow: 'visible'*/}
        {/*/>*/}
        <Menu
            onClick={onClick}
            selectedKeys={[current]}
            mode="horizontal"
            style={{
              flexShrink: 1,
              maxWidth: '100%',
              whiteSpace: 'nowrap',
              overflow: 'visible'  
            }}
            items={createMenuItems(isDisabled)}
        />

      </div>
  
      {isLoggedIn && (
        <div style={{ marginLeft: '16px' }}>
          <button
            style={{
              padding: '8px 16px',
              backgroundColor: '#ff4d4f',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
            }}
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      )}
    </div>
  );
}
export default AppHeader;
