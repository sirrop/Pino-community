package jp.gr.java_conf.alpius.pino.ui.actionSystem;

import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.tool.action.DefaultToolActions;

import java.util.HashMap;
import java.util.Map;

public class ActionRegistry {
    private final Map<String, Action> actionMap = new HashMap<>();

    public ActionRegistry() {
        for (Class<? extends Action> action : CoreActions.get()) {
            register(action);
        }
        for (Class<? extends Action> action : DefaultToolActions.get()) {
            register(action);
        }
    }

    public static ActionRegistry getInstance() {
        return ApplicationManager.getApp().getService(ActionRegistry.class);
    }

    public void register(Class<? extends Action> actionClass) {
        try {
            register(actionClass.getConstructor().newInstance());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public void register(Action action) {
        actionMap.put(action.getActionID(), action);
    }

    public Action find(String actionId) throws ActionNotFoundException {
        var res = actionMap.get(actionId);
        if (res == null) {
            throw new ActionNotFoundException(actionId);
        }
        return res;
    }
}
