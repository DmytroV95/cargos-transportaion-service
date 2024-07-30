export interface CargoMovementResponseDto {
    _id: number;
    movementDate: Date;
    cargoId: string;
    fromLocation: string;
    toLocation: string;
}
