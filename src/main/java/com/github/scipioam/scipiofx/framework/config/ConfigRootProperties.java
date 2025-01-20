package com.github.scipioam.scipiofx.framework.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Root of all properties
 *
 * @author Alan Scipio
 * @since 2024/4/26
 */
@Setter
@Getter
public class ConfigRootProperties {

    private AppProperties app;

    private Map<String, Object> custom;

    public Map<String, Object> getCustomProperties() {
        return custom;
    }

    public Object getCustomProperty(String key) {
        return custom == null ? null : custom.get(key);
    }

    public boolean containsCustomProperty(String key) {
        return custom != null && custom.containsKey(key);
    }
}
