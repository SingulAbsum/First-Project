package main.java.com.model1.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;

import main.java.com.model1.exception.DatabaseException;

public final class DatabaseManager {
    private static final String CONFIG_RESOURCE = "app.properties";
    private static final String DEFAULT_DATABASE_PATH = "data.db";
    private static final Properties CONFIG = loadConfig();

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(resolveDatabaseUrl());
        enableForeignKeys(connection);
        return connection;
    }

    public static String resolveDatabaseUrl() {
        String explicitUrl = firstNonBlank(
                System.getProperty("model1.db.url"),
                System.getenv("MODEL1_DB_URL"),
                CONFIG.getProperty("database.url"));

        if (explicitUrl != null) {
            return explicitUrl;
        }

        String databasePath = firstNonBlank(
                System.getProperty("model1.db.path"),
                System.getenv("MODEL1_DB_PATH"),
                CONFIG.getProperty("database.path"),
                DEFAULT_DATABASE_PATH);

        return "jdbc:sqlite:" + resolveExistingDatabasePath(databasePath);
    }

    private static String resolveExistingDatabasePath(String configuredPath) {
        Path configured = Paths.get(configuredPath);
        if (configured.isAbsolute()) {
            if (isUsableDatabaseFile(configured)) {
                return configuredPath;
            }
            throw new DatabaseException("Configured database file does not exist or is empty: " + configuredPath, null);
        }

        LinkedHashSet<Path> candidates = new LinkedHashSet<>();
        Path cwd = Paths.get("").toAbsolutePath();
        addDatabaseCandidates(candidates, cwd, configuredPath);
        addDatabaseCandidates(candidates, classpathRoot(), configuredPath);

        Path current = cwd;
        while (current != null) {
            addDatabaseCandidates(candidates, current, configuredPath);
            current = current.getParent();
        }

        for (Path candidate : candidates) {
            if (isUsableDatabaseFile(candidate)) {
                return candidate.toString();
            }
        }

        throw new DatabaseException("Unable to locate a usable database file named " + configuredPath, null);
    }

    private static void addDatabaseCandidates(LinkedHashSet<Path> candidates, Path basePath, String configuredPath) {
        if (basePath == null) {
            return;
        }

        Path base = basePath.toAbsolutePath().normalize();
        candidates.add(base.resolve(configuredPath));
        candidates.add(base.resolve("Model").resolve(configuredPath));
        candidates.add(base.resolve("Model1 (1)").resolve("Model").resolve(configuredPath));

        Path parent = base.getParent();
        if (parent != null) {
            candidates.add(parent.resolve(configuredPath));
            candidates.add(parent.resolve("Model").resolve(configuredPath));
        }

        Path grandparent = parent == null ? null : parent.getParent();
        if (grandparent != null) {
            candidates.add(grandparent.resolve(configuredPath));
            candidates.add(grandparent.resolve("Model").resolve(configuredPath));
        }
    }

    private static Path classpathRoot() {
        try {
            URL location = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation();
            if (location == null) {
                return null;
            }

            Path path = Paths.get(location.toURI());
            return Files.isRegularFile(path) ? path.getParent() : path;
        } catch (URISyntaxException | IllegalArgumentException ex) {
            return null;
        }
    }

    private static boolean isUsableDatabaseFile(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }

        try {
            return Files.size(path) > 0;
        } catch (IOException ex) {
            return false;
        }
    }

    public static <T> T inTransaction(SqlWork<T> work) {
        Objects.requireNonNull(work, "work");

        try (Connection connection = getConnection()) {
            boolean previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                T result = work.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException ex) {
                rollback(connection);
                throw new DatabaseException("Database transaction failed.", ex);
            } catch (RuntimeException ex) {
                rollback(connection);
                throw ex;
            } finally {
                connection.setAutoCommit(previousAutoCommit);
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Database transaction failed.", ex);
        }
    }

    public static void inTransaction(SqlVoidWork work) {
        Objects.requireNonNull(work, "work");
        inTransaction(connection -> {
            work.execute(connection);
            return null;
        });
    }

    private static void enableForeignKeys(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
    }

    private static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException rollbackError) {
            throw new DatabaseException("Database rollback failed.", rollbackError);
        }
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            throw new DatabaseException("Unable to load database configuration.", ex);
        }
        return properties;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    @FunctionalInterface
    public interface SqlWork<T> {
        T execute(Connection connection) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlVoidWork {
        void execute(Connection connection) throws SQLException;
    }
}
