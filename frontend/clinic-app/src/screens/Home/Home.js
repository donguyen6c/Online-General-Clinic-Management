import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const Home = () => {
  const [loading, setLoading] = useState(false);
  const [doctors, setDoctors] = useState([]);
  const [specialties, setSpecialties] = useState([]);
  const [kw, setKw] = useState("");
  const [specialtyId, setSpecialtyId] = useState("");

  const loadSpecialties = async () => {
    setLoading(true);
    let res = await Apis.get(endpoints["specialties"]);
    setSpecialties(res.data);
    setLoading(false);
  };

  const loadDoctors = async (keyword, specId) => {
    setLoading(true);
    let res = await Apis.get(endpoints["doctors"], {
      params: {
        kw: keyword,
        specialtyId: specId,
      },
    });
    setDoctors(res.data);
    setLoading(false);
  };

  const handleSearch = (e) => {
    e.preventDefault();
  };

  useEffect(() => {
    loadSpecialties();
  }, []);

  useEffect(() => {
    const delay = setTimeout(() => {
        loadDoctors(kw, specialtyId);
    }, 200);

    return () => clearTimeout(delay);
}, [kw, specialtyId]);

  const handleDelete = async (id) => {
    if (window.confirm("Chắc chắn xóa?")) {
      try {
        await Apis.delete(`${endpoints["doctors"]}/${id}`);
        alert("Xóa thành công");
        loadDoctors(kw, specialtyId);
      } catch (error) {
        alert("Xoá thất bại");
      }
    }
  };
  return (
    <>
      <div className="container mt-5">
        <h2 className="mb-4 text-primary">Danh Sách Bác Sĩ</h2>

        <section className="container p-0">
          {/* Bộ lọc chuyên khoa */}
          <div className="mb-3">
            <button
              onClick={() => setSpecialtyId("")}
              className={`btn ${specialtyId === "" ? "btn-secondary" : "btn-outline-secondary"} me-2`}
            >
              Tất cả
            </button>
            {specialties.map((c) => (
              <button
                key={c.id}
                onClick={() => setSpecialtyId(c.id)}
                className={`btn ${specialtyId === c.id ? "btn-primary" : "btn-outline-primary"} me-2`}
              >
                {c.name}
              </button>
            ))}
          </div>

          {/* Thanh tìm kiếm */}
          <div className="row mb-4">
            <div className="col-md-5">
              <form onSubmit={handleSearch} className="d-flex">
                <input
                  type="text"
                  value={kw}
                  onChange={(e) => setKw(e.target.value)}
                  className="form-control me-2"
                  placeholder="Nhập tên..."
                />
                <button type="submit" className="btn btn-success">
                  Tìm
                </button>
              </form>
            </div>
          </div>

          {/* Nút thêm mới */}
          <div className="mt-2 mb-3">
            <Link to="/doctor/add" className="btn btn-info text-white">
              Thêm Bác sĩ
            </Link>
          </div>

          {/* Bảng dữ liệu */}
          <table className="table table-bordered table-hover">
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Tên Bác Sĩ</th>
                <th>Chuyên Khoa</th>
                <th>Hành Động</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="4" className="text-center">
                    <MySpinner />
                  </td>
                </tr>
              ) : doctors.length > 0 ? (
                doctors.map((d) => (
                  <tr key={d.id}>
                    <td>{d.id}</td>
                    <td>{d.user?.fullName}</td>
                    <td>{d.specialty?.name}</td>
                    <td>
                      <Link
                        to={`/doctor/edit/${d.id}`}
                        className="btn btn-info text-white me-2"
                      >
                        Cập nhật
                      </Link>
                      <button
                        onClick={() => handleDelete(d.id)}
                        className="btn btn-danger btn-sm"
                      >
                        Xóa
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="text-center text-danger">
                    Không tìm thấy bác sĩ nào!
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </section>
      </div>
    </>
  );
};
export default Home;
