import React, { useState, useEffect } from 'react';
import { Card, Button, Space, Typography } from 'antd';
import { useParams } from 'react-router-dom';
// import { getMaterialsByCourseId } from '../api';
import Pagination from './Pagination';
import '../styles/LearningMaterialCard.css';
import { getMaterial, getMaterialsByCourseId } from '../api';
import { downloadFileByFilename } from '../downloadapi';

const { Meta } = Card;
const { Text, Title } = Typography;


const LearningMaterialCard = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [materials, setMaterials] = useState([]);
  const pageSize = 4;
  const { courseId } = useParams();

  const indexOfLastMaterial = currentPage * pageSize;
  const indexOfFirstMaterial = indexOfLastMaterial - pageSize;
  const currentMaterials = Array.isArray(materials)
    ? materials.slice(indexOfFirstMaterial, indexOfLastMaterial)
    : [];

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    const fetchMaterials = async () => {
      try {
        console.log(courseId);
        const response = await getMaterialsByCourseId(courseId);
        console.log("response");
        console.log(response);
        const materialsList = response.data;
        setMaterials(Array.isArray(materialsList) ? materialsList : []);
      } catch (err) {
        console.error("Failed to fetch materials:", err);
        setMaterials([]);
      }
    };
    fetchMaterials();
  }, [courseId]);

  const handleDownload = async (exerciseId) => {
    try {
      console.log(exerciseId);
      const exerciseresponse = await getMaterial(exerciseId);
      console.log(exerciseresponse.data);
      const newFilePath = exerciseresponse.data.filePath.replace(/^src[\\/]+main[\\/]+resources[\\/]+static[\\/]+files[\\/]/, '').replace(/[\\]+/g, '/');
      console.log(newFilePath);
      const response = await downloadFileByFilename(newFilePath);
      console.log(response.headers.getContentType());
      const blob = new Blob([response.data], { type: response.headers.getContentType() });
      const url = window.URL.createObjectURL(blob);

      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', newFilePath.split('/').pop());
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  };

  return (
    <div className="materials-wrapper">
      {/* <Title level={2} style={{ textAlign: 'center', marginBottom: '20px' }}>Learning Materials</Title> */}
      <div className="materials-container">
        <Space direction="horizontal" wrap size="large" style={{ display: 'flex', justifyContent: 'center' }}>
          {currentMaterials.length > 0 ? (
            currentMaterials.map((material, index) => (
              <Card
                key={index}
                hoverable
                style={{ width: 300, borderRadius: '10px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)' }}

                actions={[
                  <Button type="primary" onClick={() => handleDownload(material.materialId)}>Download Materials</Button>
                ]}
              >
                <Meta
                  title={<Title level={4} style={{ margin: 0 }}>{material.materialType || 'Untitled'}</Title>}
                />
              </Card>
            ))
          ) : (
            <Text>No materials available for this course.</Text>
          )}
        </Space>
      </div>

      <Pagination
        current={currentPage}
        pageSize={pageSize}
        total={materials.length}
        onChange={handlePageChange}
        showSizeChanger={false}
        style={{ marginTop: '20px', textAlign: 'center' }}
      />
    </div>
  );
};

export default LearningMaterialCard;
