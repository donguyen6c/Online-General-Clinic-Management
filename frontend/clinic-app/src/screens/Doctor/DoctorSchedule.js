import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import cookies from "react-cookies";

const DoctorSchedule = () => {
    const PAGE_SIZE = 4;
    const [schedules, setSchedules] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const loadSchedule = async () => {
            try {
                const token = cookies.load("token");
                const res = await Apis.get(endpoints['doctor-schedule'], {
                    headers: { "Authorization": `Bearer ${token}` },
                    params: { page }
                });
                const newData = res.data || [];
                if (page === 1) {
                    setSchedules(newData);
                } else {
                    setSchedules(prev => {
                        const existingIds = new Set(prev.map(a => a.id));
                        return [...prev, ...newData.filter(a => !existingIds.has(a.id))];
                    });
                }
                setHasMore(newData.length === PAGE_SIZE);
            } catch (error) {
                console.error("Lỗi tải lịch khám:", error);
            } finally {
                setLoading(false);
            }
        };
        loadSchedule();
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
            <h2 className="text-center mb-4">Lịch Khám Của Bác Sĩ</h2>

            {schedules.length === 0 ? (
                <div className="alert alert-info text-center">Hôm nay không có lịch khám nào.</div>
            ) : (
                <>
                    <div className="row">
                        {schedules.map(appt => (
                            <div className="col-md-6 mb-4" key={appt.id}>
                                <div className="card shadow-sm h-100 border-primary">
                                    <div className="card-body">
                                        <h5 className="card-title text-danger">Tư vấn khám bệnh</h5>
                                        <p className="mb-1"><strong>Mã cuộc hẹn:</strong> {appt.id}</p>
                                        <p className="mb-1"><strong>Lý do khám:</strong> {appt.reason || "Không ghi rõ"}</p>
                                        <p className="mb-1"><strong>Ngày:</strong> {appt.date}</p>
                                        <p className="mb-3"><strong>Giờ:</strong> {appt.time}</p>

                                        {checkReadyToJoin(appt.date, appt.time, appt.status) ? (
                                            <button className="btn btn-primary w-100 fw-bold" onClick={() => navigate(`/video-call/${appt.id}`)}>
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

export default DoctorSchedule;