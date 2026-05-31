import { useState, useContext } from "react";
import { useNavigate, useSearchParams, Link } from "react-router-dom";
import { Container, Form, Button, Alert } from "react-bootstrap";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { MyUserContext } from "../../configs/Contexts";
import MySpinner from "../../components/MySpinner";
import cookies from "react-cookies";
import { signInWithPopup } from "firebase/auth";
import { auth, googleProvider } from "../../configs/firebase";

const userInfo = [
    { field: "username", label: "Tên đăng nhập", type: "text"     },
    { field: "password", label: "Mật khẩu",      type: "password" },
];

const Login = () => {
    const [user, setUser]       = useState({});
    const [loading, setLoading] = useState(false);
    const [error, setError]     = useState("");
    const [, dispatch]          = useContext(MyUserContext);
    const [q]                   = useSearchParams();
    const navigate              = useNavigate();

    const handleAfterLogin = async (token) => {
        cookies.save("token", token);
        const p = await authApis().get(endpoints["profile"]);
        cookies.save("user", p.data);
        dispatch({ type: "LOGIN", payload: p.data });

        const next = q.get("next");
        if (next) {
            navigate(next);
        } else {
            switch (p.data.role) {
                case "admin":      navigate("/admin");      break;
                case "doctor":     navigate("/doctor-schedule");     break;
                case "pharmacist": navigate("/pharmacist"); break;
                default:           navigate("/");
            }
        }
    };

    const login = async (e) => {
        e.preventDefault();
        try {
            setLoading(true);
            const res = await Apis.post(endpoints["login"], { ...user });
            await handleAfterLogin(res.data.token);
        } catch (ex) {
            console.error(ex);
            setError("Sai tên đăng nhập hoặc mật khẩu!");
        } finally {
            setLoading(false);
        }
    };

    const loginWithGoogle = async () => {
        try {
            setLoading(true);
            const result = await signInWithPopup(auth, googleProvider);
            const idToken = await result.user.getIdToken();

            const res = await Apis.post(endpoints["google-login"], { idToken });
            await handleAfterLogin(res.data.token);
        } catch (ex) {
            console.error(ex);
            setError("Đăng nhập Google thất bại!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: "400px" }}>
            <h2 className="text-center text-primary mb-4">ĐĂNG NHẬP</h2>

            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={login} className="border p-4 shadow-sm rounded bg-white">
                {userInfo.map(f => (
                    <Form.Group key={f.field} className="mb-3" controlId={f.field}>
                        <Form.Label>{f.label}</Form.Label>
                        <Form.Control type={f.type} value={user[f.field] || ""} onChange={e => setUser({ ...user, [f.field]: e.target.value })} required/>
                    </Form.Group>
                ))}

                {loading ? <MySpinner /> : (
                    <>
                        <Button variant="primary" type="submit" className="w-100 mb-2">
                            Đăng nhập
                        </Button>

                        <Button variant="outline-danger" className="w-100" type="button" onClick={loginWithGoogle}>
                            Đăng nhập bằng Google
                        </Button>
                    </>
                )}

                <div className="text-center mt-3">
                    Chưa có tài khoản? <Link to="/register">Đăng ký</Link>
                </div>
            </Form>
        </Container>
    );
};

export default Login;