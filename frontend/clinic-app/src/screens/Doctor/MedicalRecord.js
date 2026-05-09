import React, { useEffect, useMemo, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import SearchableSelect from "../../components/SearchableSelect";
import { useSearchParams } from "react-router-dom";

const MedicalRecord = () => {
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [appointments, setAppointments] = useState([]);
    const [searchParams] = useSearchParams();

    const [form, setForm] = useState({
        sourceType: "appointment",
        appointmentId: searchParams.get("appointmentId") || "",
        patientId: "",
        symptoms: "",
        diagnosis: "",
        prescriptionNotes: "",
        diseaseIds: [],
        selectedServices: [],
        selectedMedicines: []
    });

    const [message, setMessage] = useState({ type: "", text: "" });

    const appointmentOptions = useMemo(
        () => appointments.filter(a => a.status !== "cancelled"),
        [appointments]
    );

    useEffect(() => {
        const loadInitialData = async () => {
            try {
                const apptRes = await Apis.get(`${endpoints["appointments-doctor"]}?all=true`);
                setAppointments(apptRes.data || []);
            } catch (error) {
                setMessage({ type: "danger", text: "Không thể tải dữ liệu." });
            } finally {
                setLoading(false);
            }
        };
        loadInitialData();
    }, []);

    const updateForm = (key, value) => setForm(prev => ({ ...prev, [key]: value }));

    const addServiceRow = () =>
        setForm(prev => ({ ...prev, selectedServices: [...prev.selectedServices, { serviceId: "", serviceName: "", quantity: 1 }] }));

    const addMedicineRow = () =>
        setForm(prev => ({ ...prev, selectedMedicines: [...prev.selectedMedicines, { medicineId: "", medicineName: "", quantity: 1, usageInstruction: "" }] }));

    const removeServiceRow = (idx) =>
        setForm(prev => ({ ...prev, selectedServices: prev.selectedServices.filter((_, i) => i !== idx) }));

    const removeMedicineRow = (idx) =>
        setForm(prev => ({ ...prev, selectedMedicines: prev.selectedMedicines.filter((_, i) => i !== idx) }));

    const updateServiceRow = (idx, key, value) =>
        setForm(prev => ({
            ...prev,
            selectedServices: prev.selectedServices.map((row, i) => i === idx ? { ...row, [key]: value } : row)
        }));

    const updateMedicineRow = (idx, key, value) =>
        setForm(prev => ({
            ...prev,
            selectedMedicines: prev.selectedMedicines.map((row, i) => i === idx ? { ...row, [key]: value } : row)
        }));

    const addDiseaseRow = () =>
        setForm(prev => ({ ...prev, diseaseIds: [...prev.diseaseIds, { id: "", name: "" }] }));

    const removeDiseaseRow = (idx) =>
        setForm(prev => ({ ...prev, diseaseIds: prev.diseaseIds.filter((_, i) => i !== idx) }));

    const updateDiseaseRow = (idx, item) =>
        setForm(prev => ({
            ...prev,
            diseaseIds: prev.diseaseIds.map((d, i) => i === idx ? { id: String(item.id), name: item.name } : d)
        }));

    const validate = () => {
        if (!form.symptoms.trim() || !form.diagnosis.trim())
            return "Triệu chứng và chẩn đoán là bắt buộc.";
        if (form.sourceType === "appointment" && !form.appointmentId)
            return "Vui lòng chọn lịch hẹn.";
        if (form.sourceType === "manual" && !form.patientId.trim())
            return "Vui lòng nhập ID bệnh nhân.";
        if (form.selectedServices.some(s => !s.serviceId || Number(s.quantity) <= 0))
            return "Dịch vụ phải có tên và số lượng hợp lệ.";
        if (form.selectedMedicines.some(m => !m.medicineId || Number(m.quantity) <= 0 || !m.usageInstruction.trim()))
            return "Thuốc phải có tên, số lượng và liều dùng hợp lệ.";
        return "";
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({ type: "", text: "" });
        const errorMsg = validate();
        if (errorMsg) { setMessage({ type: "warning", text: errorMsg }); return; }

        const payload = {
            symptoms: form.symptoms.trim(),
            diagnosis: form.diagnosis.trim(),
            prescriptionNotes: form.prescriptionNotes.trim(),
            diseaseIds: form.diseaseIds.filter(d => d.id !== "").map(d => Number(d.id)),
            appointmentId: form.sourceType === "appointment" ? Number(form.appointmentId) : null,
            patientId: form.sourceType === "manual" ? Number(form.patientId) : null
        };

        try {
            setSubmitting(true);
            const createRes = await Apis.post(endpoints["medical-records"], payload);
            const medicalRecordId = createRes?.data?.recordId;
            if (!medicalRecordId) throw new Error("Không nhận được medicalRecordId từ BE.");

            if (form.selectedServices.length > 0) {
                await Apis.post(endpoints["medical-record-services"](medicalRecordId), {
                    services: form.selectedServices.map(s => ({ serviceId: Number(s.serviceId), quantity: Number(s.quantity) }))
                });
            }

            if (form.selectedMedicines.length > 0) {
                await Apis.post(endpoints["medical-record-prescriptions"](medicalRecordId), {
                    medicines: form.selectedMedicines.map(m => ({
                        medicineId: Number(m.medicineId),
                        quantity: Number(m.quantity),
                        usageInstruction: m.usageInstruction.trim()
                    }))
                });
            }

            setMessage({ type: "success", text: `Tạo phiếu khám thành công. Mã hồ sơ: ${medicalRecordId}` });
            setForm({
                sourceType: "appointment", appointmentId: "", patientId: "",
                symptoms: "", diagnosis: "", prescriptionNotes: "",
                diseaseIds: [], selectedServices: [], selectedMedicines: []
            });
        } catch (error) {
            setMessage({ type: "danger", text: error?.response?.data?.error || error.message || "Tạo phiếu khám thất bại." });
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <MySpinner />;

    return (
        <div className="container py-4">
            <h2 className="text-primary mb-4">Bác sĩ tạo phiếu khám</h2>
            {message.text && <div className={`alert alert-${message.type}`}>{message.text}</div>}

            <form className="card shadow-sm" onSubmit={handleSubmit}>
                <div className="card-body">
                    {/* Nguồn tạo phiếu */}
                    <div className="mb-3">
                        <label className="form-label fw-bold d-block">Nguồn tạo phiếu</label>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" checked={form.sourceType === "appointment"} onChange={() => updateForm("sourceType", "appointment")} />
                            <label className="form-check-label">Từ lịch hẹn</label>
                        </div>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" checked={form.sourceType === "manual"} onChange={() => updateForm("sourceType", "manual")} />
                            <label className="form-check-label">Không có lịch hẹn</label>
                        </div>
                    </div>

                    {form.sourceType === "appointment" ? (
                        <div className="mb-3">
                            <label className="form-label">Lịch hẹn</label>
                            <select className="form-select" value={form.appointmentId} onChange={(e) => updateForm("appointmentId", e.target.value)}>
                                <option value="">-- Chọn lịch hẹn --</option>
                                {appointmentOptions.map(appt => (
                                    <option key={appt.id} value={appt.id}>{`#${appt.id} - ${appt.date} ${appt.time}`}</option>
                                ))}
                            </select>
                        </div>
                    ) : (
                        <div className="mb-3">
                            <label className="form-label">Patient ID</label>
                            <input className="form-control" value={form.patientId} onChange={(e) => updateForm("patientId", e.target.value)} placeholder="Nhập ID bệnh nhân" />
                        </div>
                    )}

                    {/* Triệu chứng & Chẩn đoán */}
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Triệu chứng *</label>
                            <textarea className="form-control" rows="3" value={form.symptoms} onChange={(e) => updateForm("symptoms", e.target.value)} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Chẩn đoán *</label>
                            <textarea className="form-control" rows="3" value={form.diagnosis} onChange={(e) => updateForm("diagnosis", e.target.value)} />
                        </div>
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Ghi chú</label>
                        <textarea className="form-control" rows="2" value={form.prescriptionNotes} onChange={(e) => updateForm("prescriptionNotes", e.target.value)} />
                    </div>

                    {/* Bệnh liên quan */}
                    <div className="mb-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label className="form-label mb-0">Bệnh liên quan</label>
                            <button type="button" className="btn btn-sm btn-outline-primary" onClick={addDiseaseRow}>+ Thêm bệnh</button>
                        </div>
                        {form.diseaseIds.map((disease, idx) => (
                            <div className="row g-2 mb-2" key={`disease-row-${idx}`}>
                                <div className="col-md-10">
                                    <SearchableSelect
                                        endpoint={endpoints["diseases"]}
                                        placeholder="Tìm bệnh..."
                                        onChange={(item) => updateDiseaseRow(idx, item)}
                                    />
                                </div>
                                <div className="col-md-2">
                                    <button type="button" className="btn btn-outline-danger w-100" onClick={() => removeDiseaseRow(idx)}>Xóa</button>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* Dịch vụ */}
                    <div className="mb-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label className="form-label mb-0">Dịch vụ chỉ định</label>
                            <button type="button" className="btn btn-sm btn-outline-primary" onClick={addServiceRow}>+ Thêm dịch vụ</button>
                        </div>
                        {form.selectedServices.map((row, idx) => (
                            <div className="row g-2 mb-2" key={`service-row-${idx}`}>
                                <div className="col-md-7">
                                    <SearchableSelect
                                        endpoint={endpoints["services"]}
                                        placeholder="Tìm dịch vụ..."
                                        onChange={(item) => updateServiceRow(idx, "serviceId", String(item.id))}
                                    />
                                </div>
                                <div className="col-md-3">
                                    <input type="number" min="1" className="form-control" value={row.quantity}
                                        onChange={(e) => updateServiceRow(idx, "quantity", e.target.value)} />
                                </div>
                                <div className="col-md-2">
                                    <button type="button" className="btn btn-outline-danger w-100" onClick={() => removeServiceRow(idx)}>Xóa</button>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* Thuốc */}
                    <div className="mb-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label className="form-label mb-0">Thuốc kê toa</label>
                            <button type="button" className="btn btn-sm btn-outline-primary" onClick={addMedicineRow}>+ Thêm thuốc</button>
                        </div>
                        {form.selectedMedicines.map((row, idx) => (
                            <div className="row g-2 mb-2" key={`medicine-row-${idx}`}>
                                <div className="col-md-4">
                                    <SearchableSelect
                                        endpoint={endpoints["medicines"]}
                                        placeholder="Tìm thuốc..."
                                        onChange={(item) => updateMedicineRow(idx, "medicineId", String(item.id))}
                                    />
                                </div>
                                <div className="col-md-2">
                                    <input type="number" min="1" className="form-control" value={row.quantity}
                                        onChange={(e) => updateMedicineRow(idx, "quantity", e.target.value)} />
                                </div>
                                <div className="col-md-4">
                                    <input className="form-control" placeholder="Liều dùng" value={row.usageInstruction}
                                        onChange={(e) => updateMedicineRow(idx, "usageInstruction", e.target.value)} />
                                </div>
                                <div className="col-md-2">
                                    <button type="button" className="btn btn-outline-danger w-100" onClick={() => removeMedicineRow(idx)}>Xóa</button>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="card-footer d-flex justify-content-end">
                    <button type="submit" className="btn btn-primary" disabled={submitting}>
                        {submitting ? "Đang tạo phiếu..." : "Tạo phiếu khám"}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default MedicalRecord;