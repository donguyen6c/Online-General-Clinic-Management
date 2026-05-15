import React, { useEffect, useMemo, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const Doctor = () => {
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [appointments, setAppointments] = useState([]);
    const [diseases, setDiseases] = useState([]);
    const [services, setServices] = useState([]);
    const [medicines, setMedicines] = useState([]);

    const [form, setForm] = useState({
        sourceType: "appointment",
        appointmentId: "",
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
                const [apptRes, diseaseRes, serviceRes, medicineRes] = await Promise.all([
                    Apis.get(endpoints["doctor-appointments"]),
                    Apis.get(endpoints["diseases"]),
                    Apis.get(endpoints["services"]),
                    Apis.get(endpoints["medicines"])
                ]);

                setAppointments(apptRes.data || []);
                setDiseases(diseaseRes.data || []);
                setServices(serviceRes.data || []);
                setMedicines(medicineRes.data || []);
            } catch (error) {
                console.error("Lỗi tải dữ liệu tạo phiếu khám:", error);
                setMessage({ type: "danger", text: "Không thể tải dữ liệu nền cho phiếu khám." });
            } finally {
                setLoading(false);
            }
        };

        loadInitialData();
    }, []);

    const updateForm = (key, value) => setForm(prev => ({ ...prev, [key]: value }));

    const toggleDisease = (id) => {
        setForm(prev => ({
            ...prev,
            diseaseIds: prev.diseaseIds.includes(id)
                ? prev.diseaseIds.filter(dId => dId !== id)
                : [...prev.diseaseIds, id]
        }));
    };

    const addServiceRow = () => {
        setForm(prev => ({ ...prev, selectedServices: [...prev.selectedServices, { serviceId: "", quantity: 1 }] }));
    };

    const addMedicineRow = () => {
        setForm(prev => ({ ...prev, selectedMedicines: [...prev.selectedMedicines, { medicineId: "", quantity: 1, usageInstruction: "" }] }));
    };

    const removeServiceRow = (idx) => {
        setForm(prev => ({ ...prev, selectedServices: prev.selectedServices.filter((_, i) => i !== idx) }));
    };

    const removeMedicineRow = (idx) => {
        setForm(prev => ({ ...prev, selectedMedicines: prev.selectedMedicines.filter((_, i) => i !== idx) }));
    };

    const updateServiceRow = (idx, key, value) => {
        setForm(prev => ({
            ...prev,
            selectedServices: prev.selectedServices.map((row, i) => i === idx ? { ...row, [key]: value } : row)
        }));
    };

    const updateMedicineRow = (idx, key, value) => {
        setForm(prev => ({
            ...prev,
            selectedMedicines: prev.selectedMedicines.map((row, i) => i === idx ? { ...row, [key]: value } : row)
        }));
    };

    const validate = () => {
        if (!form.symptoms.trim() || !form.diagnosis.trim())
            return "Triệu chứng và chẩn đoán là bắt buộc.";

        if (form.sourceType === "appointment" && !form.appointmentId)
            return "Vui lòng chọn lịch hẹn để tạo phiếu khám.";

        if (form.sourceType === "manual" && !form.patientId.trim())
            return "Vui lòng nhập patientId khi tạo không từ lịch hẹn.";

        const invalidService = form.selectedServices.some(s => !s.serviceId || Number(s.quantity) <= 0);
        if (invalidService) return "Dịch vụ phải có service và số lượng hợp lệ.";

        const invalidMedicine = form.selectedMedicines.some(m => !m.medicineId || Number(m.quantity) <= 0 || !m.usageInstruction.trim());
        if (invalidMedicine) return "Thuốc phải có thuốc, số lượng và liều dùng hợp lệ.";

        return "";
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({ type: "", text: "" });

        const errorMsg = validate();
        if (errorMsg) {
            setMessage({ type: "warning", text: errorMsg });
            return;
        }

        const payload = {
            symptoms: form.symptoms.trim(),
            diagnosis: form.diagnosis.trim(),
            prescriptionNotes: form.prescriptionNotes.trim(),
            diseaseIds: form.diseaseIds,
            appointmentId: form.sourceType === "appointment" ? Number(form.appointmentId) : null,
            patientId: form.sourceType === "manual" ? Number(form.patientId) : null
        };

        try {
            setSubmitting(true);
            const createRes = await Apis.post(endpoints["medical-records"], payload);
            const medicalRecordId = createRes?.data?.id;

            if (!medicalRecordId) {
                throw new Error("Không nhận được medicalRecordId từ BE.");
            }

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
            setForm(prev => ({ ...prev, symptoms: "", diagnosis: "", prescriptionNotes: "", diseaseIds: [], selectedServices: [], selectedMedicines: [] }));
        } catch (error) {
            console.error("Lỗi tạo phiếu khám:", error);
            setMessage({ type: "danger", text: error?.response?.data?.error || error?.response?.data || error.message || "Tạo phiếu khám thất bại." });
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
                                    <option key={appt.id} value={appt.id}>{`#${appt.id} - ${appt.patientName || "Bệnh nhân"} - ${appt.date} ${appt.time}`}</option>
                                ))}
                            </select>
                        </div>
                    ) : (
                        <div className="mb-3">
                            <label className="form-label">Patient ID</label>
                            <input className="form-control" value={form.patientId} onChange={(e) => updateForm("patientId", e.target.value)} placeholder="Nhập ID bệnh nhân" />
                        </div>
                    )}

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

                    <div className="mb-3">
                        <label className="form-label d-block">Bệnh liên quan</label>
                        <div className="row">
                            {diseases.map(d => (
                                <div className="col-md-4" key={d.id}>
                                    <div className="form-check">
                                        <input className="form-check-input" type="checkbox" checked={form.diseaseIds.includes(d.id)} onChange={() => toggleDisease(d.id)} id={`disease-${d.id}`} />
                                        <label className="form-check-label" htmlFor={`disease-${d.id}`}>{d.name}</label>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="mb-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label className="form-label mb-0">Dịch vụ chỉ định</label>
                            <button type="button" className="btn btn-sm btn-outline-primary" onClick={addServiceRow}>+ Thêm dịch vụ</button>
                        </div>
                        {form.selectedServices.map((row, idx) => (
                            <div className="row g-2 mb-2" key={`service-row-${idx}`}>
                                <div className="col-md-7">
                                    <select className="form-select" value={row.serviceId} onChange={(e) => updateServiceRow(idx, "serviceId", e.target.value)}>
                                        <option value="">-- Chọn dịch vụ --</option>
                                        {services.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                                    </select>
                                </div>
                                <div className="col-md-3">
                                    <input type="number" min="1" className="form-control" value={row.quantity} onChange={(e) => updateServiceRow(idx, "quantity", e.target.value)} />
                                </div>
                                <div className="col-md-2"><button type="button" className="btn btn-outline-danger w-100" onClick={() => removeServiceRow(idx)}>Xóa</button></div>
                            </div>
                        ))}
                    </div>

                    <div className="mb-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label className="form-label mb-0">Thuốc kê toa</label>
                            <button type="button" className="btn btn-sm btn-outline-primary" onClick={addMedicineRow}>+ Thêm thuốc</button>
                        </div>
                        {form.selectedMedicines.map((row, idx) => (
                            <div className="row g-2 mb-2" key={`medicine-row-${idx}`}>
                                <div className="col-md-4">
                                    <select className="form-select" value={row.medicineId} onChange={(e) => updateMedicineRow(idx, "medicineId", e.target.value)}>
                                        <option value="">-- Chọn thuốc --</option>
                                        {medicines.map(m => <option key={m.id} value={m.id}>{m.name}</option>)}
                                    </select>
                                </div>
                                <div className="col-md-2">
                                    <input type="number" min="1" className="form-control" value={row.quantity} onChange={(e) => updateMedicineRow(idx, "quantity", e.target.value)} />
                                </div>
                                <div className="col-md-4">
                                    <input className="form-control" placeholder="Liều dùng" value={row.usageInstruction} onChange={(e) => updateMedicineRow(idx, "usageInstruction", e.target.value)} />
                                </div>
                                <div className="col-md-2"><button type="button" className="btn btn-outline-danger w-100" onClick={() => removeMedicineRow(idx)}>Xóa</button></div>
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

export default Doctor;
