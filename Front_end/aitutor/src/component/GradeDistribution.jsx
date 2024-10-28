
import React, { useState } from 'react';
import { Modal, Button, Radio } from 'antd';
import { Bar, Pie } from 'react-chartjs-2';
import 'chart.js/auto'; 

const ScoreDistributionModal = () => {
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [chartType, setChartType] = useState('bar'); 

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const handleChartChange = (e) => {
    setChartType(e.target.value);
  };


  const data = {
    labels: ['0-20', '21-40', '41-60', '61-80', '81-100'],
    datasets: [
      {
        label: 'Score Distribution',
        data: [5, 10, 20, 30, 35], 
        backgroundColor: [
          'rgba(75, 192, 192, 0.6)',
          'rgba(255, 99, 132, 0.6)',
          'rgba(54, 162, 235, 0.6)',
          'rgba(255, 206, 86, 0.6)',
          'rgba(153, 102, 255, 0.6)',
        ],
      },
    ],
  };

  return (
    <>
      <Button type="primary" onClick={showModal}>
        Show Score Distribution
      </Button>
      <Modal
        title="Score Distribution"
        visible={isModalVisible}
        onCancel={handleCancel}
        footer={null}
        width={600}
        bodyStyle={{ padding: '20px' }}
      >

        <Radio.Group value={chartType} onChange={handleChartChange} style={{ marginBottom: '20px' }}>
          <Radio.Button value="bar">Bar Chart</Radio.Button>
          <Radio.Button value="pie">Pie Chart</Radio.Button>
        </Radio.Group>


        {chartType === 'bar' ? <Bar data={data} /> : <Pie data={data} />}
      </Modal>
    </>
  );
};

export default ScoreDistributionModal;
