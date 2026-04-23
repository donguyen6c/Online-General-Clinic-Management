import { initializeApp } from "firebase/app";
import { FacebookAuthProvider, getAuth, GoogleAuthProvider } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyA6ZMIl1dy7hjLr-cH-6wOPmgzICcHB-lo",
  authDomain: "clinicapp-5adfa.firebaseapp.com",
  projectId: "clinicapp-5adfa",
  storageBucket: "clinicapp-5adfa.firebasestorage.app",
  messagingSenderId: "587834879398",
  appId: "1:587834879398:web:bc8433282fe7215bee3367",
  measurementId: "G-BG4CP80G64"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();
export const facebookProvider = new FacebookAuthProvider();