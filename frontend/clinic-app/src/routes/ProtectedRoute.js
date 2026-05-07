import { Navigate, Outlet } from "react-router-dom";
import cookies from "react-cookies";

const ProtectedRoute = ({ roles }) => {
  const user = cookies.load("user");

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (roles && !roles.includes(user.role)) {
    return <Navigate to="/" />;
  }

  return <Outlet />;
};

export default ProtectedRoute;