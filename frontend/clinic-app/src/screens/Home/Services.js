import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const PAGE_SIZE = 6;

const Services = () => {
    const [services, setServices] = useState([]);
    const [loading,  setLoading]  = useState(false);
    const [page,     setPage]     = useState(1);
    const [hasMore,  setHasMore]  = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetch = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(endpoints["services"], { params: { page } });
                const data = res.data || [];

                if (page === 1) setServices(data);
                else setServices(prev => [...prev, ...data]);

                setHasMore(data.length === PAGE_SIZE);
            } catch (ex) {
                console.error(ex);
            } finally {
                setLoading(false);
            }
        };
        fetch();
    }, [page]);

    return (
        <Container className="py-5">
            <h2 className="text-center fw-bold mb-2">Dịch vụ y tế</h2>
            <p className="text-center text-muted mb-4">Các dịch vụ chăm sóc sức khỏe chất lượng cao</p>

            <div className="d-flex justify-content-center mb-4">
                <Button variant="primary" onClick={() => navigate("/health-check")}>
                    Đặt lịch khám
                </Button>
            </div>

            <Row className="g-4">
                {services.map(service => (
                    <Col key={service.id} md={6} lg={4}>
                        <Card className="h-100 shadow-sm border-primary rounded-3">
                            <Card.Body>
                                <Card.Title className="fw-bold">{service.name}</Card.Title>
                                {service.description && (
                                    <Card.Text className="text-muted small">{service.description}</Card.Text>
                                )}
                                <span className="badge rounded-pill px-3 py-2"
                                    style={{ background: "#ccfbf1", color: "#0d9488", fontSize: "0.9rem" }}>
                                    {Number(service.price).toLocaleString("vi-VN")} ₫
                                </span>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>

            {loading && <div className="text-center mt-4"><MySpinner /></div>}

            {!loading && hasMore && (
                <div className="text-center mt-4">
                    <Button variant="outline-primary" onClick={() => setPage(p => p + 1)}>
                        Xem thêm
                    </Button>
                </div>
            )}
        </Container>
    );
};

export default Services;