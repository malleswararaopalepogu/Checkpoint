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

    const getUserData = async () => {
        try {
            const response = await axios.get(backendURL+"/profile");
            if (response.status === 200) {
                setUserData(response.data);
            } else {
                toast.error("Unable to retrieve profile");
            }
        }catch(error) {
            toast.error(error.message);
        }
    }

    const getAuthState = async () => {
        try {
            const response = await axios.get(backendURL+"/is-authenticated");
            if (response.status === 200 && response.data === true) {
                setIsLoggedIn(true);
                console.log("Calling getUserData()");
                await getUserData();
            } else {
                console.log("User is NOT logged in");
                setIsLoggedIn(false);
            }
        } catch (error) {
            console.error(error);
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