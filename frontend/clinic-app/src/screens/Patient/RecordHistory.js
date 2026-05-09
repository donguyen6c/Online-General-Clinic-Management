import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const RecordHistory = () => {
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        Apis.get(endpoints["current-user-medical-records"])
            .then(res => setRecords(res.data || []))
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <MySpinner />;

    const renderPaymentBadge = (record) => {
        if (record.paymentStatus === "paid") {
            return <span className="badge bg-success">Đã thanh toán</span>;
        }
        return (
            <button
                className="badge bg-warning text-dark border-0"
                style={{ cursor: "pointer" }}
                onClick={() => navigate(`/payment/${record.recordId}`)}
            >
                {record.paymentStatus === "failed" ? "Thanh toán thất bại" : "Chưa thanh toán"}
            </button>
        );
    };

    return (
        <div className="container py-4">
            <h2 className="text-primary mb-4">Hồ sơ bệnh án</h2>

            {records.length === 0 ? (
                <p className="text-muted">Chưa có hồ sơ bệnh án nào.</p>
            ) : records.map(record => (
                <div key={record.recordId} className="card mb-3 shadow-sm">
                    <div className="card-header d-flex justify-content-between align-items-center">
                        <span className="fw-bold">#{record.recordId} — {record.doctorName}</span>
                        <div className="d-flex align-items-center gap-2">
                            {renderPaymentBadge(record)}
                            <span className="text-muted small">{record.date?.slice(0, 10)}</span>
                        </div>
                    </div>
                    <div className="card-body">
                        <p><strong>Chẩn đoán:</strong> {record.diagnosis}</p>

                        {record.prescriptions?.length > 0 && (
                            <div className="mb-2">
                                <strong>Đơn thuốc:</strong>
                                <ul className="mt-1">
                                    {record.prescriptions.map((p, i) => (
                                        <li key={i}>{p.medicineName} — SL: {p.quantity} — {p.instruction}</li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        {record.services?.length > 0 && (
                            <div className="mb-2">
                                <strong>Dịch vụ:</strong>
                                <ul className="mt-1">
                                    {record.services.map((s, i) => (
                                        <li key={i}>{s.serviceName} — SL: {s.quantity} — {Number(s.priceAtTime).toLocaleString("vi-VN")}₫</li>
                                    ))}
                                </ul>
                            </div>
                        )}

                        {record.testResults?.length > 0 && (
                            <div>
                                <strong>Kết quả xét nghiệm:</strong>
                                <ul className="mt-1">
                                    {record.testResults.map((t, i) => (
                                        <li key={i}>{t.testName}: {t.resultValue}</li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>
                    {record.paymentStatus !== "paid" && (
                        <div className="card-footer d-flex justify-content-end">
                            <button
                                className="btn btn-primary"
                                onClick={() => navigate(`/payment/${record.recordId}`)}
                            >
                                Thanh toán ngay
                            </button>
                        </div>
                    )}
                </div>
            ))}
        </div>
    );
};

export default RecordHistory;