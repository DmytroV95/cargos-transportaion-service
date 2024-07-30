import request from "supertest";
import express from "express";
import bodyParser from "body-parser";
import {
    getCargoMovementCountsPerCargoIdApi,
    getCargoMovementListApi,
    saveCargoMovementApi
} from "../../controller/CargoMovement";
import {
    saveCargoMovement,
    getCargoMovementList,
    getCargoMovementCountsPerCargoId
} from "../../service/cargoMovement";
import {CargoMovementRequestDto} from "../../dto/cargoMovement/CargoMovementRequestDto";
import {CargoMovementListRequestDto} from "../../dto/cargoMovement/CargoMovementListRequestDto";
import {MongoMemoryServer} from "mongodb-memory-server";
import mongoose from "mongoose";
import {validateCargoIsExist} from "../../client";


let mongoServer: MongoMemoryServer;

beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const uri = mongoServer.getUri();
    await mongoose.connect(uri);
});

afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
});

afterEach(async () => {
    const collections = mongoose.connection.collections;
    for (const key in collections) {
        const collection = collections[key];
        await collection.deleteMany({});
    }
});

jest.mock('../../service/cargoMovement', () => ({
    saveCargoMovement: jest.fn(),
    getCargoMovementList: jest.fn(),
    getCargoMovementCountsPerCargoId: jest.fn(),
}));

jest.mock('../../client', () => ({
    validateCargoIsExist: jest.fn(),
}));

jest.mock('../../dto/cargoMovement/CargoMovementListRequestDto', () => ({
    CargoMovementListRequestDto: {
        createFromQuery: jest.fn(),
    },
}));

jest.mock('../../dto/cargoMovement/CargoMovementRequestDto', () => ({
    CargoMovementRequestDto: jest.fn(),
}));

const app = express();
const saveCargoMovementApiUrl = "/api/cargoMovement";
const getCargoMovementListApiUrl = "/api/cargoMovements";
const getCargoMovementCountsPerCargoIdApiUrl = "/api/cargoMovement/_counts";

app.use(bodyParser.json());
app.post(saveCargoMovementApiUrl, saveCargoMovementApi);
app.get(getCargoMovementListApiUrl, getCargoMovementListApi);
app.post(getCargoMovementCountsPerCargoIdApiUrl, getCargoMovementCountsPerCargoIdApi);

const requestDto = {
    cargoId: "123",
    movementDate: "2024-05-20",
    fromLocation: "A",
    toLocation: "B"
};

describe("Cargo Movement Controller", () => {

    describe("saveCargoMovementApi", () => {
        it("should create a new cargo movement", async () => {
            (validateCargoIsExist as jest.Mock).mockResolvedValue(true);
            (CargoMovementRequestDto as jest.Mock).mockImplementation(() => requestDto);
            (saveCargoMovement as jest.Mock).mockResolvedValue(requestDto);

            const response = await request(app)
                .post(saveCargoMovementApiUrl)
                .send(requestDto);

            expect(response.status).toBe(201);
            expect(response.body).toMatchObject(requestDto);
        });

        it("should return 404 if cargo ID does not exist", async () => {
            (validateCargoIsExist as jest.Mock).mockResolvedValue(false);
            (CargoMovementRequestDto as jest.Mock).mockImplementation(() => requestDto);

            const response = await request(app)
                .post(saveCargoMovementApiUrl)
                .send(requestDto);

            expect(response.status).toBe(404);
            expect(response.body.message).toBe("Cargo with ID 123 does not exist. Please verify the cargo ID and try again.");
        });

        it("should handle internal errors", async () => {
            (validateCargoIsExist as jest.Mock)
                .mockRejectedValue(new Error("An internal server error occurred."));
            (CargoMovementRequestDto as jest.Mock).mockImplementation(() => requestDto);

            const response = await request(app)
                .post(saveCargoMovementApiUrl)
                .send(requestDto);

            expect(response.status).toBe(500);
            expect(response.body.message).toBe("An internal server error occurred.");
        });
    });

    describe("getCargoMovementListApi", () => {
        it("should return a list of cargo movements", async () => {
            const requestDto = {cargoId: "123"};
            const responseDtoList = [
                {cargoId: "123", movementDate: "2024-05-20", fromLocation: "A", toLocation: "B"}
            ];
            (CargoMovementListRequestDto.createFromQuery as jest.Mock).mockImplementation(() => requestDto);
            (getCargoMovementList as jest.Mock).mockResolvedValue(responseDtoList);

            const response = await request(app)
                .get(getCargoMovementListApiUrl)
                .query({cargoId: "123"});

            expect(response.status).toBe(200);
            expect(response.body).toMatchObject(responseDtoList);
        });

        it("should handle internal errors", async () => {
            (CargoMovementListRequestDto.createFromQuery as jest.Mock).mockImplementation(() => {
                throw new Error("An internal server error occurred.")
            });

            const response = await request(app)
                .get(getCargoMovementListApiUrl)
                .query({cargoId: "123"});

            expect(response.status).toBe(500);
            expect(response.body.message).toBe("An internal server error occurred.");
        });
    });

    describe("getCargoMovementCountsPerCargoIdApi", () => {
        it("should return counts of cargo movements per cargo ID", async () => {
            const requestDto = {cargosIds: ["123"]};
            const counts = {"123": 5};
            (getCargoMovementCountsPerCargoId as jest.Mock).mockResolvedValue(counts);

            const response = await request(app)
                .post(getCargoMovementCountsPerCargoIdApiUrl)
                .send(requestDto);

            expect(response.status).toBe(200);
            expect(response.body).toMatchObject(counts);
        });

        it("should return 400 if cargoIds are missing or invalid", async () => {
            const response = await request(app)
                .post(getCargoMovementCountsPerCargoIdApiUrl)
                .send({});

            expect(response.status).toBe(400);
            expect(response.body.message).toBe("Invalid or missing cargoIds");
        });

        it("should handle internal errors", async () => {
            const requestDto = {cargosIds: ["123"]};
            (getCargoMovementCountsPerCargoId as jest.Mock)
                .mockRejectedValue(new Error("An internal server error occurred."));

            const response = await request(app)
                .post(getCargoMovementCountsPerCargoIdApiUrl)
                .send(requestDto);

            expect(response.status).toBe(500);
            expect(response.body.message).toBe("An internal server error occurred.");
        });
    });
});