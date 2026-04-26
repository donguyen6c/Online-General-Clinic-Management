import React, { useState } from "react";
import MedicineManagement from "../../components/PharmacyDashboard/MedicineManagement";
import InventoryManagement from "../../components/PharmacyDashboard/InventoryManagement";
import InventoryAlerts from "../../components/PharmacyDashboard/InventoryAlerts";
import DispenseMedicine from "../../components/PharmacyDashboard/DispenseMedicine";

const Pharmacist = () => {
    const [activeTab, setActiveTab] = useState("dispense");

    return (
        <div className="container-fluid py-4">
            <h2 className="mb-4 text-success fw-bold border-bottom pb-2">
                BẢNG ĐIỀU KHIỂN DƯỢC SĨ
            </h2>
            
            <ul className="nav nav-tabs mb-4">
                <li className="nav-item">
                    <button 
                        className={`nav-link fw-bold ${activeTab === "dispense" ? "active text-success" : "text-secondary"}`} 
                        onClick={() => setActiveTab("dispense")}
                    >
                        <i className="bi bi-prescription2 me-2"></i> Cấp Phát Thuốc
                    </button>
                </li>
                <li className="nav-item">
                    <button 
                        className={`nav-link fw-bold ${activeTab === "alerts" ? "active text-danger" : "text-secondary"}`} 
                        onClick={() => setActiveTab("alerts")}
                    >
                        <i className="bi bi-exclamation-triangle me-2"></i> Cảnh Báo Kho
                    </button>
                </li>
                <li className="nav-item">
                    <button 
                        className={`nav-link fw-bold ${activeTab === "inventory" ? "active text-primary" : "text-secondary"}`} 
                        onClick={() => setActiveTab("inventory")}
                    >
                        <i className="bi bi-box-seam me-2"></i> Quản Lý Lô Kho
                    </button>
                </li>
                <li className="nav-item">
                    <button 
                        className={`nav-link fw-bold ${activeTab === "medicines" ? "active text-primary" : "text-secondary"}`} 
                        onClick={() => setActiveTab("medicines")}
                    >
                        <i className="bi bi-capsule me-2"></i> Danh Mục Thuốc
                    </button>
                </li>
            </ul>

            {/* KHU VỰC HIỂN THỊ NỘI DUNG TƯƠNG ỨNG VỚI TAB */}
            <div className="tab-content">
                {activeTab === "dispense" && <DispenseMedicine />}
                {activeTab === "alerts" && <InventoryAlerts />}
                {activeTab === "inventory" && <InventoryManagement />}
                {activeTab === "medicines" && <MedicineManagement />}
            </div>
        </div>
    );
};

export default Pharmacist;