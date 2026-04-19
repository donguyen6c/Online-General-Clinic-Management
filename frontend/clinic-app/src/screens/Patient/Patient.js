import MySpinner from "../../components/MySpinner";
import Apis, { endpoints } from "../../configs/Apis";
import { useEffect, useState } from "react";

const Patient = () => {
    const [doctors, setDoctors] = useState([]);
    const [specialties, setSpecialties] = useState([]);

    const [kw, setKw] = useState("");
    const [specialtyId, setSpecialtyId] = useState("");

    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);

    const [loading, setLoading] = useState(true);

    const loadDoctors = async (pageNumber = 1, append = false) => {
        try {
            setLoading(true)
    
            let url = `${endpoints['doctors']}?page=${pageNumber}`
    
            if (kw)
                url = `${url}&kw=${kw}`
            if (specialtyId)
                url = `${url}&specialtyId=${specialtyId}`
    
            const res = await Apis.get(url)
    
            if (append)
                setDoctors(prev => [...prev, ...res.data.data])
            else
                setDoctors(res.data.data)
    
            setHasMore(res.data.hasNext)
        } catch (ex) {
            console.log(ex)
        } finally {
            setLoading(false)
        }
    }

    const loadSpecialties = async () => {
        try {
            const res = await Apis.get(endpoints["specialties"]);
            setSpecialties(res.data);
        } catch (ex) {
            console.log(ex);
        }
    };

    useEffect(() => {
        loadSpecialties();
    }, []);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setPage(1)
            loadDoctors(1, false)
        }, 500)
    
        return () => clearTimeout(timeout)
    }, [kw, specialtyId])

    const handleLoadMore = () => {
        const nextPage = page + 1
        setPage(nextPage)
        loadDoctors(nextPage, true)
    }

    return (
        <div className="container py-4">
            <h2 className="text-center mb-4">Danh sách bác sĩ</h2>

            <div className="row mb-4">
                <div className="col-md-6 mb-2">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Tìm theo tên bác sĩ..."
                        value={kw}
                        onChange={(e) => setKw(e.target.value)}
                    />
                </div>

                <div className="col-md-6">
                    <select
                        className="form-select"
                        value={specialtyId}
                        onChange={(e) => setSpecialtyId(e.target.value)}
                    >
                        <option value="">-- Tất cả chuyên khoa --</option>
                        {specialties.map((s) => (
                            <option key={s.id} value={s.id}>
                                {s.name}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            <div className="row">
                {!loading && doctors.length === 0 && (
                    <div className="alert alert-warning text-center">
                        Không có bác sĩ nào
                    </div>
                )}

                {doctors.map((d) => (
                    <div className="col-md-4 mb-4" key={d.id}>
                        <div className="card h-100 shadow-sm">
                            <img
                                src={
                                    d.user.avatar}
                                className="card-img-top"
                                alt=""
                                style={{ height: "220px", objectFit: "cover" }}
                            />

                            <div className="card-body">
                                <h5 className="card-title">{d.user?.fullName}</h5>

                                <p className="mb-1">
                                    <strong>Chuyên khoa:</strong> {d.specialty?.name}
                                </p>

                                <p className="text-muted small">{d.bio}</p>
                            </div>

                            <div className="card-footer bg-white border-0">
                                <button className="btn btn-outline-primary w-100">
                                    Đặt lịch
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            {loading && (
                <div className="text-center">
                    <MySpinner />
                </div>
            )}
            {!loading && doctors.length > 0 && hasMore && (
                <div className="text-center mt-3">
                    <button
                        className="btn btn-outline-primary"
                        onClick={handleLoadMore}
                    >
                        Xem thêm
                    </button>
                </div>
            )}
        </div>

    );
};

export default Patient;
