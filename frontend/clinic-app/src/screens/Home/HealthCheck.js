import { useEffect, useState } from "react";
import {
    Container,
    Row,
    Col,
    Form,
    Card,
    Button,
    Alert
} from "react-bootstrap";
import MySpinner from "../../components/MySpinner";
import Apis, { endpoints } from "../../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";

const HealthCheck = () => {
    const [searchParams] = useSearchParams();

    const [doctors, setDoctors] = useState([]);
    const [specialties, setSpecialties] = useState([]);
    const [kw, setKw] = useState("");
    const [specialtyId, setSpecialtyId] = useState(searchParams.get("specialtyId") || "");
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const loadSpecialties = async () => {
            try {
                const res = await Apis.get(endpoints["specialties"]);
                setSpecialties(res.data);
            } catch (ex) {
                console.log(ex);
            }
        };
        loadSpecialties();
    }, []);

    useEffect(() => {
        const fetchDoctors = async () => {
            try {
                setLoading(true);

                let url = `${endpoints["doctors"]}?page=${page}`;
                if (kw) url += `&kw=${kw}`;
                if (specialtyId) url += `&specialtyId=${specialtyId}`;

                const res = await Apis.get(url);
                console.info(res.data.data)

                if (page === 1) {
                    setDoctors(res.data.data);
                } else {
                    setDoctors(prev => [...prev, ...res.data.data]);
                }

                setHasMore(res.data.hasNext);
            } catch (ex) {
                console.log(ex);
            } finally {
                setLoading(false);
            }
        };

        const timeout = setTimeout(fetchDoctors, 500);
        return () => clearTimeout(timeout);

    }, [kw, specialtyId, page]);

    const handleSearchKw = (e) => {
        setKw(e.target.value);
        setPage(1);
    };

    const handleSearchSpecialty = (e) => {
        setSpecialtyId(e.target.value);
        setPage(1);
    };

    const handleLoadMore = () => {
        setPage(prev => prev + 1);
    };

    return (
        <Container className="py-4">
            <h2 className="text-center mb-4">Danh sách bác sĩ</h2>

            <Row className="mb-4">
                <Col md={6} className="mb-2">
                    <Form.Control
                        type="text"
                        placeholder="Tìm theo tên bác sĩ..."
                        value={kw}
                        onChange={handleSearchKw}
                    />
                </Col>

                <Col md={6}>
                    <Form.Select
                        value={specialtyId}
                        onChange={handleSearchSpecialty}
                    >
                        <option value="">-- Tất cả chuyên khoa --</option>
                        {specialties.map((s) => (
                            <option key={s.id} value={s.id}>
                                {s.name}
                            </option>
                        ))}
                    </Form.Select>
                </Col>
            </Row>

            <Row>
                {!loading && doctors.length === 0 && (
                    <Alert variant="warning" className="text-center">
                        Không có bác sĩ nào
                    </Alert>
                )}

                {doctors.map((d) => (
                    <Col md={4} className="mb-4" key={d.id}>
                        <Card className="h-100 shadow-sm">
                            <Card.Img
                                variant="top"
                                src={d.user.avatar}
                                style={{ height: "220px", objectFit: "cover" }}
                            />

                            <Card.Body>
                                <Card.Title>
                                    {d.user?.fullName}
                                </Card.Title>

                                <p className="mb-1">
                                    <strong>Chuyên khoa:</strong> {d.specialty?.name}
                                </p>

                                <p className="text-muted small">
                                    {d.bio}
                                </p>
                            </Card.Body>

                            <Card.Footer className="bg-white border-0">
                                <Button variant="outline-primary" className="w-100" 
                                onClick={() => navigate(`/doctors/${d.id}/booking`)}>
                                    Đặt lịch
                                </Button>
                            </Card.Footer>
                        </Card>
                    </Col>
                ))}
            </Row>

            {loading && (
                <div className="text-center">
                    <MySpinner />
                </div>
            )}

            {!loading && doctors.length > 0 && hasMore && (
                <div className="text-center mt-3">
                    <Button variant="outline-primary" onClick={handleLoadMore}>
                        Xem thêm
                    </Button>
                </div>
            )}
        </Container>
    );
};

export default HealthCheck;