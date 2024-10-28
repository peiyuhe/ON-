import React from 'react';
import CoursesCard from './CoursesCard'; 

const Enrollment = () => {
  return (
    <div>
      <h1>Enrollment Page</h1>
      <p>Select a course to view details and enroll:</p>
      
      <CoursesCard />
    </div>
  );
};

export default Enrollment;
