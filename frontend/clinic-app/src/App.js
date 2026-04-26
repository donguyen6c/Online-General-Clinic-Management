import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./routes/ProtectedRoute";
import Login from "./screens/User/Login";
import Register from "./screens/User/Register";
import Profile from "./screens/User/Profile";
import Header from "./components/Header"
import Footer from "./components/Footer"
import Patient from "./screens/Patient/Patient";
import Pharmacist from "./screens/Pharmacist/Pharmacist";
import Doctor from "./screens/Admin/Doctors/UpdateDoctor";
import AdminDashboard from "./screens/Admin/AdminDashboard/AdminDashboard";
import 'bootstrap/dist/css/bootstrap.min.css';
import VideoCallPage from "./screens/VideoCall/VideoCallPage";
import MyAppointments from "./screens/MyAppointments/MyAppointments";
import DoctorSchedule from "./screens/DoctorSchedule/DoctorSchedule";

const App = () => {
  return (
    <BrowserRouter>
    <div className="d-flex flex-column min-vh-100">
    <Header/>
    <main className="flex-grow-1">
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route element={<ProtectedRoute />}>
            <Route path="/profile" element={<Profile />} />
        </Route>

        <Route element={<ProtectedRoute roles={'patient'} />}>
            <Route path="/patient" element={<Patient />} />
        </Route>

        <Route element={<ProtectedRoute roles={'pharmacist'} />}>
            <Route path="/pharmacist" element={<Pharmacist />} />
        </Route>

        <Route element={<ProtectedRoute roles={'doctor'} />}>
            <Route path="/doctor" element={<Doctor />} />
        </Route>

        <Route element={<ProtectedRoute roles={'admin'} />}>
            <Route path="/admin" element={<AdminDashboard />} />
            <Route path="/admin-doctors" element={<Doctor />} />
        </Route>

        <Route element={<ProtectedRoute/>}>
            <Route path="/video-call/:id" element={<VideoCallPage />} />
        </Route>
        
        <Route element={<ProtectedRoute/>}>
            <Route path="/my-appointments" element={<MyAppointments />} />
        </Route>

        <Route element={<ProtectedRoute/>}>
            <Route path="/doctor-schedule" element={<DoctorSchedule />} />
        </Route>

      </Routes>
      </main>
      <Footer/>
      </div>
    </BrowserRouter>
    
  );
}

export default App;