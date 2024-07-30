package com.varukha.emailservice.config;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

/**
 * Custom Elasticsearch container for integration tests.
 * This class extends ElasticsearchContainer to provide a custom configuration.
 */
public class CustomElasticsearchContainer extends ElasticsearchContainer {
    private static CustomElasticsearchContainer elasticsearchContainer;
    private static final String ES_IMAGE = "elasticsearch:8.6.1";
    private static final String TEST_ES_URL = "TEST_ES_URL";

    /**
     * Constructs a new CustomElasticsearchContainer.
     */
    private CustomElasticsearchContainer() {
        super(ES_IMAGE);
    }

    /**
     * Retrieves a singleton instance of CustomElasticsearchContainer.
     *
     * @return the singleton instance of CustomElasticsearchContainer.
     */
    public static synchronized CustomElasticsearchContainer getInstance() {
        if (elasticsearchContainer == null) {
            elasticsearchContainer = new CustomElasticsearchContainer();
        }
        return elasticsearchContainer;
    }

    /**
     * Starts the container and sets system properties for
     * test Elasticsearch URL.
     */
    @Override
    public void start() {
        super.start();
        System.setProperty(TEST_ES_URL, elasticsearchContainer.getHttpHostAddress());
    }

    /**
     * Stops the container.
     */
    @Override
    public void stop() {
        super.stop();
    }
}
