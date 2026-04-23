import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { JitsiMeeting } from '@jitsi/react-sdk';
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from '../../components/MySpinner'; // Dùng lại component spinner của bạn

const VideoCallPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [meetingUrl, setMeetingUrl] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    // Lấy thông tin user hiện tại. Giả sử bạn lưu trong localStorage, 
    // nếu bạn dùng Context API hoặc Redux thì lấy từ đó ra nhé.
    const currentUser = JSON.parse(localStorage.getItem('currentUser')) || { full_name: 'Khách' };

    useEffect(() => {
        const fetchMeetingUrl = async () => {
            try {
                // 1. Lấy token đăng nhập
                const token = localStorage.getItem("token") || localStorage.getItem("access_token");

                // 2. Gọi API kèm header chứa token
                let res = await Apis.get(endpoints['get-meeting-url'](id), {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                setMeetingUrl(res.data.meetingUrl);
                
            } catch (err) {
                // In lỗi ra console để dễ debug nếu vẫn tịt
                console.error("Lỗi từ server gửi về:", err.response?.data);
                setError("Bạn không có quyền truy cập hoặc lịch hẹn chưa bắt đầu.");
            } finally {
                setLoading(false);
            }
        };

        fetchMeetingUrl();
    }, [id]);

    if (loading) return <MySpinner />; // Hiển thị vòng xoay lúc đang tải
    if (error) return <div style={{ textAlign: 'center', marginTop: '50px', color: 'red' }}><h2>{error}</h2></div>;

    return (
        <div style={{ height: '100vh', width: '100%' }}>
            <JitsiMeeting
                domain="meet.jit.si"
                roomName={meetingUrl}
                configOverwrite={{
                    startWithAudioMuted: false,
                    startWithVideoMuted: false,
                    disableModeratorIndicator: true,
                    prejoinPageEnabled: false,
                }}
                interfaceConfigOverwrite={{
                    DISABLE_JOIN_LEAVE_NOTIFICATIONS: true,
                    SHOW_JITSI_WATERMARK: false,
                }}
                userInfo={{
                    displayName: currentUser.full_name 
                }}
                onApiReady={(externalApi) => {
                    externalApi.addListener('videoConferenceLeft', () => {
                        alert("Cuộc gọi kết thúc!");
                        navigate('/'); // Quay về trang chủ hoặc trang lịch sử khám
                    });
                }}
                getIFrameRef={(iframeRef) => { iframeRef.style.height = '100%'; }}
            />
        </div>
    );
};

export default VideoCallPage;