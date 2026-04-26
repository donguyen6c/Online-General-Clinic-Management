import React, { useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../MySpinner";

const DispenseMedicine = () => {
    // Input state
    const [patientId, setPatientId] = useState("");
    const [recordId, setRecordId] = useState("");
    
    // Data state
    const [recordDetail, setRecordDetail] = useState(null); 
    const [prescription, setPrescription] = useState([]); 
    
    // UI state
    const [loading, setLoading] = useState(false);
    const [dispensing, setDispensing] = useState(false);
    const [step, setStep] = useState(1); // 1: Tìm kiếm, 2: Xác nhận xuất, 3: Thành công

    // BƯỚC 1: TÌM KIẾM ĐƠN THUỐC
    const handleSearch = async (e) => {
        e.preventDefault();
        if (!patientId || !recordId) return;
        
        setLoading(true);
        try {
            const res = await Apis.get(endpoints['medical-record-detail'](patientId, recordId));
            const detail = res.data;
            
            setRecordDetail(detail);
            setPrescription(detail.prescribedMedicines || detail.prescriptions || []);
            setStep(2); // Chuyển sang bước xem chi tiết
            
        } catch (error) {
            console.error("Lỗi tìm đơn thuốc", error);
            if (error.response?.status === 404) {
                alert("Không tìm thấy Phiếu khám. Vui lòng kiểm tra lại mã trên giấy!");
            } else {
                alert("Lỗi hệ thống hoặc bạn không có quyền truy cập!");
            }
        } finally {
            setLoading(false);
        }
    };

    // BƯỚC 2: XUẤT THUỐC
    const handleDispense = async () => {
        if (!window.confirm(`Xác nhận xuất thuốc cho bệnh nhân ${recordDetail?.patientName || 'này'}?`)) return;

        setDispensing(true);
        try {
            await Apis.post(endpoints['dispense-medicine'](recordId));
            setStep(3); // Chuyển sang bước thành công
        } catch (error) {
            console.error(error);
            const msg = error.response?.data?.message || error.response?.data?.error || "Kho không đủ thuốc để xuất!";
            alert(msg);
        } finally {
            setDispensing(false);
        }
    };

    // BƯỚC 3: LÀM LẠI TỪ ĐẦU (RESET)
    const handleReset = () => {
        setPatientId("");
        setRecordId("");
        setRecordDetail(null);
        setPrescription([]);
        setStep(1);
    };

    return (
        <div className="card shadow border-0" style={{ minHeight: '60vh' }}>
            <div className="card-header bg-primary text-white p-3 d-flex justify-content-between align-items-center">
                <h5 className="mb-0 fw-bold"><i className="bi bi-prescription2 me-2"></i>Cấp Phát Thuốc Dành Cho Dược Sĩ</h5>
                {step > 1 && (
                    <span className="badge bg-light text-primary fs-6">Phiếu #{recordId}</span>
                )}
            </div>

            <div className="card-body p-4 d-flex flex-column justify-content-center">
                
                {/* === BƯỚC 1: MÀN HÌNH TÌM KIẾM === */}
                {step === 1 && (
                    <div className="row justify-content-center">
                        <div className="col-md-8 text-center">
                            <div className="mb-4 text-muted">
                                <i className="bi bi-upc-scan" style={{ fontSize: '4rem' }}></i>
                                <h4 className="mt-2">Nhập Thông Tin Bệnh Án</h4>
                                <p>Vui lòng nhập mã bệnh nhân và mã phiếu khám in trên sổ khám bệnh.</p>
                            </div>

                            <form onSubmit={handleSearch} className="bg-light p-4 rounded-3 border">
                                <div className="row g-3">
                                    <div className="col-md-6 text-start">
                                        <label className="fw-bold mb-1 text-secondary">Mã Bệnh Nhân (Patient ID)</label>
                                        <input 
                                            type="text" inputMode="numeric" className="form-control form-control-lg" 
                                            placeholder="Điền mã bệnh nhân.." 
                                            value={patientId} onChange={(e) => setPatientId(e.target.value)} required autoFocus
                                        />
                                    </div>
                                    <div className="col-md-6 text-start">
                                        <label className="fw-bold mb-1 text-secondary">Mã Phiếu Khám (Record ID)</label>
                                        <input 
                                            type="text" inputMode="numeric" className="form-control form-control-lg" 
                                            placeholder="Điền mã phiếu khám.." 
                                            value={recordId} onChange={(e) => setRecordId(e.target.value)} required 
                                        />
                                    </div>
                                    <div className="col-12 mt-4">
                                        <button type="submit" className="btn btn-primary btn-lg w-100 fw-bold" disabled={loading}>
                                            {loading ? <><MySpinner /> Đang tìm kiếm...</> : "LẤY THÔNG TIN ĐƠN THUỐC"}
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {/* === BƯỚC 2: MÀN HÌNH XEM ĐƠN VÀ XUẤT THUỐC === */}
                {step === 2 && recordDetail && (
                    <div className="animation-fade-in">
                        <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
                            <div>
                                <h4 className="text-success fw-bold mb-1">
                                    <i className="bi bi-person-badge me-2"></i>BN: {recordDetail.patientName || `Bệnh nhân #${patientId}`}
                                </h4>
                                <p className="mb-0 text-muted"><strong>Chẩn đoán:</strong> {recordDetail.diagnosis || "Không có chẩn đoán"}</p>
                            </div>
                            <button className="btn btn-outline-secondary" onClick={() => setStep(1)}>
                                <i className="bi bi-arrow-left me-1"></i> Trở lại tìm kiếm
                            </button>
                        </div>

                        {prescription.length === 0 ? (
                            <div className="alert alert-warning text-center p-4">
                                <h5><i className="bi bi-exclamation-triangle"></i> Phiếu khám này không có kê thuốc!</h5>
                                <p className="mb-0">Bệnh nhân chỉ khám hoặc làm dịch vụ.</p>
                            </div>
                        ) : (
                            <>
                                <h6 className="fw-bold text-secondary mb-3">DANH SÁCH THUỐC CẦN GIAO:</h6>
                                <div className="table-responsive border rounded mb-4">
                                    <table className="table table-hover text-center align-middle mb-0">
                                        <thead className="table-light">
                                            <tr>
                                                <th width="10%">STT</th>
                                                <th width="40%" className="text-start">Tên Thuốc</th>
                                                <th width="15%">Số Lượng</th>
                                                <th width="35%" className="text-start">Liều Dùng / Hướng Dẫn</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {prescription.map((p, index) => (
                                                <tr key={p.id || index}>
                                                    <td>{index + 1}</td>
                                                    <td className="fw-bold text-start text-primary fs-5">{p.medicine?.name || p.medicineName}</td>
                                                    <td>
                                                        <span className="badge bg-danger fs-6 px-3 py-2">{p.quantity}</span>
                                                    </td>
                                                    <td className="text-start fst-italic text-secondary">{p.instruction}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>

                                <div className="text-end bg-light p-3 rounded border">
                                    <span className="text-muted me-3">Vui lòng kiểm tra kỹ số lượng trước khi xác nhận.</span>
                                    <button 
                                        className="btn btn-success btn-lg fw-bold px-5 shadow-sm" 
                                        onClick={handleDispense}
                                        disabled={dispensing}
                                    >
                                        {dispensing ? <><MySpinner /> ĐANG TRỪ KHO...</> : <><i className="bi bi-check2-circle me-2"></i>XÁC NHẬN ĐÃ GIAO THUỐC</>}
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                )}

                {/* === BƯỚC 3: MÀN HÌNH HOÀN TẤT === */}
                {step === 3 && (
                    <div className="text-center animation-fade-in py-5">
                        <div className="display-1 text-success mb-3">
                            <i className="bi bi-check-circle-fill"></i>
                        </div>
                        <h2 className="fw-bold text-success">Xuất Thuốc Thành Công!</h2>
                        <p className="text-muted fs-5">Hệ thống đã tự động trừ kho cho phiếu khám #{recordId}.</p>
                        
                        <div className="mt-5">
                            <button className="btn btn-primary btn-lg fw-bold px-5" onClick={handleReset}>
                                <i className="bi bi-person-plus me-2"></i>Xử Lý Ca Tiếp Theo
                            </button>
                        </div>
                    </div>
                )}

            </div>
        </div>
    );
};

export default DispenseMedicine;