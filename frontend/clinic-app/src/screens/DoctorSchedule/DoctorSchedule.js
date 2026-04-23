import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const DoctorSchedule = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const loadSchedule = async () => {
            try {
                // 1. Lấy token xác thực từ localStorage
                const token = localStorage.getItem("token") || localStorage.getItem("access_token");

                // 2. Gửi request kèm header Authorization
                const res = await Apis.get(endpoints['doctor-appointments'], {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                setAppointments(res.data);
            } catch (error) {
                console.error("Lỗi tải lịch khám:", error);
            } finally {
                setLoading(false);
            }
        };
        loadSchedule();
    }, []);

    // 3. Logic kiểm tra thời gian khớp với định dạng DTO (date, time)
    const checkReadyToJoin = (dateStr, timeString, status) => {
        if (status !== 'scheduled') return false;
        
        // Cắt chuỗi "08:00 - 08:30" lấy "08:00"
        const startTime = timeString.split(" - ")[0].trim(); 
        
        const now = new Date();
        const appointmentTime = new Date(`${dateStr}T${startTime}:00`);
        const diffInMinutes = (appointmentTime - now) / 1000 / 60;
        
        // Cho phép vào trước 15 phút và trễ tối đa 60 phút
        return diffInMinutes <= 15 && diffInMinutes >= -60; 
    };

    if (loading) return <MySpinner />;

    return (
        <div className="container py-4">
            <h2 className="text-center mb-4">Lịch Khám Của Bác Sĩ</h2>
            
            {appointments.length === 0 ? (
                <div className="alert alert-info text-center">Hôm nay không có lịch khám nào.</div>
            ) : (
                <div className="row">
                    {appointments.map(appt => (
                        <div className="col-md-6 mb-4" key={appt.id}>
                            <div className="card shadow-sm h-100 border-primary">
                                <div className="card-body">
                                    {/* 4. Sử dụng tên trường từ DTO: patientName, date, time */}
                                    <h5 className="card-title text-danger">
                                        Bệnh nhân: {appt.patientName}
                                    </h5>
                                    <p className="mb-1"><strong>Lý do khám:</strong> {appt.reason || "Không ghi rõ"}</p>
                                    <p className="mb-1"><strong>Ngày:</strong> {appt.date}</p>
                                    <p className="mb-3"><strong>Giờ:</strong> {appt.time}</p>
                                    
                                    {checkReadyToJoin(appt.date, appt.time, appt.status) ? (
                                        <button 
                                            className="btn btn-primary w-100 fw-bold"
                                            onClick={() => navigate(`/video-call/${appt.id}`)}
                                        >
                                            🎥 Gọi bệnh nhân vào phòng
                                        </button>
                                    ) : (
                                        <button className="btn btn-secondary w-100" disabled>
                                            {appt.status === 'completed' ? 'Đã hoàn thành' : 'Chưa đến giờ'}
                                        </button>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default DoctorSchedule;