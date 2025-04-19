const firebaseConfig = {
  apiKey: "AIzaSyDNaSOujZTLQ2m8keQmuXYE02dtkY_D7FQ",
  authDomain: "umhackathon-zakatapp-mvp.firebaseapp.com",
  projectId: "umhackathon-zakatapp-mvp",
  storageBucket: "umhackathon-zakatapp-mvp.firebasestorage.app",
  messagingSenderId: "150330163152",
  appId: "1:150330163152:android:b693bb958b0dedd12ac3d5",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();

// Sorting Order
const ASNAF_ORDER = [
  "Fakir",
  "Miskin",
  "Mualaf",
  "Riqab",
  "Gharim",
  "Fisabilillah",
  "Ibnu Sabil",
];

// Real-time listener
function setupRealtimeUpdates() {
  db.collection("PenerimaAPI")
    .limit(20)
    .onSnapshot((snapshot) => {
      const items = snapshot.docs.map((doc) => ({
        id: doc.id,
        data: doc.data(),
      }));

      // Sorting logic
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
        return dateA - dateB;
      });

      renderRows(items);
    });
}

function renderRows(items) {
  const tbody = document.getElementById("tableBody");
  tbody.innerHTML = "";

  items.forEach((item) => {
    const data = item.data;
    const row = document.createElement("tr");
    //row.className = data.approved ? "approved" : "pending";

    row.innerHTML = `
        <td>${data["Nama Pemohon/Institusi"] || "N/A"}</td>
        <td>${data["No. K/P (baru)/Polis/Tentera/No. Pasport"] || "N/A"}</td>
        <td>${data["No. Telefon Bimbit"] || "N/A"}</td>
        <td>${
          data["Kelayakan Ramalan"] === 1
            ? "Layak"
            : data["Kelayakan Ramalan"] === 0
            ? "Tak Layak"
            : "N/A"
        }</td>
        <td>${data["Asnaf Ramalan"] || "N/A"}</td>
        <td>${data["Pemalsuan Ramalan"] || "N/A"}</td>
        <td>${data["Kegunaan"] || "N/A"}</td>
        <td>${data["JumlahMataWang"] || "N/A"}</td>
        <td>
          <span style="display: inline-flex; align-items: center; gap: 6px;">
            <span 
              style="width: 10px; height: 10px; border-radius: 50%; background-color: ${
                data.approved ? "green" : "gray"
              }; display: inline-block;">
            </span>
            ${data.approved ? "Approved" : "Pending"}
          </span>
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
            <button class="expand-btn" data-id="${
              item.id
            }">Kembangkan ▼</button>
        </td>
    `;
    tbody.appendChild(row);
  });
}

// Expand for more info
// Other field
const DETAIL_FIELDS = [
  { label: "Dokumen Lampiran Utama", key: "Dokumen Lampiran Utama" },
  { label: "Kategori Pemohon", key: "Kategori pemohon" },
  { label: "Sebab Memohon Bantuan", key: "Sebab Memohon Bantuan" },
  { label: "Alamat", key: "Alamat" },
  { label: "Daerah", key: "Daerah" },
  { label: "Poskod", key: "Poskod" },
  { label: "Bandar", key: "Bandar" },
  { label: "Negeri", key: "Negeri" },
  { label: "Kariah", key: "Kariah" },
  { label: "Warganegara", key: "Warganegara" },
  { label: "Emel", key: "Emel" },
  { label: "Tarikh Lahir", key: "Tarikh Lahir" },
  { label: "No. Telefon Rumah/Waris", key: "No. Telefon Rumah/Waris" },
  { label: "Nama Waris", key: "Nama waris" },
  { label: "Tempoh Menetap di Selangor", key: "Tempoh Menetap di Selangor" },
  { label: "Jantina", key: "Jantina" },
  { label: "Tarikh Masuk Islam", key: "Tarikh Masuk Islam" },
  { label: "Kesihatan", key: "Kesihatan" },
  { label: "Status", key: "Status" },
  { label: "Poligami", key: "Poligami" },
  { label: "Nama Pemegang Akaun", key: "Nama Pemegang Akaun" },
  { label: "Bank", key: "Bank" },
  { label: "No. Akaun Bank", key: "No. Akaun Bank" },
  { label: "Cara Pembayaran", key: "Cara Pembayaran" },
  { label: "Sebab Pembayaran Tunai", key: "Sebab Pembayaran Tunai" },
  { label: "Nama Si Mati", key: "Nama Si Mati" },
  { label: "No. Kad Pengenalan Si Mati", key: "No. Kad Pengenalan Si Mati" },
  { label: "Maklumat Isi Rumah", key: "Maklumat Isi Rumah" },
  { label: "Pekerjaan", key: "Pekerjaan" },
  { label: "Jawatan", key: "Jawatan" },
  { label: "Nama Majikan", key: "Nama Majikan" },
  { label: "No. Tel. Majikan", key: "No. Tel. Majikan" },
  { label: "Sebab Tidak Bekerja", key: "Sebab Tidak Bekerja" },
  { label: "Sektor", key: "Sektor" },
  { label: "Jenis Kerja Sendiri", key: "Jenis Kerja Sendiri" },
  { label: "Sumber Pendapatan Bulanan", key: "Sumber Pendapatan Bulanan" },
  { label: "Perbelanjaan Bulanan", key: "Perbelanjaan Bulanan" },
  {
    label: "Hubungan kekeluargaan dengan kakitangan LZS?",
    key: "Hubungan kekeluargaan dengan kakitangan LZS?",
  },
  { label: "Nama Kakitangan", key: "Nama Kakitangan" },
  {
    label: "Pejabat (Kakitangan Berhubungan)",
    key: "Pejabat (Kakitangan Berhubungan)",
  },
  { label: "Hubungan", key: "Hubungan" },
  {
    label: "Jawatan (Kakitangan Berhubungan)",
    key: "Jawatan (Kakitangan Berhubungan)",
  },
  { label: "Tarikh Permohonan", key: "Tarikh" },
];

