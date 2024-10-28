import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; 
import { Form, Input, Button, Upload, List, message, Modal, DatePicker } from 'antd'; 
import { UploadOutlined, DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { uploadMaterialFile, uploadExerciseFile, deleteMaterial, deleteExercise, getMaterialsByCourseId, getExercise, getExercisebyCourseId } from '../api';
import '../styles/CreatedCourse.css';
import moment from 'moment'; 

const CreatedCourse = () => {
  const { courseId } = useParams();
  const navigate = useNavigate(); 

  const [learningMaterial, setLearningMaterial] = useState(null); 
  const [assignment, setAssignment] = useState(null);
  const [dueDate, setDueDate] = useState(null);

  const [materials, setMaterials] = useState([]); 
  const [assignments, setAssignments] = useState([]);

  const [isUploadMaterialModalVisible, setIsUploadMaterialModalVisible] = useState(false); 
  const [isUploadAssignmentModalVisible, setIsUploadAssignmentModalVisible] = useState(false); 
  const [materialType, setMaterialType] = useState(''); 
  const [assignmentDescription, setAssignmentDescription] = useState(''); 

  useEffect(() => {
    fetchMaterials();
    fetchAssignments();
  }, [courseId]);

  const fetchMaterials = async () => {
    try {
      const response = await getMaterialsByCourseId(courseId);
      setMaterials(response.data);
    } catch (error) {
      message.error('Failed to fetch learning materials.');
    }
  };

  const fetchAssignments = async () => {
    try {
      const response = await getExercisebyCourseId(courseId); 
      setAssignments(response.data);
    } catch (error) {
      message.error('Failed to fetch assignments.');
    }
  };

  const handleMaterialUpload = async () => {
    if (!learningMaterial) {
      message.error('Please select a file to upload.');
      return;
    }

    const formData = new FormData();
    formData.append('file', learningMaterial);
    formData.append('courseId', courseId);
    formData.append('materialType', materialType); 
    
    try {
      await uploadMaterialFile(formData);
      message.success('Learning material uploaded successfully.');
      fetchMaterials(); 
      setIsUploadMaterialModalVisible(false); 
    } catch (error) {
      message.error('Failed to upload learning material.');
    }
  };

  const handleAssignmentUpload = async () => {
    if (!assignment) {
      message.error('Please select a file to upload.');
      return;
    }

    const formData = new FormData();
    formData.append('file', assignment);
    formData.append('description', assignmentDescription); 
    formData.append('courseId', courseId);
    formData.append('dueDate', dueDate ? dueDate.format('YYYY-MM-DDTHH:mm:ss') : ''); 

    try {
      await uploadExerciseFile(formData); 
      message.success('Assignment uploaded successfully.');
      fetchAssignments(); 
      setIsUploadAssignmentModalVisible(false);
    } catch (error) {
      message.error('Failed to upload assignment.');
    }
  };

  const handleDeleteMaterial = async (materialId) => {
    try {
      await deleteMaterial(courseId, materialId);
      message.success('Learning material deleted successfully!');
      fetchMaterials();
    } catch (error) {
      message.error('Failed to delete learning material.');
    }
  };


  const handleDeleteAssignment = async (assignmentId) => {
    try {
      await deleteExercise(courseId, assignmentId);
      message.success('Assignment deleted successfully!');
      fetchAssignments();
    } catch (error) {
      message.error('Failed to delete assignment.');
    }
  };


  const handleMarkAssignment = (exerciseId) => {
    navigate(`/MarkAssignment/${exerciseId}`);
  };

  return (
    <div className="created-course-container">
      <div className="upload-section">
        <h2>Upload Materials and Assignments</h2>

        {/* Upload Material Button */}
        <Button type="primary" onClick={() => setIsUploadMaterialModalVisible(true)}>
          Upload Learning Material
        </Button>

        {/* Upload Assignment Button */}
        <Button type="primary" style={{ marginLeft: '20px' }} onClick={() => setIsUploadAssignmentModalVisible(true)}>
          Upload Assignment
        </Button>
      </div>

      <div className="display-section">
        <div style={{ marginTop: '40px' }}>
          <h2>Learning Materials</h2>
          <List
            dataSource={materials}
            renderItem={(item) => (
              <List.Item
                actions={[
                  <Button icon={<DeleteOutlined />} type="danger" onClick={() => handleDeleteMaterial(item.materialId)}>Delete</Button>,
                ]}
              >
                {item.materialType}
              </List.Item>
            )}
          />
        </div>

        <div style={{ marginTop: '40px' }}>
          <h2>Assignments</h2>
          <List
            dataSource={assignments}
            renderItem={(item) => (
              <List.Item
                actions={[
                  <Button type="primary" onClick={() => handleMarkAssignment(item.exerciseId)}>Mark</Button>, 
                  <Button icon={<DeleteOutlined />} type="danger" onClick={() => handleDeleteAssignment(item.exerciseId)}>Delete</Button>,
                ]}
              >
                {item.description}
              </List.Item>
            )}
          />
        </div>
      </div>

      <Modal
        title="Upload Learning Material"
        visible={isUploadMaterialModalVisible}
        onOk={handleMaterialUpload}
        onCancel={() => setIsUploadMaterialModalVisible(false)}
        okText="Confirm"
        cancelText="Cancel"
      >
        <Form layout="vertical">
          <Form.Item label="Material Name & Description">
            <Input value={materialType} onChange={(e) => setMaterialType(e.target.value)} placeholder="Enter material type" />
          </Form.Item>
          <Form.Item label="Upload File">
            <Upload
              beforeUpload={(file) => {
                setLearningMaterial(file);
                return false;
              }}
              maxCount={1}
            >
              <Button icon={<UploadOutlined />}>Select File</Button>
            </Upload>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Upload Assignment"
        visible={isUploadAssignmentModalVisible}
        onOk={handleAssignmentUpload}
        onCancel={() => setIsUploadAssignmentModalVisible(false)}
        okText="Confirm"
        cancelText="Cancel"
      >
        <Form layout="vertical">
          <Form.Item label="Assignment Description">
            <Input value={assignmentDescription} onChange={(e) => setAssignmentDescription(e.target.value)} placeholder="Enter assignment description" />
          </Form.Item>
          <Form.Item label="Due Date">
            <DatePicker
              showTime
              value={dueDate}
              onChange={(date) => setDueDate(date)}
              placeholder="Select Due Date"
              format="YYYY-MM-DD HH:mm"
            />
          </Form.Item>
          <Form.Item label="Upload File">
            <Upload
              beforeUpload={(file) => {
                setAssignment(file);
                return false;
              }}
              maxCount={1}
            >
              <Button icon={<UploadOutlined />}>Select File</Button>
            </Upload>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CreatedCourse;
