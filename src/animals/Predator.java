package animals;

import field.Field;
import field.Location;

import java.util.List;

public abstract class Predator extends Animal {

    private int foodLevel;

    /**
     * Create a new animal at location in field.
     *
     * @param isRandomAge         If the age should be randomly assigned
     * @param field               The field currently occupied.
     * @param location            The location within the field.
     * @param isMale              SHows if the predator is male or not
     * @param breedingProbability The probability to breed
     * @param maxLitterSize       The maximum number of children
     * @param maxAge              The maximum age of the predator
     * @param breedingAge         The minimum age of breeding
     */
    public Predator(
            boolean isRandomAge,
            Field field,
            Location location,
            boolean isMale,
            double breedingProbability,
            int maxLitterSize,
            int maxAge,
            int breedingAge) {
        super(isRandomAge, field, location, isMale, breedingProbability, maxLitterSize, maxAge, breedingAge);
        foodLevel = FoodLevels.RABBIT_FOOD_VALUE.getFoodLevel();
    }

    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            giveBirth(newAnimals, adjacent);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     *
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        for (Location where : adjacent) {
            Object animalObj = field.getObjectAt(where);
            if (canEatAnimal(animalObj)) {
                Animal animal = (Animal) animalObj;
                if (animal.isAlive()) {
                    animal.setDead();
                    foodLevel = animal.getFoodLevel();
                    return where;
                }
            }
        }
        return null;
    }

    protected abstract boolean canEatAnimal(Object animal);

    protected abstract void giveBirth(List<Animal> newAnimals, List<Location> adjacentLocations);
}
