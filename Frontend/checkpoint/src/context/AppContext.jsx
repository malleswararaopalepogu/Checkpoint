import {createContext, useEffect, useState} from "react";
import { AppConstants } from "../assets/util/constants.js";
import {toast} from "react-toastify";
import axios from "axios";

export const AppContext = createContext();

export const AppContextProvider = (props) => {

     axios.defaults.withCredentials = true;

    const backendURL = AppConstants.BACKEND_URL;

    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userData, setUserData] = useState(false);

    const getUserData = async (authToken) => {
        try {
            const token = authToken || localStorage.getItem("token");
            const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
            const response = await axios.get(backendURL + "/profile", config);
            if (response.status === 200) {
                setUserData(response.data);
            }
        } catch(error) {
            console.error("Profile retrieval error:", error);
            setUserData(false);
        }
    }

    const getAuthState = async () => {
        try {
            const token = localStorage.getItem("token");
            const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
            const response = await axios.get(backendURL + "/is-authenticated", config);
            if (response.status === 200 && response.data === true) {
                setIsLoggedIn(true);
                await getUserData(token);
            } else {
                setIsLoggedIn(false);
                setUserData(false);
            }
        } catch (error) {
            console.error(error);
            setIsLoggedIn(false);
            setUserData(false);
        }
    }

    useEffect(() => {
        getAuthState();
    }, []);

    const contextValue = {
        backendURL,
        isLoggedIn, setIsLoggedIn,
        userData, setUserData,
        getUserData
    }
    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    )
}