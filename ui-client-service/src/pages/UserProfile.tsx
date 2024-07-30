import React, {useEffect, useState} from 'react';

import {IUserProfile} from "../model/IUserProfile";
import useKeycloakData from "../service/keycloak/KeycloakService";
import Container from "@mui/material/Container";
import {CircularProgress, Paper} from "@mui/material";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import {useNavigate} from "react-router-dom";

const UserProfile: React.FC = () => {
    const navigate = useNavigate();
    const {fetchUserProfile} = useKeycloakData();
    const [userProfile, setUserProfile] = useState<IUserProfile>();

    useEffect(() => {
        const loadUserData = async () => {
            try {
                const profile: IUserProfile = await fetchUserProfile();
                setUserProfile(profile);
            } catch (error) {
                console.error('UserProfile Failed to load user profile:', error);
            }
        };

        loadUserData();
    }, []);

    const handleNavigateHome = () => {
        navigate('/');
    };


    if (!userProfile) {
        return (
            <Container
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100vh'
                }}>
                <CircularProgress/>
            </Container>
        );
    }

    return (
        <Container maxWidth="sm" style={{marginTop: '10rem'}}>
            <Paper elevation={3} style={{padding: '2rem'}}>
                <Typography variant="h4" gutterBottom>
                    Welcome, {userProfile.username}
                </Typography>
                <Typography variant="body1">
                    <strong>First Name:</strong> {userProfile.username}
                </Typography>
                <Typography variant="body1">
                    <strong>Last Name:</strong> {userProfile.email}
                </Typography>
                <Box mt={3} display="flex" justifyContent="space-between">
                    <Button variant="contained" color="primary" onClick={handleNavigateHome}>
                        Back
                    </Button>
                    <Button variant="contained" color="secondary">
                        Update
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default UserProfile;
