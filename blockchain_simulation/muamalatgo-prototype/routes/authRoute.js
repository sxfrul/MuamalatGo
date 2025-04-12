const express = require('express');
const router = express.Router();
const admin = require('firebase-admin');

// Register User (create via Firebase Auth + Firestore)
router.post('/register', async (req, res) => {
  const { email, password, name } = req.body;
  try {
    const userRecord = await admin.auth().createUser({
      email,
      password,
      displayName: name
    });

    await admin.firestore().collection('users').doc(userRecord.uid).set({
      name,
      email,
      createdAt: new Date()
    });

    res.status(201).json({ message: 'User registered!', uid: userRecord.uid });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// Login is handled on frontend, but here's token verification
router.get('/me', async (req, res) => {
  const authHeader = req.headers.authorization;
  if (!authHeader) return res.status(401).json({ error: 'No token provided' });

  const token = authHeader.split(' ')[1];
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    res.json({ uid: decodedToken.uid });
  } catch (error) {
    res.status(401).json({ error: 'Invalid token' });
  }
});

module.exports = router;