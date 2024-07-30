import {useKeycloak} from "@react-keycloak/web";
import axios, {AxiosResponse} from "axios";
import {IUserData} from "../../model/UserData";
import {IUserProfile} from "../../model/IUserProfile";

export default function useKeycloakData() {
    const {keycloak} = useKeycloak();

    async function getKeycloakUser():Promise<IUserData | undefined> {
        try {
            return {
                access_token: keycloak.token,
                refresh_token: keycloak.refreshToken,
                id_token: keycloak.idToken
            };
        } catch (error) {
            console.error('Failed to load user data:', error);
        }
    }


    const fetchUserProfile = async () => {
        try {
            const user: IUserData | undefined = await getKeycloakUser();
            console.log('User:', user);

            if (user && user.access_token) {
                const response = await axios.get<IUserProfile>(
                    'http://gatewayserver:8072/user-service/api/auth/profile',
                    {
                        headers: {
                            Accept: "application/json",
                            Authorization: "Bearer " + user.access_token
                        }
                    }
                );
                console.log('Profile Response:', response.data);
                return response.data;
            } else {
                throw new Error('User is not logged in');
            }
        } catch (error) {
            console.error('Error fetching user profile:', error);
            throw error;
        }
    };


    function logout() {
        return keycloak?.logout();
    }

    return { logout, fetchUserProfile};
}


// async function getUserProfile():Promise<IUserProfile | undefined> {
//     try {
//         const keycloakUserProfile = await keycloak.loadUserProfile();
//         const keycloakUserToken = keycloak.token;
//         return {
//             username: keycloakUserProfile.username,
//             email: keycloakUserProfile.email,
//             firstName: keycloakUserProfile.firstName,
//             lastName: keycloakUserProfile.lastName,
//             token: keycloakUserToken
//         };
//     } catch (error) {
//         console.error('Failed to load user profile:', error);
//     }
// }



// const getClientCredentialsToken = async () => {
//     try {
//         const tokenEndpoint = 'http://localhost:7080/realms/delivery-service/protocol/openid-connect/token';
//
//         const response = await axios.post(tokenEndpoint, new URLSearchParams({
//             'grant_type': 'client_credentials',
//             'client_id': 'delivery-service-app',
//             'client_secret': 'your_client_secret' // Replace with your actual client secret
//         }), {
//             headers: {
//                 'Content-Type': 'application/x-www-form-urlencoded'
//             }
//         });
//
//         const accessToken = response.data.access_token;
//         console.log('Access Token:', accessToken);
//
//         return accessToken;
//     } catch (error) {
//         console.error('Error obtaining access token:', error);
//     }
// };
//
// const fetchUserProfile = async () => {
//     try {
//         const accessToken = await getClientCredentialsToken();
//
//         const profileResponse = await axios.get('http://localhost:8072/user-service/api/auth/profile', {
//             headers: {
//                 'Authorization': `Bearer ${accessToken}`,
//                 'Content-Type': 'application/json'
//             }
//         });
//
//         console.log('User Profile:', profileResponse.data);
//         return profileResponse.data;
//
//     } catch (error) {
//         console.error('Error fetching profile:', error);
//     }
// };
//
// export default fetchUserProfile;