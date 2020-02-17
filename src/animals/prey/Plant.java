package animals.prey;

import animals.Creature;
import animals.AnimalCreator;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

public class Plant extends Creature {
    // Characteristics shared by all deers (class variables).

    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a deer can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a deer breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 8;

    private static final int PLANT_FOOD_VALUE = 5;

    private Random random;

    /**
     * Create a new animal at location in field.
     *
     * @param isRandomAge if the Plant should be instantiated with random age
     * @param field       The field currently occupied.
     * @param location    The location within the field.
     */
    public Plant(boolean isRandomAge, Field field, Location location, boolean isMale) {
        super(isRandomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE);
        this.random = new Random();
    }

    /**
     * This is what the deer does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param newPlants A list to return newly born deers.
     */
    public void act(List<Creature> newPlants) {
        incrementAge();
        if (isAlive()) {
            giveBirth(newPlants);
        }
    }

    /**
     * @return returns the default food level of the plant
     */
    @Override
    protected int getFoodLevel() {
        return PLANT_FOOD_VALUE;
    }

    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newPlants A list to return newly born deers.
     */
    private void giveBirth(List<Creature> newPlants) {
        Field currentField = getField();
        List<Location> adjacent = currentField.adjacentLocations(getLocation());

        for (Location where : adjacent) {
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Plant) {
                AnimalCreator creator = (field, location) -> new Plant(false, field, location, random.nextBoolean());

                super.giveBirth(newPlants, creator);
            }
        }
    }
}
