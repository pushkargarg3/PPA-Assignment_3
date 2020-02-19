package animals;

import field.Field;
import field.Location;
import infector.Infector;
import utils.Randomizer;

import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Creature {
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
    // The animal's infection probability
    private double infectedProbability;
    // The age of breeding
    private int breedingAge;

    // The maximum possible age of an animal
    private int maxAge;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // The age of the animal
    private int age;

    //The gender of the creature
    private boolean isMale;

    // Indicates what time of the day is now
    private boolean isNight;
    // Indicates if raining
    private boolean isRaining;
    // Indicates if hiding
    private boolean isHiding;
    // Indicates if infected
    protected boolean isInfected;
    // Used to infect cross species
    protected Infector infector;

    /**
     * Create a new animal at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Creature(
            boolean isRandomAge,
            Field field,
            Location location,
            boolean isMale,
            double breedingProbability,
            int maxLitterSize,
            int maxAge,
            int breedingAge,
            double infectedProbability) {
        alive = true;
        this.field = field;
        setLocation(location);

        // Animal specific options
        this.breedingProbability = breedingProbability;
        this.infectedProbability = infectedProbability;
        this.maxLitterSize = maxLitterSize;
        this.maxAge = maxAge;
        this.breedingAge = breedingAge;

        this.isMale = isMale;
        isHiding = false;
        this.setAge(isRandomAge);

        infector = new Infector();
    }


    /**
     * Check whether the animal is alive or not.
     *
     * @return true if the animal is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setDayTime(boolean isNight) {
        this.isNight = isNight;
    }

    public void setRain(boolean isRaining) {
        this.isRaining = isRaining;
    }

    public void setHiding(boolean isHiding){
        this.isHiding = isHiding;
    }

    public void setInfected() {this.isInfected = true;}

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     *
     * @param newCreatures A list to receive newly born animals.
     */
    public abstract void act(List<Creature> newCreatures);

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

    protected void giveBirth(List<Creature> newCreatures, AnimalCreator creator) {
        // New creatures are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Creature young = creator.create(field, loc);
            newCreatures.add(young);
        }
    }

    protected boolean isRaining() {
        return this.isRaining;
    }

    protected boolean isNight() {
        return this.isNight;
    }

    protected boolean isHiding() {
        return this.isHiding;
    }

    protected void incrementAge() {
        age++;
        if (age > maxAge) {
            setDead();
        }
    }

    protected abstract int getFoodLevel();

    public abstract double getInfectedProbability();

    private void setAge(boolean isRandomAge) {
        if (isRandomAge) {
            age = rand.nextInt(maxAge);
        } else {
            age = 0;
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
}


