# Phase 2 Database Access Layer Status

Date completed: 2026-06-24

Phase 2 goal:

```text
Centralize SQLite connection handling and provide a transaction helper without changing application behavior.
```

## Completed

- Added a centralized database manager:

```text
Model1 (1)\Model\src\main\java\com\model1\repository\DatabaseManager.java
```

- Added a database exception type:

```text
Model1 (1)\Model\src\main\java\com\model1\exception\DatabaseException.java
```

- Added database configuration:

```text
Model1 (1)\Model\src\main\resources\app.properties
```

- Updated the legacy model classes to use `DatabaseManager.getConnection()`:

```text
Model1 (1)\Model\src\model\Database.java
Model1 (1)\Model\src\model\Get.java
Model1 (1)\Model\src\model\Return.java
Model1 (1)\Model\src\model\updateInsert.java
```

## New Database Configuration

Default config:

```properties
database.path=data.db
```

Default resolved JDBC URL:

```text
jdbc:sqlite:data.db
```

This preserves the current behavior: the app still expects `data.db` to be available from the working directory.

## Override Options

The database can now be redirected without changing source code.

Priority order:

1. JVM system property:

```text
-Dmodel1.db.url=jdbc:sqlite:C:/path/to/data.db
```

2. Environment variable:

```text
MODEL1_DB_URL=jdbc:sqlite:C:/path/to/data.db
```

3. `app.properties` value:

```properties
database.url=jdbc:sqlite:C:/path/to/data.db
```

If no explicit URL is set, the database path is resolved in this order:

1. JVM system property:

```text
-Dmodel1.db.path=C:/path/to/data.db
```

2. Environment variable:

```text
MODEL1_DB_PATH=C:/path/to/data.db
```

3. `app.properties`:

```properties
database.path=data.db
```

4. Built-in default:

```text
data.db
```

## DatabaseManager Responsibilities

```text
getConnection()
- Opens a SQLite connection.
- Applies PRAGMA foreign_keys = ON.

resolveDatabaseUrl()
- Resolves the active JDBC URL from system properties, environment variables, app.properties, or default path.

inTransaction(SqlWork<T>)
- Opens a connection.
- Disables auto-commit.
- Runs database work.
- Commits on success.
- Rolls back on SQL or runtime failure.
- Wraps SQL transaction failures in DatabaseException.

inTransaction(SqlVoidWork)
- Convenience overload for transaction blocks that do not return a value.
```

## Legacy Model Changes

Before Phase 2, the legacy model classes directly used:

```java
DriverManager.getConnection("jdbc:sqlite:data.db")
```

After Phase 2, they use:

```java
DatabaseManager.getConnection()
```

This keeps the existing SQL and method behavior in place while centralizing connection creation.

## Verification

Verified in this session:

- `src\model` no longer contains direct `DriverManager.getConnection(...)` calls.
- `src\model` no longer contains the hardcoded `jdbc:sqlite:data.db` URL.
- The only remaining direct `DriverManager.getConnection(...)` call is inside `DatabaseManager`.
- `app.properties` exists and points to `data.db`.

Not verified in this shell:

- Maven build.
- Java command-line compile.

Reason:

- `mvn` is not available on PATH in this shell.
- `java` and `javac` are detected but still return exit code `1` with no captured output from this shell.
- The user has confirmed the project runs through VS Code, so VS Code should be used to run the practical launch verification for now.

## Phase 2 Acceptance Criteria

Satisfied:

- `DatabaseManager` exists.
- Database path is configurable.
- Connection helper exists.
- Transaction helper exists.
- New and migrated database code uses `DatabaseManager`.
- Legacy model classes no longer own connection-string construction.

Recommended VS Code check:

```text
Run Launch MainApp (Phase 1)
Log in with a known user
Open one read-only flow such as bill statistics or product selection
```

This confirms that the centralized connection still resolves the same `data.db` at runtime.

