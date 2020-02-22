package animals;

import field.Field;
import field.Location;
import infector.Infector;
import utils.Randomizer;

import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of organism.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Organism {
    // Whether the organism is alive or not.
    private boolean alive;
    // The organism's field.
    private Field field;
    // The organism's position in the field.
    private Location location;
    // The organism's breeding probability.
    private double breedingProbability;
    // The organism's max number of births.
    private int maxLitterSize;
    // The organism's infection probability
    private double infectedProbability;
    // The age of breeding
    private int breedingAge;

    // The maximum possible age of an organism
    private int maxAge;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // The age of the organism
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
    private boolean isInfected;
    // Used to infect cross species
    private Infector infector;

    /**
     * Create a new organism at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Organism(
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

        // organism specific options
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
     * Check whether the organism is alive or not.
     *
     * @return true if the organism is still alive.
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

    public void setHiding(boolean isHiding) {
        this.isHiding = isHiding;
    }

    public void setInfected(boolean infected) {
        this.isInfected = infected;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public Infector getInfector() {
        return infector;
    }

    /**
     * Make this organism act - that is: make it do
     * whatever it wants/needs to do.
     *
     * @param newOrganisms A list to receive newly born organisms.
     */
    public abstract void act(List<Organism> newOrganisms);

    /**
     * Indicate that the organism is no longer alive.
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
     * Return the organism's location.
     *
     * @return The organism's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the organism at the new location in the given field.
     *
     * @param newLocation The organism's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the organism's field.
     *
     * @return The organism's field.
     */
    protected Field getField() {
        return field;
    }

    protected void giveBirth(List<Organism> newOrganisms, OrganismCreator creator) {
        // New creatures are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Organism young = creator.create(field, loc);
            newOrganisms.add(young);
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

    public double getInfectedProbability() {
        return infectedProbability;
    }

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


