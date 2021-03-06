package animals;

import animals.prey.day_eaters.Deer;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a tiger.
 * Tiger age, move, eat deers, and die.
 *
 * @author Andrian Stoykov, Pushkar Garg, Jonathan Rivera
 * @version 2020.02.17
 */
public class Tiger extends Eater {

    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a fox can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The probability of tiger being infected
    private static final double INFECTED_PROBABILITY = 0.01;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    private Random random;

    /**
     * Create a new animal at location in field.
     *
     * @param isRandomAge If the age should be randomly assigned.
     * @param field       The field currently occupied.
     * @param location    The location within the field.
     **/

    public Tiger(boolean isRandomAge, Field field, Location location, boolean isMale) {
        super(isRandomAge, field, location, isMale, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE,
                INFECTED_PROBABILITY);
        random = new Random();
    }

    @Override
    protected boolean canEatCreature(Object animal) {
        return animal instanceof Deer;
    }

    @Override
    protected void giveBirth(List<Organism> newTigers, List<Location> adjacentLocations) {
        // We get all adjacent locations and check if the animals is able to give a birth
        // with some organism near it
        for (Location where : adjacentLocations) {
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Tiger && ((Tiger) animal).isMale() != this.isMale()) {
                // super.giveBirth calls the method inside Creature which gives birth
                super.giveBirth(newTigers, (field, location) -> new Tiger(false, field, location, random.nextBoolean()));
            }
        }
    }

    @Override
    protected int getFoodLevel() {
        return 0;
    }

}
