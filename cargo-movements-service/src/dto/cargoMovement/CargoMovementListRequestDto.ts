import {IsInt, IsNotEmpty, IsPositive, Min} from 'class-validator';

export class CargoMovementListRequestDto {
    @IsInt({message: 'Cargo ID must be an number'})
    @IsNotEmpty({message: 'Cargo ID is required'})
    @IsPositive({
        message: 'Cargo ID will must be a positive number'
    })
    cargoId: number;

    @IsInt({
        message: 'The maximum number of objects that will be'
            + ' returned must be an number'
    })
    @Min(1, {
        message: 'The maximum number of objects that will be'
            + ' returned must be at least 1'
    })
    size?: number;

    @IsInt({
        message: 'Number of the element from which sampling '
            + 'will begin must be an number'
    })
    @Min(0, {
        message: 'Number of the element from which sampling'
            + ' will begin must be at least 0'
    })
    from?: number;

    constructor(data: { cargoId: number; size?: number; from?: number }) {
        this.cargoId = data.cargoId;
        this.size = data.size;
        this.from = data.from;
    }

    static createFromQuery(query: any): CargoMovementListRequestDto {
        const cargoId = parseInt(query.cargoId);
        const size = query.size ? parseInt(query.size) : 10;
        const from = query.from ? parseInt(query.from) : 0;
        return new CargoMovementListRequestDto({cargoId, size, from});
    }
}
