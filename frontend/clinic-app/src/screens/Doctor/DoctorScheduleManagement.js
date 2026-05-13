import { useState, useEffect } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const DoctorScheduleManagement = () => {
    const [schedules, setSchedules] = useState([]);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [submittingP, setSubmittingP] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const [form, setForm] = useState({
        workDate: "",
        isAvailable: false,
        startTime: "",
        endTime: "",
        note: ""
    });
    const [patterns, setPatterns] = useState([]);
    const [patternForm, setPatternForm] = useState({
        dayOfWeek: 1,
        startTime: "",
        endTime: ""
    });

    const update = (key, value) => setForm(prev => ({ ...prev, [key]: value }));

    const fetchSchedules = async () => {
        try {
            const res = await Apis.get(endpoints["doctor-schedules"]);
            setSchedules(res.data || []);
        } catch {
            setError("Không thể tải danh sách lịch.");
        } finally {
            setLoading(false);
        }
    };
    const fetchPatterns = async () => {
        try {
            const res = await Apis.get(endpoints["doctor-working-patterns"]);
            setPatterns(res.data || []);
        } catch {
            setError("Không thể tải lịch làm việc cố định.");
        }
    };

    useEffect(() => {
        fetchSchedules();
        fetchPatterns();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(""); setSuccess("");
        if (!form.workDate) { setError("Vui lòng chọn ngày"); return; }
        if (form.isAvailable && (!form.startTime || !form.endTime)) {
            setError("Vui lòng nhập giờ bắt đầu và kết thúc"); return;
        }
        try {
            setSubmitting(true);
            await Apis.post(endpoints["doctor-schedules"], form);
            setSuccess("Thêm ngoại lệ thành công.");
            setForm({ workDate: "", isAvailable: false, startTime: "", endTime: "", note: "" });
            fetchSchedules();
        } catch (err) {
            setError(err?.response?.data?.error || "Thêm thất bại.");
        } finally {
            setSubmitting(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Xóa ngoại lệ này?")) return;
        try {
            await Apis.delete(endpoints["doctor-schedule-detail"](id));
            setSchedules(prev => prev.filter(s => s.id !== id));
        } catch {
            setError("Xóa thất bại.");
        }
    };

    const handleSubmitPattern = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        if (!patternForm.startTime || !patternForm.endTime) {
            setError("Vui lòng nhập giờ bắt đầu và giờ kết thúc.");
            return;
        }

        if (patternForm.startTime >= patternForm.endTime) {
            setError("Giờ kết thúc phải sau giờ bắt đầu.");
            return;
        }

        try {
            setSubmittingP(true);

            await Apis.post(endpoints["doctor-working-patterns"], {
                dayOfWeek: Number(patternForm.dayOfWeek),
                startTime: patternForm.startTime,
                endTime: patternForm.endTime
            });

            setSuccess("Thêm lịch làm việc cố định thành công.");
            setPatternForm({
                dayOfWeek: 1,
                startTime: "",
                endTime: ""
            });

            fetchPatterns();
        } catch (err) {
            setError(err?.response?.data?.error || "Thêm lịch cố định thất bại.");
        } finally {
            setSubmittingP(false);
        }
    };

    const handleDeletePattern = async (id) => {
        if (!window.confirm("Xóa lịch làm việc cố định này?")) return;

        try {
            await Apis.delete(endpoints["doctor-working-pattern-detail"](id));
            setPatterns(prev => prev.filter(p => p.id !== id));
        } catch (err) {
            setError(err?.response?.data?.error || "Xóa lịch cố định thất bại.");
        }
    };

    const dayNames = {
        1: "Thứ 2",
        2: "Thứ 3",
        3: "Thứ 4",
        4: "Thứ 5",
        5: "Thứ 6",
        6: "Thứ 7",
        7: "Chủ nhật"
    };

    if (loading) return <MySpinner />;

    return (
        <div className="container py-4" style={{ maxWidth: 800 }}>
            <h4 className="text-primary mb-4">Quản lý lịch làm việc</h4>
            {error && <div className="alert alert-danger py-2">{error}</div>}
            {success && <div className="alert alert-success py-2">{success}</div>}

            <div className="card shadow-sm mb-4">
                <div className="card-header fw-bold">Thêm ngoại lệ</div>
                <div className="card-body">

                    <form onSubmit={handleSubmit}>
                        <div className="row g-3">
                            <div className="col-md-4">
                                <label className="form-label">Ngày</label>
                                <input type="date" className="form-control"
                                    value={form.workDate}
                                    onChange={e => update("workDate", e.target.value)} />
                            </div>

                            <div className="col-md-8">
                                <label className="form-label d-block">Loại</label>
                                <div className="form-check form-check-inline">
                                    <input className="form-check-input" type="radio"
                                        checked={!form.isAvailable}
                                        onChange={() => update("isAvailable", false)} />
                                    <label className="form-check-label">Nghỉ cả ngày</label>
                                </div>
                                <div className="form-check form-check-inline">
                                    <input className="form-check-input" type="radio"
                                        checked={form.isAvailable}
                                        onChange={() => update("isAvailable", true)} />
                                    <label className="form-check-label">Đổi giờ làm</label>
                                </div>
                            </div>

                            {form.isAvailable && <>
                                <div className="col-md-3">
                                    <label className="form-label">Từ</label>
                                    <input type="time" className="form-control"
                                        value={form.startTime}
                                        onChange={e => update("startTime", e.target.value)} />
                                </div>
                                <div className="col-md-3">
                                    <label className="form-label">Đến</label>
                                    <input type="time" className="form-control"
                                        value={form.endTime}
                                        onChange={e => update("endTime", e.target.value)} />
                                </div>
                            </>}

                            <div className="col-12">
                                <label className="form-label">Ghi chú</label>
                                <input className="form-control"
                                    placeholder="VD: Nghỉ ốm, họp viện..."
                                    value={form.note}
                                    onChange={e => update("note", e.target.value)} />
                            </div>
                        </div>

                        <button type="submit" className="btn btn-primary mt-3" disabled={submitting}>
                            {submitting ? "Đang lưu..." : "Lưu"}
                        </button>
                    </form>
                </div>
            </div>

            <div className="card shadow-sm mb-4">
                <div className="card-header fw-bold">Danh sách ngoại lệ</div>
                {schedules.length === 0 ? (
                    <div className="card-body text-muted">Chưa có ngoại lệ nào.</div>
                ) : (
                    <table className="table mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>Ngày</th>
                                <th>Giờ</th>
                                <th>Ghi chú</th>
                                <th>Loại</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {schedules.map(s => (
                                <tr key={s.id}>
                                    <td>{s.workDate}</td>
                                    <td>{s.isAvailable ? `${s.startTime} - ${s.endTime}` : "—"}</td>
                                    <td>{s.note || "—"}</td>
                                    <td>
                                        {s.isAvailable
                                            ? <span className="badge bg-info">Đổi giờ</span>
                                            : <span className="badge bg-danger">Nghỉ</span>}
                                    </td>
                                    <td>
                                        <button className="btn btn-sm btn-outline-danger"
                                            onClick={() => handleDelete(s.id)}>
                                            Xóa
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
            <div className="card shadow-sm mb-4">
                <div className="card-header fw-bold">Lịch làm việc cố định hằng tuần</div>

                <div className="card-body">
                    <form onSubmit={handleSubmitPattern}>
                        <div className="row g-3 align-items-end">
                            <div className="col-md-3">
                                <label className="form-label">Thứ</label>
                                <select
                                    className="form-select"
                                    value={patternForm.dayOfWeek}
                                    onChange={e =>
                                        setPatternForm(prev => ({
                                            ...prev,
                                            dayOfWeek: e.target.value
                                        }))
                                    }
                                >
                                    <option value={1}>Thứ 2</option>
                                    <option value={2}>Thứ 3</option>
                                    <option value={3}>Thứ 4</option>
                                    <option value={4}>Thứ 5</option>
                                    <option value={5}>Thứ 6</option>
                                    <option value={6}>Thứ 7</option>
                                    <option value={7}>Chủ nhật</option>
                                </select>
                            </div>

                            <div className="col-md-3">
                                <label className="form-label">Từ</label>
                                <input
                                    type="time"
                                    className="form-control"
                                    value={patternForm.startTime}
                                    onChange={e =>
                                        setPatternForm(prev => ({
                                            ...prev,
                                            startTime: e.target.value
                                        }))
                                    }
                                />
                            </div>

                            <div className="col-md-3">
                                <label className="form-label">Đến</label>
                                <input
                                    type="time"
                                    className="form-control"
                                    value={patternForm.endTime}
                                    onChange={e =>
                                        setPatternForm(prev => ({
                                            ...prev,
                                            endTime: e.target.value
                                        }))
                                    }
                                />
                            </div>

                            <div className="col-md-3">
                                <button
                                    type="submit"
                                    className="btn btn-primary w-100"
                                    disabled={submittingP}
                                >
                                    {submittingP ? "Đang lưu..." : "Thêm lịch"}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>

                {patterns.length === 0 ? (
                    <div className="card-body text-muted border-top">
                        Chưa có lịch làm việc cố định.
                    </div>
                ) : (
                    <table className="table mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>Thứ</th>
                                <th>Giờ làm</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {patterns.map(p => (
                                <tr key={p.id}>
                                    <td>{dayNames[p.dayOfWeek]}</td>
                                    <td>{p.startTime} - {p.endTime}</td>
                                    <td className="text-end">
                                        <button
                                            className="btn btn-sm btn-outline-danger"
                                            onClick={() => handleDeletePattern(p.id)}
                                        >
                                            Xóa
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
};

export default DoctorScheduleManagement;