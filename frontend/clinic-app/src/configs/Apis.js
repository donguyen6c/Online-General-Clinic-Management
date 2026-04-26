import axios from "axios";

export const endpoints = {
    'specialties':  '/specialties',
    'doctors':  '/doctors',
    'users': '/users',
    'login': '/login',
    'profile': '/secure/profile',
    'logout' : '/logout',
    'doctor-detail': doctorId => `/doctors/${doctorId}`,
    'slots': (doctorId, date) => `/doctors/${doctorId}/slots?date=${date}`,
    'booking': doctorId => `/secure/doctors/${doctorId}/appointments`,
    'patient-appointments': '/secure/patients/current-user/appointments',   
    'doctor-appointments': '/secure/doctor-schedule',     
    'get-meeting-url': (appointmentId) => `/secure/appointments/${appointmentId}/meeting-url`,
    // QUẢN LÝ DANH MỤC THUỐC
    'medicines': '/secure/pharmacy/medicines',
    'medicine-detail': (id) => `/secure/pharmacy/medicines/${id}`,

    // QUẢN LÝ KHO THUỐC
    'inventory': '/secure/pharmacy/inventory',
    'inventory-add': (medicineId) => `/secure/pharmacy/medicines/${medicineId}/inventory`,
    'inventory-detail': (id) => `/secure/pharmacy/inventory/${id}`,
    
    // CẢNH BÁO KHO
    'inventory-expiring': '/secure/pharmacy/inventory/expiring',
    'inventory-low-stock': '/secure/pharmacy/inventory/low-stock',

    // XUẤT THUỐC
    'dispense-medicine': (medicalRecordId) => `/secure/pharmacy/medical-records/${medicalRecordId}/dispense`,
    'medical-record-detail': (patientId, recordId) => `/secure/${patientId}/medical-records/${recordId}`,

    'services': '/services',
    'diseases': '/diseases',

    // QUẢN LÝ ADMIN (SECURE)
    'admin-diseases': '/secure/admin/diseases',
    'admin-disease-detail': (id) => `/secure/admin/diseases/${id}`,

    'admin-services': '/secure/admin/services',
    'admin-service-detail': (id) => `/secure/admin/services/${id}`,

}

const Apis = axios.create({
    baseURL: "http://localhost:8080/ClinicManagement/api/"
});

Apis.interceptors.request.use(
    (config) => {
        if (config.url && config.url.includes('/secure/')) {
            const token = localStorage.getItem("token"); 
            
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default Apis;

