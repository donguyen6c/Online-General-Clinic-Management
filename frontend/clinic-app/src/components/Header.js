import { Button, Container, Dropdown } from "react-bootstrap";
import { Menu, X, Phone, Globe } from 'lucide-react';
import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../configs/Apis";

const Header = () => {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const [isOpen, setIsOpen] = useState(false);
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const checkUser = () => {
            const storedUser = localStorage.getItem("user");
            if (storedUser) {
                setUser(JSON.parse(storedUser));
            } else {
                setUser(null);
            }
        };

        checkUser();

        window.addEventListener("authChange", checkUser);

        return () => {
            window.removeEventListener("authChange", checkUser);
        };
    }, []);

    const toggleMenu = () => setIsOpen(!isOpen);

    const handleLogout = async () => {
        try {
            await Apis.post(endpoints['logout']);
        } catch (error) {
            console.error("Lỗi khi đăng xuất API:", error);
        } finally {
            localStorage.removeItem("user");
            localStorage.removeItem("token");
            
            window.dispatchEvent(new Event("authChange")); 
            
            navigate("/login");
        }
    };

    return (
        <nav className="w-100 bg-white shadow-sm">
            <div className="bg-light border-bottom">
                <Container className="px-3 py-2 d-flex justify-content-between align-items-center small">
                    <div className="d-flex gap-3 text-secondary">
                        <a href="/" className="text-secondary text-decoration-none">TikTok</a>
                        <span>|</span>
                        <a href="/" className="text-secondary text-decoration-none">Facebook</a>
                        <span>|</span>
                        <a href="/" className="text-secondary text-decoration-none">Zalo</a>
                        <span>|</span>
                        <a href="/" className="text-secondary text-decoration-none">YouTube</a>
                    </div>
                    <div className="d-flex gap-3 align-items-center">
                        <button className="btn btn-link text-secondary text-decoration-none p-0 d-flex align-items-center gap-1">
                            <Globe size={16} />
                            <span>VN</span>
                        </button>
                    </div>
                </Container>
            </div>

            <Container className="px-3 py-3">
                <div className="d-flex justify-content-between align-items-center">
                    <div className="d-flex align-items-center gap-4">
                        <div className="fs-4 fw-bold text-primary flex-shrink-0">
                            <Link to="/" className="text-decoration-none">ĐA KHOA</Link>
                        </div>

                        <div className="d-none d-md-flex align-items-center gap-3 border-start ps-4">
                            <div className="bg-warning p-2 rounded text-white d-flex align-items-center justify-content-center">
                                <Phone size={20} />
                            </div>
                            <div>
                                <div className="text-secondary small fw-medium mb-1" style={{ fontSize: '0.75rem' }}>
                                    Tư vấn/Đặt khám
                                </div>
                                <div className="fs-5 fw-bold text-warning" style={{ lineHeight: 1 }}>
                                    1900 2115
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="d-none d-lg-flex align-items-center gap-4">
                        <Link to="/" className="text-dark text-decoration-none fw-medium">Cơ sở y tế</Link>
                        <Link to="/" className="text-dark text-decoration-none fw-medium">Dịch vụ y tế</Link>
                        <Link to="/" className="text-dark text-decoration-none fw-medium">Khám sức khỏe</Link>
                        <Link to="/" className="text-dark text-decoration-none fw-medium">Tin tức</Link>
                    </div>

                    <ul className="navbar-nav me-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/doctors">Tìm Bác sĩ</Link>
                        </li>

                        {/* KIỂM TRA ROLE ĐỂ HIỂN THỊ MENU TƯƠNG ỨNG */}
                        {currentUser?.role === 'patient' && (
                            <li className="nav-item">
                                <Link className="nav-link text-primary fw-bold" to="/my-appointments">
                                    📅 Lịch hẹn của tôi
                                </Link>
                            </li>
                        )}

                        {currentUser?.role === 'doctor' && (
                            <li className="nav-item">
                                <Link className="nav-link text-danger fw-bold" to="/doctor-schedule">
                                    🩺 Lịch khám hôm nay
                                </Link>
                            </li>
                        )}
                    </ul>

                    <div className="d-flex align-items-center gap-2">
                        {user ? (
                            <Dropdown className="d-none d-sm-inline-flex">
                                <Dropdown.Toggle variant="outline-primary" id="dropdown-user">
                                    Chào, {user.fullName || user.username}
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item as={Link} to="/profile">Hồ sơ cá nhân</Dropdown.Item>
                                    {user.role === 'admin' && (
                                        <Dropdown.Item as={Link} to="/admin">Trang quản trị</Dropdown.Item>
                                    )}
                                    <Dropdown.Divider />
                                    <Dropdown.Item onClick={handleLogout} className="text-danger">
                                        Đăng xuất
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        ) : (
                            <Button
                                as={Link}
                                to="/login"
                                variant="outline-primary"
                                className="d-none d-sm-inline-flex"
                            >
                                Đăng nhập
                            </Button>
                        )}

                        <button
                            onClick={toggleMenu}
                            className="d-lg-none btn btn-light border-0 p-2 d-flex align-items-center justify-content-center"
                        >
                            {isOpen ? <X size={24} className="text-dark" /> : <Menu size={24} className="text-dark" />}
                        </button>
                    </div>
                </div>

                {/* Mobile Menu */}
                {isOpen && (
                    <div className="d-lg-none mt-3 pb-3 border-top pt-3 d-flex flex-column gap-2">
                        <Link to="/" className="text-dark text-decoration-none fw-medium py-2">Cơ sở y tế</Link>
                        <Link to="/" className="text-dark text-decoration-none fw-medium py-2">Dịch vụ y tế</Link>
                        <Link to="/" className="text-dark text-decoration-none fw-medium py-2">Khám sức khỏe</Link>
                        <Link to="/" className="text-dark text-decoration-none fw-medium py-2">Tin tức</Link>
                        
                        <div className="d-flex flex-column gap-2 pt-2 border-top">
                            {user ? (
                                <>
                                    <span className="text-primary fw-bold py-2">Chào, {user.fullName || user.username}</span>
                                    <Button as={Link} to="/profile" variant="outline-info" className="w-100 mb-2">Hồ sơ</Button>
                                    <Button variant="danger" className="w-100" onClick={handleLogout}>Đăng xuất</Button>
                                </>
                            ) : (
                                <Button as={Link} to="/login" variant="primary" className="w-100">
                                    Đăng nhập / Tài khoản
                                </Button>
                            )}
                        </div>
                    </div>
                )}
            </Container>
        </nav>
    );
}

export default Header;