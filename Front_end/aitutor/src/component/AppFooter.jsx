import React from 'react';
import Footer from 'rc-footer';
import 'rc-footer/assets/index.css'; // Import the necessary styles
import '../styles/Footer.css'; 

const AppFooter = () => (
  <div className="footer-container">
    
    <Footer
      columns={[
        {
          title: '相关资源',
          items: [
            { title: 'Ant Design Pro', url: 'https://pro.ant.design/' },
            { title: 'Ant Design Mobile', url: 'https://mobile.ant.design/' },
            { title: 'Kitchen - Sketch 工具集', url: 'https://kitchen.alipay.com/' },
          ],
        },
        {
          title: '社区',
          items: [
            { title: 'Ant Design Pro', url: 'https://pro.ant.design/' },
            { title: 'Ant Design Mobile', url: 'https://mobile.ant.design/' },
            { title: 'Kitchen - Sketch 工具集', url: 'https://kitchen.alipay.com/' },
          ],
        },
        {
          title: '帮助',
          items: [
            { title: 'Ant Design Pro', url: 'https://pro.ant.design/' },
            { title: 'Ant Design Mobile', url: 'https://mobile.ant.design/' },
            { title: 'Kitchen - Sketch 工具集', url: 'https://kitchen.alipay.com/' },
          ],
        },
        {
          title: '更多产品',
          items: [
            {
              icon: (
                <img
                  src="https://gw.alipayobjects.com/zos/rmsportal/MjEImQtenlyueSmVEfUD.svg"
                  width="24px"
                  alt="Yuque"
                />
              ),
              title: '语雀',
              url: 'https://yuque.com',
              description: '知识创作与分享工具',
            },
            {
              icon: (
                <img
                  src="https://gw.alipayobjects.com/zos/rmsportal/BvZMWujHDbRATvVeCkzw.svg"
                  width="24px"
                  alt="yunfengdie"
                />
              ),
              title: '云凤蝶',
              url: 'https://yunfengdie.com',
              description: '中台建站平台',
            },
          ],
        },
      ]}
      
      theme="light" 
      backgroundColor="#ffffff" 
      className="custom-footer"
    />
  </div>
);

export default AppFooter;
