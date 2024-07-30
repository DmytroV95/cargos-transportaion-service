import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {ReactKeycloakProvider} from "@react-keycloak/web";
import UserProfile from "./pages/UserProfile";
import getKeycloakInstance from "./config/KeycloakConfig";
import Header from "./component/header";

const App: React.FC = () => {
    const tokenLogger = (tokens: any) => {
        console.log('onKeycloakTokens', tokens);
    };

    return (
        <ReactKeycloakProvider
            authClient={getKeycloakInstance()}
            initOptions={{onLoad: 'login-required'}}
            onTokens={tokenLogger}
        >
            <BrowserRouter>
                <div className="App">
                    <Routes>
                        <Route path="/" element={<Header/>}/>
                        <Route path="/profile" element={<UserProfile/>}/>
                    </Routes>
                    <Header/>
                </div>
            </BrowserRouter>
        </ReactKeycloakProvider>
    );
}

export default App;
