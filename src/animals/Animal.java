package animals;

import field.Field;
import field.Location;
import utils.Randomizer;

import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal {
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The animal's breeding probability.
    private double breedingProbability;
    // The animal's max number of births.
    private int maxLitterSize;

    // The age of breeding
    private int breedingAge;

    // The maximum possible age of an animal
    private int maxAge;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // The age of the animal
    private int age;

    private boolean isMale;

    // Indicates what time of the day is now
    private boolean isNight;

    /**
     * Create a new animal at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(
            boolean isRandomAge,
            Field field,
            Location location,
            boolean isMale,
            double breedingProbability,
            int maxLitterSize,
            int maxAge,
            int breedingAge) {
        alive = true;
        this.field = field;
        setLocation(location);

        // Animal specific options
        this.breedingProbability = breedingProbability;
        this.maxLitterSize = maxLitterSize;
        this.maxAge = maxAge;
        this.breedingAge = breedingAge;

        this.setAge(isRandomAge);
    }

    private void setAge(boolean isRandomAge) {
        if (isRandomAge) {
            age = rand.nextInt(maxAge);
        } else {
            age = 0;
        }
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     *
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     *
     * @return true if the animal is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     *
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the animal at the new location in the given field.
     *
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     *
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }

    public boolean isMale() {
        return isMale;
    }

    protected void giveBirth(List<Animal> newAnimals, AnimalCreator creator) {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal young = creator.create(field, loc);
            newAnimals.add(young);
        }
    }

    public void setDayTime(boolean isNight) {
        this.isNight = isNight;
    }

    protected boolean isNight() {
        return this.isNight;
    }

    protected void incrementAge() {
        age++;
        if (age > maxAge) {
            setDead();
        }
    }

    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        }
        return births;
    }

    private boolean canBreed() {
        return age >= breedingAge;
    }

    protected abstract int getFoodLevel();
}


