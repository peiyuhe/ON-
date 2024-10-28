//This page is the detail of a course which student has enrolled
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/EnrolledCourse.css';
import TabSwitcherCourse from './TabSwitcherCourse';
import { getCourseById } from '../api';
import { useLocation } from 'react-router-dom';
const EnrolledCourse = () => {
  const { courseId } = useParams(); 
  const [courseName, setCourseName] = useState(''); 
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null); 
  const location = useLocation();
  const { studentId } = location.state; 
  console.log("Student ID:", studentId);


  useEffect(() => {
    const fetchCourseDetails = async () => {
      try {
        const response = await getCourseById(courseId); 
        setCourseName(response.data.courseName); 
        setLoading(false); 
      } catch (err) {
        console.error('Failed to fetch course details:', err);
        setError('Failed to load course details.');
        setLoading(false); 
      }
    };

    fetchCourseDetails();
  }, [courseId]);

  if (loading) {
    return <div>Loading course details...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      <div className="course-header">
        <h1>{courseName}</h1>
      </div>
      <div>
        <TabSwitcherCourse studentId={studentId}/>
      </div>
    </div>
  );
};

export default EnrolledCourse;
