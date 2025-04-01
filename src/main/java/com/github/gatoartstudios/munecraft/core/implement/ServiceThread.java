package com.github.gatoartstudios.munecraft.core.implement;

import com.github.gatoartstudios.munecraft.core.interfaces.IInfrastructureInitiator;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ServiceThread implements IInfrastructureInitiator {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Called when the service is loaded.
     * This method is called when the service is loaded, and it is the place where you can initialize the service.
     * The service is not started yet, so you can not use the service in this method.
     */
    @Override
    public void onLoad() {
        LoggerCustom.info("Service has been loaded");
    }

    /**
     * Called when the service is enabled.
     * This method is called when the service is enabled, and it is the place where you can start the service.
     * The service is started in a new thread, so you can use the service in this method.
     */
    @Override
    public void onEnable() {
        executorService.submit(() -> {
            try {
                runService();
                LoggerCustom.info("Service has been started successfully");
            } catch (Exception e) {
                LoggerCustom.error("Error starting service: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Called when the service is disabled.
     * This method is called when the service is disabled, and it is the place where you can stop the service.
     * The service is stopped, so you can not use the service in this method.
     */
    @Override
    public void onDisable() {
        LoggerCustom.info("Service has been disabled");
        executorService.shutdownNow();
    }

    /**
     * This method is called in a new thread when the service is started.
     * You must implement this method to start the service.
     * @throws Exception
     */
    protected abstract void runService() throws Exception;
}
