import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Card, Row, Col, Button, Form } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import cookies from "react-cookies";

const profileFields = [
    { field: "fullName", label: "Họ và Tên",      type: "text"  },
    { field: "email",    label: "Email",            type: "email" },
    { field: "phone",    label: "Số điện thoại",    type: "tel"   },
    { field: "dateOfBirth", label: "Ngày sinh",         type: "date"  }
];

const Profile = () => {
    const navigate  = useNavigate();
    const avatarRef = useRef();

    const [user, setUser] = useState(cookies.load("user"));
    const [isEditing, setIsEditing] = useState(false);
    const [form,      setForm]      = useState({
        fullName: user?.fullName || "",
        email:    user?.email    || "",
        phone:    user?.phone    || "",
        gender:   user?.gender   || "",
        dateOfBirth: user?.dateOfBirth
        ? new Date(user.dateOfBirth).toISOString().split("T")[0]
        : "", 
    });
    const [avatarPreview, setAvatarPreview] = useState(null);
    const [loading,       setLoading]       = useState(false);

    const handleLogout = () => {
    cookies.remove("user");
    cookies.remove("token");
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/login");
    };

    const handleAvatarChange = (e) => {
        const file = e.target.files[0];
        if (!file) return;
        setAvatarPreview(URL.createObjectURL(file));
    };

    const handleCancel = () => {
        setForm({
            fullName: user?.fullName || "",
            email:    user?.email    || "",
            phone:    user?.phone    || "",
            gender:   user?.gender   || "",
            dateOfBirth: user?.dateOfBirth
            ? new Date(user.dateOfBirth).toISOString().split("T")[0]
            : "",
        });
        setAvatarPreview(null);
        setIsEditing(false);
    };

    const handleSave = async () => {
    try {
        setLoading(true);

        const formData = new FormData();
        for (let key of Object.keys(form))
            formData.append(key, form[key]);
        if (avatarRef.current.files.length > 0)
            formData.append("avatar", avatarRef.current.files[0]);

        const res = await Apis.patch(endpoints["profile"], formData);
        const newUser = { ...user, ...res.data };

        cookies.save("user", newUser);
        localStorage.setItem("user", JSON.stringify(newUser));

        setUser(newUser);
        setAvatarPreview(null);
        setIsEditing(false);
        alert("Cập nhật thành công!");
    } catch (ex) {
        console.error(ex.response?.data);
        alert("Cập nhật thất bại!");
    } finally {
        setLoading(false);
    }
    };

    if (!user)
        return <h3 className="text-center mt-5 text-danger">Vui lòng đăng nhập!</h3>;

    return (
        <Container className="mt-5" style={{ maxWidth: "700px" }}>
            <Card className="shadow-sm">
                <Card.Header className="bg-primary text-white text-center">
                    <h4>THÔNG TIN TÀI KHOẢN</h4>
                </Card.Header>

                <Card.Body>
                    <Row className="align-items-center">

                        <Col md={4} className="text-center mb-3">
                            <img src={avatarPreview || user.avatar} alt="Avatar" className="img-thumbnail rounded-circle" style={{ width: "150px", height: "150px", objectFit: "cover",cursor: isEditing ? "pointer" : "default" }} onClick={() => isEditing && avatarRef.current.click()}/>

                            <input ref={avatarRef} type="file" accept="image/*" style={{ display: "none" }} onChange={handleAvatarChange}/>
                        
                            {isEditing && (<div className="text-muted small mt-1">Đổi avatar</div>)}

                            <h5 className="mt-2 text-primary">{user.username}</h5>
                            <span className="badge bg-info">{user.role}</span>
                        </Col>

                        <Col md={8}>
                            {isEditing ? (
                                <Form>
                                    {profileFields.map(f => (
                                        <Form.Group key={f.field} className="mb-3">
                                            <Form.Label className="fw-bold">{f.label}</Form.Label>
                                            <Form.Control
                                                type={f.type}
                                                value={form[f.field]}
                                                onChange={e => setForm({ ...form, [f.field]: e.target.value })}
                                            />
                                        </Form.Group>
                                    ))}

                                    <Form.Group className="mb-3">
                                        <Form.Label className="fw-bold">Giới tính</Form.Label>
                                        <Form.Select
                                            value={form.gender}
                                            onChange={e => setForm({ ...form, gender: e.target.value })}
                                        >
                                            <option value="male">Nam</option>
                                            <option value="female">Nữ</option>
                                            <option value="other">Khác</option>
                                        </Form.Select>
                                    </Form.Group>
                                </Form>
                            ) : (
                                <table className="table table-borderless">
                                    <tbody>
                                        {profileFields.map(f => (
                                            <tr key={f.field}>
                                                <th>{f.label}:</th>
                                                <td>
                                                    {f.field === "dateOfBirth" && user[f.field]
                                                        ? new Date(user[f.field]).toLocaleDateString("vi-VN")
                                                        : user[f.field]}
                                                </td>
                                            </tr>
                                        ))}
                                        <tr>
                                            <th>Giới tính:</th>
                                            <td>
                                                {user.gender === "male" ? "Nam"
                                                : user.gender === "female" ? "Nữ"
                                                : "Khác"}
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            )}
                        </Col>
                    </Row>
                </Card.Body>

                <Card.Footer className="d-flex justify-content-between">
                    <Button variant="danger" onClick={handleLogout}>Đăng Xuất</Button>

                    <div className="d-flex gap-2">
                        {isEditing ? (
                            <>
                                <Button variant="secondary" onClick={handleCancel} disabled={loading}>
                                    Hủy
                                </Button>
                                {loading
                                    ? <MySpinner />
                                    : <Button variant="success" onClick={handleSave}>Lưu</Button>
                                }
                            </>
                        ) : (
                            <Button variant="primary" onClick={() => setIsEditing(true)}>
                                Chỉnh sửa
                            </Button>
                        )}
                    </div>
                </Card.Footer>
            </Card>
        </Container>
    );
};

export default Profile;