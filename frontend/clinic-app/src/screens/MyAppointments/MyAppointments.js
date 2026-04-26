import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const MyAppointments = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const loadAppointments = async () => {
            try {
                const res = await Apis.get(endpoints['patient-appointments']);
                
                setAppointments(res.data);
            } catch (error) {
                console.error("Lỗi tải lịch hẹn:", error);
            } finally {
                setLoading(false);
            }
        };
        loadAppointments();
    }, []);

    const checkReadyToJoin = (dateStr, timeString, status) => {
        if (status !== 'scheduled') return false;
        
        const startTime = timeString.split(" - ")[0].trim(); 
        
        const now = new Date();
        const appointmentTime = new Date(`${dateStr}T${startTime}:00`); 
        const diffInMinutes = (appointmentTime - now) / 1000 / 60;
        
        return diffInMinutes <= 15 && diffInMinutes >= -60; 
    };

    if (loading) return <MySpinner />;

    return (
        <div className="container py-4">
            <h2 className="text-center mb-4">Lịch Hẹn Của Tôi</h2>
            
            {appointments.length === 0 ? (
                <div className="alert alert-warning text-center">Bạn chưa có lịch hẹn nào.</div>
            ) : (
                <div className="row">
                    {appointments.map(appt => (
                        <div className="col-md-6 mb-4" key={appt.id}>
                            <div className="card shadow-sm h-100">
                                <div className="card-body">
                                    <h5 className="card-title text-primary">
                                        Khám với: Bác sĩ {appt.doctorName}
                                    </h5>
                                    <p className="mb-1"><strong>Chuyên khoa:</strong> {appt.specialty}</p>
                                    <p className="mb-1"><strong>Ngày:</strong> {appt.date}</p>
                                    <p className="mb-3"><strong>Giờ:</strong> {appt.time}</p>
                                    
                                    {/* NÚT VÀO PHÒNG VIDEO CALL */}
                                    {checkReadyToJoin(appt.date, appt.time, appt.status) ? (
                                        <button 
                                            className="btn btn-success w-100 fw-bold"
                                            onClick={() => navigate(`/video-call/${appt.id}`)}
                                        >
                                            🎥 Vào phòng khám trực tuyến
                                        </button>
                                    ) : (
                                        <button className="btn btn-secondary w-100" disabled>
                                            {appt.status === 'completed' ? 'Đã hoàn thành' : 'Chưa đến giờ khám'}
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

export default MyAppointments;