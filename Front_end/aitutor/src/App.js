import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Signup from './pages/users/Signup';
import Admin from './pages/administrator/Admin';
import CoursesCard from './component/CoursesCard';
import AIAssistant from './component/AIAssistant'; // 导入 AIAssistant 组件
import StudentDashboard from './pages/users/StudentDashboard';
import TeacherDashboard from './pages/users/TeacherDashboard';
import PlanGeneration from './pages/users/PlanGeneration';
import ReportGeneration from './pages/users/ReportGeneration';
import CourseDetail from './component/CourseDetail'; // 导入课程详情组件
import Commonlayout from './layouts/Commonlayouts';
import AppTest from './component/test';
import StudentOnlineForum from './component/OnlineForumStudent';
import TeacherOnlineForum from './component/OnlineForumTeacher';
import GenerateAvatar from './component/AvatarGeneration';
import StudentLearningReport from './pages/users/ReportGeneration';
import StudentLearningPlan from './pages/users/PlanGeneration';
import Homepage from './pages/users/Homepage';
import Enrollment from './component/Erollment';
import ChangeProfileStudent from './component/ChangeProfileStudent';
import GradeDistribution from './component/GradeDistribution';
import ChangeProfileTeacher from './component/ChangeProfileTeacher';
import CreateCourse from './component/CreateCourse';
import EnrolledCourse from './component/EnrolledCourse';
import CreatedCourse from './component/CreatedCourse';
import AssignmentDetail from './component/AssignmentDetail';
import MarkAssignment from './component/MarkAssignment';
import './styles/TabSwitcher.css'; // 导入 TabSwitcher 的样式文件
import './styles/AIAssistant.css'; // 导入 AIAssistant 的样式文件
import './styles/Signup.css';
import './styles/student.css';
import './styles/CoursesCard.css';  // 导入CoursesCard 样式文件
import './styles/StudentDashboard.css';
import './styles/TeacherDashboard.css';



function App() {
  return (
    <Router>
      <Commonlayout>
        <div className="App">
          {/* 路由配置 */}
          <Routes>
            <Route path="/" element={<AppTest />} />
            <Route path='/Enrollment' element={<Enrollment />} />
            <Route path="/admin" element={<Admin />} />
            <Route path="/StudentDashboard" element={<StudentDashboard />} />
            <Route path="/TeacherDashboard" element={<TeacherDashboard />} />
            <Route path="/CreateCourse" element={<CreateCourse />} />
            <Route path="/course/:courseId" element={<CourseDetail />} />
            <Route path="/CreatedCourse/:courseId" element={<CreatedCourse />} />
            <Route path="/studentonlineforum" element={<StudentOnlineForum />} />
            <Route path="/teacheronlineforum" element={<TeacherOnlineForum />} />
            <Route path="/generateavatar" element={<GenerateAvatar />} />
            <Route path="/studentlearningplan" element={<StudentLearningPlan />} />
            <Route path="/studentlearningReport" element={<StudentLearningReport />} />
            <Route path="/plangeneration" element={<PlanGeneration />} />
            <Route path="/homepage" element={<Homepage />} />
            <Route path="/reportgeneration" element={<ReportGeneration />} />
            <Route path="/changeprofilestudent" element={<ChangeProfileStudent />} />
            <Route path="/changeprofileteacher" element={<ChangeProfileTeacher />} />
            <Route path='/EnrolledCourse/:courseId' element={<EnrolledCourse/>}/>
            <Route path="/gradedistribution" element={<GradeDistribution />} />
            <Route path="/avatarsgeneration" element={<GenerateAvatar />} />
            <Route path="/onlineforumteacher" element={<TeacherOnlineForum />} />
            <Route path="/assignmentdetail/:exerciseId" element={<AssignmentDetail />} />
            <Route path="/MarkAssignment/:exerciseId" element={<MarkAssignment />} />
          </Routes>

        </div>
      </Commonlayout>

    </Router>

  );
}

export default App;
