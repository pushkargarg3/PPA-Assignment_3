package animals;

import field.Field;
import field.Location;

public interface AnimalCreator {
    Creature create(Field field, Location location);
}
