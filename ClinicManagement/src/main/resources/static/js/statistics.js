const COLORS = ['#0d6efd','#198754','#ffc107','#dc3545','#0dcaf0','#6f42c1','#fd7e14','#20c997'];

document.getElementById('kpiPatients').textContent = gData.reduce((a,b)=>a+b,0);
document.getElementById('kpiSpecialty').textContent = spLabels.length;
document.getElementById('kpiServices').textContent = svData.reduce((a,b)=>a+b,0);
document.getElementById('kpiDiseases').textContent = dLabels.length;

new Chart(document.getElementById('genderChart'), {
    type: 'pie',
    data: { labels: gLabels, datasets: [{ data: gData, backgroundColor: COLORS }] },
    options: { responsive: true, maintainAspectRatio: false }
});

new Chart(document.getElementById('ageChart'), {
    type: 'bar',
    data: { labels: aLabels, datasets: [{ data: aData, backgroundColor: aLabels.map((_,i) => COLORS[i % COLORS.length]) }] },
    options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
});

new Chart(document.getElementById('specialtyChart'), {
    type: 'bar',
    data: { labels: spLabels, datasets: [{ data: spData, backgroundColor: spLabels.map((_,i) => COLORS[i % COLORS.length]) }] },
    options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
});

new Chart(document.getElementById('servicesChart'), {
    type: 'bar',
    data: { labels: svLabels, datasets: [{ data: svData, backgroundColor: svLabels.map((_,i) => COLORS[i % COLORS.length]) }] },
    options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
});

new Chart(document.getElementById('diseasesChart'), {
    type: 'bar',
    data: { labels: dLabels, datasets: [{ data: dData, backgroundColor: dLabels.map((_,i) => COLORS[i % COLORS.length]) }] },
    options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
});

const total = spData.reduce((a,b)=>a+b,0) || 1;
const tbody = document.getElementById('revTableBody');
if (!spLabels.length) {
    tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted py-3">Chưa có dữ liệu</td></tr>';
} else {
    spLabels.forEach((label, i) => {
        const count = spData[i] || 0;
        const pct = Math.round(count / total * 100);
        const color = COLORS[i % COLORS.length];
        tbody.innerHTML += `<tr>
            <td>${i+1}</td>
            <td><span class="badge" style="background:${color}">${label}</span></td>
            <td>${count}</td>
            <td>${pct}%</td>
        </tr>`;
    });
}