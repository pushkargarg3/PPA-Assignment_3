package animals.prey.day_eaters;

import animals.Organism;
import animals.prey.Plant;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;


/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Rabbit extends DayEater {
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 7;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The probability of rabbit being infected
    private static final double INFECTED_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;

    private final int RABBIT_FOOD_VALUE = 9;

    // Individual characteristics (instance fields).

    private Random random;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the rabbit will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location, boolean isMale) {
        super(randomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE,
                INFECTED_PROBABILITY);
        random = new Random();
    }

    // TODO: Fix the comments

    @Override
    protected boolean canEatCreature(Object creature) {
        return creature instanceof Plant;
    }

    /**
     *
     * @return returns the default food level of the deer
     */
    @Override
    protected int getFoodLevel() {
        return RABBIT_FOOD_VALUE;
    }


    @Override
    protected void giveBirth(List<Organism> newRabbits, List<Location> adjacentLocations) {
        for (Location where : adjacentLocations) {
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Rabbit && ((Rabbit) animal).isMale() != this.isMale()) {
                // super.giveBirth calls the method inside Creature which gives birth
                super.giveBirth(newRabbits, (field, location) -> new Rabbit(false, field, location, random.nextBoolean()));
            }
        }
    }
}
