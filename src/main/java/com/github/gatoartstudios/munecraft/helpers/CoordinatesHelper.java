package com.github.gatoartstudios.munecraft.helpers;

public class CoordinatesHelper {
    public static int[] CoordinatesToChunkCoordinates(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return new int[]{chunkX, chunkZ};
    }
}
