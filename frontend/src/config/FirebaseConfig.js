// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";
import { getFirestore } from "firebase/firestore"
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDV2TIOvufBqjwvMjihMUPZK2DfhjItyRw",
  authDomain: "imagestore-f373f.firebaseapp.com",
  projectId: "imagestore-f373f",
  storageBucket: "imagestore-f373f.appspot.com",
  messagingSenderId: "888809142831",
  appId: "1:888809142831:web:3928fce0ca1bb7049f24da",
  measurementId: "G-F2R5B2RN22"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const storage = getStorage(app);
const txtDB = getFirestore(app)

export {storage, txtDB};