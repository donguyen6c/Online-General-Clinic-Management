import { useEffect, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../MySpinner";

const InventoryManagement = () => {
    const PAGE_SIZE = 5; 
    const [inventories, setInventories] = useState([]);
    const [medicines, setMedicines] = useState([]); 
    const [kw, setKw] = useState("");
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);    
    const [showModal, setShowModal] = useState(false);
    const [isEdit, setIsEdit] = useState(false);
    const [formData, setFormData] = useState({ id: null, medicineId: "", quantity: "", expiryDate: "" });
    const [loadMore, setLoadMore] = useState(false);
    const [refresh, setRefresh] = useState(0); 
    useEffect(() => {
        const fetchInventories = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(endpoints['inventory'], { params: { kw, page } });
                
                const newData = res.data || []; 
                
                if (page === 1) {
                    setInventories(newData);
                } else {
                    setInventories(prev => [...prev, ...newData]);
                }

                setLoadMore(newData.length === PAGE_SIZE);

            } catch (error) {
                console.error("Lỗi tải kho", error);
            } finally {
                setLoading(false);
            }
        };

        const timer = setTimeout(() => fetchInventories(), 300);
        return () => clearTimeout(timer);
    }, [kw, page, refresh]); 

    useEffect(() => {
        const fetchAllMedicines = async () => {
            try {
                const res = await Apis.get(endpoints['medicines'] + "?all");
                setMedicines(res.data || []);
            } catch (error) {}
        };
        fetchAllMedicines();
    }, []);

    const handleSearch = (e) => {
        setKw(e.target.value);
        setPage(1);
    };

    const handleLoadMore = () => {
        setPage(prev => prev + 1);
    };

    const handleOpenModal = (inv = null) => {
        if (inv) {
            setIsEdit(true);
            const formattedDate = new Date(inv.expiryDate).toISOString().split('T')[0];
            const medId = inv.medicine?.id || inv.medicineId || "";
            
            setFormData({ 
                id: inv.id, 
                medicineId: medId, 
                quantity: inv.quantity, 
                expiryDate: formattedDate 
            });
        } else {
            setIsEdit(false);
            setFormData({ id: null, medicineId: "", quantity: "", expiryDate: "" });
        }
        setShowModal(true);
    };

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isEdit) {
                await Apis.put(endpoints['inventory-detail'](formData.id), formData);
                alert("Cập nhật lô hàng thành công!");
            } else {
                if (!formData.medicineId) {
                    alert("Vui lòng chọn loại thuốc!"); return;
                }
                await Apis.post(endpoints['inventory-add'](formData.medicineId), formData);
                alert("Nhập kho thành công!");
            }
            setShowModal(false);
            
            setPage(1); 
            setRefresh(prev => prev + 1); 
            
        } catch (error) {
            alert("Có lỗi xảy ra!");
            console.error(error);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa lô hàng này?")) return;
        try {
            await Apis.delete(endpoints['inventory-detail'](id));
            alert("Xóa thành công!");
            
            setPage(1);
            setRefresh(prev => prev + 1);
            
        } catch (error) {
            alert("Lỗi khi xóa!");
        }
    };

    return (
        <div className="card shadow-sm mb-4">
            <div className="card-header bg-success text-white d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Quản Lý Lô Kho (Nhập Hàng)</h5>
                <button className="btn btn-light btn-sm fw-bold" onClick={() => handleOpenModal()}>+ Nhập Lô Mới</button>
            </div>
            <div className="card-body">
                <input 
                    type="text" className="form-control mb-3" 
                    placeholder="Tìm tên thuốc trong kho..." 
                    value={kw} onChange={handleSearch} 
                />
                
                <table className="table table-bordered table-hover text-center align-middle">
                    <thead className="table-light">
                        <tr>
                            <th>Mã Lô</th><th>Tên Thuốc</th><th>Tồn Kho</th><th>Hạn Sử Dụng</th><th>Hành Động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {inventories.length === 0 && !loading ? <tr><td colSpan="5">Không có dữ liệu</td></tr> : inventories.map(inv => (
                            <tr key={inv.id}>
                                <td>#{inv.id}</td>
                                <td className="fw-bold text-start">{inv.medicine?.name}</td>
                                <td>
                                    <span className={`badge ${inv.quantity < 100 ? 'bg-danger' : 'bg-success'}`}>
                                        {inv.quantity} {inv.medicine?.unit}
                                    </span>
                                </td>
                                <td>{new Date(inv.expiryDate).toLocaleDateString("vi-VN")}</td>
                                <td>
                                    <button className="btn btn-warning btn-sm me-2" onClick={() => handleOpenModal(inv)}>Sửa</button>
                                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(inv.id)}>Xóa</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                {loading && <div className="text-center my-3"><MySpinner /></div>}

                {!loading && loadMore && (
                    <div className="text-center mt-3">
                        <button className="btn btn-outline-success" onClick={handleLoadMore}>
                            Xem thêm lô kho...
                        </button>
                    </div>
                )}
            </div>

            {showModal && (
                <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog" style={{ marginTop: '20vh' }}>
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{isEdit ? "Cập Nhật Lô Hàng" : "Nhập Kho Mới"}</h5>
                                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                            </div>
                            <form onSubmit={handleSave}>
                                <div className="modal-body">
                                    <div className="mb-3">
                                        <label className="form-label">Chọn Thuốc</label>
                                        <select 
                                            className="form-select" required
                                            value={formData.medicineId} 
                                            onChange={e => setFormData({...formData, medicineId: e.target.value})}
                                        >
                                            <option value="">-- Chọn thuốc --</option>
                                            {medicines.map(m => (
                                                <option key={m.id} value={m.id}>{m.name} ({m.unit})</option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Số Lượng</label>
                                        <input type="number" min="1" className="form-control" required value={formData.quantity} onChange={e => setFormData({...formData, quantity: e.target.value})} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Hạn Sử Dụng</label>
                                        <input type="date" className="form-control" required value={formData.expiryDate} onChange={e => setFormData({...formData, expiryDate: e.target.value})} />
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Hủy</button>
                                    <button type="submit" className="btn btn-success">Lưu Lô Hàng</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default InventoryManagement;