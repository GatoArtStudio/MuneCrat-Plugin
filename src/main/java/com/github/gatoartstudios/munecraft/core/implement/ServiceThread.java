package com.github.gatoartstudios.munecraft.core.implement;

import com.github.gatoartstudios.munecraft.core.interfaces.IInfrastructureInitiator;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ServiceThread implements IInfrastructureInitiator {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    public void onLoad() {
        LoggerCustom.info("Service has been loaded");
    }

    @Override
    public void onEnable() {
        executorService.submit(() -> {
            try {
                runService();
                LoggerCustom.info("Service has been started successfully");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onDisable() {
        LoggerCustom.info("Service has been disabled");
        executorService.shutdownNow();
    }

    // Implemented in the subclasses to start service in a thread
    protected abstract void runService() throws Exception;
}
