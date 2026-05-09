import { useContext, useState } from "react";
import { Container, Button, Dropdown } from "react-bootstrap";
import { Menu, X, Phone } from 'lucide-react';
import { Link, useNavigate } from "react-router-dom";
import { MyUserContext } from "../configs/Contexts";
import NotificationBell from "./Notification/NotificationBell";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();

    const handleLogout = () => {
        dispatch({ type: "LOGOUT" }); 
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        navigate("/login");
    };

    return (
        <nav className="w-100 bg-white shadow-sm">
            <Container className="px-3 py-3">
                <div className="d-flex justify-content-between align-items-center">
                    <div className="d-flex align-items-center gap-4">
                        <div className="fs-4 fw-bold text-primary flex-shrink-0">
                            <Link to="/" className="text-decoration-none">ĐA KHOA</Link>
                        </div>
                        <div className="d-none d-md-flex align-items-center gap-3 border-start ps-4">
                            <div className="bg-warning p-2 rounded text-white d-flex align-items-center justify-content-center"><Phone size={20} /></div>
                            <div>
                                <div className="text-secondary small fw-medium mb-1" style={{ fontSize: '0.75rem' }}>Tư vấn/Đặt khám</div>
                                <div className="fs-5 fw-bold text-warning" style={{ lineHeight: 1 }}>1900 1234</div>
                            </div>
                        </div>
                    </div>

                    {user?.role !== 'admin' && (
                        <div className="d-none d-lg-flex align-items-center gap-4">
                            {user?.role === 'pharmacist' ? (
                                <Link to="/pharmacist" className="text-dark text-decoration-none fw-medium">Quản lý kho thuốc</Link>
                            ) : user?.role === 'doctor' ? (
                                <Link to="/doctor-schedule" className="text-dark text-decoration-none fw-medium">Lịch của bác sĩ</Link>
                            ) : (
                                <>
                                    <Link to="/services" className="text-dark text-decoration-none fw-medium">Dịch vụ y tế</Link>
                                    <Link to="/health-check" className="text-dark text-decoration-none fw-medium">Khám sức khỏe</Link>
                                    {user?.role === 'patient' && (
                                        <>
                                            <Link to="/my-appointments" className="text-dark text-decoration-none fw-medium">Hẹn khám online</Link>
                                        </>
                                    )}
                                </>
                            )}
                        </div>
                    )}

                    <div className="d-flex align-items-center gap-2">
                        {user ? (
                            <>
                                {user.role === 'patient' && (
                                    <div className="me-2"><NotificationBell /></div>
                                )}
                                <Dropdown className="d-none d-sm-inline-flex">
                                    <Dropdown.Toggle variant="outline-primary">
                                        Chào, {user.fullName}
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu>
                                        <Dropdown.Item as={Link} to="/profile">Hồ sơ cá nhân</Dropdown.Item>
                                        {user.role === 'admin'      && <Dropdown.Item as={Link} to="/admin">Trang quản trị</Dropdown.Item>}
                                        {user.role === 'patient'    && <Dropdown.Item as={Link} to="/appointments">Lịch hẹn</Dropdown.Item>}
                                        {user.role === 'doctor'     && <Dropdown.Item as={Link} to="/doctor-schedule">Lịch khám</Dropdown.Item>}
                                        {user.role === 'pharmacist' && <Dropdown.Item as={Link} to="/pharmacist">Kho thuốc</Dropdown.Item>}
                                        <Dropdown.Divider />
                                        <Dropdown.Item onClick={handleLogout} className="text-danger">Đăng xuất</Dropdown.Item>
                                    </Dropdown.Menu>
                                </Dropdown>
                            </>
                        ) : (
                            <Button as={Link} to="/login" variant="outline-primary" className="d-none d-sm-inline-flex">
                                Đăng nhập
                            </Button>
                        )}

                        <button onClick={() => setIsOpen(!isOpen)} className="d-lg-none btn btn-light border-0 p-2">
                            {isOpen ? <X size={24} /> : <Menu size={24} />}
                        </button>
                    </div>
                </div>

                {/* Mobile Menu */}
                {isOpen && (
                    <div className="d-lg-none mt-3 pb-3 border-top pt-3 d-flex flex-column gap-2">
                        {user?.role !== 'admin' && (
                            <>
                                <Link to="/" className="text-dark text-decoration-none fw-medium py-2">Cơ sở y tế</Link>
                                <Link to="/health-check" className="text-dark text-decoration-none fw-medium py-2">Khám sức khỏe</Link>
                            </>
                        )}
                        <div className="pt-2 border-top">
                            {user ? (
                                <>
                                    <span className="text-primary fw-bold d-block py-2">Chào, {user.fullName}</span>
                                    <Button as={Link} to="/profile" variant="outline-info" className="w-100 mb-2">Hồ sơ</Button>
                                    <Button variant="danger" className="w-100" onClick={handleLogout}>Đăng xuất</Button>
                                </>
                            ) : (<Button as={Link} to="/login" variant="primary" className="w-100">Đăng nhập</Button>)}
                        </div>
                    </div>
                )}
            </Container>
        </nav>
    );
};

export default Header;