import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./routes/ProtectedRoute";
import Login from "./screens/User/Login";
import Register from "./screens/User/Register";
import Profile from "./screens/User/Profile";
import Header from "./components/Header"
import Footer from "./components/Footer"
import Pharmacist from "./screens/Pharmacist/Pharmacist";
import AdminDashboard from "./screens/Admin/AdminDashboard/AdminDashboard";
import 'bootstrap/dist/css/bootstrap.min.css';
import VideoCallPage from "./screens/VideoCall/VideoCallPage";
import MyAppointments from "./screens/MyAppointments/MyAppointments";
import DoctorSchedule from "./screens/Doctor/DoctorSchedule";
import Booking from "./screens/Patient/Booking";
import Appointments from "./screens/Patient/Appointments";
import Home from "./screens/Home/Home"
import HealthCheck from "./screens/Home/HealthCheck";
import Services from "./screens/Home/Services";
import { useReducer } from "react";
import cookies from "react-cookies";
import { MyUserContext } from "./configs/Contexts";
import MyUserReducer from "./reducers/MyUserReducer";
import Doctor from "./screens/Doctor/Doctor"
import MedicalRecord from "./screens/Doctor/MedicalRecord";
import RecordHistory from "./screens/Patient/RecordHistory";
import Payment from "./screens/Patient/Payment";
import PaymentResult from "./screens/Patient/PaymentResult";
import PatientRecordHistory from "./screens/Doctor/PatientRecordHistory";
import DoctorScheduleManagement from "./screens/Doctor/DoctorScheduleManagement";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, cookies.load("user") || null);

  return (
    <MyUserContext.Provider value={[user, dispatch]}>
    <BrowserRouter>
    <div className="d-flex flex-column min-vh-100">
    <Header/>
    <main className="flex-grow-1">
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Home/>} />
        <Route path="/health-check" element={<HealthCheck />} />
        <Route path="/services" element={<Services/>}/>

        <Route element={<ProtectedRoute />}>
            <Route path="/profile" element={<Profile />} />
        </Route>

        <Route element={<ProtectedRoute roles={'patient'} />}>
            <Route path="/doctors/:doctorId/booking" element={<Booking />} />
            <Route path="/appointments" element={<Appointments />} />
            <Route path="/my-appointments" element={<MyAppointments />} />
            <Route path="/record-history" element={<RecordHistory />} />
            <Route path="/payment/:recordId" element={<Payment />} />
            <Route path="/payment-result" element={<PaymentResult />} />
        </Route>

        <Route element={<ProtectedRoute roles={'pharmacist'} />}>
            <Route path="/pharmacist" element={<Pharmacist />} />
        </Route>

        <Route element={<ProtectedRoute roles={'doctor'} />}>
            <Route path="/doctor" element={<Doctor />} />
            <Route path="/doctor-schedule" element={<DoctorSchedule />} />
            <Route path="/medical-record" element={<MedicalRecord />} />
            <Route path="/patient-record-history" element={<PatientRecordHistory />} />
            <Route path="/doctor/schedules" element={<DoctorScheduleManagement />}/>
        </Route>

        <Route element={<ProtectedRoute roles={'admin'} />}>
            <Route path="/admin" element={<AdminDashboard />} />
        </Route>

        <Route element={<ProtectedRoute/>}>
            <Route path="/video-call/:id" element={<VideoCallPage />} />
        </Route>s

      </Routes>
      </main>
      <Footer/>
      </div>
    </BrowserRouter>
    </MyUserContext.Provider>
  );
}

export default App;