package animals.prey.day_eaters;

import animals.Organism;
import animals.Eater;
import field.Field;
import field.Location;

import java.util.List;

public abstract class DayEater extends Eater {

    /**
     * Creates a creature which is able to move only during the day
     * When its night it sleeps
     *
     * @param isRandomAge         If the age should be randomly assigned
     * @param field               The field currently occupied.
     * @param location            The location within the field.
     * @param isMale              Shows if the predator is male or not
     * @param breedingProbability The probability to breed
     * @param maxLitterSize       The maximum number of children
     * @param maxAge              The maximum age of the predator
     * @param breedingAge         The minimum age of breeding
     * @param infectedProbability The probability of infection
     */
    public DayEater(boolean isRandomAge, Field field, Location location, boolean isMale, double breedingProbability, int maxLitterSize, int maxAge, int breedingAge,
                    double infectedProbability) {
        super(isRandomAge, field, location, isMale, breedingProbability, maxLitterSize, maxAge, breedingAge, infectedProbability);
    }

    /**
     * This is what the abstract day eater does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * And most importantly it stays on one place when its night
     * @param newOrganisms A list to return newly born creatures.
     */
    @Override
    public void act(List<Organism> newOrganisms)
    {
        // If the animal is infected it dies faster
        if (isInfected()) {
            incrementAge();
        }
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            Field currentField = getField();
            List<Location> adjacent = currentField.adjacentLocations(getLocation());
            giveBirth(newOrganisms, adjacent);
            if(isInfected())
                getInfector().infect(this, getField().adjacentLocations(getLocation()), getField());
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
}
