import axios from "axios";
import log4js from "log4js";

const CARGOS_API_URL = 'http://gatewayserver:8072/delivery-service/api/cargos';

/**
 * Send a GET request to the CARGOS_API_URL to check if the cargo exists.
 * @param cargoId The ID of the cargo to validate.
 * @returns A Promise that resolves to a boolean indicating whether the cargo exists.
 */
export const validateCargoIsExist = async (cargoId: number): Promise<boolean> => {
    try {
        const response = await axios.get(`${CARGOS_API_URL}/${cargoId}`);
        return response.status === 200;
    } catch (error) {
        const errorMessage = `Cargo by id ${cargoId} not found`;
        log4js.getLogger().error(errorMessage, error);
        return false;
    }
};
