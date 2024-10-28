import React from 'react';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import AppFooter from '../component/AppFooter';
import AppHeader from '../component/AppHeader';
const { Header, Content, Footer } = Layout;

const items = new Array(15).fill(null).map((_, index) => ({
  key: index + 1,
  label: `nav ${index + 1}`,
}));

const Commonlayout = ({ children }) => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  return (
    <Layout>
      <Header
        style={{
          display: 'flex',
          alignItems: 'center',
          background: 'white'
        }}
      >
        <AppHeader/>
        {/* <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['2']}
          items={items}
          style={{
            flex: 1,
            minWidth: 0,
          }}
        /> */}
      </Header>
      <Content
        style={{
          padding: '0 48px',
          
        }}
      >
        <Breadcrumb
          style={{
            margin: '15px 0',
          }}
        >
          {/* <Breadcrumb.Item>Home</Breadcrumb.Item>
          <Breadcrumb.Item>List</Breadcrumb.Item>
          <Breadcrumb.Item>App</Breadcrumb.Item> */}
        </Breadcrumb>
        <div
          style={{
            background: colorBgContainer,
            minHeight: 600,
            padding: 24,
            borderRadius: borderRadiusLG,
          }}
        >
          {children}
        </div>
      </Content>
      <Footer
        style={{
          textAlign: 'center',
        }}
      >
        {/* Ant Design ©{new Date().getFullYear()} Created by Ant UED */}
        {/* <AppFooter/> */}
      </Footer>
    </Layout>
  );
};

export default Commonlayout;
