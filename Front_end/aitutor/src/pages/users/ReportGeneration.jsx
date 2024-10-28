
import React, { useState, useEffect } from 'react';
import { Card, Button, message, Select } from 'antd';
import '../../styles/StudentPanel.css';
import Userlayout from '../../layouts/Userlayout';
import {
  generateRecentLearningReport,
  generateReportByCourse,
  generateReportByExercise,
  viewReport,
  translateReport,
  getEnrollmentsByStudentId,
  getExercisebyCourseId
} from '../../api';

const { Option } = Select;

const StudentLearningReport = () => {
  const [studentId, setStudentId] = useState(null); 
  const [reportData, setReportData] = useState(null);
  const [selectedCourse, setSelectedCourse] = useState(null); 
  const [selectedExercise, setSelectedExercise] = useState(null); 
  const [targetLanguage, setTargetLanguage] = useState('en'); 
  const [courses, setCourses] = useState([]); 
  const [exercises, setExercises] = useState([]); 


  useEffect(() => {
    const storedStudentId = localStorage.getItem('Id');
    if (storedStudentId) {
      setStudentId(storedStudentId);
      fetchCoursesAndExercises(storedStudentId); 
    } else {
      message.error('No student ID found in local storage.');
    }
  }, []);


  const fetchCoursesAndExercises = async (studentId) => {
    try {
      const response = await getEnrollmentsByStudentId(studentId);
      const enrollments = response.data;
      const courseList = enrollments.map((enrollment) => ({courseId: enrollment.courseId}));
      const courseIds = courseList.map(course => course.courseId);
      const exerciseResponses = await Promise.all(
      courseIds.map(courseId => getExercisebyCourseId(courseId)));
      const exerciseList =exerciseResponses.map(response => response.data);
      // console.log(exerciseList);

      const flatExerciseList = exerciseList.flat();
      console.log(flatExerciseList);
      setCourses(courseList);
      setExercises(flatExerciseList); 
    } catch (error) {
      message.error('Failed to fetch courses and exercises.');
    }
  };

  const handleGenerateRecentReport = async () => {
    if (!studentId) return;
    try {
      const response = await generateRecentLearningReport(studentId);
      setReportData(response.data);
      message.success('Recent learning report generated successfully!');
    } catch (error) {
      console.error(error);
      message.error('Failed to generate recent report.');
    }
  };

  const handleGenerateReportByCourse = async () => {
    if (!studentId || !selectedCourse) return;
    try {
      console.log(studentId);
      console.log(selectedCourse);
      const response = await generateReportByCourse(studentId, selectedCourse);
      setReportData(response.data);
      message.success('Report by course generated successfully!');
    } catch (error) {
      console.error(error);
      message.error('Failed to generate report by course.');
    }
  };

  const handleGenerateReportByExercise = async () => {
    if (!studentId || !selectedExercise) return;
    try {
      console.log(studentId);
      console.log(selectedExercise);
      const response = await generateReportByExercise(studentId, selectedExercise);
      setReportData(response.data);
      message.success('Report by exercise generated successfully!');
    } catch (error) {
      console.error(error);
      message.error('Failed to generate report by exercise.');
    }
  };


  const handleTranslateReport = async () => {
    const reportId = reportData.reportId !== '' ? reportData.reportId: 1
    try {
      const translateRequestDTO = {
        reportId: reportData.reportId ? reportData.reportId : 1,
        targetLanguage: targetLanguage
      };
      console.log(translateRequestDTO);
      const response = await translateReport(translateRequestDTO);
      setReportData(response.data); 
      message.success(`Report translated successfully: ${response.data}`);
    } catch (error) {
      console.error(error);
      message.error('Failed to translate report.');
    }
  };

  return (
    <Userlayout>
      <Card className="report-section">
        <h3>Generate Your Learning Report</h3>

        <Button type="primary" onClick={handleGenerateRecentReport} style={{ marginRight: '10px', marginBottom: '10px' }}>
          Generate Recent Report
        </Button>

        {/* <Select
          placeholder="Select Course"
          onChange={(value) => setSelectedCourse(value)}
          style={{ width: 180, marginRight: '10px', marginBottom: '10px' }}
        >
          {courses.map((course) => (
            <Option key={course.courseId} value={course.courseId}>
              {course.courseId}
            </Option>
          ))}
        </Select>
        <Button type="primary" onClick={handleGenerateReportByCourse} disabled={!selectedCourse}>
          Generate by Course
        </Button>

        <Select
          placeholder="Select Exercise"
          onChange={(value) => setSelectedExercise(value)}
          style={{ width: 180, marginRight: '10px', marginTop: '10px', marginBottom: '10px' }}
        >
          {exercises.map((exercise) => (
            <Option key={exercise.exerciseId} value={exercise.exerciseId}>
              {exercise.exerciseId}
            </Option>
          ))}
        </Select>
        <Button type="primary" onClick={handleGenerateReportByExercise} disabled={!selectedExercise}>
          Generate by Exercise
        </Button> */}

        <Select
          placeholder="Select Language"
          onChange={(value) => setTargetLanguage(value)}
          style={{ width: 180, marginTop: '10px', marginBottom: '10px' }}
        >
          <Option value="en">English</Option>
          <Option value="es">Spanish</Option>
          <Option value="zh">Chinese</Option>
        </Select>
        <Button type="primary" onClick={handleTranslateReport} style={{ marginTop: '10px' }}>
          Translate Report
        </Button>
      </Card>


        <Card className="report-content" style={{ marginTop: '20px' }}>
          <h3>Report Details</h3>
          <p><strong>Content: {reportData !== null ? reportData : "Generating……"}</strong> </p>
        </Card>
    </Userlayout>
  );
};

export default StudentLearningReport;
