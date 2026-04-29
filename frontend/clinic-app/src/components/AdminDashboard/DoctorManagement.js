import React, { useState, useEffect, useRef } from "react";
import { Table, Button, Modal, Form, Spinner, Badge, InputGroup } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";

const DoctorManagement = () => {
    const [doctors, setDoctors] = useState([]);
    const [specialties, setSpecialties] = useState([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(false);
    const [kw, setKw] = useState("");
    const [refresh, setRefresh] = useState(0);

    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({
        id: "",
        username: "",
        password: "",
        fullName: "",
        email: "",
        phone: "",
        gender: "male",
        specialtyId: "",
        bio: "",
        file: null
    });

    const fileInputRef = useRef();

    useEffect(() => {
        const fetchSpecialties = async () => {
            try {
                const res = await Apis.get(endpoints['specialties']);
                setSpecialties(res.data);
            } catch (error) {
                console.error("Lỗi tải chuyên khoa", error);
            }
        };
        fetchSpecialties();
    }, []);

    useEffect(() => {
        const fetchDoctors = async () => {
            setLoading(true);
            try {
                const res = await Apis.get(endpoints['doctors'], {
                    params: { page, kw } 
                });
                
                const newData = res.data.data || [];
                
                if (page === 1) {
                    setDoctors(newData);
                } else {
                    setDoctors(prev => [...prev, ...newData]);
                }

                setHasMore(res.data.hasNext);

            } catch (error) {
                console.error("Lỗi tải danh sách bác sĩ", error);
                alert("Không thể tải danh sách bác sĩ!");
            } finally {
                setLoading(false);
            }
        };

        const timer = setTimeout(() => fetchDoctors(), 300);
        return () => clearTimeout(timer);
    }, [page, kw, refresh]);

    const handleSearch = (e) => {
        setKw(e.target.value);
        setPage(1);
    };

    const handleLoadMore = () => {
        setPage(prev => prev + 1);
    };

    const handleClose = () => {
        setShowModal(false);
        setFormData({ id: "", username: "", password: "", fullName: "", email: "", phone: "", gender: "male", specialtyId: "", bio: "", file: null });
        if (fileInputRef.current) fileInputRef.current.value = "";
    };

    const handleShow = (doctor = null) => {
    if (doctor) {
        setFormData({
            id: doctor.id,
            userId: doctor.user.id,
            username: doctor.user.username || "",
            password: "",
            fullName: doctor.user.fullName || "",
            email: doctor.user.email || "",
            phone: doctor.user.phone || "",
            gender: doctor.user.gender || "male",
            specialtyId: doctor.specialty?.id || "",
            bio: doctor.bio || "",
            file: null
        });
    } else {
        setFormData({ id: "", userId: "", username: "", password: "", fullName: "", email: "", phone: "", gender: "male", specialtyId: "", bio: "", file: null });
    }
    setShowModal(true);
};

    const handleFileChange = (e) => {
        setFormData({ ...formData, file: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const form = new FormData();
            
            if (formData.id) form.append("id", formData.id);
            form.append("bio", formData.bio);
            form.append("specialtyId.id", formData.specialtyId);

            if (formData.userId) form.append("userId.id", formData.userId);
            form.append("userId.username", formData.username);
            form.append("userId.fullName", formData.fullName);
            form.append("userId.email", formData.email);
            form.append("userId.phone", formData.phone);
            form.append("userId.gender", formData.gender);
            
            if (formData.password) form.append("userId.password", formData.password);
            if (formData.file) form.append("userId.file", formData.file); 

            if (formData.id) {
                await Apis.post(endpoints['admin-doctor-detail'](formData.id), form, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                alert("Cập nhật thành công!");
            } else {
                await Apis.post(endpoints['admin-doctors'], form, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
                alert("Thêm bác sĩ thành công!");
            }

            setPage(1);
            setRefresh(prev => prev + 1);
            handleClose();
        } catch (error) {
            console.error("Lỗi lưu bác sĩ", error);
            alert("Có lỗi xảy ra, vui lòng kiểm tra lại thông tin (Email/Phone có thể bị trùng).");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Bạn có chắc chắn muốn xóa bác sĩ này cùng tài khoản của họ?")) {
            try {
                await Apis.delete(endpoints['admin-doctor-detail'](id));
                alert("Xóa thành công!");
                
                setPage(1);
                setRefresh(prev => prev + 1);
            } catch (error) {
                console.error("Lỗi xóa", error);
                alert("Không thể xóa bác sĩ này!");
            }
        }
    };

    return (
        <div className="card shadow-sm border-0 mb-4">
            <div className="card-header bg-dark text-white d-flex justify-content-between align-items-center p-3">
                <h5 className="mb-0"><i className="bi bi-heart-pulse me-2"></i>Danh Sách Bác Sĩ</h5>
                <button className="btn btn-success btn-sm fw-bold" onClick={() => handleShow()}>
                    <i className="bi bi-person-plus me-1"></i> Thêm Bác sĩ
                </button>
            </div>

            <div className="card-body">
                <div className="mb-3">
                    <InputGroup>
                        <InputGroup.Text className="bg-light"><i className="bi bi-search"></i></InputGroup.Text>
                        <Form.Control 
                            type="text" 
                            placeholder="Tìm kiếm theo tên bác sĩ..." 
                            value={kw} 
                            onChange={handleSearch} 
                        />
                    </InputGroup>
                </div>

                <Table striped bordered hover responsive className="align-middle">
                    <thead className="table-secondary text-center">
                        <tr>
                            <th>ID</th>
                            <th>Ảnh</th>
                            <th>Họ Tên</th>
                            <th>Chuyên khoa</th>
                            <th>Số điện thoại</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        {doctors.length === 0 && !loading ? (
                            <tr><td colSpan="6" className="text-center p-4 text-muted">Không tìm thấy bác sĩ nào</td></tr>
                        ) : (
                            doctors.map(d => (
                                <tr key={d.id} className="text-center">
                                    <td><Badge bg="light" text="dark" className="border">#{d.id}</Badge></td>
                                    <td>
                                        <img src={d.user?.avatar || "https://via.placeholder.com/50"} alt="avatar" width="50" height="50" className="rounded-circle shadow-sm" style={{objectFit: 'cover'}}/>
                                    </td>
                                    <td className="text-start">
                                        <div className="fw-bold">{d.user?.fullName}</div>
                                        <small className="text-muted">{d.user?.email}</small>
                                    </td>
                                    <td><Badge bg="info">{d.specialty?.name}</Badge></td>
                                    <td>{d.user?.phone}</td>
                                    <td>
                                        <Button variant="outline-warning" size="sm" className="me-2" onClick={() => handleShow(d)}>
                                            <i className="bi bi-pencil-square"></i> Sửa
                                        </Button>
                                        <Button variant="outline-danger" size="sm" onClick={() => handleDelete(d.id)}>
                                            <i className="bi bi-trash"></i> Xóa
                                        </Button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </Table>

                {loading && <div className="text-center my-3"><Spinner animation="border" variant="primary"/></div>}

                {!loading && hasMore && (
                    <div className="text-center mt-3">
                        <button className="btn btn-outline-dark btn-sm px-4 fw-bold" onClick={handleLoadMore}>
                            Xem thêm bác sĩ...
                        </button>
                    </div>
                )}
            </div>

            <Modal show={showModal} onHide={handleClose} size="lg" style={{ backgroundColor: 'rgba(0,0,0,0.4)' }}>
                <Modal.Header closeButton className="bg-dark text-white">
                    <Modal.Title>{formData.id ? "Cập nhật Bác sĩ" : "Thêm Bác sĩ Mới"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <div className="row">
                            <div className="col-md-6 border-end pe-3">
                                <h6 className="text-primary mb-3"><i className="bi bi-person-badge me-2"></i>Thông tin Tài khoản</h6>
                                <Form.Group className="mb-2">
                                    <Form.Label className="small fw-bold">Tên đăng nhập <span className="text-danger">*</span></Form.Label>
                                    <Form.Control size="sm" type="text" disabled={!!formData.id} value={formData.username} onChange={(e) => setFormData({...formData, username: e.target.value})} required />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label className="small fw-bold">Mật khẩu {formData.id && "(Bỏ trống nếu không đổi)"} {(!formData.id) && <span className="text-danger">*</span>}</Form.Label>
                                    <Form.Control size="sm" type="password" value={formData.password} onChange={(e) => setFormData({...formData, password: e.target.value})} required={!formData.id} />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label className="small fw-bold">Họ và Tên <span className="text-danger">*</span></Form.Label>
                                    <Form.Control size="sm" type="text" value={formData.fullName} onChange={(e) => setFormData({...formData, fullName: e.target.value})} required />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label className="small fw-bold">Email <span className="text-danger">*</span></Form.Label>
                                    <Form.Control size="sm" type="email" value={formData.email} onChange={(e) => setFormData({...formData, email: e.target.value})} required />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label className="small fw-bold">Số điện thoại (9 số) <span className="text-danger">*</span></Form.Label>
                                    <Form.Control size="sm" type="text" value={formData.phone} onChange={(e) => setFormData({...formData, phone: e.target.value})} required pattern="^\d{9}$" />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label className="small fw-bold">Giới tính</Form.Label>
                                    <Form.Select size="sm" value={formData.gender} onChange={(e) => setFormData({...formData, gender: e.target.value})}>
                                        <option value="male">Nam</option>
                                        <option value="female">Nữ</option>
                                    </Form.Select>
                                </Form.Group>
                            </div>

                            <div className="col-md-6 ps-3">
                                <h6 className="text-primary mb-3"><i className="bi bi-clipboard2-pulse me-2"></i>Thông tin Y tế</h6>
                                <Form.Group className="mb-3">
                                    <Form.Label className="small fw-bold">Chuyên khoa <span className="text-danger">*</span></Form.Label>
                                    <Form.Select 
                                        size="sm" 
                                        value={formData.specialtyId} 
                                        onChange={(e) => setFormData({...formData, specialtyId: e.target.value})}
                                        required
                                    >
                                        <option value="">-- Chọn chuyên khoa --</option>
                                        {specialties.map(spec => (
                                            <option key={spec.id} value={spec.id}>
                                                {spec.name}
                                            </option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label className="small fw-bold">Giới thiệu</Form.Label>
                                    <Form.Control size="sm" as="textarea" rows={4} value={formData.bio} onChange={(e) => setFormData({...formData, bio: e.target.value})} />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label className="small fw-bold">Ảnh đại diện{(!formData.id) && <span className="text-danger">*</span>}</Form.Label>
                                    <Form.Control size="sm" type="file" ref={fileInputRef} onChange={handleFileChange} accept="image/*" required={!formData.id} />
                                </Form.Group>
                            </div>
                        </div>
                        <div className="text-end mt-4 pt-3 border-top">
                            <Button variant="secondary" className="px-4 me-2" onClick={handleClose}>Hủy</Button>
                            <Button variant="success" className="px-4 fw-bold" type="submit" disabled={loading}>
                                {loading ? <Spinner size="sm" /> : (formData.id ? "Lưu Thay Đổi" : "Tạo Mới")}
                            </Button>
                        </div>
                    </Form>
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default DoctorManagement;