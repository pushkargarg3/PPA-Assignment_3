package animals.prey.day_eaters;

import animals.Organism;
import animals.prey.Plant;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a deer.
 * Deers age, move, breed, eat, and die.
 *
 * @author Andrian Stoykov, Pushkar Garg
 * @version 2020.02.13
 */
public class Deer extends DayEater {
    // Characteristics shared by all deers (class variables).

    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a deer can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a deer breeding.
    private static final double BREEDING_PROBABILITY = 0.09;
    // The probability of deer being infected
    private static final double INFECTED_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The amount of food that is going to gain the animal which eats the deer
    private static final int DEER_FOOD_VALUE = 18;

    // Individual characteristics (instance fields).

    private Random random;

    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the deer will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Deer(boolean randomAge, Field field, Location location, boolean isMale) {
        super(randomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE,
                INFECTED_PROBABILITY);
        random = new Random();
    }

    /**
     * The deer can eat only plants
     *
     * @param organism organism which is passed by the Eater class
     * @return true if the organism is a Plant
     */
    @Override
    protected boolean canEatCreature(Object organism) {
        return organism instanceof Plant;
    }

    /**
     * @return returns the default food level of the deer
     */
    @Override
    protected int getFoodLevel() {
        return DEER_FOOD_VALUE;
    }

    @Override
    public double getInfectedProbability() {
        return INFECTED_PROBABILITY;
    }

    @Override
    protected void giveBirth(List<Organism> newDeers, List<Location> adjacentLocations) {
        for (Location where : adjacentLocations) {
            Object organism = getField().getObjectAt(where);
            if (organism instanceof Deer && ((Deer) organism).isMale() != this.isMale()) {
                // super.giveBirth calls the method inside Creature which gives birth
                super.giveBirth(newDeers, (field, location) -> new Deer(false, field, location, random.nextBoolean()));
            }
        }
    }
}
