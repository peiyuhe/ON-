import React, { useState } from 'react';
import { Form, Button, Upload, Avatar } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

const ChangeAvatar = () => {
  const [userData, setUserData] = useState({
    avatar: '', 
  });


  const handleAvatarChange = ({ file }) => {
    const fileURL = URL.createObjectURL(file); 
    // console.log(fileURL);
    setUserData((prevData) => ({ ...prevData, avatar: fileURL })); 
  };

  return (
    <Form layout="vertical">
      <Form.Item label="" name="avatar">
        <Upload
          listType="picture"
          maxCount={1}
          showUploadList={false}
          beforeUpload={() => false}
          onChange={handleAvatarChange} 
        >
          <Avatar size={80} src={userData.avatar} />
          <Button icon={<UploadOutlined />}>Change Avatar</Button>
        </Upload>
      </Form.Item>
    </Form>
  );
};

export default ChangeAvatar;
