import { useEffect, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const DiseaseManagement = () => {
    const PAGE_SIZE = 5;
    
    const [diseases, setDiseases] = useState([]);
    const [kw, setKw] = useState("");
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [refresh, setRefresh] = useState(0); 
    const [hasMore, setHasMore] = useState(false);

    const [showModal, setShowModal] = useState(false);
    const [isEdit, setIsEdit] = useState(false);
    const [formData, setFormData] = useState({ id: null, name: "" });

    useEffect(() => {
        const fetchDiseases = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(endpoints['diseases'], { 
                    params: { kw, page } 
                });
                
                const newData = res.data || [];
                
                if (page === 1) {
                    setDiseases(newData);
                } else {
                    setDiseases(prev => [...prev, ...newData]);
                }

                setHasMore(newData.length === PAGE_SIZE);

            } catch (error) {
                console.error("Lỗi tải danh mục bệnh", error);
            } finally {
                setLoading(false);
            }
        };

        const timer = setTimeout(() => fetchDiseases(), 300);
        return () => clearTimeout(timer);
    }, [kw, page, refresh]);

    const handleSearch = (e) => {
        setKw(e.target.value);
        setPage(1);
    };

    const handleLoadMore = () => {
        setPage(prev => prev + 1);
    };

    const handleOpenModal = (dis = null) => {
        if (dis) {
            setIsEdit(true);
            setFormData({ id: dis.id, name: dis.name });
        } else {
            setIsEdit(false);
            setFormData({ id: null, name: "" });
        }
        setShowModal(true);
    };

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isEdit) {
                await Apis.put(endpoints['admin-disease-detail'](formData.id), formData);
                alert("Cập nhật loại bệnh thành công!");
            } else {
                await Apis.post(endpoints['admin-diseases'], formData);
                alert("Thêm bệnh mới thành công!");
            }
            
            setShowModal(false);
            // Ép load lại trang 1 để đồng bộ
            setPage(1);
            setRefresh(prev => prev + 1);
            
        } catch (error) {
            alert("Có lỗi xảy ra khi lưu dữ liệu!");
            console.error(error);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Cảnh báo: Bạn có chắc chắn muốn xóa loại bệnh này không?")) return;
        
        try {
            await Apis.delete(endpoints['admin-disease-detail'](id));
            alert("Đã xóa loại bệnh thành công!");
            
            // Ép load lại bảng từ trang 1 để cập nhật dữ liệu
            setPage(1);
            setRefresh(prev => prev + 1);
        } catch (error) {
            // Bắt lỗi nếu Backend ném ra Exception do vướng khóa ngoại
            alert("Không thể xóa! Loại bệnh này đã được sử dụng trong bệnh án của bệnh nhân.");
            console.error("Lỗi khi xóa:", error);
        }
    };

    return (
        <div className="card shadow-sm border-0 mb-4">
            <div className="card-header bg-dark text-white d-flex justify-content-between align-items-center p-3">
                <h5 className="mb-0"><i className="bi bi-virus2 me-2"></i>Danh Mục Loại Bệnh</h5>
                <button className="btn btn-success btn-sm fw-bold" onClick={() => handleOpenModal()}>
                    <i className="bi bi-plus-circle me-1"></i> Thêm Bệnh Mới
                </button>
            </div>
            
            <div className="card-body">
                <div className="mb-3">
                    <div className="input-group">
                        <span className="input-group-text bg-light"><i className="bi bi-search"></i></span>
                        <input 
                            type="text" className="form-control" 
                            placeholder="Tìm kiếm tên bệnh..." 
                            value={kw} onChange={handleSearch} 
                        />
                    </div>
                </div>
                
                <div className="table-responsive">
                    <table className="table table-hover text-center align-middle border">
                        <thead className="table-secondary">
                            <tr>
                                <th width="15%">ID</th>
                                <th width="65%" className="text-start">Tên Loại Bệnh</th>
                                <th width="20%">Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {diseases.length === 0 && !loading ? (
                                <tr><td colSpan="3" className="p-4 text-muted">Không tìm thấy loại bệnh nào</td></tr>
                            ) : (
                                diseases.map(d => (
                                    <tr key={d.id}>
                                        <td><span className="badge bg-light text-dark border">#{d.id}</span></td>
                                        <td className="fw-bold text-start text-dark">{d.name}</td>
                                        <td>
                                            <button className="btn btn-outline-warning btn-sm me-2" onClick={() => handleOpenModal(d)}>
                                                <i className="bi bi-pencil-square"></i> Sửa
                                            </button>
                                            <button className="btn btn-outline-danger btn-sm" onClick={() => handleDelete(d.id)}>
                                                <i className="bi bi-trash"></i> Xóa
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>

                {loading && <div className="text-center my-3"><MySpinner /></div>}

                {!loading && hasMore && (
                    <div className="text-center mt-3">
                        <button className="btn btn-outline-dark btn-sm px-4 fw-bold" onClick={handleLoadMore}>
                            Xem thêm danh mục bệnh...
                        </button>
                    </div>
                )}
            </div>

            {/* MODAL THÊM/SỬA - ĐÃ ĐẨY LÊN CAO 15VH */}
            {showModal && (
                <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.6)' }}>
                    <div className="modal-dialog" style={{ marginTop: '15vh' }}>
                        <div className="modal-content shadow-lg border-0">
                            <div className="modal-header bg-dark text-white">
                                <h5 className="modal-title">
                                    {isEdit ? "Cập Nhật Thông Tin Bệnh" : "Khai Báo Bệnh Mới"}
                                </h5>
                                <button type="button" className="btn-close btn-close-white" onClick={() => setShowModal(false)}></button>
                            </div>
                            <form onSubmit={handleSave}>
                                <div className="modal-body p-4">
                                    <div className="mb-3">
                                        <label className="form-label fw-bold">Tên loại bệnh</label>
                                        <input 
                                            type="text" 
                                            className="form-control form-control-lg" 
                                            placeholder="Nhập tên bệnh"
                                            required 
                                            value={formData.name} 
                                            onChange={e => setFormData({...formData, name: e.target.value})} 
                                        />
                                    </div>
                                </div>
                                <div className="modal-footer bg-light">
                                    <button type="button" className="btn btn-secondary px-4" onClick={() => setShowModal(false)}>Hủy</button>
                                    <button type="submit" className="btn btn-success px-4 fw-bold">
                                        {isEdit ? "Lưu Thay Đổi" : "Tạo Mới"}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DiseaseManagement;