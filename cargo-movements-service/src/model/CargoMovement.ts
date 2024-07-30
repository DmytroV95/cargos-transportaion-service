import mongoose, {Schema, Document} from "mongoose";

export interface ICargoMovement extends Document {
    cargoId: string;
    movementDate: Date;
    fromLocation: string;
    toLocation: string;
}

const cargoMovementSchema = new Schema<ICargoMovement>({
    cargoId: {type: String, required: true},
    movementDate: {type: Date, required: true},
    fromLocation: {type: String, required: true},
    toLocation: {type: String, required: true},
});

const CargoMovement = mongoose.model<ICargoMovement>(
    "CargoMovement",
    cargoMovementSchema
);

export default CargoMovement;
