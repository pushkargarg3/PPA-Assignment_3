package animals.prey;

import animals.Creature;
import animals.Eater;
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
public class Rabbit extends Eater {
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;

    private final int RABBIT_FOOD_VALUE = 9;

    // Individual characteristics (instance fields).

    Random random;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the rabbit will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location, boolean isMale) {
        super(randomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE);
        random = new Random();
    }

    // TODO: Fix the comments
    /**
     * This is what the rabbits does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born deers.
     */
    @Override
    public void act(List<Creature> newRabbits)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            Field currentField = getField();
            List<Location> adjacent = currentField.adjacentLocations(getLocation());
            giveBirth(newRabbits, adjacent);
            findFood();
            // Try to move into a free location.
            if(this.isNight()) {
                return;
            }
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

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
    protected void giveBirth(List<Creature> newRabbits, List<Location> adjacentLocations) {
        for (Location where : adjacentLocations) {
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Rabbit && ((Rabbit) animal).isMale() != this.isMale())
                super.giveBirth(newRabbits, (field, location) -> new Rabbit(false, field, location, random.nextBoolean()));
        }
    }
}
