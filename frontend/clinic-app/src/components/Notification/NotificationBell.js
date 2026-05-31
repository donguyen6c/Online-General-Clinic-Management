import React, { useState, useEffect, useContext } from 'react';
import { MyUserContext } from "../../configs/Contexts";
import { db } from "../../configs/firebase";
import { collection, query, where, orderBy, onSnapshot, doc, updateDoc } from "firebase/firestore";

const NotificationBell = () => {
    const [user] = useContext(MyUserContext);
    const [notifications, setNotifications] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [loading, setLoading] = useState(true);
    const [totalUnread, setTotalUnread] = useState(0);

    useEffect(() => {
        if (!user?.id) {
            setNotifications([]);
            setTotalUnread(0);
            setLoading(false);
            return;
        }

        const notificationsQuery = query(
            collection(db, 'notifications'),
            where('userId', '==', user.id),
            orderBy('createdAt', 'desc')
        );

        const unsubscribe = onSnapshot(
            notificationsQuery,
            (snapshot) => {
                const newNotifications = snapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
                setNotifications(newNotifications);
                setTotalUnread(newNotifications.filter((notif) => !notif.isRead).length);
                setLoading(false);
            },
            (error) => {
                console.error("Lỗi realtime notification:", error);
                setLoading(false);
            }
        );

        return () => unsubscribe();
    }, [user?.id]);

    const handleNotificationClick = async (e, id, isRead) => {
        e.stopPropagation();
        if (isRead) return;

        try {
            const notificationRef = doc(db, 'notifications', id);
            await updateDoc(notificationRef, { isRead: true });
        } catch (error) {
            console.error("Error marking notification read:", error);
        }
    };

    return (
        <div style={{ position: 'relative', display: 'inline-block' }}>
            <button 
                onClick={() => setIsOpen(!isOpen)} 
                style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: '24px' }}
            >
                <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" className="bi bi-bell" viewBox="0 0 16 16">
                <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2M8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5 5 0 0 1 13 6c0 .88.32 4.2 1.22 6"/>
                </svg>
                {totalUnread > 0 && (
                    <span style={{
                        position: 'absolute', top: '-5px', right: '-5px',
                        backgroundColor: 'red', color: 'white', borderRadius: '50%',
                        padding: '2px 6px', fontSize: '12px', fontWeight: 'bold'
                    }}>
                        {totalUnread}
                    </span>
                )}
            </button>
            {isOpen && (
                <div style={{
                    position: 'absolute', right: 0, top: '40px', width: '320px',
                    backgroundColor: 'white', boxShadow: '0px 4px 8px rgba(0,0,0,0.1)',
                    border: '1px solid #ddd', borderRadius: '8px', zIndex: 1000,
                    maxHeight: '400px', 
                    display: 'flex', flexDirection: 'column'
                }}>
                    <h4 style={{ padding: '12px', margin: 0, borderBottom: '1px solid #ddd', backgroundColor: '#f8f9fa' }}>Thông báo</h4>
                    
                    <div style={{ overflowY: 'auto', flex: 1 }}>
                        {notifications.length === 0 && !loading ? (
                            <p style={{ padding: '20px', textAlign: 'center', color: '#888', margin: 0 }}>Không có thông báo nào</p>
                        ) : (
                            <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
                                {notifications.map(notif => (
                                    <li 
                                        key={notif.id} 
                                        onClick={(e) => handleNotificationClick(e,notif.id, notif.isRead)}
                                        style={{
                                            padding: '12px', 
                                            borderBottom: '1px solid #eee',
                                            cursor: 'pointer',
                                            backgroundColor: notif.isRead ? '#fff' : '#eaf4fc',
                                            fontWeight: notif.isRead ? 'normal' : 'bold'
                                        }}
                                    >
                                        <div style={{ fontSize: '14px', marginBottom: '4px', color: '#333' }}>{notif.title}</div>
                                        <div style={{ fontSize: '13px', color: '#666' }}>{notif.message}</div>
                                        <div style={{ fontSize: '11px', color: '#aaa', marginTop: '6px' }}>
                                            {new Date(notif.createdAt).toLocaleString('vi-VN')}
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        )}

                    </div>
                </div>
            )}
        </div>
    );
};

export default NotificationBell;