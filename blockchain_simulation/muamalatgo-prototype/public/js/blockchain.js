// Simulated Blockchain
window.addEventListener("load", () => {
  const { auth, db } = window.firebaseModule;

  function sha256(str) {
    return crypto.subtle.digest("SHA-256", new TextEncoder().encode(str)).then(buf =>
      Array.from(new Uint8Array(buf)).map(b => b.toString(16).padStart(2, '0')).join('')
    );
  }

  async function donate() {
    const user = auth.currentUser;
    if (!user) return alert('You must be logged in.');

    const amount = parseFloat(document.getElementById('amount').value);
    const fundType = document.getElementById('fundType').value;
    const selectedGateway = document.getElementById('walletSelect').value;
    const timestamp = new Date().toISOString();

    const lastBlockSnap = await db.collection('blocks').orderBy('index', 'desc').limit(1).get();
    let previousHash = "0";
    let index = 1;

    if (!lastBlockSnap.empty) {
      const lastBlock = lastBlockSnap.docs[0].data();
      previousHash = lastBlock.hash;
      index = lastBlock.index + 1;
    }

    const blockData = { index, txId: "TX-" + Date.now(), userId: user.uid, amount, fundType, selectedGateway, timestamp, previousHash };
    const hash = await sha256(JSON.stringify(blockData));
    blockData.hash = hash;

    db.collection('blocks').add(blockData)
      .then(() => alert('Donation recorded on the chain!'))
      .catch(err => alert(err.message));
  }

  async function mohonBantuan() {
    const user = firebaseModule.auth.currentUser;
    const db = firebaseModule.db;

    if (!user) {
      alert("Sila login terlebih dahulu.");
      return;
    }

    const kegunaan = document.getElementById('kegunaan').value;
    const jumlah = document.getElementById('amount').value;

    if (!kegunaan || !jumlah) {
      alert("Sila isi semua maklumat permohonan.");
      return;
    }

    try {
      const penerimaSnap = await db.collection("penerima").doc(user.uid).get();

      if (!penerimaSnap.exists) {
        alert("Maklumat penerima tidak dijumpai.");
        return;
      }

      const data = penerimaSnap.data();

      const permohonan = {
        nama: data.nama,
        ic: data.ic,
        phone: data.phone,
        pendapatan: data.pendapatan,
        kegunaan,
        jumlah,
        status: "dalam semakan",
        timestamp: new Date().toISOString(),
        uid: user.uid
      };

      await db.collection("permohonan").add(permohonan);
      alert("Permohonan berjaya dihantar!");
      document.getElementById('kegunaan').value = "";
      document.getElementById('amount').value = "";
    } catch (err) {
      alert("Ralat semasa menghantar permohonan: " + err.message);
    }
  }


  async function verifyChain() {
    const snapshot = await db.collection('blocks').orderBy('index').get();
    const blocks = snapshot.docs.map(doc => doc.data());

    let isValid = true;

    for (let i = 0; i < blocks.length; i++) {
      const b = blocks[i];
      const dataToHash = {
        index: b.index, txId: b.txId, userId: b.userId, amount: b.amount,
        fundType: b.fundType, selectedGateway: b.selectedGateway, timestamp: b.timestamp,
        previousHash: b.previousHash
      };
      const recalculatedHash = await sha256(JSON.stringify(dataToHash));
      if (recalculatedHash !== b.hash || (i > 0 && b.previousHash !== blocks[i - 1].hash)) {
        console.warn(`❌ Tampered or broken chain at index ${b.index}`);
        isValid = false;
        break;
      }
    }

    alert(isValid ? "✅ Chain valid!" : "❌ Chain broken!");
  }

  function showDetails(block) {
    alert(`
      Transaction ID: ${block.txId}
      Wallet From: ${block.selectedGateway}
      Amount: RM${block.amount}
      Fund Type: ${block.fundType}
      Time: ${new Date(block.timestamp).toLocaleString()}
      Hash: ${block.hash}
      Prev Hash: ${block.previousHash}
    `);
  }

  function loadBlocks() {
    const blockDiv = document.getElementById('blocks');
    blockDiv.style.display = 'block';
    db.collection('blocks').orderBy('index', 'desc').get().then(snapshot => {
      const blocks = snapshot.docs.map(doc => doc.data()); // store all in array
      const blockDiv = document.getElementById('blocks');
      blockDiv.innerHTML = '<h3>Blockchain Blocks:</h3>';

      blocks.forEach(b => {
        const amountColor = b.amount < 0 ? 'red' : 'green';

        const blockHTML = `
  <div class="block" data-index="${b.index}" style="border-left: 5px solid ${amountColor}; padding-left: 10px;">
    <strong style="color: ${amountColor};">${b.fundType.toUpperCase()}</strong> - <span style="color: ${amountColor};">RM${b.amount}</span><br>
    <div class="hash"><b>Hash:</b> ${b.hash}</div>
    <div class="hash"><b>Prev:</b> ${b.previousHash}</div>
    <div><small>${new Date(b.timestamp).toLocaleString()}</small></div>
  </div>
`;

        blockDiv.innerHTML += blockHTML;
      });

      // ✅ Add click event AFTER all blocks are rendered
      document.querySelectorAll('.block').forEach(blockEl => {
        blockEl.addEventListener('click', () => {
          const index = parseInt(blockEl.getAttribute('data-index')); // 🎯 ambil index dari HTML
          const clickedBlock = blocks.find(b => b.index === index); // 🔎 cari block yang padan
          showDetails(clickedBlock); // 🚀 pass the full block object
        });
      });
    });
  }


  window.blockchainModule = { donate, verifyChain, loadBlocks, showDetails, mohonBantuan };
  console.log("✅ blockchain.js loaded");
});
