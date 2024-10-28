import { useParams } from 'react-router-dom'; 
import React, { useState, useEffect } from 'react';
import { List, Button, Modal, Form, message, Input } from 'antd'; 
import '../styles/MarkAssignment.css';
import { getSubmissionsByExerciseId, updateSubmission } from '../api'; 

const MarkAssignment = () => {
    const { exerciseId } = useParams();
    const [submissions, setSubmissions] = useState([]); 
    const [selectedSubmission, setSelectedSubmission] = useState({}); 
    const [score, setScore] = useState(0); 
    const [feedback, setFeedback] = useState('');
    const [filePath, setFilePath] = useState('');
    const [isModalVisible, setIsModalVisible] = useState(false); 

    useEffect(() => {
        fetchSubmissions(); 
    }, [exerciseId]);

    const fetchSubmissions = async () => {
        try {
            const response = await getSubmissionsByExerciseId(exerciseId);
            console.log(response.data);
            setSubmissions(response.data);
            
        } catch (error) {
            message.error('Failed to fetch submissions.');
        }
    };

    const handleMarkSubmission = (submission) => {
        setSelectedSubmission(submission);
        const newFilepath = submission.filePath.replace(/^src[\\/]+main[\\/]+resources[\\/]+static[\\/]+/, '').replace(/[\\]+/g, '/')
        // console.log(`http://localhost:8080/${newFilepath}`);
        setFilePath(`http://localhost:8080/${newFilepath}`);
        setIsModalVisible(true);
    };


    const handleSubmitScore = async () => {
        if (score === '') {
            message.error('Please enter a score.');
            return;
        }

        try {
            console.log(score);
            await updateSubmission(selectedSubmission.submissionId, selectedSubmission.filePath, feedback, score);
            console.log(`Graded: ${selectedSubmission.submissionName}, Score: ${score}`);
            message.success('Submission marked successfully.');
            setIsModalVisible(false); 
            fetchSubmissions(); 
        } catch (error) {
            message.error('Failed to submit score.');
        }
    };

    const onFinish = async(values) => {
        console.log('Submitted values:', values);
        await updateSubmission(selectedSubmission.submissionId, {
            filepath: filePath, 
            feedback: values.feedback, 
            score: values.score
        });
        message.success('Submission marked successfully!');
        setIsModalVisible(false);
    };

    return (
        <div className="mark-assignment-container">
            <h2>Unmarked Submissions</h2>
            <List
                dataSource={submissions}
                renderItem={(item) => (
                    <List.Item
                        actions={[
                            <Button type="primary" onClick={() => handleMarkSubmission(item)}>
                                Mark
                            </Button>,
                        ]}
                    >   
                        {`  Submission by student ${item.studentId} at ${item.submittedAt} feedback: ${item.feedback!== null ? item.feedback:'not marked yet'}`}
                    </List.Item>
                )}
            />

            <Modal
                title="Mark Submission"
                visible={isModalVisible}
                footer={null} 
                closable={false}
            >
                {selectedSubmission && (
                    <>
                        <img
                            src={filePath}
                            alt="Submission"
                            style={{ width: '100%', marginBottom: '20px' }}
                        />
                       <Form
                            layout="vertical"
                            onFinish={onFinish}
                        >
                            <Form.Item
                                label="Score"
                                name="score"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please enter a score',
                                    },
                                    {
                                        type: 'number',
                                        min: 0,
                                        message: 'Score must be a non-negative number',
                                        transform: (value) => Number(value),
                                    },
                                ]}
                            >
                                <Input type="number" placeholder="Enter score" />
                            </Form.Item>

                            <Form.Item
                                label="Feedback"
                                name="feedback"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please enter feedback',
                                    },
                                ]}
                            >
                                <Input.TextArea rows={4} placeholder="Enter feedback" />
                            </Form.Item>

                            <Form.Item>
                                <Button type="primary" htmlType="submit" style={{ marginRight: 8 }}>
                                    Submit
                                </Button>
                                <Button onClick={() => setIsModalVisible(false)}>Cancel</Button>
                            </Form.Item>
                        </Form>
                    </>
                )}
            </Modal>
        </div>
    );
};

export default MarkAssignment;
