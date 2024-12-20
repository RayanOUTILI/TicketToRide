package fr.cotedazur.univ.polytech.ttr.equipeb.utils;

import java.util.Random;

public class RandomGenerator {
    private Random random;

    public RandomGenerator() {
        random = new Random();
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
