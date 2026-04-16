import React from "react";
import { Container, Card, Row, Col, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const Profile = () => {
    const navigate = useNavigate();
    
    const user = JSON.parse(localStorage.getItem("user"));

    const handleLogout = () => {
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        navigate("/login");
    };

    if (!user) {
        return <h3 className="text-center mt-5 text-danger">Vui lòng đăng nhập!</h3>;
    }

    return (
        <Container className="mt-5" style={{ maxWidth: "700px" }}>
            <Card className="shadow-sm">
                <Card.Header className="bg-primary text-white text-center">
                    <h4>THÔNG TIN TÀI KHOẢN</h4>
                </Card.Header>
                <Card.Body>
                    <Row className="align-items-center">
                        <Col md={4} className="text-center mb-3">
                            <img 
                                src={user.avatar || "https://res.cloudinary.com/dtlq778r7/image/upload/v1727715421/defau_c0pzzn.png"} 
                                alt="Avatar" 
                                className="img-thumbnail rounded-circle"
                                style={{ width: "150px", height: "150px", objectFit: "cover" }}
                            />
                            <h5 className="mt-3 text-primary">{user.username}</h5>
                            <span className="badge bg-info">{user.role}</span>
                        </Col>
                        
                        <Col md={8}>
                            <table className="table table-borderless">
                                <tbody>
                                    <tr>
                                        <th>Họ và Tên:</th>
                                        <td>{user.fullName}</td>
                                    </tr>
                                    <tr>
                                        <th>Email:</th>
                                        <td>{user.email}</td>
                                    </tr>
                                    <tr>
                                        <th>Số điện thoại:</th>
                                        <td>{user.phone}</td>
                                    </tr>
                                    <tr>
                                        <th>Giới tính:</th>
                                        <td>{user.gender === 'male' ? 'Nam' : user.gender === 'female' ? 'Nữ' : 'Khác'}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </Col>
                    </Row>
                </Card.Body>
                <Card.Footer className="text-end">
                    <Button variant="danger" onClick={handleLogout}>Đăng Xuất</Button>
                </Card.Footer>
            </Card>
        </Container>
    );
};

export default Profile;