import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Container, Form, Button, Alert } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            const res = await Apis.post(endpoints['login'], {
                username: username,
                password: password
            });
            
            const token = res.data.token;
            localStorage.setItem("token", token);

            const profileRes = await Apis.get(endpoints['profile'], {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            const userData = profileRes.data;

            localStorage.setItem("user", JSON.stringify(profileRes.data));

            window.dispatchEvent(new Event("authChange"));

            switch (userData.role) {
                case 'admin':
                    navigate("/admin");
                    break;
                case 'doctor':
                    navigate("/doctor");
                    break;
                case 'pharmacist':
                    navigate("/pharmacist");
                    break;
                case 'patient':
                    navigate("/patient");
                    break;
                default:
                    navigate("/profile");
                    break;
                }
        } catch (ex) {
            console.error(ex);
            setError("Sai tên đăng nhập hoặc mật khẩu!");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (user) {
        switch (user.role) {
            case 'admin': return navigate("/");
            case 'doctor': return navigate("/doctor");
            case 'pharmacist': return navigate("/pharmacist");
            case 'patient': return navigate("/patient");
            default: return navigate("/profile");
        }
    }
    }, [navigate]);

    return (
        <Container className="mt-5" style={{ maxWidth: "400px" }}>
            <h2 className="text-center text-primary mb-4">ĐĂNG NHẬP</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            
            <Form onSubmit={handleLogin} className="border p-4 shadow-sm rounded bg-white">
                <Form.Group className="mb-3">
                    <Form.Label>Tên đăng nhập</Form.Label>
                    <Form.Control type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                </Form.Group>

                <Form.Group className="mb-3">
                    <Form.Label>Mật khẩu</Form.Label>
                    <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </Form.Group>

                <div className="d-grid mt-4">
                    <Button variant="primary" type="submit" disabled={loading}>
                        {loading ? "Đang đăng nhập..." : "Đăng Nhập"}
                    </Button>
                </div>
                <div className="text-center mt-3">
                    Chưa có tài khoản? <Link to="/register">Đăng ký</Link>
                </div>
            </Form>
        </Container>
    );
};

export default Login;