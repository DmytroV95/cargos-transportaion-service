const config = {
    log4js: {
        appenders: {
            console: {
                type: 'console',
            },
            ms: {
                type: 'dateFile',
                pattern: '-yyyy-MM-dd.log',
                alwaysIncludePattern: true,
                filename: 'logs/ms',
                maxLogSize: 1000000,
                compress: true,
            },
        },
        categories: {
            default: {
                appenders: ['ms', 'console'],
                level: 'debug',
            },
        },
    },
    swagger: {
        swaggerDefinition: {
            openapi: "3.0.0",
            info: {
                title: "Cargo Movement API",
                version: "1.0.0",
                description: "Cargo Movement service API to provide movement information about cargo",
                contact: {name: "Dmytro Varukha"},
            },
            servers: [{url: "http://localhost:8081", description: "Development server"}],
            components: {
                schemas: {
                    CargoMovementRequestDto: {
                        type: 'object',
                        properties: {
                            cargoId: {type: 'string'},
                            movementDate: {type: 'string', format: 'date'},
                            fromLocation: {type: 'string'},
                            toLocation: {type: 'string'},
                        },
                        required: ['cargoId', 'movementDate', 'fromLocation', 'toLocation'],
                    },
                    CargoMovementResponseDto: {
                        type: 'object',
                        properties: {
                            _id: {type: 'string'},
                            cargoId: {type: 'string'},
                            movementDate: {type: 'string', format: 'date'},
                            fromLocation: {type: 'string'},
                            toLocation: {type: 'string'},
                        },
                    },
                },
            },
        },
        apis: ["./src/controller/*.ts", "./src/dto/cargoMovement/*.ts"],
    },
};
export default config;
