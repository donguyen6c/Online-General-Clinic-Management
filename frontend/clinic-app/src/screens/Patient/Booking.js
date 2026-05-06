import Apis, { endpoints } from "../../configs/Apis";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
    Container,
    Row,
    Col,
    Card,
    Form,
    Button,
    Alert
} from "react-bootstrap";
import MySpinner from "../../components/MySpinner";

const Booking = () => {
    const { doctorId } = useParams();

    const [doctor, setDoctor] = useState(null);
    const [slotData, setSlotData] = useState(null);
    const [selectedDate, setSelectedDate] = useState("");
    const [selectedSlot, setSelectedSlot] = useState(null);

    const [doctorLoading, setDoctorLoading] = useState(true);
    const [slotLoading, setSlotLoading] = useState(false);
    const [bookingLoading, setBookingLoading] = useState(false);

    const today = new Date().toISOString().split("T")[0];

    const loadDoctor = async () => {
        try {
            setDoctorLoading(true);
            const res = await Apis.get(endpoints["doctor-detail"](doctorId));
            setDoctor(res.data);
        } catch (ex) {
            console.log(ex);
        } finally {
            setDoctorLoading(false);
        }
    };

    const loadSlots = async (date) => {
        try {
            setSlotLoading(true);
            setSelectedSlot(null);

            const res = await Apis.get(endpoints["slots"](doctorId, date));
            setSlotData(res.data);
        } catch (ex) {
            console.log(ex);
        } finally {
            setSlotLoading(false);
        }
    };

    useEffect(() => {
        loadDoctor();
        setSelectedDate(today);
    }, [doctorId]);

    useEffect(() => {
        if (selectedDate) loadSlots(selectedDate);
    }, [selectedDate]);

    const handleBooking = async () => {
        if (!selectedSlot || bookingLoading) return;

        try {
            setBookingLoading(true);

            await Apis.post(endpoints["booking"](doctorId), {
                date: selectedDate,
                startTime: selectedSlot.startTime,
                endTime: selectedSlot.endTime,
                reason: "Khám tổng quát"
            });

            alert("Đặt lịch thành công!");
            loadSlots(selectedDate);
        } catch (ex) {
            console.log(ex);
            alert("Đặt lịch thất bại!");
        } finally {
            setBookingLoading(false);
        }
    };

    return (
        <Container className="py-4">
            <h2 className="text-center text-primary mb-4">Đặt lịch khám</h2>

            {/* Doctor */}
            {doctorLoading ? (
                <div className="text-center py-4">
                    <MySpinner />
                </div>
            ) : doctor && (
                <Card className="shadow-sm mb-4 border-0">
                    <Row className="g-0">
                        <Col md={4}>
                            <Card.Img
                                src={doctor.user.avatar}
                                style={{ height: "300px", objectFit: "cover" }}
                            />
                        </Col>
                        <Col md={8}>
                            <Card.Body>
                                <Card.Title className="mb-3">
                                    {doctor.user?.fullName}
                                </Card.Title>
                                <p className="mb-2">
                                    <strong>Chuyên khoa:</strong>{" "}
                                    {doctor.specialty?.name}
                                </p>
                                <p className="mb-2">
                                    <strong>Email:</strong>{" "}
                                    {doctor.user?.email || "Chưa cập nhật"}
                                </p>
                                <p className="mb-2">
                                    <strong>SĐT:</strong>{" "}
                                    {doctor.user?.phone || "Chưa cập nhật"}
                                </p>
                                <p className="mb-0">
                                    <strong>Giới thiệu:</strong>{" "}
                                    {doctor.bio || "Chưa có mô tả"}
                                </p>
                            </Card.Body>
                        </Col>
                    </Row>
                </Card>
            )}

            {/* Booking */}
            <Card className="shadow-sm border-0">
                <Card.Body>
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold">
                            Chọn ngày khám
                        </Form.Label>
                        <Form.Control
                            type="date"
                            value={selectedDate}
                            min={today}
                            onChange={(e) =>
                                setSelectedDate(e.target.value)
                            }
                        />
                    </Form.Group>

                    {slotLoading ? (
                        <div className="text-center py-4">
                            <MySpinner />
                        </div>
                    ) : slotData?.available === false ? (
                        <Alert variant="warning" className="text-center">
                            Bác sĩ không làm việc ngày này.
                        </Alert>
                    ) : (
                        <>
                            <div className="mb-3">
                                <strong>Giờ làm việc:</strong>{" "}
                                {slotData?.workingTime?.startTime} -{" "}
                                {slotData?.workingTime?.endTime}
                            </div>

                            <Row>
                                {slotData?.slots?.map((slot, index) => (
                                    <Col md={3} xs={6} className="mb-3" key={index}>
                                        <Button
                                            className="w-100"
                                            variant={
                                                !slot.available
                                                    ? "light"
                                                    : selectedSlot?.startTime ===
                                                      slot.startTime &&
                                                      selectedSlot?.endTime ===
                                                          slot.endTime
                                                    ? "primary"
                                                    : "outline-primary"
                                            }
                                            disabled={!slot.available || bookingLoading}
                                            onClick={() => setSelectedSlot(slot)}
                                        >
                                            {slot.startTime} - {slot.endTime}
                                        </Button>
                                    </Col>
                                ))}
                            </Row>

                            <div className="text-end mt-3">
                                <Button
                                    variant="success"
                                    onClick={handleBooking}
                                    disabled={!selectedSlot || bookingLoading}
                                >
                                    {bookingLoading
                                        ? "Đang xử lý..."
                                        : "Xác nhận đặt lịch"}
                                </Button>
                            </div>
                        </>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Booking;