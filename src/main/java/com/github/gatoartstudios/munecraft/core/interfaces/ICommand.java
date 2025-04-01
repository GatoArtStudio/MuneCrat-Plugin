package com.github.gatoartstudios.munecraft.core.interfaces;

/**
 * Interface that represents a command
 */
public interface ICommand {
    /**
     * Execute the command
     * @return true if the command was executed successfully, false otherwise
     */
    boolean execute();
}