import React, { useState, useEffect } from 'react';
import { Card, Button, Typography, Space, Spin, message } from 'antd';
import '../styles/AssignmentCard.css';
import Pagination from './Pagination';
import { getExercisebyCourseId, getExercise } from '../api';
import {downloadFileByFilename} from '../downloadapi';
import { useParams, useNavigate } from 'react-router-dom';

const { Meta } = Card;
const { Title, Text } = Typography;

const AssignmentCard = ({ studentId }) => {
  const { courseId } = useParams(); 
  const navigate = useNavigate(); 
  const [assignments, setAssignments] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [exercise, setExercise] = useState({});
  const [loading, setLoading] = useState(true);
  const pageSize = 4;
  console.log("this is AssignmentCard studentId");
  console.log(studentId);
  useEffect(() => {
    const fetchAssignments = async () => {
      try {
        const exercisesResponse = await getExercisebyCourseId(courseId);
        // console.log(exercisesResponse);
        const exercises = exercisesResponse.data;

        // console.log("Fetched Exercises for Course:", exercises);

        setAssignments(exercises);
      } catch (error) {
        console.error('Failed to fetch exercises:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAssignments();
  }, [courseId]);

 
  const indexOfLastAssignment = currentPage * pageSize;
  const indexOfFirstAssignment = indexOfLastAssignment - pageSize;
  const currentAssignments = assignments.slice(indexOfFirstAssignment, indexOfLastAssignment);


  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleViewDetails = async(exerciseId) => {
    navigate(`/assignmentDetail/${exerciseId}`, { state: { studentId } });
  };

  return (
    <div className="assignments-wrapper">
      <Title level={2} style={{ textAlign: 'center', marginBottom: '20px' }}>Assignments</Title>

      {loading ? (
        <Spin tip="Loading assignments..." style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }} />
      ) : (
        <div className="assignments-container">
          <Space direction="horizontal" wrap size="large" style={{ display: 'flex', justifyContent: 'center' }}>
            {currentAssignments.length > 0 ? (
              currentAssignments.map((assignment, index) => (
                <Card
                  key={index}
                  hoverable
                  style={{ width: 300, borderRadius: '10px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)' }}
                  actions={[
                    <Button type="primary" onClick={() => handleViewDetails(assignment.exerciseId)}>
                      View Details
                    </Button>
                  ]}
                >
                  <Meta
                    title={<Title level={4} style={{ margin: 0 }}>{assignment.exerciseId || 'Assignment'}</Title>}
                    description={
                      <Text type="secondary">{assignment.description || 'No description available'}</Text>
                    }
                  />
                </Card>
              ))
            ) : (
              <Text>No assignments available for this course.</Text>
            )}
          </Space>
        </div>
      )}

      <Pagination
        current={currentPage}
        pageSize={pageSize}
        total={assignments.length}
        onChange={handlePageChange}
        showSizeChanger={false}
        style={{ marginTop: '20px', textAlign: 'center' }}
      />
    </div>
  );
};

export default AssignmentCard;
