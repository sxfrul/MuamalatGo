const firebaseConfig = {
    apiKey: "AIzaSyDGhwxfDthhmEBvmj7gjbrw2q6NHActeIw",
    authDomain: "zakathon-f0fb9.firebaseapp.com",
    projectId: "zakathon-f0fb9",
    storageBucket: "zakathon-f0fb9.firebasestorage.app",
    messagingSenderId: "788168249299",
    appId: "1:788168249299:web:f70fc8807e2e1644faa926"
  };
  
  firebase.initializeApp(firebaseConfig);
  
  const auth = firebase.auth();
  const db = firebase.firestore();
  
  window.firebaseModule = { auth, db };
  