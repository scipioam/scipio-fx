package com.github.scipioam.scipiofx.framework.fxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alan Scipio
 * @since 2024/4/26
 */
public class ViewArgs extends HashMap<String, Object> {

    public static ViewArgs create() {
        return new ViewArgs();
    }

    public static ViewArgs of(String key, Object value) {
        return new ViewArgs().setArg(key, value);
    }

    public ViewArgs setArg(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public ViewArgs setArgs(ViewArgs args) {
        this.putAll(args);
        return this;
    }

    public ViewArgs setArgs(Map<String, Object> args) {
        this.putAll(args);
        return this;
    }

    public Object getArg(String key) {
        return this.get(key);
    }

    public <T> T getArg(String key, Class<T> clazz) {
        return clazz.cast(this.get(key));
    }

    public void clearArgs() {
        this.clear();
    }

    public boolean hasArg(String key) {
        return this.containsKey(key);
    }

    public List<Object> getArgList(String key) {
        return new ArrayList<>(this.values());
    }

}
