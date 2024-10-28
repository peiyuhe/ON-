import React, { useState, useEffect } from 'react';
import { Card, Button, Row, Col, Spin, Alert } from 'antd';
import axios from 'axios';
import '../styles/AllCourse.css';

const CoursesPage = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 从后端 API 获取课程数据
  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true);
        const response = await axios.get('http://localhost:8080/api/courses'); 
        setCourses(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch courses. Please try again later.');
        setLoading(false);
      }
    };

    fetchCourses();
  }, []);

  const handleEnrollment = (courseId) => {
    
    console.log(`Enrolling in course with ID: ${courseId}`);
    
  };

  return (
    <div className="courses-page">
      <h2>All Courses</h2>
      {loading ? (
        <Spin tip="Loading courses..." />
      ) : error ? (
        <Alert message="Error" description={error} type="error" showIcon />
      ) : (
        <Row gutter={[16, 16]}>
          {courses.map((course) => (
            <Col key={course.id} xs={24} sm={12} md={8} lg={6}>
              <Card
                title={course.title}
                bordered={false}
                hoverable
                cover={<img alt={course.title} src={course.image || 'https://via.placeholder.com/300'} />}
              >
                <p>{course.description}</p>
                <Button type="primary" block onClick={() => handleEnrollment(course.id)}>
                  Enroll
                </Button>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
};

export default CoursesPage;
