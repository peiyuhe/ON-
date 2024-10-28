import React from 'react';
import '../../styles/Signup.css';

const Signup = () => {
  return (
    <div className="container">
      {/* Header Navigation */}
      {/* Sign Up and Login Section */}
      <div className="form-container">
        {/* Sign Up Form */}
        <div className="form sign-up">
          <h2>Sign Up</h2>
          <p>Follow and subscribe to be one of the first to know the latest on Jay Chou</p>
          <form>
            <div className="form-group">
              <label>User Name</label>
              <input type="text" placeholder="User Name" />
            </div>
            <div className="form-group">
              <label>Password</label>
              <input type="password" placeholder="Password" />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input type="email" placeholder="Email Address" />
            </div>
            <button className="btn">Sign Up</button>
          </form>
        </div>

        {/* Login Form */}
        <div className="form login">
          <h2>Login</h2>
          <p>Welcome back and join us!</p>
          <form>
            <div className="form-group">
              <label>User Name</label>
              <input type="text" placeholder="User Name" />
            </div>
            <div className="form-group">
              <label>Password</label>
              <input type="password" placeholder="Password" />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input type="email" placeholder="Email" />
            </div>
            <button className="btn">Login</button>
            <button className="btn secondary">Send Verification Code</button>
          </form>
        </div>
      </div>

    </div>
  );
};

export default Signup;
