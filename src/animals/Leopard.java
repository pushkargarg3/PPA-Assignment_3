package animals;

import animals.prey.day_eaters.Capybara;
import animals.prey.Rat;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a leopard.
 * Leopards age, move, eat capybaras or rats, and die.
 *
 * @author Andrian Stoykov, Pushkar Garg, Jonathan Rivera
 * @version 2020.02.17
 */
public class Leopard extends Eater {
    // Characteristics shared by all leopards (class variables).

    // The age at which a leopard can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a leopard can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a leopard breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The probability of leopard being infected
    private static final double INFECTED_PROBABILITY = 0.01;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;

    private Random random;

    /**
     * Create a leopard. A leopard can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the leopard will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Leopard(boolean randomAge, Field field, Location location, boolean isMale) {
        super(randomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE,
                INFECTED_PROBABILITY);
        random = new Random();
    }

    /**
     * Checks if leopard can eat the animal passed as an argument
     * @param animal An organism object
     * @return boolean of whether leopard can eat the organism
     */
    @Override
    protected boolean canEatCreature(Object animal) {
        return animal instanceof Capybara || animal instanceof Rat;
    }

    /**
     * Check whether or not this leopard is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newLeopards A list to return newly born leopards.
     */
    @Override
    protected void giveBirth(List<Organism> newLeopards, List<Location> adjacentLocations) {
        // We get all adjacent locations and check if the animals is able to give a birth
        // with some organism near it
        for (Location where : adjacentLocations) {
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Leopard && ((Leopard) animal).isMale() != this.isMale()) {
                // super.giveBirth calls the method inside Creature which gives birth
                super.giveBirth(newLeopards, (field, location) -> new Leopard(false, field, location, random.nextBoolean()));
            }
        }
    }

    /**
     * @return default food level of leopard.
     */
    @Override
    protected int getFoodLevel() {
        return 0;
    }

}
