const firebaseConfig = {
    apiKey: "AIzaSyDNaSOujZTLQ2m8keQmuXYE02dtkY_D7FQ",
    authDomain: "umhackathon-zakatapp-mvp.firebaseapp.com",
    projectId: "umhackathon-zakatapp-mvp",
    storageBucket: "umhackathon-zakatapp-mvp.firebasestorage.app",
    messagingSenderId: "150330163152",
    appId: "1:150330163152:android:b693bb958b0dedd12ac3d5"
  };

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();

// Sorting Order
const ASNAF_ORDER = ['Fakir', 'Miskin', 'Mualaf', 'Riqab', 'Gharim', 'Fisabilillah', 'Ibnu Sabil'];

// Real-time listener
function setupRealtimeUpdates() {
    // db.collection("Penerima").onSnapshot((snapshot) => {
    db.collection("Penerima")
      .limit(10)
      .get()
      .then((snapshot) => {
        const items = snapshot.docs.map(doc => ({
          id: doc.id,
          data: doc.data()
        }));
    
    // Sorting logic (Chatgpt Referred)
    items.sort((a, b) => {
      const grpA = a.data["Kategori Asnaf"] || "";
      const grpB = b.data["Kategori Asnaf"] || "";
      const idxA = ASNAF_ORDER.indexOf(grpA);
      const idxB = ASNAF_ORDER.indexOf(grpB);

      const orderA = idxA === -1 ? ASNAF_ORDER.length : idxA;
      const orderB = idxB === -1 ? ASNAF_ORDER.length : idxB;

      if (orderA !== orderB) return orderA - orderB;

      // Same Asnaf group → compare dates
      const dateA = new Date(a.data["Tarikh"]);
      const dateB = new Date(b.data["Tarikh"]);
      return dateA - dateB;  // Earlier date first
    });

    renderRows(items);
  });
}

function renderRows(items){
  const tbody = document.getElementById("tableBody");
    tbody.innerHTML = "";   

    items.forEach((item) => {
      const data = item.data;
      const row = document.createElement("tr");
      //row.className = data.approved ? "approved" : "pending";

      row.innerHTML = `
        <td>${data["Nama Pemohon/Institusi"] || "N/A"}</td>
        <td>${
          data["No. K/P (baru)/Polis/Tentera/No. Pasport"] || "N/A"
        }</td>
        <td>${data["Emel"] || "N/A"}</td>
        <td>${data["No. Telefon Bimbit"] || "N/A"}</td>
        <td>${data["Kelayakan"] || "N/A"}</td>
        <td>${data["Kategori Asnaf"] || "N/A"}</td>
        <td>
            <span class="status-indicator"></span>
            ${data.approved ? "Approved" : "Pending"}
        </td>
        <td>
            <button 
                class="${data.approved ? "revoke" : ""}" 
                onclick="toggleApproval('${item.id}', ${!data.approved})"
                ${
                  data.approved
                    ? 'title="Revoke Approval"'
                    : 'title="Approve Applicant"'
                }
            >
                ${data.approved ? "Revoke" : "Approve"}
            </button>
        </td>
        <td>
            <button class="expand-btn" data-id="${item.id}">Kembangkan ▼</button>
        </td>
    `;
      tbody.appendChild(row);
    });
  };

