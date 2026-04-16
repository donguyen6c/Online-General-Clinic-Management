import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Apis, { endpoints } from './configs/Apis';

const Doctor = () => {
    const { id } = useParams(); 
    const navigate = useNavigate();
    
    const [specialties, setSpecialties] = useState([]);
    const [errMsg, setErrMsg] = useState('');
    
    const [doctor, setDoctor] = useState({
        id: '',
        bio: '',
        userId: {
            id: '',
            fullName: '',
            username: '',
            password: '',
            email: '',
            phone: '',
            gender: 'male',
            avatar: '',
            file: null 
        },
        specialtyId: {
            id: ''
        }
    });

    useEffect(() => {
        const loadInitialData = async () => {
            try {
                // Tải chuyên khoa cho select box
                const specRes = await Apis.get(endpoints['specialties']);
                setSpecialties(specRes.data);

                // Nếu có id trên URL -> Chế độ cập nhật
                if (id) {
                    const docRes = await Apis.get(`${endpoints['doctors']}/${id}`);
                    // Đảm bảo dữ liệu Apis trả về khớp với cấu trúc state
                    setDoctor({
                        ...docRes.data,
                        userId: docRes.data.userId || doctor.userId,
                        specialtyId: docRes.data.specialtyId || doctor.specialtyId
                    });
                }
            } catch (error) {
                console.error("Lỗi khi tải dữ liệu:", error);
                setErrMsg("Không thể tải dữ liệu từ máy chủ.");
            }
        };
        
        loadInitialData();
    }, [id]);

    const handleUserChange = (e) => {
        const { name, value } = e.target;
        setDoctor(prev => ({
            ...prev,
            userId: { ...prev.userId, [name]: value }
        }));
    };

    const handleDoctorChange = (e) => {
        const { name, value } = e.target;
        setDoctor(prev => ({ ...prev, [name]: value }));
    };

    const handleSpecialtyChange = (e) => {
        setDoctor(prev => ({
            ...prev,
            specialtyId: { id: e.target.value }
        }));
    };

    const handleFileChange = (e) => {
        setDoctor(prev => ({
            ...prev,
            userId: { ...prev.userId, file: e.target.files[0] }
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrMsg('');
        
        const formData = new FormData();
        // Append dữ liệu cho Spring MVC xử lý. 
        // Lưu ý: Tên field ("userId.fullName", v.v) cần khớp với DTO nhận bên backend
        formData.append("bio", doctor.bio);
        formData.append("specialtyId.id", doctor.specialtyId.id);
        
        formData.append("userId.fullName", doctor.userId.fullName);
        formData.append("userId.username", doctor.userId.username);
        formData.append("userId.email", doctor.userId.email);
        formData.append("userId.phone", doctor.userId.phone);
        formData.append("userId.gender", doctor.userId.gender);
        
        if (!id) {
            formData.append("userId.password", doctor.userId.password);
        } else {
            formData.append("id", doctor.id);
            formData.append("userId.id", doctor.userId.id);
        }

        if (doctor.userId.file) {
            formData.append("userId.file", doctor.userId.file);
        }

        try {
            if (id) {
                // Update
                await Apis.post(`${endpoints['doctors']}/${id}`, formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
            } else {
                // Create
                await Apis.post(endpoints['doctors'], formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
            }
            alert("Lưu thông tin thành công!");
            navigate('/');
        } catch (error) {
            console.error("Lỗi khi lưu bác sĩ:", error);
            setErrMsg("Đã xảy ra lỗi khi lưu thông tin.");
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="text-center text-primary">
                {id ? 'CẬP NHẬT THÔNG TIN BÁC SĨ' : 'THÊM MỚI BÁC SĨ'}
            </h2>
            
            {errMsg && <div className="alert alert-danger">{errMsg}</div>}

            <form onSubmit={handleSubmit} className="mt-4 p-4 border rounded shadow-sm bg-white">
                {/* Giữ nguyên phần HTML/JSX form như cũ */}
                <div className="row">
                    <div className="col-md-6">
                        <h4 className="text-info mb-3">Thông tin cơ bản</h4>
                        
                        <div className="mb-3">
                            <label className="form-label">Họ và Tên (*)</label>
                            <input type="text" name="fullName" value={doctor.userId.fullName} onChange={handleUserChange} className="form-control" required />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Tên đăng nhập (*)</label>
                            <input type="text" name="username" value={doctor.userId.username} onChange={handleUserChange} className="form-control" readOnly={!!id} required />
                        </div>

                        {!id && (
                            <div className="mb-3">
                                <label className="form-label">Mật khẩu (*)</label>
                                <input type="password" name="password" value={doctor.userId.password} onChange={handleUserChange} className="form-control" required />
                            </div>
                        )}

                        <div className="mb-3">
                            <label className="form-label">Email (*)</label>
                            <input type="email" name="email" value={doctor.userId.email} onChange={handleUserChange} className="form-control" required />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Số điện thoại (*)</label>
                            <input type="text" name="phone" value={doctor.userId.phone} onChange={handleUserChange} className="form-control" required />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Giới tính</label>
                            <select name="gender" value={doctor.userId.gender} onChange={handleUserChange} className="form-select">
                                <option value="male">Nam</option>
                                <option value="female">Nữ</option>
                            </select>
                        </div>
                    </div>

                    <div className="col-md-6">
                        <h4 className="text-info mb-3">Thông tin chuyên môn</h4>

                        <div className="mb-3">
                            <label className="form-label">Ảnh đại diện (Avatar)</label>
                            <input type="file" onChange={handleFileChange} className="form-control" accept="image/*" />
                            {doctor.userId.avatar && typeof doctor.userId.avatar === 'string' && (
                                <div className="mt-2">
                                    <img src={doctor.userId.avatar} width="100" className="img-thumbnail" alt="Avatar hiện tại" />
                                </div>
                            )}
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Chuyên khoa (*)</label>
                            <select value={doctor.specialtyId.id} onChange={handleSpecialtyChange} className="form-select" required>
                                <option value="">-- Chọn chuyên khoa --</option>
                                {specialties.map(s => (
                                    <option key={s.id} value={s.id}>{s.name}</option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Tiểu sử / Kinh nghiệm làm việc</label>
                            <textarea name="bio" value={doctor.bio} onChange={handleDoctorChange} className="form-control" rows="5"></textarea>
                        </div>
                    </div>
                </div>

                <div className="text-center mt-4">
                    <button type="submit" className="btn btn-success px-5 me-3">LƯU THÔNG TIN</button>
                    <Link to="/" className="btn btn-secondary px-5">HỦY</Link>
                </div>
            </form>
        </div>
    );
};

export default Doctor;