import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { createCourse } from '../api'; 


const CreateCourse = () => {
  const [courseName, setCourseName] = useState('');
  const [courseDescription, setCourseDescription] = useState('');
  const [loading, setLoading] = useState(false);


  const handleSubmit = async () => {
    if (!courseName || !courseDescription) {
      message.error('Please fill in both the course name and description.');
      return;
    }

    setLoading(true); 

    try {
        const Id=localStorage.getItem("Id");
        console.log(Id);
      await createCourse( courseName, Id,courseDescription );
      message.success('Course created successfully!');
      

      setCourseName('');
      setCourseDescription('');
    } catch (error) {
      message.error('Failed to create course. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '50px auto' }}>
      <h1>Create New Course</h1>
      <Form layout="vertical">
        <Form.Item label="Course Name" required>
          <Input
            value={courseName}
            onChange={(e) => setCourseName(e.target.value)}
            placeholder="Enter the course name"
          />
        </Form.Item>

        <Form.Item label="Course Description" required>
          <Input.TextArea
            value={courseDescription}
            onChange={(e) => setCourseDescription(e.target.value)}
            placeholder="Enter a brief description of the course"
            rows={4}
          />
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            onClick={handleSubmit}
            loading={loading}
            disabled={!courseName || !courseDescription}
          >
            Submit
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default CreateCourse;

