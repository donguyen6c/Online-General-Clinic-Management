import { useEffect, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../MySpinner";

const MedicineManagement = () => {
    const PAGE_SIZE = 5;
    const [medicines, setMedicines] = useState([]);
    const [kw, setKw] = useState("");
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [isEdit, setIsEdit] = useState(false);
    const [formData, setFormData] = useState({ id: null, name: "", unit: "", price: "" });
    const [refresh, setRefresh] = useState(0);
    const [hasMore, setHasMore] = useState(false);

    useEffect(() => {
        const fetchMedicines = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(endpoints['medicines'], { params: { kw, page } });
                
                const newData = res.data || [];
                
                if (page === 1) {
                    setMedicines(newData);
                } else {
                    setMedicines(prev => [...prev, ...newData]);
                }

                setHasMore(newData.length === PAGE_SIZE); 

            } catch (error) {
                console.error("Lỗi tải thuốc", error);
            } finally {
                setLoading(false);
            }
        };

        const timer = setTimeout(() => fetchMedicines(), 300);
        return () => clearTimeout(timer);
        
    // Đưa biến 'refresh' vào để lắng nghe sự thay đổi
    }, [kw, page, refresh]);

    const handleSearch = (e) => {
        setKw(e.target.value);
        setPage(1);
    };

    const handleLoadMore = () => {
        setPage(prev => prev + 1);
    };

    const handleOpenModal = (med = null) => {
        if (med) {
            setIsEdit(true);
            setFormData({ id: med.id, name: med.name, unit: med.unit, price: med.price });
        } else {
            setIsEdit(false);
            setFormData({ id: null, name: "", unit: "", price: "" });
        }
        setShowModal(true);
    };

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isEdit) {
                await Apis.put(endpoints['medicine-detail'](formData.id), formData);
                alert("Cập nhật thành công!");
            } else {
                await Apis.post(endpoints['medicines'], formData);
                alert("Thêm mới thành công!");
            }
            setShowModal(false);
            
            // Ép Load lại bảng từ trang 1 để đồng bộ data mới nhất
            setPage(1); 
            setRefresh(prev => prev + 1);
            
        } catch (error) {
            alert("Có lỗi xảy ra! Vui lòng kiểm tra lại thông tin.");
            console.error(error);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa loại thuốc này?")) return;
        try {
            await Apis.delete(endpoints['medicine-detail'](id));
            alert("Xóa thành công!");
            
            // Ép Load lại bảng từ trang 1 thay vì dùng filter
            setPage(1);
            setRefresh(prev => prev + 1);
            
        } catch (error) {
            alert("Không thể xóa thuốc này (Có thể thuốc đã được kê trong đơn)!");
            console.error(error);
        }
    };

    return (
        <div className="card shadow-sm mb-4">
            <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Danh Mục Thuốc</h5>
                <button className="btn btn-light btn-sm fw-bold" onClick={() => handleOpenModal()}>+ Thêm Thuốc</button>
            </div>
            <div className="card-body">
                <input 
                    type="text" className="form-control mb-3" 
                    placeholder="Tìm tên thuốc..." 
                    value={kw} onChange={handleSearch} 
                />
                
                <table className="table table-bordered table-hover text-center align-middle">
                    <thead className="table-light">
                        <tr>
                            <th>ID</th><th>Tên Thuốc</th><th>Đơn Vị</th><th>Giá Gốc</th><th>Hành Động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {medicines.length === 0 && !loading ? <tr><td colSpan="5">Không có dữ liệu</td></tr> : medicines.map(m => (
                            <tr key={m.id}>
                                <td>{m.id}</td>
                                <td className="fw-bold text-primary text-start">{m.name}</td>
                                <td>{m.unit}</td>
                                <td>{Number(m.price).toLocaleString("vi-VN")} VNĐ</td>
                                <td>
                                    <button className="btn btn-warning btn-sm me-2" onClick={() => handleOpenModal(m)}>Sửa</button>
                                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(m.id)}>Xóa</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                
                {loading && <div className="text-center my-3"><MySpinner /></div>}

                {!loading && hasMore && (
                    <div className="text-center mt-3">
                        <button className="btn btn-outline-primary" onClick={handleLoadMore}>
                            Xem thêm thuốc...
                        </button>
                    </div>
                )}
            </div>

            {showModal && (
                <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{isEdit ? "Cập Nhật Thuốc" : "Thêm Thuốc Mới"}</h5>
                                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                            </div>
                            <form onSubmit={handleSave}>
                                <div className="modal-body">
                                    <div className="mb-3">
                                        <label className="form-label">Tên thuốc</label>
                                        <input type="text" className="form-control" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Đơn vị (Viên, Hộp, Chai...)</label>
                                        <input type="text" className="form-control" required value={formData.unit} onChange={e => setFormData({...formData, unit: e.target.value})} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Giá (VNĐ)</label>
                                        <input type="number" min="0" className="form-control" required value={formData.price} onChange={e => setFormData({...formData, price: e.target.value})} />
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Hủy</button>
                                    <button type="submit" className="btn btn-primary">Lưu Lại</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MedicineManagement;