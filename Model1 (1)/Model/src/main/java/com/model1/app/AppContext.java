package main.java.com.model1.app;

import java.util.Optional;

import main.java.com.model1.model.dto.UserSession;
import main.java.com.model1.model.enums.Role;

public final class AppContext {
    private static final AppContext INSTANCE = new AppContext();

    private UserSession activeSession;
    private SceneRouter sceneRouter;

    private AppContext() {
    }

    public static AppContext getInstance() {
        return INSTANCE;
    }

    public Optional<String> getActiveUsername() {
        return getActiveSession().map(UserSession::username);
    }

    public Optional<Role> getActiveRole() {
        return getActiveSession().map(UserSession::role);
    }

    public Optional<UserSession> getActiveSession() {
        return Optional.ofNullable(activeSession);
    }

    public Optional<SceneRouter> getSceneRouter() {
        return Optional.ofNullable(sceneRouter);
    }

    public void setSceneRouter(SceneRouter sceneRouter) {
        this.sceneRouter = sceneRouter;
    }

    public void setActiveSession(UserSession session) {
        this.activeSession = session;
    }

    public void setActiveUser(String username, Role role) {
        setActiveSession(new UserSession(username, role));
    }

    public void setActiveUser(String username, String role) {
        setActiveUser(username, Role.valueOf(role));
    }

    public void clearActiveUser() {
        this.activeSession = null;
    }
}
