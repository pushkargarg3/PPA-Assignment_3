package animals.prey.day_eaters;

import animals.Creature;
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
     * @param isMale              SHows if the predator is male or not
     * @param breedingProbability The probability to breed
     * @param maxLitterSize       The maximum number of children
     * @param maxAge              The maximum age of the predator
     * @param breedingAge         The minimum age of breeding
     */
    public DayEater(boolean isRandomAge, Field field, Location location, boolean isMale, double breedingProbability, int maxLitterSize, int maxAge, int breedingAge) {
        super(isRandomAge, field, location, isMale, breedingProbability, maxLitterSize, maxAge, breedingAge);
    }

    /**
     * This is what the abstract day eater does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * And most importantly it stays on one place when its night
     * @param newCreatures A list to return newly born creatures.
     */
    @Override
    public void act(List<Creature> newCreatures)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            Field currentField = getField();
            List<Location> adjacent = currentField.adjacentLocations(getLocation());
            giveBirth(newCreatures, adjacent);
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
