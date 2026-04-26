// import { useEffect, useState } from "react";
// import { Link } from "react-router-dom";
// import Apis, { endpoints } from "../../configs/Apis";
// import MySpinner from "../../components/MySpinner";

// const Home = () => {
//   const [loading, setLoading] = useState(false);
//   const [doctors, setDoctors] = useState([]);
//   const [specialties, setSpecialties] = useState([]);
  
//   const [kw, setKw] = useState("");
//   const [specialtyId, setSpecialtyId] = useState("");

//   // 1. TẢI CHUYÊN KHOA (Chỉ chạy 1 lần khi mở trang)
//   useEffect(() => {
//     const fetchSpecialties = async () => {
//       try {
//         let res = await Apis.get(endpoints["specialties"]);
//         // Đảm bảo res.data là một mảng
//         setSpecialties(res.data);
//       } catch (error) {
//         console.error("Lỗi tải chuyên khoa:", error);
//       }
//     };
//     fetchSpecialties();
//   }, []);

//   // 2. TẢI BÁC SĨ (Tự động chạy khi mở trang hoặc khi đổi từ khóa/chuyên khoa)
//   useEffect(() => {
//     const fetchDoctors = async () => {
//       try {
//         setLoading(true);
//         let res = await Apis.get(endpoints["doctors"], {
//           params: {
//             kw: kw,
//             specialtyId: specialtyId,
//           },
//         });
        
//         // CHỖ NÀY QUAN TRỌNG: Nếu BE có phân trang, phải trỏ đúng vào mảng (vd: res.data.data hoặc res.data.content)
//         // Mình đang để res.data.data giống file Patient.js của bạn
//         setDoctors(res.data.data || res.data); 

//       } catch (error) {
//         console.error("Lỗi tải bác sĩ:", error);
//       } finally {
//         setLoading(false);
//       }
//     };

//     const delay = setTimeout(() => {
//       fetchDoctors();
//     }, 200);

//     return () => clearTimeout(delay);
//   }, [kw, specialtyId]);

//   // Handle Search Input (Thực ra không cần làm gì vì onChange của ô input đã setKw và kích hoạt useEffect rồi)
//   const handleSearch = (e) => {
//     e.preventDefault();
//   };

//   const handleDelete = async (id) => {
//     if (window.confirm("Chắc chắn xóa bác sĩ này?")) {
//       try {
//         await Apis.delete(`${endpoints["doctors"]}/${id}`);
//         alert("Xóa thành công");
        
//         // Cập nhật lại UI bằng cách lọc bỏ bác sĩ vừa xóa ra khỏi State (không cần gọi lại API cho nặng máy)
//         setDoctors(prev => prev.filter(d => d.id !== id));
//       } catch (error) {
//         alert("Xoá thất bại");
//         console.error(error);
//       }
//     }
//   };

//   return (
//     <>
//       <div className="container mt-5">
//         <h2 className="mb-4 text-primary">Danh Sách Bác Sĩ</h2>

//         <section className="container p-0">
//           {/* Bộ lọc chuyên khoa */}
//           <div className="mb-3">
//             <button
//               onClick={() => setSpecialtyId("")}
//               className={`btn ${specialtyId === "" ? "btn-secondary" : "btn-outline-secondary"} me-2`}
//             >
//               Tất cả
//             </button>
//             {/* Thêm check specialties && specialties.length để tránh lỗi map nếu mảng rỗng */}
//             {specialties && specialties.length > 0 && specialties.map((c) => (
//               <button
//                 key={c.id}
//                 onClick={() => setSpecialtyId(c.id)}
//                 className={`btn ${specialtyId === c.id ? "btn-primary" : "btn-outline-primary"} me-2`}
//               >
//                 {c.name}
//               </button>
//             ))}
//           </div>

//           {/* Thanh tìm kiếm */}
//           <div className="row mb-4">
//             <div className="col-md-5">
//               <form onSubmit={handleSearch} className="d-flex">
//                 <input
//                   type="text"
//                   value={kw}
//                   onChange={(e) => setKw(e.target.value)}
//                   className="form-control me-2"
//                   placeholder="Nhập tên bác sĩ..."
//                 />
//                 <button type="submit" className="btn btn-success">
//                   Tìm
//                 </button>
//               </form>
//             </div>
//           </div>

//           {/* Nút thêm mới */}
//           <div className="mt-2 mb-3">
//             <Link to="/doctor/add" className="btn btn-info text-white">
//               Thêm Bác sĩ
//             </Link>
//           </div>

//           {/* Bảng dữ liệu */}
//           <table className="table table-bordered table-hover">
//             <thead className="table-dark">
//               <tr>
//                 <th>ID</th>
//                 <th>Tên Bác Sĩ</th>
//                 <th>Chuyên Khoa</th>
//                 <th>Hành Động</th>
//               </tr>
//             </thead>
//             <tbody>
//               {loading ? (
//                 <tr>
//                   <td colSpan="4" className="text-center">
//                     <MySpinner />
//                   </td>
//                 </tr>
//               ) : doctors && doctors.length > 0 ? (
//                 doctors.map((d) => (
//                   <tr key={d.id}>
//                     <td>{d.id}</td>
//                     <td>{d.user?.fullName}</td>
//                     <td>{d.specialty?.name}</td>
//                     <td>
//                       <Link
//                         to={`/doctor/edit/${d.id}`}
//                         className="btn btn-info text-white me-2"
//                       >
//                         Cập nhật
//                       </Link>
//                       <button
//                         onClick={() => handleDelete(d.id)}
//                         className="btn btn-danger btn-sm"
//                       >
//                         Xóa
//                       </button>
//                     </td>
//                   </tr>
//                 ))
//               ) : (
//                 <tr>
//                   <td colSpan="4" className="text-center text-danger">
//                     Không tìm thấy bác sĩ nào!
//                   </td>
//                 </tr>
//               )}
//             </tbody>
//           </table>
//         </section>
//       </div>
//     </>
//   );
// };

// export default Home;