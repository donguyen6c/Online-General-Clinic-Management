import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { JitsiMeeting } from '@jitsi/react-sdk';
import Apis, { endpoints } from "../../configs/Apis";
import { MyUserContext } from "../../configs/Contexts";
import MySpinner from '../../components/MySpinner';

const VideoCallPage = () => {
    const [user] = useContext(MyUserContext);
    const { id } = useParams();
    const navigate = useNavigate();
    const [meetingUrl, setMeetingUrl] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMeetingUrl = async () => {
            try {
                const token = localStorage.getItem("token") || localStorage.getItem("access_token");
                let res = await Apis.get(endpoints['get-meeting-url'](id), {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                setMeetingUrl(res.data.meetingUrl);
            } catch (err) {
                console.error("Lỗi từ server:", err.response?.data);
                setError("Bạn không có quyền truy cập hoặc lịch hẹn chưa bắt đầu.");
            } finally {
                setLoading(false);
            }
        };

        fetchMeetingUrl();
    }, [id]);

    if (loading) return <MySpinner />;
    if (error) return (
        <div style={{ textAlign: 'center', marginTop: '50px', color: 'red' }}>
            <h2>{error}</h2>
        </div>
    );

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
                    displayName: user?.full_name || 'Khách'
                }}
                onApiReady={(externalApi) => {
                    externalApi.addListener('videoConferenceLeft', () => {
                        alert("Cuộc gọi kết thúc!");
                        if (user?.role === 'doctor') {
                            navigate(`/medical-record?appointmentId=${id}`); // id từ useParams
                        } else {
                            navigate('/');
                        }
                    });
                }}
                getIFrameRef={(iframeRef) => { iframeRef.style.height = '100%'; }}
            />
        </div>
    );
};

export default VideoCallPage;