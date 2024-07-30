import {Request, Response} from 'express';
import {CargoMovementRequestDto} from "../dto/cargoMovement/CargoMovementRequestDto";
import httpStatus from "http-status";
import {getCargoMovementCountsPerCargoId, getCargoMovementList, saveCargoMovement} from "../service/cargoMovement"
import {InternalError} from "../system/internalError";
import log4js from "log4js";
import {CargoMovementListRequestDto} from "../dto/cargoMovement/CargoMovementListRequestDto";
import {validateCargoIsExist} from "../client";
import {validate, ValidationError} from "class-validator";

/**
 * @swagger
 * /api/cargo-movement:
 *   post:
 *     summary: Save a new cargo movement
 *     description: Create a new cargo movement record in the system.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CargoMovementRequestDto'
 *     responses:
 *       201:
 *         description: Cargo movement created successfully
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/CargoMovementResponseDto'
 *       400:
 *         description: Bad request. Validation errors occurred.
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   target:
 *                     type: object
 *                   value:
 *                     type: string
 *                   property:
 *                     type: string
 *                   children:
 *                     type: array
 *                   constraints:
 *                     type: object
 *       404:
 *         description: Cargo not found
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   example: Cargo with ID {cargoId} does not exist. Please verify the cargo ID and try again.
 *       500:
 *         description: Internal server error
 */
export const saveCargoMovementApi = async (req: Request, res: Response): Promise<void> => {
    try {
        const requestDto: CargoMovementRequestDto = new CargoMovementRequestDto(req.body);
        await validateRequestData(res, requestDto);
        const isCargoValid: boolean = await validateCargoIsExist(requestDto.cargoId);
        if (!isCargoValid) {
            res.status(404).send(
                {
                    message: `Cargo with ID ${requestDto.cargoId} does not exist. Please verify the cargo ID and try again.`
                });
            return;
        }
        const responseDto = await saveCargoMovement(requestDto);
        res.status(httpStatus.CREATED).send(responseDto);
    } catch (error) {
        const {message, status} = new InternalError(error);
        log4js.getLogger().error('Error in creating CargoMovement.', error);
        res.status(status).send({message});
    }
};

/**
 * @swagger
 * /api/cargo-movement:
 *   get:
 *     summary: Retrieve a list of cargo movements
 *     description: Fetch a list of cargo movement records based on query parameters.
 *     parameters:
 *       - in: query
 *         name: cargoId
 *         required: true
 *         schema:
 *           type: number
 *         description: Cargo ID to filter the cargo movements. This parameter is required and must be a number.
 *       - in: query
 *         name: size
 *         schema:
 *           type: number
 *           minimum: 1
 *         description: The maximum number of objects that will be returned in the response. It must be at least 1.
 *       - in: query
 *         name: from
 *         schema:
 *           type: number
 *           minimum: 0
 *         description: Number of the element from which sampling will begin. It must be a non-negative number.
 *     responses:
 *       200:
 *         description: A list of cargo movements
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/CargoMovementResponseDto'
 *       400:
 *         description: Bad request. Validation errors occurred.
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   target:
 *                     type: object
 *                   value:
 *                     type: string
 *                   property:
 *                     type: string
 *                   children:
 *                     type: array
 *                   constraints:
 *                     type: object
 *       500:
 *         description: Internal server error
 */
export const getCargoMovementListApi = async (req: Request, res: Response): Promise<void> => {
    try {
        const requestDto = CargoMovementListRequestDto.createFromQuery(req.query);
        await validateRequestData(res, requestDto);
        const responseDtoList = await getCargoMovementList(requestDto);
        res.status(200).send(responseDtoList);
    } catch (error) {
        const {message, status} = new InternalError(error);
        log4js.getLogger().error('Error fetching CargoMovementList.', error);
        res.status(status).send({message});
    }
};

/**
 * @swagger
 * /api/cargo-movement/_counts:
 *   post:
 *     summary: Get cargo movement counts per cargo ID
 *     description: Retrieve the count of movements for each specified cargo ID.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               cargosIds:
 *                 type: array
 *                 items:
 *                   type: string
 *                 example: ["1", "2", "3"]
 *     responses:
 *       200:
 *         description: Cargo movement counts retrieved successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               additionalProperties:
 *                 type: integer
 *                 example: 5
 *       400:
 *         description: Invalid or missing cargoIds
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   example: Invalid or missing cargoIds
 *       500:
 *         description: Internal server error
 */
export const getCargoMovementCountsPerCargoIdApi = async (req: Request, res: Response): Promise<void> => {
    try {
        const {cargosIds} = req.body;
        if (!cargosIds || !Array.isArray(cargosIds)) {
            res.status(400).send({message: 'Invalid or missing cargoIds'});
            return;
        }
        const counts = await getCargoMovementCountsPerCargoId(cargosIds);
        res.status(200).send(counts);
    } catch (error) {
        const {message, status} = new InternalError(error);
        log4js.getLogger().error('Error fetching CargoMovement count per cargo Id.', error);
        res.status(status).send({message});
    }
};

const validateRequestData = async (res: Response, requestDto: any): Promise<void> => {
    const errors: ValidationError[] = await validate(requestDto);
    if (errors.length > 0) {
        res.status(400).send(errors);
        return;
    }
}
