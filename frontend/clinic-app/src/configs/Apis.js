import axios from "axios";

export const endpoints = {
    'specialties':  '/specialties',
    'doctors':  '/doctors',
    'users': '/users',
    'login': '/login',
    'profile': '/secure/profile',
    'logout' : '/logout'
}

export default axios.create({
    baseURL:"http://localhost:8080/ClinicManagement/api/"
})