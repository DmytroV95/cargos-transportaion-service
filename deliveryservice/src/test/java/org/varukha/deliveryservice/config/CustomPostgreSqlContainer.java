package org.varukha.deliveryservice.config;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Custom PostgreSQL container for integration tests.
 * This class extends PostgreSQLContainer to provide a custom configuration.
 */
public class CustomPostgreSqlContainer extends PostgreSQLContainer<CustomPostgreSqlContainer> {
    private static final String DB_IMAGE = "postgres";
    private static final String TEST_DB_URL = "TEST_DB_URL";
    private static final String TEST_DB_USERNAME = "TEST_DB_USERNAME";
    private static final String TEST_DB_PASSWORD = "TEST_DB_PASSWORD";
    private static CustomPostgreSqlContainer postgreSqlContainer;

    /**
     * Constructs a new CustomPostgreSqlContainer.
     */
    private CustomPostgreSqlContainer() {
        super(DB_IMAGE);
    }

    /**
     * Retrieves a singleton instance of CustomPostgreSqlContainer.
     *
     * @return the singleton instance of CustomPostgreSqlContainer.
     */
    public static synchronized CustomPostgreSqlContainer getInstance() {
        if (postgreSqlContainer == null) {
            postgreSqlContainer = new CustomPostgreSqlContainer();
        }
        return postgreSqlContainer;
    }

    /**
     * Starts the container and sets system properties for
     * test database URL, username, and password.
     */
    @Override
    public void start() {
        super.start();
        System.setProperty(TEST_DB_URL, postgreSqlContainer.getJdbcUrl());
        System.setProperty(TEST_DB_USERNAME, postgreSqlContainer.getUsername());
        System.setProperty(TEST_DB_PASSWORD, postgreSqlContainer.getPassword());
    }

    /**
     * Stops the container.
     */
    @Override
    public void stop() {
        super.stop();
    }
}
