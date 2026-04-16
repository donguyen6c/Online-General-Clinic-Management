import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Container, Form, Button, Alert, Row, Col } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";

const Register = () => {
    const [user, setUser] = useState({
        fullName: "",
        email: "",
        phone: "",
        gender: "male",
        username: "",
        password: ""
    });
    const [avatar, setAvatar] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const change = (e, field) => {
        setUser({ ...user, [field]: e.target.value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        setError("");

        if (!avatar) {
            setError("Vui lòng chọn ảnh đại diện!");
            return;
        }

        try {
            setLoading(true);
            let form = new FormData();
            for (let key in user) {
                form.append(key, user[key]);
            }
            form.append("avatar", avatar);

            await Apis.post(endpoints['users'], form, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            });

            alert("Đăng ký thành công! Vui lòng đăng nhập.");
            navigate("/login");
        } catch (ex) {
            console.error(ex);
            setError("Hệ thống đang bận hoặc tên đăng nhập/email đã tồn tại.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: "600px" }}>
            <h2 className="text-center text-primary mb-4">ĐĂNG KÝ TÀI KHOẢN</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            
            <Form onSubmit={handleRegister} className="border p-4 shadow-sm rounded bg-white">
                <Form.Group className="mb-3">
                    <Form.Label>Họ và Tên</Form.Label>
                    <Form.Control type="text" value={user.fullName} onChange={(e) => change(e, "fullName")} required />
                </Form.Group>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên đăng nhập</Form.Label>
                            <Form.Control type="text" value={user.username} onChange={(e) => change(e, "username")} required />
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Mật khẩu</Form.Label>
                            <Form.Control type="password" value={user.password} onChange={(e) => change(e, "password")} required />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" value={user.email} onChange={(e) => change(e, "email")} required />
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Số điện thoại</Form.Label>
                            <Form.Control type="text" value={user.phone} onChange={(e) => change(e, "phone")} required />
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Giới tính</Form.Label>
                            <Form.Select value={user.gender} onChange={(e) => change(e, "gender")}>
                                <option value="male">Nam</option>
                                <option value="female">Nữ</option>
                                <option value="other">Khác</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Ảnh đại diện</Form.Label>
                            <Form.Control type="file" accept="image/*" onChange={(e) => setAvatar(e.target.files[0])} required />
                        </Form.Group>
                    </Col>
                </Row>

                <div className="d-grid mt-4">
                    <Button variant="primary" type="submit" disabled={loading}>
                        {loading ? "Đang xử lý..." : "Đăng Ký"}
                    </Button>
                </div>
                <div className="text-center mt-3">
                    Đã có tài khoản? <Link to="/login">Đăng nhập ngay</Link>
                </div>
            </Form>
        </Container>
    );
};

export default Register;