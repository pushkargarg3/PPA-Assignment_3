package animals;

import field.Field;
import field.Location;

import java.util.List;

public class Tiger extends Predator {

    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    /**
     * Create a new animal at location in field.
     *
     * @param isRandomAge         If the age should be randomly assigned.
     * @param field               The field currently occupied.
     * @param location            The location within the field.
     *
     **/

    public Tiger(boolean isRandomAge, Field field, Location location) {
        super(isRandomAge, field, location, BREEDING_PROBABILITY, MAX_LITTER_SIZE, MAX_AGE, BREEDING_AGE);
    }

    @Override
    protected boolean canEatAnimal(Object animal) {
        return animal instanceof Deer;
    }

    @Override
    protected void giveBirth(List<Animal> newTigers) {
        super.giveBirth(newTigers, (field, location) -> new Tiger(false, field, location));
    }

    @Override
    protected int getFoodLevel() {
        return 0;
    }
}
