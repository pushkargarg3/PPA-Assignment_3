package animals.prey.day_eaters;

import animals.Organism;
import animals.prey.Plant;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;


/**
 * A simple model of a capybara.
 * Capybaras age, move, breed, eat, and die.
 * @author Andrian Stoykov, Jonathan Rivera, Pushkar Garg
 * @version 2020.02.17
 */
public class Capybara extends DayEater {
    // Characteristics shared by all capybaras (class variables).

    // The age at which a capybara can start to breed.
    private static final int BREEDING_AGE = 7;
    // The age to which a capybara can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a capybara breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The probability of capybara being infected
    private static final double INFECTED_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of capybara
    private static final int CAPYBARA_FOOD_VALUE = 9;

    // Individual characteristics (instance fields).

    private Random random;

    /**
     * Create a new capybara. A capybara may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the capybara will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Capybara(boolean randomAge, Field field, Location location, boolean isMale) {
        super(randomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE,
                INFECTED_PROBABILITY);
        random = new Random();
    }

    /**
     * The method checks if the current animal can eat the given organism
     * @param organism the organism which is near passed from the Eater abstract class
     * @return true if the current animal can eat this organism
     */
    @Override
    protected boolean canEatCreature(Object organism) {
        return organism instanceof Plant;
    }

    /**
     *
     * @return returns the default food level of the capybara
     */
    @Override
    protected int getFoodLevel() {
        return CAPYBARA_FOOD_VALUE;
    }


    /**
     * Capybaras can give birth only if they meet animal of the same specie and opposite gender
     * @param newCapybaras List of all the new capybaras
     * @param adjacentLocations locations which are near the current animal
     */
    @Override
    protected void giveBirth(List<Organism> newCapybaras, List<Location> adjacentLocations) {
        for (Location where : adjacentLocations) {
            // obtains organisms surrounding capybara
            Object organism = getField().getObjectAt(where);
            if (organism instanceof Capybara && ((Capybara) organism).isMale() != this.isMale()) {
                // super.giveBirth calls the method inside Creature which gives birth
                super.giveBirth(newCapybaras, (field, location) -> new Capybara(false, field, location, random.nextBoolean()));
            }
        }
    }
}
