import { assets } from "../assets"
import { Form, Link } from "react-router-dom"
import { useState, useContext } from "react"
import { AppContext } from "../../context/AppContext.jsx" 
import axios from "axios"
import { toast } from "react-toastify"
import { useNavigate } from "react-router-dom" 

const Login = () =>
{
    const [isCreateAccount, setIsCreateAccount] = useState(false);
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const {backendURL, setIsLoggedIn, getUserData} = useContext(AppContext);
    const navigate = useNavigate();

    const onSubmitHandler = async (e) => {
        e.preventDefault();
        axios.defaults.withCredentials = true;
        setLoading(true);
        try {
            if (isCreateAccount) {
                //register API
                const response = await axios.post(`${backendURL}/register`, {name, email, password});
                if (response.status === 201) {
                    navigate("/");
                    setName("");
                    setEmail("");
                    setPassword("");
                    toast.success("Account created successfully.");
                } else {
                    toast.error("Email already exists");
                }
            } 
             else {
                //Login API
                const response = await axios.post(`${backendURL}/login`, {email, password});
                if (response.status === 200) {
                    setIsLoggedIn(true);
                    getUserData();
                    navigate("/");
                    toast.success("Login successful.");
                } else {
                    toast.error("Email/Password incorrect");
                }
            }
        }catch(error) {
            console.log("LOGIN ERROR:", error);
            console.log("RESPONSE:", error.response);
            toast.error(error.response?.data?.message || "Login Failed");
        } finally {
            setLoading(false);
        }
    }

    return(
        <div className="position-relative min-vh-100 d-flex justify-content-center align-items-center"
            style={{background: "linear-gradient(90deg, #6a5af9, #8268f9)", border: "none"}}>
                <div style={{position: "absolute", top: "20px", left: "30px", display: "flex", alignItems: "center"}}>
                    <Link to="/" style={{
                        display: "flex",
                        gap: "8px",
                        alignItems: "center",
                        fontWeight: "bold",
                        fontSize: "24px",
                        textDecoration: "none"
                    }}>
                    <img src={assets.logo} alt="logo" height={32} width={32} />
                    <span className="fw-bold fs-4 text-light">Checkpoint</span>
                </Link>
                </div>
                
                <div className="card p-4" style={{maxWidth: "400px", width: "100%"}}>
                    <h2 className="text-center mb-4">
                        {isCreateAccount ? "Create Account" : "Login"}
                    </h2>
                    <form onSubmit={onSubmitHandler}>
                    {
                        isCreateAccount && 
                        (
                            <div className="mb-3">
                                <label htmlFor="fullName" className="form-label">Full Name</label>
                                <input
                                    type="text"
                                    id="fullName"
                                    className="form-control"
                                    placeholder="Enter fullname"
                                    required
                                    onChange={(e) => setName(e.target.value)}
                                    value={name}
                                />
                            </div>
                        )
                    }
                    <div className="mb-3">
                        <label htmlFor="Email" className="form-label">Email ID</label>
                        <input type="email"
                                id="email"
                                autoComplete="off"
                                className="form-control"
                                placeholder="Enter email"
                                required
                                onChange={(e) => setEmail(e.target.value)}
                                value={email}
                        />
                        </div>

                        <div className="mb-3">
                            <label htmlFor="Password" className="form-label">Password</label>
                            <input type="password"
                                id="password"
                                className="form-control"
                                placeholder="**********"
                                required
                                onChange={(e) => setPassword(e.target.value)}
                                value={password}
                            />
                        </div>

                        <div className="d-flex justify-content-between mb-3">
                            <Link to="/reset-password" className="text-decoration-none">
                                Forgot password?
                            </Link>
                        </div>

                        <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                            {loading ? "Loading..." : isCreateAccount ? "Sign Up" : "Login" }
                        </button>
                    </form>

                    <div className="text-center mt-3">
                        <p className="mb-0">
                            {
                                isCreateAccount ?
                                (
                                    <>
                                        Already have an account?{" "}  
                                        <span
                                            onClick={() => {
                                                setIsCreateAccount(false);
                                                setName("");
                                                setEmail("");
                                                setPassword("");
                                            }}
                                            className="text-decoration-underline"
                                            style={{cursor: "pointer"}}
                                        >
                                            Login here
                                        </span>
                                    </>
                                ):
                                (
                                    <>
                                        Don't have an account?{" "}
                                        <span
                                            onClick={() => {
                                                setIsCreateAccount(true);
                                                setName("");
                                                setEmail("");
                                                setPassword("");
                                            }}
                                            className="text-decoration-underline"
                                            style={{cursor: "pointer"}}
                                        >
                                            Sign up
                                        </span>
                                    </>
                                )
                            }
                        </p>
                    </div>
                </div>  
        </div>
    )
}
export default Login;