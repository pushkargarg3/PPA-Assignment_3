package animals;

import field.Field;
import field.Location;

import java.util.List;

public abstract class Predator extends Animal {

    private int foodLevel;

    /**
     * Create a new animal at location in field.
     *
     * @param isRandomAge
     * @param field               The field currently occupied.
     * @param location            The location within the field.
     * @param breedingProbability
     * @param maxLitterSize
     * @param maxAge
     * @param breedingAge
     */
    public Predator(
            boolean isRandomAge,
            Field field,
            Location location,
            double breedingProbability,
            int maxLitterSize,
            int maxAge,
            int breedingAge) {
        super(isRandomAge, field, location, breedingProbability, maxLitterSize, maxAge, breedingAge);
    }

    @Override
    public void act(List<Animal> newAnimals)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newAnimals);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        for (Location where : adjacent) {
            Object animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = FoodLevels.RABBIT_FOOD_VALUE.getFoodLevel();
                    return where;
                }
            }
        }
        return null;
    }

    protected abstract void giveBirth(List<Animal> newAnimals);
}
