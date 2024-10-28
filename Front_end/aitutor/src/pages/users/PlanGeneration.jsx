
import React, { useState, useEffect } from 'react';
import { Card, Button, message, Form, Input, Checkbox, Modal, Select, DatePicker } from 'antd';
import Userlayout from '../../layouts/Userlayout';
import '../../styles/StudentPanel.css';
import {
  getLearningPlansByStudentId,
  updateLearningPlan,
  createLearningPlan
} from '../../api';

const { Option } = Select;
const { RangePicker } = DatePicker;

const StudentLearningPlan = () => {
  const [learningPlans, setLearningPlans] = useState([]); 
  const [selectedPlan, setSelectedPlan] = useState(null); 
  const [planDetails, setPlanDetails] = useState(''); 
  const [completionStatus, setCompletionStatus] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false); 
  const [newPlan, setNewPlan] = useState({}); 

  const studentId = localStorage.getItem('Id'); 


  useEffect(() => {
    const fetchLearningPlans = async () => {
      if (!studentId) {
        message.error('No student ID found in local storage.');
        return;
      }

      try {
        const response = await getLearningPlansByStudentId(studentId);
        setLearningPlans(response.data);
      } catch (error) {
        console.error(error);
        message.error('Failed to load learning plans.');
      }
    };

    fetchLearningPlans();
  }, [studentId]);


  const handleSelectPlan = (planId) => {
    const plan = learningPlans.find((p) => p.planId === planId);
    if (plan) {
      setSelectedPlan(plan);
      setPlanDetails(plan.planDetails || '');
      setCompletionStatus(plan.completionStatus || false);
    }
  };


  const getSystemGeneratedDates = () => {
    const startDate = new Date();
    const randomDays = Math.floor(Math.random() * 10) + 1; 
    const endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + randomDays);


    const formatDate = (date) => date.toISOString().split('T')[0];
    return { startDate: formatDate(startDate), endDate: formatDate(endDate) };
  };


  const handleUpdatePlan = async () => {
    if (!selectedPlan) return;

    const { startDate, endDate } = getSystemGeneratedDates();

    const updatedPlan = {
      studentId, 
      planDetails,
      startDate,
      endDate,
      completionStatus
    };

    try {
      await updateLearningPlan(selectedPlan.planId, updatedPlan);
      message.success('Learning plan updated successfully!');
      setLearningPlans((prevPlans) =>
        prevPlans.map((plan) => (plan.planId === selectedPlan.planId ? { ...plan, ...updatedPlan } : plan))
      );
    } catch (error) {
      console.error(error);
      message.error('Failed to update learning plan.');
    }
  };


  const showNewPlanModal = () => {
    setIsModalVisible(true);
  };


  const handleCancel = () => {
    setIsModalVisible(false);
    setNewPlan({});
  };


  const handleCreatePlan = async () => {
    const { startDate, endDate } = getSystemGeneratedDates();

    const newPlanData = {
      student: { studentId: studentId },
      planDetails: newPlan.planDetails,
      startDate: startDate,
      endDate: endDate,
      completionStatus: newPlan.completionStatus || false
    };

    try {
      const response = await createLearningPlan(newPlanData);
      console.log(response);
      setLearningPlans([...learningPlans, response.data]);
      message.success('Learning plan created successfully!');
      setIsModalVisible(false);
      setNewPlan({});
    } catch (error) {
      console.error(error);
      message.error('Failed to create learning plan.');
    }
  };

  return (
    <Userlayout>
      <Card className="plan-section">
        <h3>Your Learning Plans</h3>


        <Button type="primary" onClick={showNewPlanModal} style={{ marginBottom: '20px' }}>
          Create New Learning Plan
        </Button>


        <Form layout="vertical">
          <Form.Item label="Select Learning Plan">
            <Select placeholder="Select a plan" onChange={handleSelectPlan} style={{ width: '100%' }}>
              {learningPlans.map((plan) => (
                <Option key={plan.planId} value={plan.planId}>
                  {`Plan #${plan.planId} - ${plan.startDate} to ${plan.endDate}`}
                </Option>
              ))}
            </Select>
          </Form.Item>

          {selectedPlan && (
            <>
              <Form.Item label="Plan Details">
                <Input.TextArea
                  rows={4}
                  value={planDetails}
                  onChange={(e) => setPlanDetails(e.target.value)}
                />
              </Form.Item>

              <Form.Item>
                <Checkbox
                  checked={completionStatus}
                  onChange={(e) => setCompletionStatus(e.target.checked)}
                >
                  Mark as Completed
                </Checkbox>
              </Form.Item>

              <Button type="primary" onClick={handleUpdatePlan}>
                Update Plan
              </Button>
            </>
          )}
        </Form>
      </Card>

      {selectedPlan && (
        <Card className="plan-content" style={{ marginTop: '20px' }}>
          <h4>Selected Plan Details</h4>
          <p><strong>Details:</strong> {planDetails}</p>
          <p><strong>Start Date:</strong> {selectedPlan.startDate}</p>
          <p><strong>End Date:</strong> {selectedPlan.endDate}</p>
          <p><strong>Completion Status:</strong> {completionStatus ? 'Completed' : 'Not Completed'}</p>
        </Card>
      )}


      <Modal
        title="Create New Learning Plan"
        visible={isModalVisible}
        onOk={handleCreatePlan}
        onCancel={handleCancel}
        okText="Create"
      >
        <Form layout="vertical">
          <Form.Item label="Plan Details">
            <Input.TextArea
              rows={4}
              value={newPlan.planDetails}
              onChange={(e) => setNewPlan({ ...newPlan, planDetails: e.target.value })}
              placeholder="Enter plan details"
            />
          </Form.Item>

          <Form.Item label="Date Range">
             <RangePicker
               onChange={(dates) =>
                 setNewPlan({
                   ...newPlan,
                   startDate: dates ? dates[0].format('YYYY-MM-DD') : null,
                   endDate: dates ? dates[1].format('YYYY-MM-DD') : null
                 })
               }
             />
           </Form.Item>

          <Form.Item>
            <Checkbox
              checked={newPlan.completionStatus || false}
              onChange={(e) => setNewPlan({ ...newPlan, completionStatus: e.target.checked })}
            >
              Mark as Completed
            </Checkbox>
          </Form.Item>
        </Form>
      </Modal>
    </Userlayout>
  );
};

export default StudentLearningPlan;
