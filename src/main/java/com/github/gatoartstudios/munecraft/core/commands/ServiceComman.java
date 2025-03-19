package com.github.gatoartstudios.munecraft.core.commands;

import com.github.gatoartstudios.munecraft.core.interfaces.ICommand;
import com.github.gatoartstudios.munecraft.core.interfaces.IInfrastructureInitiator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceComman implements ICommand {
    List<IInfrastructureInitiator> services = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean execute() {
        if (services.isEmpty()) return false;

        services.forEach(IInfrastructureInitiator::onEnable);
        services.forEach(IInfrastructureInitiator::onLoad);

        return true;
    }

    public boolean undo() {
        if (services.isEmpty()) return false;

        services.forEach(IInfrastructureInitiator::onDisable);

        return true;
    }

    public List<IInfrastructureInitiator> getServices() {
        // We return a copy to avoid external modifications
        return new ArrayList<>(services);
    }

    public void addService(IInfrastructureInitiator service) {
        services.add(service);
    }
}
