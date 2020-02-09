package animals;

import field.Field;
import field.Location;

public interface AnimalCreator {
    Animal create(Field field, Location location);
}
