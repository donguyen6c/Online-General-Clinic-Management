import { useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import SearchableSelect from "../../components/SearchableSelect";
import {
  ExportContext,
  pdfStrategy,
  excelStrategy,
} from "../../configs/strategy/ExportStrategy";

const PatientRecordHistory = () => {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [patientName, setPatientName] = useState("");

  const handleSelectPatient = async (patient) => {
    setPatientName(patient.fullName);
    setLoading(true);
    try {
      const res = await Apis.get(
        endpoints["patient-medical-records"](patient.id),
      );
      setRecords(res.data || []);
    } catch {
      setRecords([]);
    } finally {
      setLoading(false);
      setSearched(true);
    }
  };

  const handleExport = (record, type) => {
    const ctx = new ExportContext(type === "pdf" ? pdfStrategy : excelStrategy);
    ctx.execute(record);
  };

  return (
    <div className="container py-4" style={{ maxWidth: 800 }}>
      <h4 className="text-primary mb-4">Hồ sơ bệnh án bệnh nhân</h4>

      <div className="mb-4">
        <SearchableSelect
          endpoint={endpoints["patients"]}
          placeholder="Tìm bệnh nhân theo tên..."
          labelKey="fullName"
          onChange={handleSelectPatient}
        />
      </div>

      {loading && <MySpinner />}

      {!loading && searched && patientName && (
        <p className="text-muted mb-3">
          Kết quả cho: <strong>{patientName}</strong>
        </p>
      )}

      {!loading && searched && records.length === 0 && (
        <p className="text-muted">Không có hồ sơ nào.</p>
      )}

      {records.map((r) => (
        <div key={r.recordId} className="card mb-3 shadow-sm">
          <div className="card-header d-flex justify-content-between">
            <span className="fw-bold">BS. {r.doctorName}</span>
            <small className="text-muted">{r.date?.slice(0, 10)}</small>
          </div>
          <div className="card-body">
            <p className="mb-2">
              <strong>Chẩn đoán:</strong> {r.diagnosis}
            </p>

            {r.prescriptions?.length > 0 && (
              <>
                <p className="mb-1">
                  <strong>Đơn thuốc:</strong>
                </p>
                <ul>
                  {r.prescriptions.map((p, i) => (
                    <li key={i}>
                      {p.medicineName} × {p.quantity} — {p.instruction}
                    </li>
                  ))}
                </ul>
              </>
            )}

            {r.services?.length > 0 && (
              <>
                <p className="mb-1">
                  <strong>Dịch vụ:</strong>
                </p>
                <ul>
                  {r.services.map((s, i) => (
                    <li key={i}>
                      {s.serviceName} × {s.quantity} —{" "}
                      {Number(s.priceAtTime).toLocaleString("vi-VN")}₫
                    </li>
                  ))}
                </ul>
              </>
            )}

            {r.testResults?.length > 0 && (
              <>
                <p className="mb-1">
                  <strong>Kết quả xét nghiệm:</strong>
                </p>
                <ul>
                  {r.testResults.map((t, i) => (
                    <li key={i}>
                      {t.testName}: {t.resultValue}
                    </li>
                  ))}
                </ul>
              </>
            )}

            <div className="d-flex gap-2 mt-2">
              <button
                className="btn btn-sm btn-outline-danger"
                onClick={() => handleExport(r, "pdf")}
              >
                📄 Xuất PDF
              </button>
              <button
                className="btn btn-sm btn-outline-success"
                onClick={() => handleExport(r, "excel")}
              >
                📊 Xuất Excel
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default PatientRecordHistory;
