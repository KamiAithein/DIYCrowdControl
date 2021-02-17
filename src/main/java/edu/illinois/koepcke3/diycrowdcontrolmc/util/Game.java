package edu.illinois.koepcke3.diycrowdcontrolmc.util;

/**
 * Utility class dealing with game value conversions
 */
public final class Game {
    /**
     * Converts minecraft health from float to int format
     * @return health in number of hearts
     */
    public static int heartsFromHealth(float health){
        return (int)(health/2);
    }

    /**
     * Converts minecraft health from int to float format
     * @return health in number of hearts
     */
    public static int heartsFromHealth(int hearts){
        return 2*hearts;
    }

    /**
     * return the correct float value for number of hearts
     * @param health float formatted health
     * @param adj number of hearts to adjust health by
     * @return a float representing the correct health adjusted by hearts
     */
    public static float adjustHearts(float health, int adj){
        return health + heartsFromHealth(adj);
    }
}
