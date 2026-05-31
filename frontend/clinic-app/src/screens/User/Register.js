import { useRef, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Container, Form, Button, Alert, Row, Col } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const userInfo = [
    { field: "fullName",  label: "Họ và Tên",       type: "text"     },
    { field: "username",  label: "Tên đăng nhập",    type: "text"     },
    { field: "password",  label: "Mật khẩu",         type: "password" },
    { field: "confirm",   label: "Xác nhận mật khẩu",type: "password" },
    { field: "email",     label: "Email",             type: "email"    },
    { field: "phone",     label: "Số điện thoại",     type: "tel"      },
];

const Register = () => {
    const [user, setUser]       = useState({});
    const [err, setErr]         = useState("");
    const [loading, setLoading] = useState(false);
    const avatar                = useRef();
    const navigate              = useNavigate();

    const validate = () => {
        const phoneRegex = /^\d{9}$/;
        const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
    
        if (!user.fullName || user.fullName.trim() === "") {
            setErr("Họ và tên không được để trống.");
            return false;
        }
    
        if (!user.username || user.username.trim() === "") {
            setErr("Tên đăng nhập không được để trống.");
            return false;
        }
    
        if (!user.password || user.password.length < 6) {
            setErr("Mật khẩu phải có ít nhất 6 ký tự.");
            return false;
        }
    
        if (user.password !== user.confirm) {
            setErr("Mật khẩu không khớp!");
            return false;
        }
    
        if (!user.email || !emailRegex.test(user.email)) {
            setErr("Email không đúng định dạng.");
            return false;
        }
    
        if (!user.phone || !phoneRegex.test(user.phone)) {
            setErr("Số điện thoại không hợp lệ! Vui lòng nhập đúng 9 chữ số.");
            return false;
        }
    
        if (!avatar.current.files.length) {
            setErr("Vui lòng chọn ảnh đại diện!");
            return false;
        }
    
        return true;
    };

    const register = async (e) => {
        e.preventDefault();
        setErr("");
        if (!validate()) return;

        let form = new FormData();
        for (let key of Object.keys(user)) {
            if (key !== "confirm")
                form.append(key, user[key]);
        }
        form.append("avatar",  avatar.current.files[0]);
        form.append("gender",  user.gender || "male");

        try {
            setLoading(true);
            const res = await Apis.post(endpoints["users"], form, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            if (res.status === 201)
                navigate("/login");
        } catch (ex) {
            console.error(ex);
            setErr('Lỗi');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: "600px" }}>
            <h2 className="text-center text-primary mb-4">ĐĂNG KÝ TÀI KHOẢN</h2>

            {err && <Alert variant="danger">{err}</Alert>}

            <Form onSubmit={register} className="border p-4 shadow-sm rounded bg-white">
                {userInfo.map(u => (
                    <Form.Group key={u.field} className="mb-3" controlId={u.field}>
                        <Form.Label>{u.label}</Form.Label>
                        <Form.Control type={u.type} placeholder={u.label} value={user[u.field] || ""} onChange={e => setUser({ ...user, [u.field]: e.target.value })} required/>
                    </Form.Group>
                ))}

                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Giới tính</Form.Label>
                            <Form.Select value={user.gender || "male"} onChange={e => setUser({ ...user, gender: e.target.value })} >
                                <option value="male">Nam</option>
                                <option value="female">Nữ</option>
                                <option value="other">Khác</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Ảnh đại diện</Form.Label>
                            <Form.Control ref={avatar} type="file" accept="image/*" required />
                        </Form.Group>
                    </Col>
                </Row>

                <Form.Group className="mb-3">
                    {loading
                        ? <MySpinner />
                        : <Button variant="primary" type="submit" className="w-100">Đăng Ký</Button>
                    }
                </Form.Group>

                <div className="text-center">
                    Đã có tài khoản? <Link to="/login">Đăng nhập</Link>
                </div>
            </Form>
        </Container>
    );
};

export default Register;