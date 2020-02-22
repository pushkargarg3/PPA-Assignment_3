package animals;

import field.Field;
import field.Location;

import java.util.List;

public abstract class Eater extends Organism {

    private int foodLevel;

    /**
     * Create a new creature at location in field.
     * This creature is able to eat.
     *
     * @param isRandomAge         If the age should be randomly assigned
     * @param field               The field currently occupied.
     * @param location            The location within the field.
     * @param isMale              SHows if the predator is male or not
     * @param breedingProbability The probability to breed
     * @param maxLitterSize       The maximum number of children
     * @param maxAge              The maximum age of the predator
     * @param breedingAge         The minimum age of breeding
     * @param infectedProbability The probability of infection
     */
    public Eater(
            boolean isRandomAge,
            Field field,
            Location location,
            boolean isMale,
            double breedingProbability,
            int maxLitterSize,
            int maxAge,
            int breedingAge,
            double infectedProbability) {
        super(isRandomAge, field, location, isMale, breedingProbability, maxLitterSize, maxAge, breedingAge, infectedProbability);
        foodLevel = 40;
    }

    @Override
    public void act(List<Organism> newOrganisms) {
        // If the animal is infected it dies faster
        if (isInfected()) {
            incrementAge();
        }
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            giveBirth(newOrganisms, getLocationList());
            if(isInfected())
                getInfector().infect(this, getField().adjacentLocations(getLocation()), getField());
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
    protected void incrementHunger() {
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
    protected Location findFood() {
        for (Location where : getLocationList()) {
            Object animalObj = getField().getObjectAt(where);
            if (canEatCreature(animalObj)) {
                Organism organism = (Organism) animalObj;
                if (organism.isAlive() && !isHiding()) {
                    organism.setDead();
                    foodLevel = organism.getFoodLevel();
                    return where;
                }
            }
        }
        return null;
    }

    protected abstract boolean canEatCreature(Object animal);

    protected abstract void giveBirth(List<Organism> newOrganisms, List<Location> adjacentLocations);

    private List<Location> getLocationList() {
        Field field = getField();
        return field.adjacentLocations(getLocation());
    }
}
