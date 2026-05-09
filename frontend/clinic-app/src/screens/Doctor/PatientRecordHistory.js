import { useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const PatientRecordHistory = () => {
    const [patientId, setPatientId] = useState("");
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searched, setSearched] = useState(false);

    const handleSearch = async () => {
        if (!patientId.trim()) return;
        setLoading(true);
        try {
            const res = await Apis.get(endpoints["patient-medical-records"](patientId));
            setRecords(res.data || []);
        } catch {
            setRecords([]);
        } finally {
            setLoading(false);
            setSearched(true);
        }
    };

    return (
        <div className="container py-4" style={{ maxWidth: 800 }}>
            <h4 className="text-primary mb-4">Hồ sơ bệnh án bệnh nhân</h4>

            <div className="input-group mb-4">
                <input
                    className="form-control"
                    placeholder="Nhập ID bệnh nhân..."
                    value={patientId}
                    onChange={e => setPatientId(e.target.value)}
                    onKeyDown={e => e.key === "Enter" && handleSearch()}
                />
                <button className="btn btn-primary" onClick={handleSearch}>Tìm</button>
            </div>

            {loading && <MySpinner />}

            {!loading && searched && records.length === 0 && (
                <p className="text-muted">Không có hồ sơ nào.</p>
            )}

            {records.map(r => (
                <div key={r.recordId} className="card mb-3 shadow-sm">
                    <div className="card-header d-flex justify-content-between">
                        <span className="fw-bold">BS. {r.doctorName}</span>
                        <small className="text-muted">{r.date?.slice(0, 10)}</small>
                    </div>
                    <div className="card-body">
                        <p className="mb-2"><strong>Chẩn đoán:</strong> {r.diagnosis}</p>

                        {r.prescriptions?.length > 0 && <>
                            <p className="mb-1"><strong>Đơn thuốc:</strong></p>
                            <ul>{r.prescriptions.map((p, i) =>
                                <li key={i}>{p.medicineName} × {p.quantity} — {p.instruction}</li>
                            )}</ul>
                        </>}

                        {r.services?.length > 0 && <>
                            <p className="mb-1"><strong>Dịch vụ:</strong></p>
                            <ul>{r.services.map((s, i) =>
                                <li key={i}>{s.serviceName} × {s.quantity} — {Number(s.priceAtTime).toLocaleString("vi-VN")}₫</li>
                            )}</ul>
                        </>}

                        {r.testResults?.length > 0 && <>
                            <p className="mb-1"><strong>Kết quả xét nghiệm:</strong></p>
                            <ul>{r.testResults.map((t, i) =>
                                <li key={i}>{t.testName}: {t.resultValue}{" "}
                                    {t.fileUrl && <a href={t.fileUrl} target="_blank" rel="noreferrer">[Xem file]</a>}
                                </li>
                            )}</ul>
                        </>}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default PatientRecordHistory;