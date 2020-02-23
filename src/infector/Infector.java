package infector;

import animals.Organism;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

/**
 * The Infector class deals with spreading diseases between the same and
 * differing species of organisms.
 *
 * @author Jonathan Rivera, Andrian Stoykov, Pushkar Garg
 * @version 2020.02.17
 */
public class Infector {
    private Random random;

    public Infector() {
        random = new Random();
    }

    /**
     * Infects nearby animals by generating random double and comparing with different
     * animals' probabilities.
     * @param thisAnimal animal object calling the infect method
     * @param adjacent list of adjacent locations in the grid
     * @param currentField current field object
     */
    public void infect(Organism thisAnimal, List<Location> adjacent, Field currentField) {
        for (Location where : adjacent) {
            Organism animal = (Organism) currentField.getObjectAt(where);
            //if location empty
            if(animal == null)
                return;
            double rand = random.nextDouble();
            //if animals in contact are the same species
            if (thisAnimal.getClass() == animal.getClass()) {
                //probability of infection is triple
                if (rand <= animal.getInfectedProbability() * 3) {
                    animal.setInfected(true);
                }
            }
            // if random double less than animal's getInfected probability
            if (rand <= animal.getInfectedProbability()) {
                animal.setInfected(true);
            }
        }
    }
}
