import html2pdf from "html2pdf.js";
import * as XLSX from "xlsx";

class ExportStrategy {
    pass
}

class PDFExportStrategy extends ExportStrategy {
    export(record) {
        const paymentText = record.paymentStatus === "paid" ? "Đã thanh toán" :
                            record.paymentStatus === "failed" ? "Thất bại" : "Chưa thanh toán";

        const content = `
            <div style="font-family: Arial, sans-serif; padding: 32px; font-size: 13px; color: #333;">

                <h2 style="text-align:center; font-size:18px; margin-bottom:4px;">HỒ SƠ BỆNH ÁN</h2>
                <p style="text-align:center; color:#888; margin-top:0;">#${record.recordId}</p>
                <hr style="border:none; border-top:1px solid #ddd; margin-bottom:16px;"/>

                <p><b>Bác sĩ:</b> ${record.doctorName || ""} &nbsp;&nbsp; <b>Ngày:</b> ${record.date?.slice(0,10) || ""}</p>
                <p><b>Chẩn đoán:</b> ${record.diagnosis || "Chưa có"}</p>
                <p><b>Thanh toán:</b> ${paymentText}</p>

                ${record.prescriptions?.length > 0 ? `
                    <p style="margin-top:16px;"><b>Đơn thuốc</b></p>
                    <table style="width:100%; border-collapse:collapse; font-size:12px;">
                        <tr style="border-bottom:2px solid #333;">
                            <td style="padding:6px;"><b>Tên thuốc</b></td>
                            <td style="padding:6px; width:60px;"><b>SL</b></td>
                            <td style="padding:6px;"><b>Hướng dẫn</b></td>
                        </tr>
                        ${record.prescriptions.map(p => `
                            <tr style="border-bottom:1px solid #eee;">
                                <td style="padding:6px;">${p.medicineName}</td>
                                <td style="padding:6px;">${p.quantity}</td>
                                <td style="padding:6px;">${p.instruction || ""}</td>
                            </tr>
                        `).join("")}
                    </table>
                ` : ""}

                ${record.services?.length > 0 ? `
                    <p style="margin-top:16px;"><b>Dịch vụ</b></p>
                    <table style="width:100%; border-collapse:collapse; font-size:12px;">
                        <tr style="border-bottom:2px solid #333;">
                            <td style="padding:6px;"><b>Tên dịch vụ</b></td>
                            <td style="padding:6px; width:60px;"><b>Số lượng</b></td>
                            <td style="padding:6px; width:100px;"><b>Đơn giá</b></td>
                        </tr>
                        ${record.services.map(s => `
                            <tr style="border-bottom:1px solid #eee;">
                                <td style="padding:6px;">${s.serviceName}</td>
                                <td style="padding:6px;">${s.quantity}</td>
                                <td style="padding:6px;">${Number(s.priceAtTime).toLocaleString("vi-VN")}₫</td>
                            </tr>
                        `).join("")}
                    </table>
                ` : ""}

                ${record.testResults?.length > 0 ? `
                    <p style="margin-top:16px;"><b>Kết quả xét nghiệm</b></p>
                    <table style="width:100%; border-collapse:collapse; font-size:12px;">
                        <tr style="border-bottom:2px solid #333;">
                            <td style="padding:6px;"><b>Tên xét nghiệm</b></td>
                            <td style="padding:6px;"><b>Kết quả</b></td>
                        </tr>
                        ${record.testResults.map(t => `
                            <tr style="border-bottom:1px solid #eee;">
                                <td style="padding:6px;">${t.testName}</td>
                                <td style="padding:6px;">${t.resultValue}</td>
                            </tr>
                        `).join("")}
                    </table>
                ` : ""}

            </div>
        `;

        html2pdf().set({
            margin: 15,
            filename: `HoSoBenhAn_${record.recordId}.pdf`,
            html2canvas: { scale: 2 },
            jsPDF: { unit: "mm", format: "a4", orientation: "portrait" }
        }).from(content).save();
    }
}

class ExcelExportStrategy extends ExportStrategy {
    export(record) {
        const wb = XLSX.utils.book_new();

        const rows = [];

        rows.push(["THÔNG TIN HỒ SƠ BỆNH ÁN"]);
        rows.push(["Mã hồ sơ", record.recordId]);
        rows.push(["Bác sĩ", record.doctorName]);
        rows.push(["Ngày", record.date?.slice(0, 10)]);
        rows.push(["Chẩn đoán", record.diagnosis]);
        rows.push(["Thanh toán", record.paymentStatus === "paid" ? "Đã thanh toán" :
                                  record.paymentStatus === "failed" ? "Thất bại" : "Chưa thanh toán"]);

        if (record.prescriptions?.length > 0) {
            rows.push([]);
            rows.push(["ĐƠN THUỐC"]);
            rows.push(["Tên thuốc", "Số lượng", "Hướng dẫn"]);
            record.prescriptions.forEach(p => {
                rows.push([p.medicineName, p.quantity, p.instruction || ""]);
            });
        }

        if (record.services?.length > 0) {
            rows.push([]);
            rows.push(["DỊCH VỤ"]);
            rows.push(["Tên dịch vụ", "Số lượng", "Đơn giá"]);
            record.services.forEach(s => {
                rows.push([s.serviceName, s.quantity, Number(s.priceAtTime)]);
            });
        }

        if (record.testResults?.length > 0) {
            rows.push([]);
            rows.push(["KẾT QUẢ XÉT NGHIỆM"]);
            rows.push(["Tên xét nghiệm", "Kết quả"]);
            record.testResults.forEach(t => {
                rows.push([t.testName, t.resultValue]);
            });
        }

        const ws = XLSX.utils.aoa_to_sheet(rows);
        ws['!cols'] = [
            { wch: 25 },  // cột A
            { wch: 50 },  // cột B
            { wch: 65 },  // cột C
        ];
        XLSX.utils.book_append_sheet(wb, ws, "Ho so benh an");
        XLSX.writeFile(wb, `HoSoBenhAn_${record.recordId}.xlsx`);
    }
}

export class ExportContext {
    constructor(strategy) { this.strategy = strategy; }
    setStrategy(strategy) { this.strategy = strategy; }
    execute(record) { this.strategy.export(record); }
}

export const pdfStrategy   = new PDFExportStrategy();
export const excelStrategy = new ExcelExportStrategy();