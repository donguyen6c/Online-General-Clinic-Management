import axios from "axios";
import cookies from 'react-cookies'

export const endpoints = {
    //CHUNG
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
    'google-login': "/google-login", 


    'cancel-appointment': appointmentId => `/secure/appointments/${appointmentId}`,
    // CUỘC HẸN CỦA BÁC SĨ 
    'doctor-appointments': '/secure/doctor-schedule',
    'doctor-schedule': '/secure/doctor-schedule',
    'patients': '/secure/patients/',    
    'get-meeting-url': (appointmentId) => `/secure/appointments/${appointmentId}/meeting-url`,

    // LỊCH CỦA BÁC SĨ
    'doctor-schedules': '/secure/doctor/schedules',
    'doctor-schedule-detail': (id) => `/secure/doctor/schedules/${id}`,
    "doctor-working-patterns": "/secure/doctor/working-patterns",
    "doctor-working-pattern-detail": (id) => `/secure/doctor/working-patterns/${id}`,
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

    // QUẢN LÝ ADMIN
    'admin-diseases': '/secure/admin/diseases',
    'admin-disease-detail': (id) => `/secure/admin/diseases/${id}`,

    'admin-services': '/secure/admin/services',
    'admin-service-detail': (id) => `/secure/admin/services/${id}`,

    'admin-doctors': '/secure/admin/doctors', 
    'admin-doctor-detail': (id) => `/secure/admin/doctors/${id}`,

    // THÔNG BÁO
    'notifications': '/secure/notifications/',
    'mark-notification-read': (id) => `/secure/notifications/${id}/read`,
    'unread-notification-count': '/secure/notifications/unread-count',
    'appointments': '/secure/patients/current-user/appointments',
    'cancel-appointments': ap_id => `/secure/appointments/${ap_id}`,

    // HỒ SƠ BỆNH ÁN
    'medical-records': '/secure/medical-records',
    'medical-record-prescriptions': (medicalRecordId) => `/secure/medical-records/${medicalRecordId}/prescriptions`,
    'medical-record-services': (medicalRecordId) => `/secure/medical-records/${medicalRecordId}/services`,
    'current-user-medical-records': '/secure/patients/current-user/medical-records', 
    'patient-medical-records': (patientId) => `/secure/${patientId}/medical-records`,
    'appointments-doctor': '/secure/doctor-schedule',
    
    'medical-record-payment':(medicalRecordId) =>`/secure/patients/current-user/medical-records/${medicalRecordId}`, 
    'create-momo-payment':'/secure/payments/momo/create',
    'create-vnpay-payment':'/secure/payments/vnpay/create',
}

const Apis = axios.create({
    baseURL: process.env.REACT_APP_BASE_URL
});

Apis.interceptors.request.use(
    (config) => {
        if (config.url && config.url.includes('/secure/')) {
            const token = cookies.load('token') || localStorage.getItem("token");
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export const authApis = () => {
    return axios.create({
        baseURL: process.env.REACT_APP_BASE_URL,
        headers: {
            'Authorization': `Bearer ${cookies.load('token')}`
        }
    })
}

export default Apis;

