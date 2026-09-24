import { Route, Routes } from 'react-router-dom'
import './App.css'
import Home from './assets/pages/Home'
import Login from './assets/pages/Login'
import EmailVerify from './assets/pages/EmailVerify'
import ResetPassword from './assets/pages/ResetPassword'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

const App = () =>{
  return(
    <div> 
      <ToastContainer/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/email-verify" element={<EmailVerify/>}/>
        <Route path="/reset-password" element={<ResetPassword/>}/>
      </Routes>
    </div>
  )
}
 

export default App