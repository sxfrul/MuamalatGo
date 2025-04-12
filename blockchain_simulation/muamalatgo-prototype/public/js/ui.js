function teleportTo(sectionId) {
    const sections = [
      'login-section',
      'signup-section',
      'donation-section',
      'logout-section',
      'penerima-dashboard',
      'permohonan-form'
    ];
  
    sections.forEach(id => {
      const el = document.getElementById(id);
      if (el) el.style.display = (id === sectionId) ? 'block' : 'none';
    });
  
    const blockView = document.getElementById('blocks');
    if (blockView) blockView.style.display = 'none';
  
    // 💨 Clear old status display
    const status1 = document.getElementById('status-penerima-dashboard');
    if (status1) status1.innerHTML = '';
  
    const status2 = document.getElementById('status-donation-section');
    if (status2) status2.innerHTML = '';
  }
  

function showAlert(message, type = "info") {
    alert(`${type.toUpperCase()}: ${message}`);
}

function setLoading(buttonId, isLoading) {
    const btn = document.getElementById(buttonId);
    if (btn) {
        btn.disabled = isLoading;
        btn.textContent = isLoading ? 'Please wait...' : btn.getAttribute('data-label');
    }
}

function resetForm(formId) {
    const form = document.getElementById(formId);
    if (form) form.reset();
}

function showPenerimaStep(step) {
    const steps = [];
  
    for (let i = 1; i <= 15; i++) {
      steps.push(document.getElementById(`signup-penerima-step${i}`));
    }
  
    steps.forEach((el, index) => {
      el.style.display = (step === index + 1) ? 'block' : 'none';
    });
  
    if (step === 0) {
      steps.forEach(el => el.style.display = 'none');
    }
  }
  
  function showLatestActivityFund(targetId) {
    const statusEl = document.getElementById(targetId);
    if (!statusEl) return;
  
    statusEl.innerHTML = 'Loading...';
  
    db.collection('distributions')
      .orderBy('date', 'desc')
      .limit(5)
      .get()
      .then(snapshot => {
        if (!snapshot.empty) {
          let output = '<ul>';
          snapshot.forEach(doc => {
            const data = doc.data();
            const date = data.date.toDate().toLocaleDateString('ms-MY', {
              day: 'numeric',
              month: 'long',
              year: 'numeric'
            });
            output += `<li>✅ RM${data.amount} was distributed to ${data.activity} on ${date}</li>`;
          });
          output += '</ul>';
          statusEl.innerHTML = output;
        } else {
          statusEl.textContent = 'No recent fund distribution found.';
        }
      })
      .catch(err => {
        console.error(err);
        statusEl.textContent = 'Failed to load distribution info.';
      });
  }
  
  
  
window.ui = {
    teleportTo,
    showAlert,
    setLoading,
    resetForm,
    showPenerimaStep,
    showLatestActivityFund
  };