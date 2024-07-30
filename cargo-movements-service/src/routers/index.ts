import express from 'express';
import cargoMovement from "./cargoMovement"

const router = express.Router();

router.use('/api/cargo-movement', cargoMovement);

export default router;
