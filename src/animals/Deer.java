package animals;

import field.Field;
import field.Location;
import java.util.List;

public class Deer extends Animal
{
    // Characteristics shared by all deers (class variables).

    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a deer can live.
    private static final int MAX_AGE = 55;
    // The likelihood of a deer breeding.
    private static final double BREEDING_PROBABILITY = 0.06;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    private final int DEER_FOOD_VALUE = 18;

    // Individual characteristics (instance fields).

    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the deer will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Deer(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE);
    }

    /**
     * This is what the deer does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newDeers A list to return newly born deers.
     */
    public void act(List<Animal> newDeers)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newDeers);
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
     * @return returns the default food level of the deer
     */
    @Override
    protected int getFoodLevel() {
        return DEER_FOOD_VALUE;
    }

    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDeers A list to return newly born deers.
     */
    private void giveBirth(List<Animal> newDeers) {
        AnimalCreator creator = (field, location) -> new Deer(false, field, location);

        super.giveBirth(newDeers, creator);
    }
}
