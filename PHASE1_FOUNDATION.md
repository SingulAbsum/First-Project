# Phase 1 Foundation Status

Date completed: 2026-06-24

Phase 1 goal:

```text
Introduce a modern project foundation while keeping the existing JavaFX MVC application intact.
```

## Completed

- Added a Maven project descriptor:

```text
Model1 (1)\Model\pom.xml
```

- Added the standard migration directories:

```text
Model1 (1)\Model\src\main\java
Model1 (1)\Model\src\main\resources
Model1 (1)\Model\src\test\java
```

- Added the initial modern `app` package:

```text
Model1 (1)\Model\src\main\java\com\model1\app\MainApp.java
Model1 (1)\Model\src\main\java\com\model1\app\AppContext.java
Model1 (1)\Model\src\main\java\com\model1\app\SceneRouter.java
```

- Added a second VS Code launch configuration:

```text
Launch MainApp (Phase 1)
```

The original VS Code launch configuration remains available:

```text
Launch Main (JavaFX)
```

## Build Tool Choice

Maven was selected for the modernization path.

Reasons:

- It is a common fit for JavaFX desktop applications.
- It gives predictable dependency management.
- It works well with JUnit, SQLite JDBC, JavaFX plugins, and VS Code Java tooling.
- It is simpler than Gradle for this migration's first foundation step.

## Maven Dependencies Declared

The new `pom.xml` declares:

```text
Java release: 22
JavaFX controls: 23.0.1
JavaFX FXML: 23.0.1
SQLite JDBC: 3.47.1.0
JUnit Jupiter: 5.11.4
SLF4J API: 2.0.16
Logback Classic: 1.5.12
```

## Temporary Source Layout Bridge

The current legacy source code still lives directly under:

```text
Model1 (1)\Model\src\control
Model1 (1)\Model\src\main
Model1 (1)\Model\src\model
Model1 (1)\Model\src\view
```

Because of that, `pom.xml` temporarily uses:

```xml
<sourceDirectory>src</sourceDirectory>
```

This keeps the existing app buildable while the future phases migrate code gradually into:

```text
src\main\java
```

Once the legacy packages are moved or replaced, the Maven source directory can be returned to the standard default.

## New Entry Point

New Phase 1 entry point:

```text
com.model1.app.MainApp
```

Current behavior:

- Delegates to the existing JavaFX application class:

```text
view.mainLogin
```

This means the modern launcher exists, but the existing UI and behavior remain unchanged.

## New App Package Responsibilities

```text
MainApp
- Modern entry point.
- Launches the current JavaFX login app.

AppContext
- Placeholder for future active-user/session state.
- Currently stores active username and role.

SceneRouter
- Placeholder for future centralized navigation.
- Currently exposes bridge methods to the existing role dashboards.
```

## VS Code Launch Configurations

Current launch file:

```text
.vscode\launch.json
```

Available launch targets:

```text
Launch Main (JavaFX)
- main.Main
- legacy entry point

Launch MainApp (Phase 1)
- com.model1.app.MainApp
- new foundation entry point
```

Both launch targets use the existing JavaFX module path:

```text
C:/Users/User/Downloads/javafx-sdk-23.0.1/lib
```

## Verification

Verified in this session:

- `pom.xml` is well-formed XML.
- `.vscode\launch.json` is valid JSON.
- The new `app` package files exist.
- The standard resource and test directories exist.

Not verified in this shell:

- Maven command-line build.
- Java command-line launch.

Reason:

- `mvn` and `gradle` are not installed in this shell.
- `java` and `javac` are detected on the machine but return exit code `1` with no captured output from this shell.
- The user has confirmed JavaFX runs from VS Code, so VS Code launch verification should be used for now.

## Phase 1 Acceptance Criteria

Satisfied:

- Maven foundation file exists.
- Standard directories exist.
- Initial `app` package exists.
- App can be launched through a new intended entry point in VS Code.
- Legacy app remains intact.

Pending external/tooling confirmation:

- Install or expose Maven in the shell if command-line Maven verification is required.
- Run `Launch MainApp (Phase 1)` in VS Code to confirm the new wrapper starts the current UI.

