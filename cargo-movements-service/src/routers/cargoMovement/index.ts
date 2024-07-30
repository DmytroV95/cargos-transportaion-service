import { Router } from 'express';
import {
    getCargoMovementCountsPerCargoIdApi,
    getCargoMovementListApi,
    saveCargoMovementApi
} from "../../controller/CargoMovement";

const router = Router();

router.post('', saveCargoMovementApi);
router.get('', getCargoMovementListApi);
router.post('/_counts', getCargoMovementCountsPerCargoIdApi);

export default router;
