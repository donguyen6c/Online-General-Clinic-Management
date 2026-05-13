import { useEffect, useState } from "react";
import { Container, Table, Badge, Alert, Button, Modal} from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const Appointments = () => {
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(false);

    const [showModal, setShowModal] = useState(false);
    const [selectedAppointment, setSelectedAppointment] = useState(null);
    const [cancelLoading, setCancelLoading] = useState(false);

    const loadAppointments = async () => {
        try {
            setLoading(true);

            const res = await Apis.get(endpoints["appointments"]);
            setAppointments(res.data);

        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadAppointments();
    }, []);

    const renderStatus = (status) => {
        switch (status) {
            case "scheduled":
                return <Badge bg="primary">Đã đặt</Badge>;

            case "completed":
                return <Badge bg="success">Hoàn thành</Badge>;

            case "cancelled":
                return <Badge bg="danger">Đã hủy</Badge>;

            default:
                return <Badge bg="secondary">{status}</Badge>;
        }
    };

    const canCancelAppointment = (appointment) => {
        const startTime = appointment.time.split(" - ")[0];
    
        const appointmentDateTime = new Date(
            `${appointment.date}T${startTime}`
        );
    
        return (
            appointment.status === "scheduled" &&
            appointmentDateTime > new Date()
        );
    };

    const openCancelModal = (appointment) => {
        setSelectedAppointment(appointment);
        setShowModal(true);
    };

    const handleCancelAppointment = async () => {
        if (!selectedAppointment) return;

        try {
            setCancelLoading(true);

            await Apis.delete(
                endpoints["cancel-appointments"](selectedAppointment.id)
            );

            setAppointments(prev =>
                prev.map(item =>
                    item.id === selectedAppointment.id
                        ? { ...item, status: "cancelled" }
                        : item
                )
            );

            setShowModal(false);
            setSelectedAppointment(null);

        } catch (err) {
            console.error(err);
            alert("Hủy lịch hẹn thất bại!");
        } finally {
            setCancelLoading(false);
        }
    };

    return (
        <Container className="mt-4">
            <h3 className="mb-4">Danh sách lịch hẹn</h3>

            {loading && (
                <div className="text-center">
                    <MySpinner />
                </div>
            )}

            {!loading && appointments.length === 0 && (
                <Alert variant="info">
                    Chưa có lịch hẹn nào
                </Alert>
            )}

            {!loading && appointments.length > 0 && (
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ngày</th>
                            <th>Giờ</th>
                            <th>Bác sĩ</th>
                            <th>Chuyên khoa</th>
                            <th>Lý do</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>

                    <tbody>
                        {appointments.map((item) => (
                            <tr key={item.id}>
                                <td>{item.id}</td>
                                <td>{item.date}</td>
                                <td>{item.time}</td>
                                <td>{item.doctorName}</td>
                                <td>{item.specialty}</td>
                                <td>{item.reason}</td>

                                <td>
                                    {renderStatus(item.status)}
                                </td>

                                <td>
                                    {canCancelAppointment(item) && (
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => openCancelModal(item)}
                                        >
                                            Hủy lịch
                                        </Button>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}

            <Modal
                show={showModal}
                onHide={() => setShowModal(false)}
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title>
                        Xác nhận hủy lịch
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    Bạn có chắc muốn hủy lịch hẹn này không?
                </Modal.Body>

                <Modal.Footer>
                    <Button
                        variant="secondary"
                        onClick={() => setShowModal(false)}
                        disabled={cancelLoading}
                    >
                        Đóng
                    </Button>

                    <Button
                        variant="danger"
                        onClick={handleCancelAppointment}
                        disabled={cancelLoading}
                    >
                        {cancelLoading ? "Đang hủy..." : "Xác nhận hủy"}
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default Appointments;