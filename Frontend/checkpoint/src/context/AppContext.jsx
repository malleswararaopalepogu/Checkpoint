import {createContext, useEffect, useState} from "react";
import { AppConstants } from "../assets/util/constants.js";

export const AppContext = createContext();

export const AppContextProvider = (props) => {

    const backendURL = AppConstants.BACKEND_URL;

    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userData, setUserData] = useState(false);

    const contextValue = {
        backendURL,
        isLoggedIn, setIsLoggedIn,
        userData, setUserData
    }
    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    )
}