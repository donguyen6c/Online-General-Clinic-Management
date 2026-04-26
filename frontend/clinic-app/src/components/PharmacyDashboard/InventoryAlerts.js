import { useEffect, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";

const InventoryAlerts = () => {
    const [expiring, setExpiring] = useState([]);
    const [lowStock, setLowStock] = useState([]);

    useEffect(() => {
        const fetchAlerts = async () => {
            try {
                const [resExp, resLow] = await Promise.all([
                    Apis.get(endpoints['inventory-expiring']),
                    Apis.get(endpoints['inventory-low-stock'])
                ]);
                setExpiring(resExp.data || []);
                setLowStock(resLow.data || []);
            } catch (error) {
                console.error("Lỗi tải dữ liệu cảnh báo", error);
            }
        };
        fetchAlerts();
    }, []);

    return (
        <div className="row">
            <div className="col-md-6 mb-4">
                <div className="card border-danger h-100">
                    <div className="card-header bg-danger text-white fw-bold">
                        <i className="bi bi-calendar-x me-2"></i>Cảnh báo: Sắp hết hạn (Dưới 6 tháng)
                    </div>
                    <div className="card-body p-0 table-responsive">
                        <table className="table table-striped mb-0 text-center align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th>Mã Lô</th>
                                    <th>Tên Thuốc</th>
                                    <th>Hạn Dùng</th>
                                </tr>
                            </thead>
                            <tbody>
                                {expiring.length === 0 ? <tr><td colSpan="3" className="py-3">Kho an toàn</td></tr> : 
                                expiring.map(inv => (
                                    <tr key={inv.id}>
                                        <td>#{inv.id}</td>
                                        <td className="text-start">{inv.medicine?.name}</td>
                                        <td className="text-danger fw-bold">
                                            {new Date(inv.expiryDate).toLocaleDateString("vi-VN")}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div className="col-md-6 mb-4">
                <div className="card border-warning h-100">
                    <div className="card-header bg-warning text-dark fw-bold">
                        <i className="bi bi-box-arrow-down me-2"></i>Cảnh báo: Sắp hết hàng (Dưới 100)
                    </div>
                    <div className="card-body p-0 table-responsive">
                        <table className="table table-striped mb-0 text-center align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th>Mã Lô</th>
                                    <th>Tên Thuốc</th>
                                    <th>Tồn Kho</th>
                                </tr>
                            </thead>
                            <tbody>
                                {lowStock.length === 0 ? <tr><td colSpan="3" className="py-3">Kho an toàn</td></tr> : 
                                lowStock.map(inv => (
                                    <tr key={inv.id}>
                                        <td>#{inv.id}</td>
                                        <td className="text-start">{inv.medicine?.name}</td>
                                        <td className="text-danger fw-bold">{inv.quantity}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default InventoryAlerts;