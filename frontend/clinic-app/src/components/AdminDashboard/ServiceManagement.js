import { useEffect, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const ServiceManagement = () => {
    const PAGE_SIZE = 5; // Khớp với services_disease trong env của bạn
    const [services, setServices] = useState([]);
    const [kw, setKw] = useState("");
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [refresh, setRefresh] = useState(0);
    const [hasMore, setHasMore] = useState(false);

    const [showModal, setShowModal] = useState(false);
    const [isEdit, setIsEdit] = useState(false);
    const [formData, setFormData] = useState({ id: null, name: "", price: "" });

    useEffect(() => {
        const fetchServices = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(endpoints['services'], { params: { kw, page } });
                const newData = res.data || [];
                if (page === 1) setServices(newData);
                else setServices(prev => [...prev, ...newData]);
                setHasMore(newData.length === PAGE_SIZE);
            } catch (error) { console.error(error); } 
            finally { setLoading(false); }
        };
        const timer = setTimeout(() => fetchServices(), 300);
        return () => clearTimeout(timer);
    }, [kw, page, refresh]);

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isEdit) await Apis.put(endpoints['admin-service-detail'](formData.id), formData);
            else await Apis.post(endpoints['admin-services'], formData);
            setShowModal(false);
            setPage(1);
            setRefresh(prev => prev + 1);
        } catch (error) { alert("Lỗi khi lưu!"); }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Cảnh báo: Bạn có chắc chắn muốn xóa dịch vụ này không?")) return;
        
        try {
            await Apis.delete(endpoints['admin-service-detail'](id));
            alert("Đã xóa dịch vụ thành công!");
            
            setPage(1);
            setRefresh(prev => prev + 1);
        } catch (error) {
            alert("Không thể xóa! Dịch vụ này đã được xuất trong hóa đơn hoặc phiếu khám.");
            console.error("Lỗi khi xóa:", error);
        }
    };

    return (
        <div className="card shadow-sm border-0">
            <div className="card-header bg-danger text-white d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Quản Lý Dịch vụ</h5>
                <button className="btn btn-light btn-sm fw-bold" onClick={() => { setIsEdit(false); setFormData({id: null, name: "", price: ""}); setShowModal(true); }}>+ Thêm Dịch Vụ</button>
            </div>
            <div className="card-body">
                <input type="text" className="form-control mb-3" placeholder="Tìm tên dịch vụ..." value={kw} onChange={(e) => {setKw(e.target.value); setPage(1);}} />
                <table className="table table-hover text-center align-middle">
                    <thead className="table-light">
                        <tr><th>ID</th><th>Tên Dịch Vụ</th><th>Đơn Giá</th><th>Hành Động</th></tr>
                    </thead>
                    <tbody>
                        {services.map(s => (
                            <tr key={s.id}>
                                <td>{s.id}</td>
                                <td className="text-start fw-bold text-primary">{s.name}</td>
                                <td>{Number(s.price).toLocaleString()} VNĐ</td>
                                <td>
                                    <button className="btn btn-warning btn-sm me-2" onClick={() => { setIsEdit(true); setFormData(s); setShowModal(true); }}>
                                        Sửa
                                    </button>
                                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(s.id)}>
                                        Xóa
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {loading && <div className="text-center"><MySpinner /></div>}
                {!loading && hasMore && <div className="text-center"><button className="btn btn-outline-danger btn-sm" onClick={() => setPage(p => p + 1)}>Xem thêm...</button></div>}
            </div>

            {showModal && (
                <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog" style={{ marginTop: '15vh' }}>
                        <div className="modal-content">
                            <form onSubmit={handleSave}>
                                <div className="modal-header"><h5>{isEdit ? "Sửa Dịch Vụ" : "Thêm Dịch Vụ"}</h5></div>
                                <div className="modal-body">
                                    <div className="mb-3">
                                        <label>Tên dịch vụ</label>
                                        <input type="text" className="form-control" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
                                    </div> 
                                    <div className="mb-3">
                                        <label>Giá dịch vụ (VNĐ)</label>
                                        <input type="number" className="form-control" required value={formData.price} onChange={e => setFormData({...formData, price: e.target.value})} />
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Hủy</button>
                                    <button type="submit" className="btn btn-danger">Lưu</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ServiceManagement;