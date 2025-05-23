package com.github.gatoartstudios.munecraft.helpers;

public class CoordinatesHelper {
    /**
     * Converts block coordinates to chunk coordinates.
     * Each chunk is 16x16 blocks, so this method divides the block coordinates by 16.
     *
     * @param x The block X coordinate.
     * @param z The block Z coordinate.
     * @return An array containing the chunk X and chunk Z coordinates.
     */
    public static int[] CoordinatesToChunkCoordinates(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return new int[]{chunkX, chunkZ};
    }

    /**
     * Gets the minimum block coordinates (corner) of a given chunk.
     * Calculates the world block coordinates of the chunk's origin (top-left corner).
     *
     * @param chunkX The chunk X coordinate.
     * @param chunkZ The chunk Z coordinate.
     * @return An array containing the minimum block X and block Z coordinates of the chunk.
     */
    public static int[] CoordinatesMinInChunk(int chunkX, int chunkZ) {
        int minBlockX = chunkX << 4;
        int minBlockZ = chunkZ << 4;
        return new int[]{minBlockX, minBlockZ};
    }
}
