function deleteDoctor(url) {
    if (confirm("Bạn chắc chắn xóa?")) {
        fetch(url, {
            method: 'delete'
        })
        .then(res => {
            if (res.status === 204) {
                location.reload(); 
            } else {
                alert("Có lỗi xảy ra khi xóa.");
            }
        });
    }
}