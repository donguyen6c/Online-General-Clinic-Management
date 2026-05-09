import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import cookies from "react-cookies";

const MyAppointments = () => {
    const PAGE_SIZE = 4;
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const loadAppointments = async () => {
            try {
                const token = cookies.load("token");
                const res = await Apis.get(endpoints['patient-appointments'], {
                    headers: { "Authorization": `Bearer ${token}` },
                    params: { page }
                });
                const newData = res.data || [];
                if (page === 1) {
                    setAppointments(newData);
                } else {
                    setAppointments(prev => {
                        const existingIds = new Set(prev.map(a => a.id));
                        return [...prev, ...newData.filter(a => !existingIds.has(a.id))];
                    });
                }
                setHasMore(newData.length === PAGE_SIZE);
            } catch (error) {
                console.error("Lỗi tải lịch hẹn:", error);
            } finally {
                setLoading(false);
            }
        };
        loadAppointments();
    }, [page]);

    const checkReadyToJoin = (dateStr, timeString, status) => {
        if (status !== 'scheduled') return false;
        const startTime = timeString.split(" - ")[0].trim();
        const now = new Date();
        const appointmentTime = new Date(`${dateStr}T${startTime}:00`);
        const diffInMinutes = (appointmentTime - now) / 1000 / 60;
        return diffInMinutes <= 15 && diffInMinutes >= -60;
    };

    if (loading && page === 1) return <MySpinner />;

    return (
        <div className="container py-4">
            <h2 className="text-center mb-4">Lịch Hẹn Của Tôi</h2>

            {appointments.length === 0 ? (
                <div className="alert alert-warning text-center">Bạn chưa có lịch hẹn nào.</div>
            ) : (
                <>
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

                                        {checkReadyToJoin(appt.date, appt.time, appt.status) ? (
                                            <button className="btn btn-success w-100 fw-bold" onClick={() => navigate(`/video-call/${appt.id}`)}>
                                                Vào phòng khám
                                            </button>
                                        ) : (
                                            <button
                                            className={`btn w-100 ${ appt.status === "completed" ? "btn-success" : "btn-warning"}`} disabled >
                                            {appt.status === "completed" ? "Đã hoàn thành" : "Chưa đến giờ"}
                                            </button>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {hasMore && (
                        <div className="text-center mt-2">
                            <button
                                className="btn btn-outline-primary px-4"
                                onClick={() => setPage(prev => prev + 1)}
                                disabled={loading}
                            >
                                {loading ? "Đang tải..." : "Xem thêm"}
                            </button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
};

export default MyAppointments;