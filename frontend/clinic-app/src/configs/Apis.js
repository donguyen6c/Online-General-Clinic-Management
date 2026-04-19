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
}

export default axios.create({
    baseURL:"http://localhost:8081/ClinicManagement/api/"
})

