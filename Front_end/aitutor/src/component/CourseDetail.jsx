import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Button, message, Modal } from 'antd';
import { getCourseById,createEnrollment } from '../api';
import '../styles/CourseDetail.css';

const CourseDetail = () => {
  const { courseId } = useParams(); 
  const [courseDetailContent, setCourseDetailContent] = useState(null); 
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null); 
  const [isModalVisible, setIsModalVisible] = useState(false);

  useEffect(() => {
    const fetchCourse = async () => {
      try {
        const response = await getCourseById(courseId); 
        setCourseDetailContent(response.data);
        console.log(response.data); 
        setLoading(false);
      } catch (err) {
        setError('Failed to load course details');
        setLoading(false);
      }
    };
    fetchCourse();
  }, [courseId]);

  const handleEnroll = async () => {
    try {
      const Id= localStorage.getItem("Id");
      console.log(Id);
      console.log(courseId);

      await createEnrollment(courseId, Id);
      message.success('You have successfully enrolled in the course!');
    } catch (error) {
      message.error('Failed to enroll in the course. Please try again.');
      console.error(error);
    }
  };


  const showEnrollConfirmModal = () => {
    setIsModalVisible(true);
  };


  const handleConfirm = async () => {
    await handleEnroll();
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  if (loading) {
    return <div>Loading course details...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="course-detail-container">
      <div className="course-header">
        <h1>{courseDetailContent.courseName}</h1>
        {/* <h2>Instructor: {courseDetailContent.student.studentID}</h2> */}
      </div>
      <div className="course-description">
        <p>{courseDetailContent.courseDescription}</p>
      </div>

      <div style={{ marginTop: '20px' }}>
        <Button type="primary" onClick={showEnrollConfirmModal}>
          Enroll in Course
        </Button>
      </div>

      <Modal
        title="Confirm Enrollment"
        visible={isModalVisible}
        onOk={handleConfirm}
        onCancel={handleCancel}
        okText="Confirm"
        cancelText="Cancel"
      >
        <p>Are you sure you want to enroll in this course?</p>
      </Modal>
    </div>
  );
};

export default CourseDetail;
