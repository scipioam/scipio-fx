package com.github.scipioam.scipiofx.framework.concurrent;

import com.github.scipioam.scipiofx.framework.exception.FrameworkException;
import javafx.concurrent.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaFX的Service池
 *
 * @see Service
 * @since 2022/9/19
 */
public class ServicePool {

    private final Map<String, Service<?>> pool = new HashMap<>();

    //=========================================== ↓↓↓↓↓↓ API ↓↓↓↓↓↓ ===========================================

    public void clear() {
        pool.clear();
    }

    public void startService(String key) {
        Service<?> service = getOrThrow(key);
        service.start();
    }

    public void startService(Class<?> serviceClass) {
        startService(serviceClass.getName());
    }

    public void restartService(String key) {
        Service<?> service = getOrThrow(key);
        service.restart();
    }

    public void restartService(Class<?> serviceClass) {
        restartService(serviceClass.getName());
    }

    public void resetService(String key) {
        Service<?> service = getOrThrow(key);
        service.reset();
    }

    public void resetService(Class<?> serviceClass) {
        resetService(serviceClass.getName());
    }

    public boolean cancelService(String key) {
        Service<?> service = getOrThrow(key);
        return service.cancel();
    }

    public boolean cancelService(Class<?> serviceClass) {
        return cancelService(serviceClass.getName());
    }

    public boolean isServiceRunning(String key) {
        Service<?> service = getOrThrow(key);
        return service.isRunning();
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        return isServiceRunning(serviceClass.getName());
    }

    //=========================================== ↓↓↓↓↓↓ getter/setter ↓↓↓↓↓↓ ===========================================

    public Service<?> getService(String key) {
        return pool.get(key);
    }

    public Service<?> getService(Class<?> serviceClass) {
        return pool.get(serviceClass.getName());
    }

    public ServicePool addService(String serviceKey, Service<?> service) {
        pool.put(serviceKey, service);
        return this;
    }

    public ServicePool addService(Service<?> service) {
        return addService(service.getClass().getName(), service);
    }

    public ServicePool setServices(List<Service<?>> services) {
        if (services == null || services.size() == 0) {
            throw new IllegalArgumentException("method argument (list) is null or empty");
        }
        for (Service<?> service : services) {
            addService(service);
        }
        return this;
    }

    public ServicePool setServices(Map<String, Service<?>> services) {
        if (services == null || services.size() == 0) {
            throw new IllegalArgumentException("method argument (map) is null or empty");
        }
        pool.putAll(services);
        return this;
    }

    //=========================================== ↓↓↓↓↓↓ 内服方法 ↓↓↓↓↓↓ ===========================================

    private Service<?> getOrThrow(String key) {
        Service<?> service = pool.get(key);
        if (service == null) {
            throw new FrameworkException("service is null by key: \"" + key + "\"");
        }
        return service;
    }

}
