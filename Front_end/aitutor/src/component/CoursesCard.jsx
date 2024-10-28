//This component is For get all Courses
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/CoursesCard.css';
import Pagination from './Pagination';
import { getAllCourses } from '../api'; 
import { Spin, Alert, Button } from 'antd'; 

const CoursesCard = () => {
  const navigate = useNavigate();


  const [courses, setCourses] = useState([]);  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(4);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true);
        const response = await getAllCourses();
        console.log(response.data);
        setCourses(response.data || []);
        setLoading(false); 
      } catch (err) {
        setError('Failed to get courses. Please try again later.');
        setLoading(false);
      }
    };

    fetchCourses();
  }, []);

  const safeCourses = Array.isArray(courses) ? courses : [];

  const indexOfLastCourse = currentPage * pageSize;
  const indexOfFirstCourse = indexOfLastCourse - pageSize;
  const currentCourses = safeCourses.slice(indexOfFirstCourse, indexOfLastCourse); 

  const handlePageChange = (page, newPageSize) => {
    setCurrentPage(page);
    setPageSize(newPageSize);
  };

  const handleViewCourse = (courseId) => {
    navigate(`/course/${courseId}`);
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
              <div className="course-card" key={course.courseName}>
                <div className="course-image">
                  <img src={course.image} alt={course.title} />
                </div>
                <div className="course-title">
                  <h4>{course.courseName}</h4>
                </div>
                <Button type="primary" onClick={() => handleViewCourse(course.courseId)}>
                  View Details
                </Button>
              </div>
            ))}
          </div>

          <Pagination
            current={currentPage}
            pageSize={pageSize}
            total={safeCourses.length}
            onChange={handlePageChange}
            showSizeChanger
            pageSizeOptions={['4', '8', '12']}
          />
        </>
      )}
    </div>
  );
};

export default CoursesCard;
