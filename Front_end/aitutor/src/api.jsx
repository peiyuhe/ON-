import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`, 
  },
});


export const getAllUsers = () => {
  return api.get('/admins');
}

// 课程相关 API

// 获取所有课程
export const getAllCourses = () => {
  return api.get('/courses/all');
};

// 根据 ID 获取课程
export const getCourseById = (courseId) => {
  return api.get(`/courses/${ courseId }`);
};

// 创建课程
export const createCourse = (courseName, teacherId, courseDescription) => {
  return api.post('/courses/create', {
    teacher: {
      teacherId: teacherId
    },
    courseName: courseName,
    courseDescription: courseDescription
  });
};

// 更新课程信息
export const updateCourse = (courseId, teacherId, courseName, courseDescription) => {
  return api.put(`/courses/update/${courseId}`, {
    teacher: {
      teacherId: teacherId
    },
    courseName: courseName,
    courseDescription: courseDescription
  });
};

// 删除课程
export const deleteCourse = (courseId) => {
  return api.delete(`/courses/delete/${courseId}`);
};
//老师相关API
//根据 ID 获取教师信息

export const getTeacherById = (teacherId) => {
  return api.get(`/teachers/${teacherId}`);
};

//删除老师
export const deleteTeacher = (teacherId) => {
  return api.delete(`/teachers/delete/${teacherId}`);
};

export const updateTeacher = (teacherId, teacherDetails) => {
  return api.put(`/teachers/update/${teacherId}`, teacherDetails);
};




// 学生相关 API


//查找学生报名的课程
export const getEnrollmentsByStudentId = (studentId) => {
  return api.get(`/enrollments/student/${studentId}`);
};
// 创建学生
export const createStudent = (facility, major) => {
  return api.post('/students', {
    facility: facility,
    major: major
  });
};

// 根据 ID 获取学生信息
export const getStudentById = (studentId) => {
  return api.get(`/students/${studentId}`);
};

// 更新学生信息
export const updateStudent = (studentId, studentDetails) => {
  return api.put(`/students/update/${studentId}`, studentDetails);
};

// 删除学生
export const deleteStudent = (studentId) => {
  return api.delete(`/students/delete/${studentId}`);
};



export const getAllSubmissionsByStudent = (studentId) => {
  return api.get(`/students/${studentId}/submission`);
};

// 作业和提交相关 API

// 创建作业
// export const createExercise = (courseId, description, filePath) => {
//   return api.post(`/exercises/create`, {
//     course: {
//       courseId: courseId
//     },
//     description: description,
//     filePath: filePath,
//     dueDate: new Date()
//   });
// };
export const createExercise = (courseId, description, filePath) => {
  const currentDate = new Date();
  const randomDays = Math.floor(Math.random() * 10); // Generates a random number between 0 and 9
  const dueDate = new Date(currentDate.setDate(currentDate.getDate() + 7 + randomDays)); // Adds 7 days and randomDays to the current date
  
  return api.post(`/exercises/create`, {
    course: {
      courseId: courseId
    },
    description: description,
    filePath: filePath,
    dueDate: dueDate
  });
};

// 更新作业
export const updateExercise = (courseId, exerciseId, description, filePath, dueDate) => {
  return api.put(`/exercises/${exerciseId}`, {
    description: description,
    filePath: filePath,
    dueDate: dueDate
  });
};

// 删除作业
export const deleteExercise = (courseId, exerciseId) => {
  return api.delete(`/exercises/delete/${exerciseId}`);
};

// 获取作业
export const getExercise = (exerciseId) => {
  return api.get(`exercises/${ exerciseId } `);
};

export const getExercisebyCourseId = (courseId) => {
  return api.get(`/exercises/course/${courseId}`);
};

export const getAllSubmissionsByCourse = (courseId, exerciseId) => {
  return api.get(`/exercises/{exerciseId}/submission`);
};
// 创建提交
export const createSubmission = (courseId, exercisesId, studentId, feedback, filePath) => {
  return api.post(`/submission/create`, {
    student: {
      studentId: studentId
    },
    feedback: feedback,
    filePath: filePath,
    submittedAt: new Date(),
    graded: false
  });
};

// 更新提交
export const updateSubmission = (submissionId, submissionDetails) => {
  return api.put(`/submission/${submissionId}`, submissionDetails);
};

export const getUnmarkedAssignments = (exerciseId) => {
  return api.get(`submission/exercise/${exerciseId}`);
};

// 论坛相关 API

// 创建论坛问题
export const PostQuestion = (studentId, courseId, question) => {
  return api.post('/forums/post-question',
    {
      studentId: 2,
      courseId: 1,
      question: question
    });
}

export const createForum = (studentId, courseId, question, aiAnswer, teacherAnswer) => {
  return api.post('/forums/create', {
    student: {
      studentId: studentId
    },
    course: {
      courseId: courseId
    },
    question: question,
    aiAnswer: aiAnswer,
    teacherAnswer: teacherAnswer,
    postedAt: new Date()
  });
};

// 获取所有论坛帖子
export const getAllForums = () => {
  return api.get(`/forums/get/all`);
};

// 根据 ID 获取论坛帖子
export const getForumById = (forumId) => {
  return api.get(`/forums/${forumId}`);
};

// 更新论坛帖子
export const updateForum = (forumId, question, aiAnswer, teacherAnswer) => {
  return api.put(`/forums/update/${forumId}`, {
    question: question,
    aiAnswer: aiAnswer,
    teacherAnswer: teacherAnswer,
    answeredAt: new Date()
  });
};

// 课程资料相关 API

// 创建课程资料
export const createMaterial = (courseId, materialType, filePath) => {
  return api.post(`/materials/create`, {
    course: {
      courseId: courseId
    },
    materialType: materialType,
    filePath: filePath,
    uploadedAt: new Date()
  });
};

// 更新课程资料
export const updateMaterial = (courseId, materialId, materialType, filePath) => {
  return api.put(`/materials/${materialId}`, {
    materialType: materialType,
    filePath: filePath,
    uploadedAt: new Date()
  });
};

// 删除课程资料
export const deleteMaterial = (courseId, materialId) => {
  return api.delete(`/materials/delete/${materialId}`);
};

// 根据 ID 获取课程资料
export const getMaterial = (materialId) => {
  return api.get(`materials/${materialId}`);
};

export const getMaterialsByCourseId = (courseId) => {
  return api.get(`materials/course/${courseId}`);
};

export const createAdmin = (username, password) => {
  return api.post('/admins/create', {
    username: username,
    password: password
  });
};

// 根据ID获取管理员
export const getAdminById = (adminId) => {
  return api.get(`/admins/${adminId}`);
};

// 更新管理员信息
export const updateAdmin = (adminId, username, password) => {
  return api.put(`/admins/update/${adminId}`, {
    username: username,
    password: password
  });
};

// 删除管理员
export const deleteAdmin = (adminId) => {
  return api.delete(`/admins/delete/${adminId}`);
};

export const generateTemporaryAvatar = (keyword) => {
  return api.post('/user/avatar/generate', null, {
    params: { keyword: keyword }
  });
};

// 保存头像
export const saveAvatar = (userId, imageUrl, keyword) => {
  return api.post('/user/avatar/save', null, {
    params: {
      userId: userId,
      imageUrl: imageUrl,
      keyword: keyword
    }
  });
};

// 查看用户头像
export const viewAvatar = (userId) => {
  return api.get(`/user/avatar/view/${userId}`);
};

// 删除用户头像
export const deleteAvatar = (userId) => {
  return api.delete(`/user/avatar/delete/${userId}`);
};

// 创建报名信息
export const createEnrollment = (courseId, studentId) => {
  return api.post('/enrollments/create', {
    course: {
      courseId: courseId
    },
    student: {
      studentId: studentId
    }
  });
};

// 更新报名信息
export const updateEnrollment = (courseId, studentId, enrollmentId, enrollmentDetails) => {
  return api.put(`/enrollments/${enrollmentId}`, {
    course: {
      courseId: courseId
    },
    student: {
      studentId: studentId
    }
  });
};

// 删除报名信息
export const deleteEnrollment = (enrollmentId) => {
  return api.delete(`/enrollments/delete/${enrollmentId}`);
};

// 根据ID获取单条报名信息
export const getEnrollment = (enrollmentId) => {
  return api.get(`/enrollments/${enrollmentId}`);
};

// 根据学生ID获取报名列表
export const getEnrollmentsByStudent = (studentId) => {
  return api.get(`/enrollments/student/${studentId}`);
};

// 创建学习计划
export const createLearningPlan = (learningPlan) => {
  return api.post('/learningPlans/create', learningPlan);
};

// 更新学习计划
export const updateLearningPlan = (planId, planDetails) => {
  return api.put(`/learningPlans/update/${planId}`, planDetails);
};

// 删除学习计划
export const deleteLearningPlan = (planId) => {
  return api.delete(`/learningPlans/delete/${planId}`);
};

// 获取学习计划
export const getLearningPlan = (planId) => {
  return api.get('/learningPlans/', {
    params: { planId: planId }
  });
};

export const getLearningPlansByStudentId = (studentId) => {
  return api.get(`/learningPlans/student/${studentId}`);
};

// 生成最近的学习报告
export const generateRecentLearningReport = (studentId) => {
  return api.post('/learning-report/generate-recent-report', null, {
    params: { studentId: studentId }
  });
};

// 根据课程生成学习报告
export const generateReportByCourse = (studentId, courseId) => {
  return api.post('/learning-report/generate-by-course', {
    studentId: studentId,
    courseId: courseId
  });
};

// 根据练习生成学习报告
export const generateReportByExercise = (studentId, exerciseId) => {
  return api.post('/learning-report/generate-by-exercise', {
    studentId: studentId,
    exerciseId: exerciseId
  });
};

// .requestMatchers("/learning-report/{studentId}").hasAuthority("STUDENT")
// 查看学生的学习报告
export const viewReport = (studentId) => {
  return api.get(`/learning-report/view-report/${studentId}`);
};

// 删除学生的学习报告
export const deleteReportByStudentId = (studentId) => {
  return api.delete(`/learning-report/delete-report/${studentId}`);
};

//.requestMatchers("/learning-report/translate-report").hasAuthority("STUDENT")
// @PostMapping("/translate-report")
//     public ResponseEntity<String> translateReport(@RequestBody TranslateReportRequestDTO translateReportRequestDTO) 
// public class TranslateReportRequestDTO {
//   // Getters and setters
//   private Long reportId;
//   private String targetLanguage;

// }
export const translateReport = (translateReportRequestDTO) => {
  return api.post('/learning-report/translate-report', translateReportRequestDTO);
};



// 检查是否设置了安全问题
export const checkSecurityQuestion = (userId) => {
  return api.get(`/user/check-security/${userId}`);
};

// 设置安全问题
export const setSecurityQuestion = (securitySetupDTO) => {
  return api.post('/user/set-security-question', securitySetupDTO);
};

// 更新安全问题
export const updateSecurityQuestion = (userId, securitySetupDTO) => {
  return api.put(`/user/update-security-question/${userId}`, securitySetupDTO);
};

// 删除安全问题
export const deleteSecurityQuestion = (userId) => {
  return api.delete(`/user/delete-security-question/${userId}`);
};

// 上传头像
export const uploadAvatar = (userId,file) => {

  const formData = new FormData();
  // formData.append("userId",userId);
  formData.append("file",file);
  return api.put(`/user/update-avatar/${userId}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

