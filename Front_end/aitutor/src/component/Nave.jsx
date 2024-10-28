import React, { useState } from 'react';
import { AppstoreOutlined, MailOutlined, SettingOutlined } from '@ant-design/icons';
import { Menu } from 'antd';
// import logo from '../Pictures/logo192.png';

const items = [
  {
    label: 'Home',
    key: 'home',
    icon: <MailOutlined />,
    disabled: true,
  },
  {
    label: 'University',
    key: 'university',
    icon: <SettingOutlined />,
    children: [
      {
        type: 'group',
        label: 'China',
        children: [
          {
            label: (
                <a href=" " target="_blank" rel="noopener noreferrer">
                  Peking University
                </a >
              ),
            key: 'setting:1',
          },
          {
            label: (
                <a href="https://www.tsinghua.edu.cn/" target="_blank" rel="noopener noreferrer">
                   Tsinghua University
                </a >
              ),
            key: 'setting:2',
          },
        ],
      },
      {
        type: 'group',
        label: 'Australia',
        children: [
          {
            label: (
                <a href="https://www.sydney.edu.au/" target="_blank" rel="noopener noreferrer">
                   USYD
                </a >
              ),
            key: 'setting:3',
          },
          {
            label: (
                <a href="https://www.unsw.edu.au/" target="_blank" rel="noopener noreferrer">
                   UNSW
                </a >
              ),
            key: 'setting:4',
          },
        ],
      },
    ],
  },
  {
    label: 'Community',
    key: 'community',
    icon: <AppstoreOutlined />,
    disabled: true,
  },
  {
    label: 'Resources',
    key: 'resources',
    icon: <AppstoreOutlined />,
    disabled: true,
  },
  {
    label: 'Assignment',
    key: 'assignment',
    icon: <AppstoreOutlined />,
    disabled: true,
  },
  {
    label: 'Profile',
    key: 'profile',
    icon: <AppstoreOutlined />,
    disabled: true,
  },
];

const NaveComponent = () => {
  const [current, setCurrent] = useState('mail');
  const onClick = (e) => {
    console.log('click ', e);
    setCurrent(e.key);
  };
  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', width: '100%' }}>
      {/* < img src={logo} alt="Logo" style={{ width: '30px', marginRight: '16px' }} /> */}
      <div style={{ flexGrow: 1, display: 'flex', justifyContent: 'flex-end' }}>
        <Menu onClick={onClick} selectedKeys={[current]} mode="horizontal" items={items} />
      </div>

    </div>
  );
};

export default NaveComponent;