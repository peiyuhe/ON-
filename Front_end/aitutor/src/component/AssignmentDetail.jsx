import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Button, Modal, Upload, message, Spin } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { getExercise, uploadSubmissionFile } from '../api';
import {downloadFileByFilename} from '../downloadapi';
import { useLocation } from 'react-router-dom';
import '../styles/AssignmentDetail.css';
import axios from 'axios';

const AssignmentDetail = () => {
  const { exerciseId } = useParams();
  const [imageSrc, setImageSrc] = useState(null); 
  const [isModalVisible, setIsModalVisible] = useState(false); 
  const [exercise, setExercise] = useState({});
  const [selectedFile, setSelectedFile] = useState(null); 
  const [loading, setLoading] = useState(true); 
  const location = useLocation();
  const { studentId } = location.state;


  const api = axios.create({
    
  });

  useEffect(() => {
    const fetchExercise = async () => {
      try {
        
        const response = await getExercise(exerciseId);
        console.log(response.data);
        setExercise(response.data);
        setLoading(false);
      } catch (error) {
        message.error('Failed to fetch image from server.');
        setLoading(false);
      }
    };

    fetchExercise();
  }, []);


  const handleFileUpload = async () => {
    if (!selectedFile) {
      message.error('Please select a file to upload.');
      return;
    }
    console.log(selectedFile);
    console.log('exerciseId',exerciseId);
    console.log("Student ID:", studentId.studentId);
    const formData = new FormData();
    formData.append('file', selectedFile); 
    formData.append('exerciseId',exerciseId);
    formData.append('studentId',studentId.studentId);
    try {
      await uploadSubmissionFile(formData); 
      message.success('File uploaded successfully.');
      setIsModalVisible(false); 
    } catch (error) {
      message.error('Failed to upload file.');
    }
  };

  const handleDownload = async () => {
    try {
        const newFilePath = exercise.filePath.replace(/^src[\\/]+main[\\/]+resources[\\/]+static[\\/]+files[\\/]/, '').replace(/[\\]+/g, '/');
        console.log(newFilePath);
        const response = await downloadFileByFilename(newFilePath);
        console.log(response.headers.getContentType());
        const blob = new Blob([response.data], { type: response.headers.getContentType() });
        const url = window.URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', newFilePath.split('/').pop());
        document.body.appendChild(link);
        link.click();
        link.parentNode.removeChild(link);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Error downloading file:', error);
    }
  };
  return (
    <div className="assignment-detail-container">
      <h2 className="assignment-title">Assignment Detail</h2>
      <div className="image-wrapper">
        {loading ? (
          <Spin tip="Loading exercise..." size="large" />
        ) : exercise ? (
          <div>
            <h2>{exercise.description}</h2>
            <a onClick={handleDownload}>{exercise.filePath}</a>
          </div>
        ) : (
          <p>No exercise available</p>
        )}
      </div>

      <Button type="primary" size="large" onClick={() => setIsModalVisible(true)} style={{ marginTop: '20px' }}>
        Submit Assignment
      </Button>

      <Modal
        title="Upload Submission"
        visible={isModalVisible}
        onOk={handleFileUpload}
        onCancel={() => setIsModalVisible(false)}
        okText="Confirm"
        cancelText="Cancel"
      >
        <div className="upload-section">
          <Upload
            beforeUpload={(file) => {
              setSelectedFile(file); 
              return false; 
            }}
            maxCount={1}
          >
            <Button icon={<UploadOutlined />}>Select File</Button>
          </Upload>
          <p className="upload-hint"></p>
        </div>
      </Modal>
    </div>
  );
};

export default AssignmentDetail;
