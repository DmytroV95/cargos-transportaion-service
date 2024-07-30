import {IsString, IsNotEmpty, IsNumber} from 'class-validator';

export class CargoMovementRequestDto {
    @IsNumber({}, { message: 'Cargo ID must be a number' })
    @IsNotEmpty({ message: 'Cargo ID is required' })
    cargoId: number;

    movementDate?: Date;

    @IsString({ message: 'The starting point of the cargo movement must be a string' })
    @IsNotEmpty({ message: 'The starting point of the cargo movement is required' })
    fromLocation: string;

    @IsString({ message: 'The destination point of the cargo movement must be a string' })
    @IsNotEmpty({ message: 'The destination point of the cargo movement is required' })
    toLocation: string;

    constructor(data: CargoMovementRequestDto) {
        this.cargoId = data.cargoId;
        this.movementDate = data.movementDate;
        this.fromLocation = data.fromLocation;
        this.toLocation = data.toLocation;
    }
}
