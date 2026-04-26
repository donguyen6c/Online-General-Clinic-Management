import React, { useState } from "react";
import { Tab, Tabs } from "react-bootstrap";
import ServiceManagement from "../../../components/AdminDashboard/ServiceManagement";
import DiseaseManagement from "../../../components/AdminDashboard/DiseaseManagement";

const AdminDashboard = () => {
    const [key, setKey] = useState("services");

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4 fw-bold text-uppercase text-danger">
                <i className="bi bi-shield-lock me-2"></i>Bảng Điều Khiển Quản Trị
            </h2>
            
            <Tabs
                id="admin-tabs"
                activeKey={key}
                onSelect={(k) => setKey(k)}
                className="mb-4 nav-pills nav-justified shadow-sm rounded bg-white p-2"
            >
                <Tab eventKey="services" title={<span><i className="bi bi-gear-wide-connected me-2"></i>Dịch Vụ Y Tế</span>}>
                    <ServiceManagement />
                </Tab>
                <Tab eventKey="diseases" title={<span><i className="bi bi-virus me-2"></i>Danh Mục Bệnh</span>}>
                    <DiseaseManagement />
                </Tab>
            </Tabs>
        </div>
    );
};

export default AdminDashboard;