// Expand for more info
// Other field
const DETAIL_FIELDS = [
    { label: "Dokumen Lampiran Utama", key: "Dokumen Lampiran Utama" },
    { label: "Kategori Pemohon",               key: "Kategori pemohon" },
    { label: "Sebab Memohon Bantuan",          key: "Sebab Memohon Bantuan" },
    { label: "Alamat",                         key: "Alamat" },
    { label: "Daerah",                         key: "Daerah" },
    { label: "Poskod",                         key: "Poskod" },
    { label: "Bandar",                         key: "Bandar" },
    { label: "Negeri",                         key: "Negeri" },
    { label: "Kariah",                         key: "Kariah" },
    { label: "Warganegara",                    key: "Warganegara" },
    { label: "Tarikh Lahir",                   key: "Tarikh Lahir" },
    { label: "No. Telefon Rumah/Waris",        key: "No. Telefon Rumah/Waris" },
    { label: "Nama Waris",                     key: "Nama waris" },
    { label: "Tempoh Menetap di Selangor",     key: "Tempoh Menetap di Selangor" },
    { label: "Jantina",                        key: "Jantina" },
    { label: "Tarikh Masuk Islam",             key: "Tarikh Masuk Islam" },
    { label: "Kesihatan",                      key: "Kesihatan" },
    { label: "Status",                         key: "Status" },
    { label: "Poligami",                       key: "Poligami" },
    { label: "Nama Pemegang Akaun",            key: "Nama Pemegang Akaun" },
    { label: "Bank",                           key: "Bank" },
    { label: "No. Akaun Bank",                 key: "No. Akaun Bank" },
    { label: "Cara Pembayaran",                key: "Cara Pembayaran" },
    { label: "Sebab Pembayaran Tunai",         key: "Sebab Pembayaran Tunai" },
    { label: "Nama Si Mati",                   key: "Nama Si Mati" },
    { label: "No. Kad Pengenalan Si Mati",     key: "No. Kad Pengenalan Si Mati" },
    { label: "Maklumat Isi Rumah",             key: "Maklumat Isi Rumah" },
    { label: "Pekerjaan",                      key: "Pekerjaan" },
    { label: "Jawatan",                        key: "Jawatan" },
    { label: "Nama Majikan",                   key: "Nama Majikan" },
    { label: "No. Tel. Majikan",               key: "No. Tel. Majikan" },
    { label: "Sebab Tidak Bekerja",            key: "Sebab Tidak Bekerja" },
    { label: "Sektor",                         key: "Sektor" },
    { label: "Jenis Kerja Sendiri",            key: "Jenis Kerja Sendiri" },
    { label: "Sumber Pendapatan Bulanan",      key: "Sumber Pendapatan Bulanan" },
    { label: "Perbelanjaan Bulanan",           key: "Perbelanjaan Bulanan" },
    { label: "Hubungan kekeluargaan dengan kakitangan LZS?", key: "Hubungan kekeluargaan dengan kakitangan LZS?" },
    { label: "Nama Kakitangan",                key: "Nama Kakitangan" },
    { label: "Pejabat (Kakitangan Berhubungan)", key: "Pejabat (Kakitangan Berhubungan)" },
    { label: "Hubungan",                       key: "Hubungan" },
    { label: "Jawatan (Kakitangan Berhubungan)", key: "Jawatan (Kakitangan Berhubungan)" },
    { label: "Tarikh Permohonan",              key: "Tarikh" },
];

function buildDetailHTML(extra) {
  return DETAIL_FIELDS
    .map(f => {
      const value = extra[f.key];

      // Special formatting for "Maklumat Isi Rumah"
      if (f.key === "Maklumat Isi Rumah" && Array.isArray(value)) {
        const rumahHTML = value.map((item, index) => {
          const details = Object.entries(item)
            .map(([k, v]) => `<strong>${k}:</strong> ${v}`)
            .join("<br>");
          return `<div style="margin-bottom: 1em;"><u>Ahli Isi Rumah ${index + 1}</u><br>${details}</div>`;
        }).join("");
        return `<strong>${f.label}:</strong><br>${rumahHTML}`;
      }

      // Default for other fields
      return `<strong>${f.label}:</strong> ${value || "–"}<br>`;
    })
    .join("");
}


document.getElementById("tableBody").addEventListener("click", (e) => {
    const btn = e.target.closest(".expand-btn");
    if (!btn) return;
  
    const tr = btn.closest("tr");
    const docId = btn.dataset.id;
    const isOpen = tr.nextSibling?.classList.contains("detail-row");
  
    if (isOpen) {
      // close it
      tr.nextSibling.remove();
      btn.textContent = "Kembangkan ▼";
    } else {
      // open it: fetch extra data (if needed) and insert a new row
      db.collection("Penerima")
        .doc(docId)
        .get()
        .then(docSnap => {
          const extra = docSnap.data();
          const detailTr = document.createElement("tr");
          detailTr.classList.add("detail-row");
          detailTr.innerHTML = `<td colspan="9">${buildDetailHTML(extra)}</td>`;
          tr.parentNode.insertBefore(detailTr, tr.nextSibling);
          btn.textContent = "Tutup ▲";
        });
    }
  });

// Toggle approval status
async function toggleApproval(applicantId, newStatus) {
    try {
        await db.collection("applicants").doc(applicantId).update({
            approved: newStatus,
            updatedAt: firebase.firestore.FieldValue.serverTimestamp()
        });
        console.log(`Status updated to ${newStatus ? 'Approved' : 'Pending'}`);
    } catch (error) {
        console.error("Error updating status:", error);
        alert("Error updating status. Please try again.");
    }
}

// Initialize when page loads
window.addEventListener('load', () => {
    setupRealtimeUpdates();
});

//Reference from
// 1. Chatgpt
// 2. Deepseek