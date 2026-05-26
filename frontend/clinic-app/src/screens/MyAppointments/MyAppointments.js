import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

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
                setLoading(true);

                const res = await Apis.get(endpoints["patient-appointments"], {
                    params: { page },
                });

                const newData = res.data || [];

                if (page === 1) {
                    setAppointments(newData);
                } else {
                    setAppointments(prev => {
                        const existingIds = new Set(prev.map(a => a.id));
                        return [
                            ...prev,
                            ...newData.filter(a => !existingIds.has(a.id)),
                        ];
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

    const getAppointmentTime = (dateStr, timeString) => {
        const startTime = timeString.split(" - ")[0].trim();
        return new Date(`${dateStr}T${startTime}:00`);
    };

    const checkReadyToJoin = (dateStr, timeString, status) => {
        if (status !== "scheduled") return false;

        const now = new Date();
        const appointmentTime = getAppointmentTime(dateStr, timeString);

        const diffInMinutes = (appointmentTime - now) / 1000 / 60;

        return diffInMinutes <= 15 && diffInMinutes >= -60;
    };

    const canCancelAppointment = (dateStr, timeString, status) => {
        if (status !== "scheduled") return false;

        const now = new Date();
        const appointmentTime = getAppointmentTime(dateStr, timeString);

        return now < appointmentTime;
    };

    const cancelAppointment = async (appointmentId) => {
        if (!window.confirm("Bạn có chắc muốn hủy lịch hẹn này không?")) return;

        try {
            await Apis.delete(
                endpoints["cancel-appointment"](appointmentId),
                {}
            );

            setAppointments(prev =>
                prev.map(item =>
                    item.id === appointmentId
                        ? { ...item, status: "cancelled" }
                        : item
                )
            );

            alert("Hủy lịch hẹn thành công!");
        } catch (error) {
            console.error("Lỗi hủy lịch hẹn:", error);
            alert("Không thể hủy lịch hẹn!");
        }
    };

    const renderActionButton = (appt) => {
        if (checkReadyToJoin(appt.date, appt.time, appt.status)) {
            return (
                <button
                    className="btn btn-success w-100 fw-bold"
                    onClick={() => navigate(`/video-call/${appt.id}`)}
                >
                    Vào phòng khám
                </button>
            );
        }

        if (appt.status === "completed") {
            return (
                <button className="btn btn-success w-100" disabled>
                    Đã hoàn thành
                </button>
            );
        }

        if (appt.status === "cancelled") {
            return (
                <button className="btn btn-secondary w-100" disabled>
                    Đã hủy
                </button>
            );
        }

        if (canCancelAppointment(appt.date, appt.time, appt.status)) {
            return (
                <div className="d-flex gap-2">
                    <button className="btn btn-warning w-50" disabled>
                        Chưa đến giờ
                    </button>

                    <button
                        className="btn btn-outline-danger w-50"
                        onClick={() => cancelAppointment(appt.id)}
                    >
                        Hủy lịch
                    </button>
                </div>
            );
        }

        return (
            <button className="btn btn-warning w-100" disabled>
                Chưa đến giờ
            </button>
        );
    };

    if (loading && page === 1) return <MySpinner />;

    return (
        <div className="container py-4">
            <h2 className="text-center mb-4">Lịch Hẹn Của Tôi</h2>

            {appointments.length === 0 ? (
                <div className="alert alert-warning text-center">
                    Bạn chưa có lịch hẹn nào.
                </div>
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

                                        <p className="mb-1">
                                            <strong>Chuyên khoa:</strong>{" "}
                                            {appt.specialty}
                                        </p>

                                        <p className="mb-1">
                                            <strong>Ngày:</strong> {appt.date}
                                        </p>

                                        <p className="mb-3">
                                            <strong>Giờ:</strong> {appt.time}
                                        </p>

                                        {renderActionButton(appt)}
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