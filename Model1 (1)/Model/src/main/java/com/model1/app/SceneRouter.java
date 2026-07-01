package main.java.com.model1.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.com.model1.model.enums.Role;

public final class SceneRouter {
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 820;
    private static final String APP_STYLESHEET = "/styles/app.css";

    private final Stage primaryStage;

    public SceneRouter(Stage primaryStage) {
        this.primaryStage = Objects.requireNonNull(primaryStage, "primaryStage");
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showLogin() {
        AppContext.getInstance().clearActiveUser();
        showView("/view/login.fxml", "Model1 Command Center");
    }

    public void showDashboard(Role role) {
        showView("/view/dashboard.fxml", "Model1 " + displayRole(role) + " Dashboard");
    }

    public void showActiveDashboard() {
        Role role = AppContext.getInstance()
                .getActiveRole()
                .orElse(Role.CASHIER);
        showDashboard(role);
    }

    public void openCashierBillEditor() {
        showView("/view/cashier/bill-editor.fxml", "Model1 Bill Editor");
    }

    public void openCashierBillHistory() {
        showView("/view/cashier/bill-history.fxml", "Model1 Bill History");
    }

    public void openManagerNewProductCategory() {
        showView("/view/manager/inventory.fxml", "Model1 Inventory");
    }

    public void openManagerAddProduct() {
        showView("/view/manager/inventory.fxml", "Model1 Inventory");
    }

    public void openManagerReports() {
        showView("/view/manager/reports.fxml", "Model1 Manager Reports");
    }

    public void openManagerNotifications() {
        showView("/view/manager/notifications.fxml", "Model1 Notifications");
    }

    public void openAdminAddEmployee() {
        showView("/view/admin/employees.fxml", "Model1 Employees");
    }

    public void openAdminEditEmployee() {
        showView("/view/admin/employees.fxml", "Model1 Employees");
    }

    public void openAdminDeleteEmployee() {
        showView("/view/admin/employees.fxml", "Model1 Employees");
    }

    public void openAdminFinancialReports() {
        showView("/view/admin/financial-reports.fxml", "Model1 Financial Reports");
    }

    private void showView(String resource, String title) {
        try {
            Parent root = loadFxml(resource);
            Scene scene = new Scene(root, currentWidth(), currentHeight());
            applyStylesheet(scene);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
        } catch (IOException ex) {
            logRouteFailure(resource, ex);
            throw new IllegalStateException("Unable to load view: " + resource, ex);
        } catch (RuntimeException ex) {
            logRouteFailure(resource, ex);
            throw ex;
        }
    }

    private Parent loadFxml(String resource) throws IOException {
        String relativePath = normalizeResourcePath(resource);
        Path filePath = locateResourcePath(relativePath);
        if (filePath != null) {
            return new FXMLLoader(filePath.toUri().toURL()).load();
        }

        URL viewUrl = locateResourceUrl(resource);
        if (viewUrl == null) {
            throw new IllegalStateException("Missing FXML resource: " + resource);
        }

        FXMLLoader loader = new FXMLLoader(viewUrl);
        return loader.load();
    }

    private void applyStylesheet(Scene scene) {
        String stylesheetPath = normalizeResourcePath(APP_STYLESHEET);
        Path cssPath = locateResourcePath(stylesheetPath);
        if (cssPath != null) {
            scene.getStylesheets().add(cssPath.toUri().toString());
            return;
        }

        URL cssUrl = locateResourceUrl(APP_STYLESHEET);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
    }

    private String normalizeResourcePath(String resource) {
        return resource.startsWith("/") ? resource.substring(1) : resource;
    }

    private URL locateResourceUrl(String resource) {
        URL url = SceneRouter.class.getResource(resource);
        if (url != null) {
            return url;
        }

        String legacyPath = resource.startsWith("/") ? "/main/resources" + resource : "/main/resources/" + resource;
        url = SceneRouter.class.getResource(legacyPath);
        if (url != null) {
            return url;
        }

        String classLoaderPath = normalizeResourcePath(resource);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(classLoaderPath);
            if (url != null) {
                return url;
            }
        }

        Path filePath = locateResourcePath(classLoaderPath);
        return filePath == null ? null : toFileUrl(filePath);
    }

    private Path locateResourcePath(String relativePath) {
        LinkedHashSet<Path> candidates = new LinkedHashSet<>();
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        collectResourceCandidates(candidates, cwd, relativePath);

        Path current = cwd;
        for (int depth = 0; depth < 8 && current != null; depth++) {
            collectResourceCandidates(candidates, current, relativePath);
            current = current.getParent();
        }

        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private void collectResourceCandidates(LinkedHashSet<Path> candidates, Path basePath, String relativePath) {
        if (basePath == null) {
            return;
        }

        Path base = basePath.toAbsolutePath().normalize();
        List<Path> roots = List.of(
                base,
                base.resolve("bin"),
                base.resolve("src/main/resources"),
                base.resolve("Model1 (1)/Model"),
                base.resolve("Model1 (1)/Model/bin"),
                base.resolve("Model1 (1)/Model/src/main/resources"),
                base.resolve("Model"));

        for (Path root : roots) {
            candidates.add(root.resolve(relativePath));
        }
    }

    private URL toFileUrl(Path path) {
        if (!Files.isRegularFile(path)) {
            return null;
        }

        try {
            return path.toUri().toURL();
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    private void logRouteFailure(String resource, Throwable throwable) {
        System.err.println("[Model1] Unable to open route: " + resource);
        System.err.println("[Model1] Cause tree:");
        int depth = 0;
        Throwable cursor = throwable;
        while (cursor != null) {
            System.err.println("  ".repeat(depth) + "- " + cursor.getClass().getName() + ": " + cursor.getMessage());
            cursor = cursor.getCause();
            depth++;
        }
        System.err.println("[Model1] Full stack trace:");
        throwable.printStackTrace(System.err);
    }

    private double currentWidth() {
        Scene scene = primaryStage.getScene();
        return scene == null || scene.getWidth() <= 0 ? DEFAULT_WIDTH : scene.getWidth();
    }

    private double currentHeight() {
        Scene scene = primaryStage.getScene();
        return scene == null || scene.getHeight() <= 0 ? DEFAULT_HEIGHT : scene.getHeight();
    }

    private String displayRole(Role role) {
        return switch (role) {
            case CASHIER -> "Cashier";
            case MANAGER -> "Manager";
            case ADMINISTRATOR -> "Administrator";
        };
    }
}
