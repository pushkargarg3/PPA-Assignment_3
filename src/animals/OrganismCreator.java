package animals;

import field.Field;
import field.Location;

/**
 * A functional interfaces which is used inside every animal in order to remove code duplication.
 * Every animal has it inside as a lambda function which creates a new animal on a certain condition
 * @authors Andrian Stoykov, Jonathan Rivera, Pushkar Garg
 * @version 2020.02.17
 */
public interface OrganismCreator {
    /**
     * Lambda function which creates a new animal
     * @param field is passed from the giveBirth method in Creature.
     * @param location is passed from the giveBirth method in Creature
     * @return a new creature, can be Fox, Deer, whatever
     */
    Organism create(Field field, Location location);
}
