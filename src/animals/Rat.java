package animals;

import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

public class Rat extends Animal {
    // Characteristics shared by all rats (class variables).

    // The age at which a rat can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a rat can live.
    private static final int MAX_AGE = 8;
    // The likelihood of a rat breeding.
    private static final double BREEDING_PROBABILITY = 0.20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;

    private final int RAT_FOOD_VALUE = 2;

    private Random random;

    /**
     * Create a new animal at location in field.
     *
     * @param randomAge           Shows if the Rat should have a random age
     * @param field               The field currently occupied.
     * @param location            The location within the field.
     */
    public Rat(boolean randomAge, Field field, Location location, boolean isMale) {
        super(randomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE);
        random = new Random();
    }


    /**
     * This is what the rat does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newRats A list to return newly born rats.
     */
    public void act(List<Animal> newRats)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newRats);
            // Try to move into a free location.
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

    /**
     *
     * @return returns the default food level of the rat
     */
    @Override
    protected int getFoodLevel() {
        return RAT_FOOD_VALUE;
    }

    /**
     * Check whether or not this rat is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRats A list to return newly born rats.
     */
    private void giveBirth(List<Animal> newRats) {
        Field currentField = getField();
        List<Location> adjacent = currentField.adjacentLocations(getLocation());

        for (Location where : adjacent) {
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Rat && ((Rat) animal).isMale() != this.isMale()) {
                AnimalCreator creator = (field, location) -> new Rat(false, field, location, random.nextBoolean());

                super.giveBirth(newRats, creator);
            }
        }
    }
}
