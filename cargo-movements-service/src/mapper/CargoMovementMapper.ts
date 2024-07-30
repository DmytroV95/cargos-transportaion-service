import { CargoMovementRequestDto } from "../dto/cargoMovement/CargoMovementRequestDto";
import CargoMovement, { ICargoMovement } from "../model/CargoMovement";
import { CargoMovementResponseDto } from "../dto/cargoMovement/CargoMovementResponseDto";

export const toCargoMovementResponseDto = ({
                                               _id,
                                               cargoId,
                                               movementDate,
                                               fromLocation,
                                               toLocation
                                           }: ICargoMovement): CargoMovementResponseDto => {
    if (_id == null) {
        throw new Error("Invalid input: _id is null or undefined");
    }
    if (cargoId == null) {
        throw new Error("Invalid input: cargoId is null or undefined");
    }
    if (movementDate == null) {
        throw new Error("Invalid input: movementDate is null or undefined");
    }
    if (fromLocation == null) {
        throw new Error("Invalid input: fromLocation is null or undefined");
    }
    if (toLocation == null) {
        throw new Error("Invalid input: toLocation is null or undefined");
    }

    return <CargoMovementResponseDto>{
        _id,
        cargoId,
        movementDate,
        fromLocation,
        toLocation
    };
};

export const toCargoMovement = ({
                                    cargoId,
                                    movementDate,
                                    fromLocation,
                                    toLocation
                                }: CargoMovementRequestDto) => {
    if (cargoId == null) {
        throw new Error("Invalid input: cargoId is null or undefined");
    }
    if (movementDate == null) {
        throw new Error("Invalid input: movementDate is null or undefined");
    }
    if (fromLocation == null) {
        throw new Error("Invalid input: fromLocation is null or undefined");
    }
    if (toLocation == null) {
        throw new Error("Invalid input: toLocation is null or undefined");
    }

    return new CargoMovement({
        cargoId,
        movementDate,
        fromLocation,
        toLocation
    });
};