function escapeHTML(str) {
  const element = document.createElement("div");
  if (str) {
    element.innerText = str;
    return element.innerHTML;
  }
  return "";
}

const FIELDS_AS_TABLE = [
  "Maklumat Isi Rumah",
  "Sumber Pendapatan Bulanan",
  "Perbelanjaan Bulanan",
];

function buildDetailHTML(extra) {
  return DETAIL_FIELDS.map((f) => {
    const raw = extra[f.key];
    let parsed;

    // Only try JSON.parse if it looks like a JSON string
    if (typeof raw === "string") {
      const t = raw.trim();
      if (t.startsWith("{") || t.startsWith("[")) {
        try {
          parsed = JSON.parse(t);
        } catch (e) {
          console.warn(`Couldn’t parse ${f.key} JSON`, e);
        }
      }
    }

    // Case A: parsed is an array → full, dynamic‑column table
    if (Array.isArray(parsed)) {
      if (parsed.length === 0) {
        return `<strong>${f.label}:</strong> Tiada data<br>`;
      }
      const headers = Object.keys(parsed[0]);
      const rows = parsed
        .map(
          (row) =>
            `<tr>${headers
              .map((h) => `<td>${escapeHTML(row[h] ?? "")}</td>`)
              .join("")}</tr>`
        )
        .join("");
      return `
        <strong>${f.label}:</strong><br>
        <table style="width:100%;border:1px solid #ccc;border-collapse:collapse;margin:.5em 0;">
          <thead><tr>${headers
            .map((h) => `<th>${escapeHTML(h)}</th>`)
            .join("")}</tr></thead>
          <tbody>${rows}</tbody>
        </table>
      `;
    }

    // Case B: parsed is an object → two‑column key→value table
    if (parsed && typeof parsed === "object") {
      const rows = Object.entries(parsed)
        .map(
          ([k, v]) => `
          <tr>
            <td style="padding:4px;border:1px solid #ccc;">${escapeHTML(k)}</td>
            <td style="padding:4px;border:1px solid #ccc;">${escapeHTML(
              String(v)
            )}</td>
          </tr>`
        )
        .join("");
      return `
        <strong>${f.label}:</strong><br>
        <table style="width:50%;border:1px solid #ccc;border-collapse:collapse;margin:.5em 0;">
          <thead><tr><th>Perkara</th><th>Nilai</th></tr></thead>
          <tbody>${rows}</tbody>
        </table>
      `;
    }

    // Fallback: simple value (including dates, strings, numbers, etc.)
    const safe = raw === null || raw === "" ? "–" : escapeHTML(String(raw));
    return `<strong>${f.label}:</strong> ${safe}<br>`;
  }).join("");
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
    db.collection("PenerimaAPI")
      .doc(docId)
      .get()
      .then((docSnap) => {
        console.log("Full doc:", docSnap.data());
        console.log(docId);
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
    await db.collection("PenerimaAPI").doc(applicantId).update({
      approved: newStatus,
      updatedAt: firebase.firestore.FieldValue.serverTimestamp(),
    });
    console.log(`Status updated to ${newStatus ? "Approved" : "Pending"}`);
  } catch (error) {
    console.error("Error updating status:", error);
    alert("Error updating status. Please try again.");
  }
}

// Initialize when page loads
window.addEventListener("load", () => {
  setupRealtimeUpdates();
});

//Reference from
// 1. Chatgpt
// 2. Deepseek
