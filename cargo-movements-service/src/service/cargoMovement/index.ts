import {CargoMovementRequestDto} from "../../dto/cargoMovement/CargoMovementRequestDto";
import {toCargoMovement, toCargoMovementResponseDto} from "../../mapper/CargoMovementMapper";
import CargoMovement from "../../model/CargoMovement";
import log4js from 'log4js';
import {EntityNotSavedError} from "../../errors/EntityNotSavedError";
import {CargoMovementListRequestDto} from "../../dto/cargoMovement/CargoMovementListRequestDto";
import {CargoMovementResponseDto} from "../../dto/cargoMovement/CargoMovementResponseDto";
import {EntityNotFoundError} from "../../errors/EntityNotFoundError";

const SORT_DESC_ORDER = -1;
const MILLISECONDS_IN_HOUR = 3600000;
const UTC_PLUS_3_OFFSET = 3 * MILLISECONDS_IN_HOUR;

interface AggregationResult {
    _id: string;
    count: number;
}

/**
 * Save a new cargo movement record.
 *
 * @param {CargoMovementRequestDto} requestDto - The DTO containing cargo movement details.
 * @returns {Promise<CargoMovementResponseDto>} - The saved cargo movement as a response DTO.
 * @throws {EntityNotSavedError} - If there is an error in saving the cargo movement.
 */
export const saveCargoMovement = async (
    requestDto: CargoMovementRequestDto): Promise<CargoMovementResponseDto> => {
    try {
        if (!requestDto.movementDate) {
            const now = new Date();
            requestDto.movementDate = new Date(now.getTime() + UTC_PLUS_3_OFFSET);
        }
        const cargoMovement = await toCargoMovement(requestDto).save();
        return toCargoMovementResponseDto(cargoMovement);
    } catch (error) {
        const errorMessage = 'Error in creating cargoMovement in service';
        log4js.getLogger().error(errorMessage, error);
        throw new EntityNotSavedError(errorMessage);
    }
};

/**
 * Get a list of cargo movements based on request DTO.
 *
 * @param {CargoMovementListRequestDto} requestDto - The DTO containing query parameters for
 * fetching cargo movements.
 * @returns {Promise<CargoMovementResponseDto[]>} - A list of cargo movements as response DTOs.
 * @throws {EntityNotFoundError} - If the cargo with the specified ID does not exist.
 */
export const getCargoMovementList = async (
    requestDto: CargoMovementListRequestDto): Promise<CargoMovementResponseDto[]> => {
    const cargoExist = await CargoMovement.exists({cargoId: requestDto.cargoId});
    if (!cargoExist) {
        throw new EntityNotFoundError(`Cargo with ID ${requestDto.cargoId} does not exist.`);
    }
    const cargoMovements = await CargoMovement
        .find({cargoId: requestDto.cargoId})
        .sort({movementDate: SORT_DESC_ORDER})
        .skip(Number(requestDto.from))
        .limit(Number(requestDto.size));
    return cargoMovements.map(toCargoMovementResponseDto);
};

/**
 * Get cargo movement counts per cargo ID.
 *
 * @param {string[]} cargosIds - An array of cargo IDs to fetch counts for.
 * @returns {Promise<Record<string, number>>} - An object mapping cargo IDs to
 * their respective movement counts.
 */
export const getCargoMovementCountsPerCargoId = async (
    cargosIds: string[]): Promise<Record<string, number>> => {
    const pipeline = [
        {
            $match: {
                cargoId: {$in: cargosIds}
            }
        },
        {
            $group: {
                _id: "$cargoId",
                count: {$sum: 1}
            }
        }
    ];
    const counts: Record<string, number> = {};
    const results: AggregationResult[] = await CargoMovement.aggregate(pipeline);
    results.forEach((result: any) => {
        counts[result._id] = result.count;
    });

    return counts;
};
