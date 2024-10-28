import React, { useState } from 'react';
import { Button } from 'antd';
import SignupComponent from './Signup';
import LoginComponent from './Login';

const LoginComponent1 = () => (
  <div style={{ padding: '20px', textAlign: 'center' }}>
    <h2>Login</h2>
    <p>Welcome back and join us!</p>
    <input type="text" placeholder="User Name" style={{ display: 'block', margin: '10px auto', width: '80%' }} />
    <input type="password" placeholder="Password" style={{ display: 'block', margin: '10px auto', width: '80%' }} />
    <Button type="primary" style={{ width: '80%' }}>Login</Button>
  </div>
);

const SignupComponent1 = () => (
  <div style={{ padding: '20px', textAlign: 'center' }}>
    <h2>Sign Up</h2>
    <p>Follow and subscribe to be one of the first to know the latest on Jay Chou</p>
    <input type="text" placeholder="User Name" style={{ display: 'block', margin: '10px auto', width: '80%' }} />
    <input type="password" placeholder="Password" style={{ display: 'block', margin: '10px auto', width: '80%' }} />
    <input type="email" placeholder="Email" style={{ display: 'block', margin: '10px auto', width: '80%' }} />
    <Button type="primary" style={{ width: '80%' }}>Sign Up</Button>
  </div>
);

const Content = () => {
  const [showSignup, setShowSignup] = useState(false);

  const handleToggleSignup = () => {
    setShowSignup(!showSignup);
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#fff' }}>
      {/* <div style={{ width: showSignup ? '50%' : '100%', transition: 'width 0.5s', display: 'flex', justifyContent: 'center' }}>
        <LoginComponent />
        <Button type="default" onClick={handleToggleSignup}>
          {showSignup ? 'Hide Sign Up' : 'Show Sign Up'}
        </Button>
      </div> */}
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#fff' }}>
      <div style={{ width: showSignup ? '50%' : '100%', transition: 'width 0.5s', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
        <LoginComponent />
        <Button 
          type="primary" 
          onClick={handleToggleSignup} 
          style={{ width: '80%', backgroundColor: '#1890ff', color: '#fff', border: 'none', marginTop: '-40px' }}
        >
          {showSignup ? 'Hide Sign Up' : 'Show Sign Up'}
        </Button>
      </div>
    </div>

      {showSignup && (
        <div style={{ width: '100%', transition: 'width 0.5s', display: 'flex', justifyContent: 'center' }}>
          <SignupComponent />
        </div>
      )}
      
    </div>
  );
};

const AppTest = () => {
  return (
  <div>
    {/* <Header /> */}
    <Content />
    {/* <Footer /> */}
  </div>);
};

export default AppTest;
