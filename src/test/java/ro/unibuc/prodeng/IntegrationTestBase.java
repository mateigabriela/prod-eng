package ro.unibuc.prodeng;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests that need a real MongoDB database.
 * Uses Testcontainers to spin up a MongoDB instance in Docker.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public abstract class IntegrationTestBase {
    private static final String MONGO_ENV_URL = resolveMongoEnvUrl();

    private static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:6.0.20")
                    .withLabel("ro.unibuc.prodeng", "integration-test-mongo");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        if (MONGO_ENV_URL != null) {
            registry.add("mongodb.connection.url", () -> MONGO_ENV_URL);
            return;
        }

        try {
            if (!mongoDBContainer.isRunning()) {
                mongoDBContainer.start();
            }
            registry.add("mongodb.connection.url", mongoDBContainer::getReplicaSetUrl);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Cannot configure MongoDB for integration tests. Set MONGODB_CONNECTION_URL (or MONGODB_CONECTION_URL) " +
                            "or make sure Docker is available for Testcontainers.",
                    ex
            );
        }
    }

    private static String resolveMongoEnvUrl() {
        String correctName = System.getenv("MONGODB_CONNECTION_URL");
        if (correctName != null && !correctName.isBlank()) {
            return correctName;
        }

        String legacyTypoName = System.getenv("MONGODB_CONECTION_URL");
        if (legacyTypoName != null && !legacyTypoName.isBlank()) {
            return legacyTypoName;
        }

        return null;
    }
}
