import Keycloak from 'keycloak-js';

interface KeycloakConfig {
    url: string;
    realm: string;
    clientId: string,
}

const keycloakConfig: KeycloakConfig = {
    url: 'http://localhost:7080',
    realm: 'delivery-service',
    clientId: 'delivery-service-app',
};

let keycloakInstance: Keycloak | null = null;

const getKeycloakInstance = () => {
    if (!keycloakInstance) {
        keycloakInstance = new Keycloak(keycloakConfig);
    }
    return keycloakInstance;
};

export default getKeycloakInstance;
