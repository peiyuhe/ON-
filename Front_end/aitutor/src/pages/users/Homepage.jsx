import React, { useEffect, useState } from "react";
import { Card, Carousel, Button, List } from "antd";
import { useNavigate } from 'react-router-dom';
import T1 from '../../imge/T1.png';
import T2 from '../../imge/T2.png';
import T3 from '../../imge/T3.png';
import '../../styles/Homepage.css';

const universities = [
  { name: "Peking University", url: "https://www.pku.edu.cn/" },
  { name: "Zhejiang University", url: "http://www.zju.edu.cn/" },
  { name: "Nanjing University", url: "https://www.nju.edu.cn/" },
  { name: "Wuhan University", url: "https://www.whu.edu.cn/" },
  { name: "Tsinghua University", url: "https://www.tsinghua.edu.cn/" }
];

const Homepage = () => {
  const [role, setRole] = useState("");
  const [username, setName] = useState("");
  const [avatar, setAvatar] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const storedRole = localStorage.getItem('role');
    const storedName = localStorage.getItem('username');
    const storedAvatar = localStorage.getItem('avatar');
    setRole(storedRole);
    setName(storedName);
    setAvatar(storedAvatar);
  }, []);

  const handleDashboardNavigation = () => {
    if (role === "STUDENT") {
      navigate('/StudentDashboard');
    } else if (role === "TEACHER") {
      navigate('/TeacherDashboard');
    }
  };

  const openUniversityWebsite = (url) => {
    window.open(url, '_blank');
  };

  return (
      <div className="homepage">
        <div className="container">

          <div className="universities">
            {/*<h>University</h>*/}
            <h3 className="universities-title">Partner Universities</h3>
            <List
                size="small"
                // bordered
                dataSource={universities}
                renderItem={(university) => (
                    <List.Item>
                      <Button type="text" onClick={() => openUniversityWebsite(university.url)}>
                        {university.name}
                      </Button>
                    </List.Item>
                )}
            />
          </div>


          <div className="carousel-and-user">
            <div className="carousel-section">
              <Carousel autoplay arrows={true}>
                <div>
                  <img src={T1} alt="slide1" className="slide-image" />
                </div>
                <div>
                  <img src={T2} alt="slide2" className="slide-image" />
                </div>
                <div>
                  <img src={T3} alt="slide3" className="slide-image" />
                </div>
              </Carousel>
            </div>

            <div className="user-info">
              <Card className="user-info-card">
                <div className="avatar">
                <img src={avatar || 'https://via.placeholder.com/50'}  className="avatar-image" />
                </div>
                <h3>{username}</h3>
                <p>{role}</p>
                {role === "STUDENT" ? (
                    <Button type="primary" onClick={handleDashboardNavigation}>Student Dashboard</Button>
                ) : role === "TEACHER" ? (
                    <Button type="primary" onClick={handleDashboardNavigation}>Teacher Dashboard</Button>
                ) : (
                    <Button disabled>No Role Found</Button>
                )}
              </Card>
            </div>

          </div>
        </div>
      </div>
  );
};

export default Homepage;