// 更新头像
export const updateAvatar = (userId, file) => {
  const formData = new FormData();
  formData.append('file', file);

  return api.put(`/user/update-avatar/${userId}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};


// 获取头像URL
export const getAvatar = (userId) => {
  return api.get(`/user/get-avatar/${userId}`);
};
//删除用户
export const deleteUser = (studentId) => {
  return api.delete(`/student/delete/${studentId}`);
};


// 根据 teacherId 获取教师的课程
export const getCoursesByTeacherId = (teacherId) => {
  return api.get(`/courses/teacher/${teacherId}`);
};

// 根据 exerciseId 获取某个作业的提交情况
export const getSubmissionsByExerciseId = (exerciseId) => {
  return api.get(`/files/submissions/${exerciseId}`);
};

// 根据 studentId 获取某个学生的提交情况
export const getSubmissionsByStudentId = (studentId) => {
  return api.get(`/submission/student/${studentId}`);
};


//File upload and download part
// GET request to fetch a file by filename
export const getFileByFilename = (filename) => {
  // This function fetches a file from the server using the filename
  return api.get(`/files/download/${filename}`);
};

// POST request to upload a material file
export const uploadMaterialFile = (fileData) => {
  // This function uploads a material file to the server
  // The fileData is expected to be a FormData object containing the file
  return api.post('/files/upload/material', fileData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

// POST request to upload an exercise file
export const uploadExerciseFile = (fileData) => {
  // This function uploads an exercise file to the server
  // The fileData is expected to be a FormData object containing the file
  return api.post('/files/upload/exercise', fileData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

// POST request to upload a submission file
export const uploadSubmissionFile = (fileData) => {
  // This function uploads a submission file to the server
  // The fileData is expected to be a FormData object containing the file
  return api.post('/files/upload/submission', fileData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};





