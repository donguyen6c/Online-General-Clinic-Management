import { Card, Container } from "react-bootstrap";
import { useSearchParams } from "react-router-dom";

const PaymentResult = () => {
    const [params] = useSearchParams();

    const status = params.get("status");
    const paymentCode = params.get("paymentCode");
    const responseCode = params.get("responseCode");

    const isSuccess = status === "paid" && responseCode === "00";

    return (
        <Container className="py-5">
            <Card className="shadow-sm mx-auto" style={{ maxWidth: "600px" }}>
                <Card.Body className="text-center p-5">
                    <h2 className={isSuccess ? "text-success" : "text-danger"}>
                        {isSuccess ? "Thanh toán thành công" : "Thanh toán thất bại"}
                    </h2>

                    <p className="text-muted mt-3">
                        {isSuccess
                            ? "Cảm ơn bạn. Phiếu khám đã được thanh toán thành công."
                            : "Giao dịch chưa được hoàn tất. Vui lòng thử lại."}
                    </p>

                    {paymentCode && (
                        <p>
                            <strong>Mã thanh toán:</strong> {paymentCode}
                        </p>
                    )}

                    <p>
                        <strong>Mã phản hồi:</strong> {responseCode}
                    </p>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default PaymentResult;