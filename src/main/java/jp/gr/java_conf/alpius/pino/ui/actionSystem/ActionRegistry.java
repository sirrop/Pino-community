package jp.gr.java_conf.alpius.pino.ui.actionSystem;

import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.internal.filter.action.FilterActions;
import jp.gr.java_conf.alpius.pino.tool.action.DefaultToolActions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActionRegistry {
    private final Map<String, Action> actionMap = new HashMap<>();

    public ActionRegistry() {
        List<Class<? extends Action>> defaultActions = new LinkedList<>(CoreActions.get());
        defaultActions.addAll(DefaultToolActions.get());
        defaultActions.addAll(FilterActions.getActions());

        for (Class<? extends Action> action : defaultActions) {
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
