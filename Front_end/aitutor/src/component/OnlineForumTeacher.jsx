
import React, { useState, useEffect } from "react";
import { Card, Button, Modal, Pagination, Input } from 'antd';
import '../styles/TeacherPanel.css';
import ScoreDistributionModal from "./GradeDistribution";
import UserProfileTeacher from "./UserProfileTeacher";
import Userlayout from "../layouts/Userlayout";
import {updateForum, getAllForums} from '../api';


const userData = {
  name: "kun_teacher",
  role: "Teacher",
  materialsUploaded: 9,
  exercisesUploaded: 12,
  avatar: null,
};

const posts = [
  { id: 1, topic: "Math", title: "Need Help with Calculus Problem - Stuck on Limits and Derivatives", details: "I'm struggling with limits and derivatives in calculus." },
  { id: 2, topic: "Computer Basics", title: "Need Assistance with Basic Computer Operations - New User Struggling", details: "I'm new to computers and need help with basic operations." },
  { id: 3, topic: "Physics", title: "Need Clarification on Newton's Laws", details: "I am confused about how Newton's Laws apply to real-world situations." },
  { id: 4, topic: "Chemistry", title: "Understanding Chemical Bonding", details: "Can someone explain covalent and ionic bonds in detail?" },
  { id: 5, topic: "Biology", title: "Help with Cell Structure", details: "I don't understand the differences between plant and animal cells." }
];

const TeacherOnlineForum = () => {
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [selectedPost, setSelectedPost] = useState(null);
  const [answer, setAnswer] = useState("");
  const [answerError, setAnswerError] = useState("");
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

  const openModal = (post) => {
    setSelectedPost(post);
    setModalIsOpen(true);
    console.log(post.forumId);
    localStorage.setItem('forumId', post.forumId);
    setAnswer(""); 
    setAnswerError(""); 
  };


  const closeModal = () => {
    setModalIsOpen(false);
    setSelectedPost(null);
  };


  const submitAnswer = async(post) => {
    setSelectedPost(post);
    if (!answer.trim()) {
      setAnswerError("Please enter an answer");
      return;
    }
    console.log(post)
 
    console.log("Answer Submitted:", answer);
    await updateForum(post.forumId, post.question, post.aiAnswer, answer);
    closeModal();
  };

  return (
    <Userlayout>
      <Card className="forum-section">
        <h3 className="forum-title">Online Forum</h3>

        {currentPosts.map((Forums) => (
          <div key={Forums.forumId} className="post-item">
            <div className="post-content">
            <p><strong>Question:</strong> {Forums.question}</p>
            </div>
            <Button type="primary" onClick={() => openModal(Forums)}>Answer</Button>
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
        title="Answer Post"
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
            <Input.TextArea
              placeholder="Type your answer here..."
              value={answer}
              onChange={(e) => {
                setAnswer(e.target.value);
                setAnswerError(""); 
              }}
              rows={4}
              className="textarea"
            />
            {answerError && <p className="error-text">{answerError}</p>} 
            <div className="modal-buttons" style={{ marginTop: '10px', textAlign: 'right' }}>
              <Button type="primary" onClick={() => submitAnswer(selectedPost)}>Submit Answer</Button>
              <Button onClick={closeModal}>Close</Button>
            </div>
          </div>
        )}
      </Modal>
    </Userlayout>
  );
};

export default TeacherOnlineForum;
