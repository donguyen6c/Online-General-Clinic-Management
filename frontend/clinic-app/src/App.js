import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./routes/ProtectedRoute";
import Login from "./screens/User/Login";
import Register from "./screens/User/Register";
import Profile from "./screens/User/Profile";
import Home from "./screens/Home/Home"
import Header from "./components/Header"
import Footer from "./components/Footer"
import Patient from "./screens/Patient/Patient";
import Pharmacist from "./screens/Pharmacist/Pharmacist";
import Doctor from "./screens/Doctor/Doctor";
import 'bootstrap/dist/css/bootstrap.min.css';

const App = () => {
  return (
    <BrowserRouter>
    <Header/>
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
            <Route path="/" element={<Home />} />
        </Route>

      </Routes>
      <Footer/>
    </BrowserRouter>
    
  );
}

export default App;