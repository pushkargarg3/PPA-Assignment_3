package infector;

import animals.Fox;
import animals.Tiger;
import animals.prey.Plant;
import animals.prey.Rat;
import animals.prey.day_eaters.Deer;
import animals.prey.day_eaters.Rabbit;
import field.Field;
import field.Location;

import java.util.List;
import java.util.Random;

public class Infector {

    private Random random;

    public Infector() { random = new Random();}

    public void infect (List<Location> adjacent, Field currentField) {
        for (Location where : adjacent){
            Object animal = currentField.getObjectAt(where);
            double rand = random.nextDouble();
            if (animal instanceof Deer && rand <= ((Deer) animal).getInfectedProbability()){
                ((Deer) animal).setInfected();
            }
            else if (animal instanceof Rabbit && rand <= ((Rabbit) animal).getInfectedProbability()){
                ((Rabbit) animal).setInfected();
            }
            else if (animal instanceof Plant && rand <= ((Plant) animal).getInfectedProbability()){
                ((Plant) animal).setInfected();
            }
            else if (animal instanceof Rat && rand <= ((Rat) animal).getInfectedProbability()){
                ((Rat) animal).setInfected();
            }
            else if ((animal instanceof Fox) && (rand <= ((Fox) animal).getInfectedProbability())){
                ((Fox) animal).setInfected();
            }
            else if (animal instanceof Tiger && rand <= ((Tiger) animal).getInfectedProbability()){
                ((Tiger) animal).setInfected();
            }

        }
    }
}
