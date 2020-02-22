package animals.prey;

import animals.Organism;
import animals.AnimalCreator;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a plant.
 * Plants age, breed and die.
 *
 * @author Jonathan Rivera
 * @version 2020.02.17
 */
public class Plant extends Organism {
    // Characteristics shared by all plants (class variables).

    // The age at which a plant can start to breed.
    private static final int BREEDING_AGE = 4;
    // The age to which a plant can live.
    private static final int MAX_AGE = 8;
    // The likelihood of a plant breeding.
    private static final double BREEDING_PROBABILITY = 0.06;
    // The probability of plant being infected
    private static final double INFECTED_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    private static final int PLANT_FOOD_VALUE = 5;

    private Random random;

    /**
     * Create a new plant at location in field.
     *
     * @param isRandomAge if the Plant should be instantiated with random age
     * @param field       The field currently occupied.
     * @param location    The location within the field.
     */
    public Plant(boolean isRandomAge, Field field, Location location, boolean isMale) {
        super(isRandomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE,
                INFECTED_PROBABILITY);
        this.random = new Random();
    }

    /**
     * This is what the plant does most of the time - it does nothing.
     * Sometimes it will breed or die of old age.
     *
     * @param newPlants A list to return newly born plants.
     */
    public void act(List<Organism> newPlants) {
        // Plants grow when it rains
        if (isRaining())
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
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param newPlants A list to return newly born plants.
     */
    private void giveBirth(List<Organism> newPlants) {
        Field currentField = getField();
        List<Location> adjacent = currentField.adjacentLocations(getLocation());

        for (Location where : adjacent) {
            Object organism = getField().getObjectAt(where);
            if (organism instanceof Plant) {
                AnimalCreator creator = (field, location) -> new Plant(false, field, location, random.nextBoolean());
                // super.giveBirth calls the method inside Creature which gives birth
                super.giveBirth(newPlants, creator);
            }
        }
    }
}
