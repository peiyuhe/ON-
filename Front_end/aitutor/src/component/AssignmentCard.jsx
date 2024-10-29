import React, { useState, useEffect } from 'react';
import { Card, Button, Typography, Space, Spin, message } from 'antd';
import '../styles/AssignmentCard.css';
import Pagination from './Pagination';
import { getExercisebyCourseId } from '../api';
import { useParams, useNavigate } from 'react-router-dom';

const { Meta } = Card;
const { Title, Text } = Typography;

const AssignmentCard = ({ studentId }) => {
  const { courseId } = useParams(); 
  const navigate = useNavigate(); 
  const [assignments, setAssignments] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const pageSize = 4;

  useEffect(() => {
    const fetchAssignments = async () => {
      try {
        setLoading(true);
        const exercisesResponse = await getExercisebyCourseId(courseId);
        const exercises = exercisesResponse.data;

        // 处理 assignment 和 submissions
        const assignmentsWithScores = exercises.map((exercise) => {
          const submission = exercise.submissions[0]; 
          return {
            ...exercise,
            score: submission ? submission.score : 'No Score', // 如果有成绩就显示成绩，没有则显示 'No Score'
          };
        });

        setAssignments(assignmentsWithScores);
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

  const handleViewDetails = (exerciseId) => {
    navigate(`/assignmentDetail/${exerciseId}`, { state: { studentId } });
  };

  return (
    <div className="assignments-wrapper">
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
                    title={<Title level={4} style={{ margin: 0 }}>{assignment.description || 'Assignment'}</Title>}
                    description={
                      <>
                        <Text type="secondary">{assignment.exerciseId || 'No Exercise ID available'}</Text>
                        <br />
                        {/* <Text type="secondary">Score: {assignment.score}</Text> 显示成绩 */}
                      </>
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
