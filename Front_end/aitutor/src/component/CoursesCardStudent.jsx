import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/CoursesCard.css';
import Pagination from './Pagination';
import { getEnrollmentsByStudentId, deleteEnrollment, getCourseById } from '../api'; 
import { Spin, Alert, Button, Modal, Card } from 'antd'; 

const CoursesCardStudent = () => {
  const navigate = useNavigate();


  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [courses, setCourses] = useState("");
  const [enrollments, setEnrollments] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(4);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedEnrollmentId, setSelectedEnrollmentId] = useState(null);


  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true);
        const Id = localStorage.getItem("Id");
        console.log("Id", Id);
        

        const response = await getEnrollmentsByStudentId(Id); 
        const enrollments = response.data;
        setEnrollments(enrollments);
  
        console.log("enrollments", enrollments);
        

        const courseIds = enrollments.map(enrollment => enrollment.courseId);
        console.log("courseIds", courseIds);
  

        const courseResponses = await Promise.all(courseIds.map(courseId => getCourseById(courseId)));
        

        const courses = courseResponses.map(response => response.data);
        setCourses(courses);
  
        setLoading(false); 
      } catch (err) {
        console.error(err);
        setError('Failed to get courses. Please try again later.');
        setLoading(false);
      }
    };
  
    fetchCourses();
  }, []);
  

  const indexOfLastCourse = currentPage * pageSize;
  const indexOfFirstCourse = indexOfLastCourse - pageSize;
  const currentCourses = courses.slice(indexOfFirstCourse, indexOfLastCourse);  // Slice the array for pagination

  const handlePageChange = (page, newPageSize) => {
    setCurrentPage(page);
    setPageSize(newPageSize);
  };

  const handleViewCourse = (courseId) => {
    const Id = localStorage.getItem("Id"); 
    navigate(`/EnrolledCourse/${courseId}`, { state: { studentId: Id } }); 
  };

  const showDropCourseModal = (enrollmentId) => {
    setSelectedEnrollmentId(enrollmentId); 
    setIsModalVisible(true);
  };

  const handleDropCourse = async () => {
    try {
      await deleteEnrollment(selectedEnrollmentId);
      setEnrollments(enrollments.filter(enrollment => enrollment.enrollmentId !== selectedEnrollmentId));
      setCourses(courses.filter(course => course.courseId !== selectedEnrollmentId));
      setIsModalVisible(false);
    } catch (error) {
      console.error('Failed to drop the course:', error);
    }
  };

  const handleCancel = () => {
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
            {currentCourses.map((course, index) => (
              <Card
                key={course.courseId}
                title={course.courseName}
                style={{ width: 300, marginBottom: '20px' }}
                actions={[
                  <Button key="view" type="primary" onClick={() => handleViewCourse(course.courseId)}>
                    View Details
                  </Button>,
                  <Button
                    key="drop"
                    type="primary" 
                    danger 
                    onClick={() => showDropCourseModal(enrollments[index].enrollmentId)}
                  >
                    Drop Course
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
            title="Confirm Drop Course"
            visible={isModalVisible}
            onOk={handleDropCourse}
            onCancel={handleCancel}
            okText="Confirm"
            cancelText="Cancel"
          >
            <p>Are you sure you want to drop this course?</p>
          </Modal>
        </>
      )}
    </div>
  );
};

export default CoursesCardStudent;
