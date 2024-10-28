import React, { useState, useEffect } from 'react';
import UserCard from './UserCard'; 
import { Pagination, message, Modal } from 'antd'; 
import { getAllTeachers, getAllStudents } from "../AdminApi"; 
import { deleteStudent, deleteTeacher } from "../api"; 

const TabSwitcherUser = ({ searchText }) => {
  const [activeTab, setActiveTab] = useState('Student'); 
  const [users, setUsers] = useState([]); 
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null); 
  const [currentPage, setCurrentPage] = useState(1); 
  const [pageSize, setPageSize] = useState(5); 
  const [isModalVisible, setIsModalVisible] = useState(false); 
  const [userToDelete, setUserToDelete] = useState(""); 

  useEffect(() => {
    const fetchUsers = async () => {
      setLoading(true);
      try {
        let response;
        if (activeTab === 'Student') {
          response = await getAllStudents();
        } else {
          response = await getAllTeachers();
        }
        setUsers(response.data);
        console.log(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch users from the server.');
        setLoading(false);
      }
    };
    fetchUsers();
  }, [activeTab]); 


  const handleDeleteUser = async () => {
    try {
      console.log("this is userToDelete");
      console.log(userToDelete);
      if (userToDelete.role === 'STUDENT') {

        await deleteStudent(userToDelete.student_id); 
        setUsers(users.filter(user => user.student_id !== userToDelete.student_id)); 
      } else {
        await deleteTeacher(userToDelete.teacher_id); 
        setUsers(users.filter(user => user.teacher_id !== userToDelete.teacher_id));
      }
      message.success('User deleted successfully');
      setIsModalVisible(false);
    } catch (error) {
      message.error('Failed to delete user.');
    }
  };


  const showDeleteModal = (user) => {
    setUserToDelete(user); 
    setIsModalVisible(true); 
  };


  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const filteredUsers = users.filter(user => {
    const matchesSearch = user.username.toLowerCase().includes(searchText.toLowerCase());
    return matchesSearch;
  });

  const indexOfLastUser = currentPage * pageSize;
  const indexOfFirstUser = indexOfLastUser - pageSize;
  const currentUsers = filteredUsers.slice(indexOfFirstUser, indexOfLastUser); 

  const handlePageChange = (page, newPageSize) => {
    setCurrentPage(page);
    if (newPageSize !== pageSize) {
      setPageSize(newPageSize);
    }
  };

  return (
    <div className="tab-switcher">

      <div className="tab-buttons">
        <button
          className={`tab-button ${activeTab === 'Student' ? 'active' : ''}`}
          onClick={() => setActiveTab('Student')}
        >
          Student
        </button>
        <button
          className={`tab-button ${activeTab === 'Teacher' ? 'active' : ''}`}
          onClick={() => setActiveTab('Teacher')}
        >
          Teacher
        </button>
      </div>


      {loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p>{error}</p>
      ) : (
        <div className="tab-content">

          {currentUsers.length > 0 ? (
            currentUsers.map(user => (
              <UserCard
                key={user.user_Id}
                username={user.username}
                role={user.role}
                avatar={user.avatar}
                user_Id={user.user_Id}
                onDelete={() => showDeleteModal(user)} 
              />
            ))
          ) : (
            <p>No users found for {activeTab}.</p>
          )}
        </div>
      )}

      <footer className="pagination">
        <Pagination
          current={currentPage}
          pageSize={pageSize}
          total={filteredUsers.length}
          onChange={handlePageChange}
          showSizeChanger={true}
          pageSizeOptions={['5', '10', '20']}
          onShowSizeChange={handlePageChange}
        />
      </footer>

      <Modal
        title="Confirm Delete"
        visible={isModalVisible}
        onOk={handleDeleteUser}
        onCancel={handleCancel}
        okText="Confirm"
        cancelText="Cancel"
      >
        <p>Are you sure you want to delete this user?</p>
      </Modal>
    </div>
  );
};

export default TabSwitcherUser;
