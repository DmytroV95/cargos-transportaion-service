export class EntityNotSavedError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'EntityNotSavedError';
    }
}
