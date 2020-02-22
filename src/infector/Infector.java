package infector;

import animals.Organism;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

/**
 * The Infector class calculates the probability of every animal getting infected
 *
 * @author Jonathan Rivera
 * @version 2020.02.17
 */
public class Infector {
    private Random random;

    public Infector() {
        random = new Random();
    }

    public void infect(Organism thisAnimal, List<Location> adjacent, Field currentField) {
        for (Location where : adjacent) {
            Organism animal = (Organism) currentField.getObjectAt(where);
            if(animal == null)
                return;
            double rand = random.nextDouble();
            //if animals in contact are the same species
            if (thisAnimal.getClass() == animal.getClass()) {
                //probability of infection is double
                if (rand <= animal.getInfectedProbability() * 3) {
                    animal.setInfected(true);
                }
            }
            if (rand <= animal.getInfectedProbability()) {
                animal.setInfected(true);
            }
        }
    }
}
