import { useEffect, useState } from "react";
import { Button, Card, Col, Container, Row, Table, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const Payment = () => {
    const { recordId } = useParams();

    const [record, setRecord] = useState(null);
    const [loading, setLoading] = useState(true);
    const [paying, setPaying] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        Apis.get(endpoints["medical-record-detail"](recordId))
            .then(res => setRecord(res.data))
            .catch(err => {
                console.error(err);
                setError("Không thể tải thông tin thanh toán.");
            })
            .finally(() => setLoading(false));
    }, [recordId]);

    if (loading) return <MySpinner />;

    if (error) {
        return (
            <Container className="py-4">
                <Alert variant="danger">{error}</Alert>
            </Container>
        );
    }

    const formatMoney = (value) => {
        if (value == null) return "Chưa có giá";
        return Number(value).toLocaleString("vi-VN") + "₫";
    };

    const serviceTotal = record.services?.reduce((sum, s) => {
        return sum + Number(s.priceAtTime || 0) * Number(s.quantity || 0);
    }, 0) || 0;

    const medicineTotal = record.prescriptions?.reduce((sum, p) => {
        return sum + Number(p.price || 0) * Number(p.quantity || 0);
    }, 0) || 0;

    const totalAmount = serviceTotal + medicineTotal;

    const handlePayment = () => {
        setPaying(true);

        Apis.post(endpoints["create-payment"], {
            medicalRecordId: recordId
        })
            .then(res => {
                window.location.href = res.data.paymentUrl;
            })
            .catch(err => {
                console.error(err);
                alert("Tạo thanh toán thất bại.");
            })
            .finally(() => setPaying(false));
    };

    return (
        <Container className="py-4">
            <h2 className="text-primary mb-4">Thanh toán phiếu khám</h2>

            <Row>
                <Col md={8}>
                    <Card className="mb-3 shadow-sm">
                        <Card.Header className="d-flex justify-content-between">
                            <span className="fw-bold">#{record.recordId} — {record.doctorName}</span>
                            <span className="text-muted small">{record.date}</span>
                        </Card.Header>

                        <Card.Body>
                            <p><strong>Chẩn đoán:</strong> {record.diagnosis}</p>
                        </Card.Body>
                    </Card>

                    <Card className="mb-3 shadow-sm">
                        <Card.Header className="fw-bold">Dịch vụ</Card.Header>
                        <Card.Body>
                            <Table bordered hover responsive>
                                <thead>
                                    <tr>
                                        <th>Tên dịch vụ</th>
                                        <th>Số lượng</th>
                                        <th>Đơn giá</th>
                                        <th>Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {record.services?.length > 0 ? record.services.map((s, i) => (
                                        <tr key={i}>
                                            <td>{s.serviceName}</td>
                                            <td>{s.quantity}</td>
                                            <td>{formatMoney(s.priceAtTime)}</td>
                                            <td>{formatMoney(s.priceAtTime * s.quantity)}</td>
                                        </tr>
                                    )) : (
                                        <tr>
                                            <td colSpan="4" className="text-center text-muted">
                                                Không có dịch vụ
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>

                    <Card className="shadow-sm">
                        <Card.Header className="fw-bold">Đơn thuốc</Card.Header>
                        <Card.Body>
                            <Table bordered hover responsive>
                                <thead>
                                    <tr>
                                        <th>Tên thuốc</th>
                                        <th>Số lượng</th>
                                        <th>Hướng dẫn</th>
                                        <th>Đơn giá</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {record.prescriptions?.length > 0 ? record.prescriptions.map((p, i) => (
                                        <tr key={i}>
                                            <td>{p.medicineName}</td>
                                            <td>{p.quantity}</td>
                                            <td>{p.instruction}</td>
                                            <td>{formatMoney(p.price)}</td>
                                        </tr>
                                    )) : (
                                        <tr>
                                            <td colSpan="4" className="text-center text-muted">
                                                Không có thuốc
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={4}>
                    <Card className="shadow-sm">
                        <Card.Header className="fw-bold bg-primary text-white">
                            Tổng thanh toán
                        </Card.Header>

                        <Card.Body>
                            <div className="d-flex justify-content-between mb-2">
                                <span>Tiền dịch vụ:</span>
                                <strong>{formatMoney(serviceTotal)}</strong>
                            </div>

                            <div className="d-flex justify-content-between mb-2">
                                <span>Tiền thuốc:</span>
                                <strong>{formatMoney(medicineTotal)}</strong>
                            </div>

                            <hr />

                            <div className="d-flex justify-content-between fs-5 mb-3">
                                <span>Tổng tiền:</span>
                                <strong className="text-danger">{formatMoney(totalAmount)}</strong>
                            </div>

                            <Button
                                variant="success"
                                className="w-100"
                                disabled={paying || totalAmount <= 0}
                                onClick={handlePayment}
                            >
                                {paying ? "Đang xử lý..." : "Thanh toán VNPay"}
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Payment;