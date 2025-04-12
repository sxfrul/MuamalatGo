window.addEventListener("load", () => {
  const { auth, db } = window.firebaseModule;

  async function registerPenderma() {
    const nama = document.getElementById('penderma-nama').value;
    const emel = document.getElementById('penderma-emel').value;
    const phone = document.getElementById('penderma-phone').value;
    const password = document.getElementById('penderma-password').value;
  
    try {
      const userCredential = await auth.createUserWithEmailAndPassword(emel, password);
      const user = userCredential.user;
      
      const data = {
        nama,
        emel,
        phone,
        uid: user.uid
      };
  
      await db.collection("penderma").doc(user.uid).set(data);
      alert("Berjaya daftar sebagai Penderma!");
    } catch (err) {
      alert(err.message);
    }
  }
  

  async function registerPenerima() {
    const nama = document.getElementById('penerima-nama').value;
    const emel = document.getElementById('penerima-emel').value;
    const kadJenis = document.getElementById('penerima-kad').value;
    const ic = document.getElementById('penerima-ic').value;
    const phone = document.getElementById('penerima-phone').value;
    const pendapatan = document.getElementById('penerima-pendapatan').value;
    const password = document.getElementById('penerima-password').value;

    // Dokumen lampiran
    const dokumen = {
      kadPengenalan: document.getElementById('dokumen-kp').checked,
      penyataBank: document.getElementById('dokumen-bank').checked,
      sijil: document.getElementById('dokumen-sijil').checked,
      bilUtiliti: document.getElementById('dokumen-bil').checked,
      kwsp: document.getElementById('dokumen-kwsp').checked
    };

    const kategoriPemohon = document.querySelector('input[name="kategori-pemohon"]:checked').value;

    try {
      const userCredential = await auth.createUserWithEmailAndPassword(emel, password);
      const user = userCredential.user;

      const penerimaData = {
        nama,
        emel,
        jenisKad: kadJenis,
        ic,
        phone,
        pendapatan,
        kategoriPemohon,
        dokumen,
        uid: user.uid
      };

      await db.collection("penerima").doc(user.uid).set(penerimaData);
      alert("Berjaya daftar sebagai Penerima!");
      return true;
    } catch (err) {
      alert(err.message);
      return false;
    }
  }


  async function login() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
  
    if (!email || !password) {
      alert("Sila masukkan email dan kata laluan.");
      return;
    }
  
    try {
      const userCredential = await auth.signInWithEmailAndPassword(email, password);
      const user = userCredential.user;
  
      const penerimaDoc = await db.collection("penerima").doc(user.uid).get();
      if (penerimaDoc.exists) {
        const data = penerimaDoc.data();
  
        document.getElementById('penerima-dash-nama').textContent = data.nama;
        document.getElementById('penerima-dash-ic').textContent = data.ic;
  
        ui.teleportTo('penerima-dashboard');
      } else {
        ui.teleportTo('donation-section');
      }
  
      document.getElementById('logout-section').style.display = 'block';
  
    } catch (err) {
      console.error("Login error:", err);
  
      if (err.code === "auth/user-not-found") {
        alert("Email tidak berdaftar.");
      } else if (err.code === "auth/wrong-password") {
        alert("Kata laluan salah.");
      } else if (err.code === "auth/invalid-email") {
        alert("Format email tidak sah.");
      } else {
        alert("Ralat semasa log masuk: " + err.message);
      }
    }
  }
  

  function logout() {
    auth.signOut().then(() => {
      ui.teleportTo('login-section');
      document.getElementById('blocks').innerHTML = '';
      alert("Logged out!");
    });
  }

  window.authModule = { registerPenderma, registerPenerima, login, logout };
});
