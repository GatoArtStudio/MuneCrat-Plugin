package com.github.gatoartstudios.munecraft.core.interfaces;

/**
 * Interface for initiating infrastructure components.
 * Implementing classes should provide actions to perform on load, enable, and disable states.
 */
public interface IInfrastructureInitiator {

    /**
     * Called when the infrastructure component is loaded.
     */
    void onLoad();

    /**
     * Called when the infrastructure component is enabled.
     */
    void onEnable();

    /**
     * Called when the infrastructure component is disabled.
     */
    void onDisable();
}