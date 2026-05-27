import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";


const Home = () => {
  const navigate = useNavigate();

  const [specialties, setSpecialties] = useState([]);
  const [doctors,     setDoctors]     = useState([]);
  const [services,    setServices]    = useState([]);

  useEffect(() => {
    Apis.get(endpoints["specialties"])
      .then(res => setSpecialties(res.data))
      .catch(console.error);
  }, []);

  useEffect(() => {
    Apis.get(endpoints["doctors"])
      .then(res => {
        const data = res.data.data || res.data;
        setDoctors(Array.isArray(data) ? data.slice(0, 4) : []);
      })
      .catch(console.error);
  }, []);

  useEffect(() => {
    Apis.get(endpoints["services"])
      .then(res => setServices(Array.isArray(res.data) ? res.data.slice(0, 3) : []))
      .catch(console.error);
  }, []);

  return (
    <>
      <section
        className="text-white py-5"
        style={{ background: "linear-gradient(135deg, #0a6e6e, #06b6d4)" }}
      >
        <div className="container py-3">
          <h1 className="fw-bold mb-2" style={{ fontSize: "2.2rem" }}>
            Đặt lịch khám nhanh &amp; tiện lợi
          </h1>
          <p className="mb-4 opacity-75">
            Kết nối với đội ngũ bác sĩ chuyên khoa — mọi lúc, mọi nơi.
          </p>
        </div>
      </section>

      {/* ── SPEC ── */}
      <section className="py-5 bg-white">
        <div className="container">
          <h4 className="fw-bold mb-1" style={{ color: "#0a6e6e" }}>Chuyên khoa</h4>
          <p className="text-muted mb-4">Chọn chuyên khoa để tìm bác sĩ phù hợp</p>

          <div className="row g-3">
            {specialties.map((spec, idx) => (
              <div key={spec.id} className="col-6 col-sm-4 col-md-3 col-lg-2">
                <div
                  className="border rounded-3 text-center p-3 h-100"
                  style={{ cursor: "pointer", transition: "box-shadow .2s" }}
                  onClick={() => navigate(`/health-check?specialtyId=${spec.id}`)}
                  onMouseEnter={e => e.currentTarget.style.boxShadow = "0 4px 16px rgba(13,148,136,.18)"}
                  onMouseLeave={e => e.currentTarget.style.boxShadow = "none"}
                >
                  <div className="fw-semibold small">{spec.name}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-5" style={{ background: "#f8fafc" }}>
        <div className="container">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <div>
              <h4 className="fw-bold mb-1" style={{ color: "#0a6e6e" }}>Bác sĩ nổi bật</h4>
              <p className="text-muted mb-0">Đội ngũ chuyên gia giàu kinh nghiệm</p>
            </div>
            <button className="btn btn-outline-secondary btn-sm" onClick={() => navigate("/health-check")}>Xem tất cả →</button>
          </div>

          <div className="row g-3">
            {doctors.map(doc => (
              <div key={doc.id} className="col-6 col-md-4 col-lg-3">
                <div className="card h-100 border rounded-3 shadow-sm">
                  <img src={doc.user?.avatar || "https://cdn-icons-png.flaticon.com/512/3774/3774299.png"} alt={doc.user?.fullName} className="card-img-top" 
                  style={{ height: 180, objectFit: "cover" }}/>
                  <div className="card-body d-flex flex-column">
                    <div className="fw-bold mb-1" style={{ color: "#0a6e6e" }}>{doc.user?.fullName || "Bác sĩ"}</div>
                    <div className="text-muted small mb-3">{doc.specialty?.name || "Đa khoa"}</div>
                    <button className="btn btn-sm mt-auto text-white" style={{ background: "#0d9488" }} onClick={() => navigate(`/doctors/${doc.id}/booking`)}>
                      Đặt lịch
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── DỊCH VỤ ── */}
      <section className="py-5 bg-white">
        <div className="container">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <div>
              <h4 className="fw-bold mb-1" style={{ color: "#0a6e6e" }}>Dịch vụ y tế</h4>
              <p className="text-muted mb-0">Đội ngũ chuyên gia giàu kinh nghiệm</p>
            </div>
            <button
              className="btn btn-outline-secondary btn-sm"
              onClick={() => navigate("/services")}
            >
              Xem tất cả →
            </button>
          </div>
          <div className="row g-3">
            {services.map((svc, i) => (
              <div key={svc.id} className="col-12 col-sm-6 col-lg-4">
                <div className="border rounded-3 p-3 h-100">
                  <div className="fw-semibold mb-1">{svc.name}</div>
                  {svc.description && <p className="text-muted small mb-2">{svc.description}</p>}
                  <span className="badge" style={{ background: "#ccfbf1", color: "#0d9488", fontSize: "0.85rem" }}>
                    {Number(svc.price).toLocaleString("vi-VN")} ₫
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── ABOUT + CTA ── */}
      <section className="p-3" style={{ background: "#f0fdfa" }}>
          {/* CTA */}
          <div className="text-center mt-5">
            <h5 className="fw-bold mb-2">Sẵn sàng đặt lịch khám?</h5>
            <p className="text-muted mb-3">Đăng ký ngay để trải nghiệm dịch vụ y tế hiện đại</p>
            <div className="d-flex justify-content-center gap-3 flex-wrap">
              <button
                className="btn text-white px-4"
                style={{ background: "#0d9488" }}
                onClick={() => navigate("/health-check")}
              >
                Tìm bác sĩ ngay
              </button>
              <button
                className="btn btn-outline-secondary px-4"
                onClick={() => navigate("/register")}
              >
                Đăng ký tài khoản
              </button>
            </div>
        </div>
      </section>
    </>
  );
};

export default Home;