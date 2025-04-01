package com.github.gatoartstudios.munecraft.helpers;

public class Utils {

    /**
     * Checks if the application is running on the Folia platform.
     *
     * @return true if running on Folia, false otherwise
     */
    public static boolean isFolia() {
        try {
            // Check if running on Folia
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}