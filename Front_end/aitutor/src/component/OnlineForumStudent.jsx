import React, { useState, useEffect } from "react";
import { Card, Button, Modal, Pagination, Input } from 'antd';
import '../styles/StudentPanel.css';
import {PostQuestion, getAllForums} from '../api';
import UserProfile from './UserProfile';
import Userlayout from "../layouts/Userlayout";


const userData = {
  name: "ikun",
  role: "Student",
  materialsDownloaded: 9,
  exercisesSubmitted: 12,
  avatar: null,
};

// const postsList = await getAllForums();
// const posts = postsList.data;
const posts = [
  { id: 1, topic: "Math", title: "Need Help with Calculus Problem - Stuck on Limits and Derivatives", details: "I'm struggling with limits and derivatives in calculus.", answer: "The answer to this question will be posted soon." },
  { id: 2, topic: "Computer Basics", title: "Need Assistance with Basic Computer Operations - New User Struggling", details: "I'm new to computers and need help with basic operations.", answer: "We recommend starting with the basics of file management." },
  { id: 3, topic: "Physics", title: "Need Clarification on Newton's Laws", details: "I am confused about how Newton's Laws apply to real-world situations.", answer: "Newton's Laws apply to everyday objects in motion and at rest." },
  { id: 4, topic: "Chemistry", title: "Understanding Chemical Bonding", details: "Can someone explain covalent and ionic bonds in detail?", answer: "Covalent bonds involve sharing electrons, while ionic bonds involve transferring them." },
  { id: 5, topic: "Biology", title: "Help with Cell Structure", details: "I don't understand the differences between plant and animal cells.", answer: "Plant cells have a cell wall, while animal cells do not." }
];

const StudentOnlineForum = () => {
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [selectedPost, setSelectedPost] = useState(null);
  const [questionModalOpen, setQuestionModalOpen] = useState(false);
  const [newQuestion, setNewQuestion] = useState({question: '' });
  const [AiAnswer, setAiAnswer] = useState('');
  const [errors, setErrors] = useState({question:''});
  const [AIanswer, setAIanswer] = useState('');
  const [Teacheranswer, setTeacheranswer] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 2;
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [Forums, setForums] = useState([]); 

  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = Forums.slice(indexOfFirstPost, indexOfLastPost);
  const totalPages = Math.ceil(Forums.length / postsPerPage);
  
  const paginate = (pageNumber) => setCurrentPage(pageNumber);


  useEffect(() => {
    const fetchForums = async () => {
      try {
        setLoading(true); 
        const response = await getAllForums(); 
        setForums(response.data); 
        console.log(response.data)
        setLoading(false); 
      } catch (err) {
        setError(err.message); 
        setLoading(false);
      }
    };
    fetchForums();
  }, []);

  const openPostModal = (post) => {
    setSelectedPost(post);
    setModalIsOpen(true);
  };


  const openQuestionModal = () => {
    setNewQuestion({ question: '' });
    setErrors({ question: '' });
    setQuestionModalOpen(true);
  };

  
  const closeModal = () => {
    setModalIsOpen(false);
    setQuestionModalOpen(false);
    setSelectedPost(null);
  };

  const submitQuestion = async() => {
    const { question } = newQuestion;  // Extract the question from newQuestion object
    let hasError = false;

    const newErrors = { question: '' };
    // Check if the question is empty or only whitespace
    if (!question || !question.trim()) {
      newErrors.question = "Please enter a question";
      hasError = true;  // Set error flag to true
    }

    setErrors(newErrors);  // Update the error state to show the error message

    if (!hasError) {
      // If no error, proceed with the submission
      console.log("Question Submitted:", newQuestion);
      const request = await PostQuestion(1, 1, question);
      console.log(request.data);
      setAiAnswer(request.data);
      closeModal();  // Close the modal after submission
    }
};



  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewQuestion({ ...newQuestion, [name]: value });
    setErrors({ ...errors, [name]: '' });
  };

  return (
    <Userlayout>

<Card className="forum-section">
        <div className="forum-header">
          <h3>Online Forum</h3>
          <Button type="primary" style={{ float: "right" }} onClick={openQuestionModal}>Add Question</Button>
        </div>

        {currentPosts.map((Forums) => (
          <div key={Forums.forumId} className="post-item">
            <div className="post-content">
              <p><strong>Question:</strong> {Forums.question}</p>
            </div>
            <Button type="primary" onClick={() => openPostModal(Forums)}>View</Button>
          </div>
        ))}


        <Pagination
          current={currentPage}
          total={Forums.length}
          pageSize={postsPerPage}
          onChange={paginate}
          style={{ display: 'flex', justifyContent: 'center', marginTop: "20px" }}
        />
      </Card>

      <Modal
        title="View Post"
        visible={modalIsOpen}
        onCancel={closeModal}
        footer={null}
        width={700}
      >
        {selectedPost && (
          <div className="modal-content">
            <div className="post-details">
              <p><strong>Question:</strong> {selectedPost.question}</p>
              <p><strong>AI Answer:</strong> {selectedPost.aiAnswer}</p>
              <p><strong>Teacher Answer:</strong> {selectedPost.teacherAnswer}</p>
            </div>
            <Button onClick={closeModal}>Close</Button>
          </div>
        )}
      </Modal>

      <Modal
        title="Ask a Question"
        visible={questionModalOpen}
        onCancel={closeModal}
        footer={null}
        width={700}
      >
        <div>
          <Input.TextArea 
            placeholder="Question" 
            value={newQuestion.details} 
            onChange={handleInputChange} 
            name="question" 
            rows={6} 
          />
          {errors.question && <p className="error-text">{errors.question}</p>}
        </div>
        <div className="modal-buttons" style={{ marginTop: '10px', textAlign: 'right' }}>
          <Button type="primary" onClick={submitQuestion}>Submit Question</Button>
          <Button onClick={closeModal}>Close</Button>
        </div>
      </Modal>
    </Userlayout>
    // <div className="student-panel">
      
    // </div>
  );
};

export default StudentOnlineForum;
