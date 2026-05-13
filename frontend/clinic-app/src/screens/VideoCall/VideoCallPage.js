import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import { MyUserContext } from "../../configs/Contexts";
import MySpinner from "../../components/MySpinner";

const VideoCallPage = () => {
  const [user] = useContext(MyUserContext);
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const role = user.role;

    const fetchAndOpen = async () => {
      try {
        const token =
          localStorage.getItem("token") || localStorage.getItem("access_token");
        const res = await Apis.get(endpoints["get-meeting-url"](id), {
          headers: { Authorization: `Bearer ${token}` },
        });

        window.open(`https://meet.jit.si/${res.data.meetingUrl}`, "_blank");

        if (role === "doctor") {
          navigate(`/medical-record?appointmentId=${id}`);
        } else {
          navigate("/");
        }
      } catch (err) {
        setError("Bạn không có quyền truy cập hoặc lịch hẹn chưa bắt đầu.");
      } finally {
        setLoading(false);
      }
    };

    fetchAndOpen();
  }, [id, navigate, user]);

  if (loading) return <MySpinner />;
  if (error)
    return (
      <div style={{ textAlign: "center", marginTop: "50px", color: "red" }}>
        <h2>{error}</h2>
      </div>
    );

  return null;
};

export default VideoCallPage;
