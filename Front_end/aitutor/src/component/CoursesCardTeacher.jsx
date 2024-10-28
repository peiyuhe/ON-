import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Button, Modal, Spin, Alert } from 'antd'; 
import Pagination from './Pagination';
import { deleteCourse, getCoursesByTeacherId } from '../api'; 

const CoursesCardTeacher = () => {
  const navigate = useNavigate();


  const [courses, setCourses] = useState("");  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(4);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedCourseId, setSelectedCourseId] = useState(null); 


  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true); 
        const Id=localStorage.getItem('Id')
        console.log("this is teacherid");
        console.log(Id);
        const response = await getCoursesByTeacherId(Id);
        console.log("this is teacher course");
        console.log(response.data);
        const course=response.data;
        setCourses(course); 
        console.log(courses);
        setLoading(false); 
      } catch (err) {
        setError('Failed to get courses. Please try again later.');
        setLoading(false);
      }
    };

    fetchCourses();
  }, []);


  const indexOfLastCourse = currentPage * pageSize;
  const indexOfFirstCourse = indexOfLastCourse - pageSize;
  const currentCourses = courses.slice(indexOfFirstCourse, indexOfLastCourse);

  const handlePageChange = (page, newPageSize) => {
    setCurrentPage(page);
    setPageSize(newPageSize);
  };

  const handleViewCourse = (courseId) => {
    navigate(`/CreatedCourse/${courseId}`);
  };


  const showDeleteModal = (courseId) => {
    setSelectedCourseId(courseId);
    setIsModalVisible(true);
  };


  const handleDeleteCourse = async () => {
    try {
      await deleteCourse(selectedCourseId);
      setCourses(courses.filter(course => course.courseId !== selectedCourseId)); 
      setIsModalVisible(false); 
    } catch (error) {
      console.error('Failed to delete course:', error);
    }
  };

  const handleCancelDelete = () => {
    setIsModalVisible(false); 
  };

  return (
    <div className="courses-wrapper">
      {loading ? (
        <Spin tip="Loading courses..." />
      ) : error ? (
        <Alert message={error} type="error" showIcon />
      ) : (
        <>
          <div className="courses-container">
            {currentCourses.map((course) => (
              <Card
                key={course.courseId}
                title={course.courseName}
                bordered={false}
                style={{ width: 300, marginBottom: '20px' }}
                actions={[
                  <Button type="primary" onClick={() => handleViewCourse(course.courseId)}>
                    View Details
                  </Button>,
                  <Button type="primary" danger onClick={() => showDeleteModal(course.courseId)}>
                    Delete Course
                  </Button>,
                ]}
              >
                <p>{course.courseDescription}</p>
              </Card>
            ))}
          </div>

          <Pagination
            current={currentPage}
            pageSize={pageSize}
            total={courses.length}
            onChange={handlePageChange}
            showSizeChanger
            pageSizeOptions={['4', '8', '12']}
          />

          <Modal
            title="Confirm Delete Course"
            visible={isModalVisible}
            onOk={handleDeleteCourse}
            onCancel={handleCancelDelete}
            okText="Confirm"
            cancelText="Cancel"
          >
            <p>Are you sure you want to delete this course?</p>
          </Modal>
        </>
      )}
    </div>
  );
};

export default CoursesCardTeacher;
