
import React, { useState } from 'react';
import { Button, Modal } from 'antd';
import ResetPasswordComponent from './ResetPassword';

const PopupSignupComponent = () => {
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [modalText, setModalText] = useState('Please tell us your username and input your new password!');
  const [formRef, setFormRef] = useState(null); 

  const showModal = () => {
    setOpen(true);
  };

  const handleOk = () => {
  
    if (formRef) {
      formRef.submit(); 
    }
  };

  const handleCancel = () => {
    console.log('Clicked cancel button');
    setOpen(false);
  };

  const onFormSubmit = (values) => {
  
    console.log('Form Submitted with values: ', values);
    setModalText('The process will be finished after two seconds');
    setConfirmLoading(true);


    setTimeout(() => {
      setOpen(false);
      setConfirmLoading(false);
    }, 2000);
  };

  return (
    <>
      <Button type="" onClick={showModal}>
        Forget password
      </Button>
      <Modal
        title="Change password"
        open={open}
        onOk={handleOk} 
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
        okText="Confirm"
      >
        <p>{modalText}</p>
        <ResetPasswordComponent onSubmit={onFormSubmit} setFormRef={setFormRef} /> 
      </Modal>
    </>
  );
};

export default PopupSignupComponent;
