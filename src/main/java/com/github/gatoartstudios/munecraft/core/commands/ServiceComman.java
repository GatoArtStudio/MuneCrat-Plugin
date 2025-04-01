package com.github.gatoartstudios.munecraft.core.commands;

import com.github.gatoartstudios.munecraft.core.interfaces.ICommand;
import com.github.gatoartstudios.munecraft.core.interfaces.IInfrastructureInitiator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceComman implements ICommand {
    /**
     * A list of services to be executed in the enable method.
     */
    private List<IInfrastructureInitiator> services = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean execute() {
        if (services.isEmpty()) return false;

        // We start all the services
        services.forEach(IInfrastructureInitiator::onEnable);
        // We load all the services
        services.forEach(IInfrastructureInitiator::onLoad);

        return true;
    }

    /**
     * Undo all the services, reversing the order of the enable method.
     * @return true if the undo was successful, false if not.
     */
    public boolean undo() {
        if (services.isEmpty()) return false;

        // We disable all the services
        services.forEach(IInfrastructureInitiator::onDisable);

        return true;
    }

    /**
     * Returns an immutable list of services, avoiding external modifications.
     * @return a copy of the list of services.
     */
    public List<IInfrastructureInitiator> getServices() {
        // We return a copy to avoid external modifications
        return new ArrayList<>(services);
    }

    /**
     * Adds a service to the list of services.
     * @param service the service to be added.
     */
    public void addService(IInfrastructureInitiator service) {
        services.add(service);
    }
}
