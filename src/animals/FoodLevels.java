package animals;

/**
 * Enum for keeping all the possible food levels inside the application
 */
public enum FoodLevels {
    // The food value of a single rabbit. In effect, this is the
    // number of steps a predator can go before it has to eat again
    RABBIT_FOOD_VALUE(9);

    private final int foodLevel;

    FoodLevels(int foodLevel) {
        this.foodLevel  = foodLevel;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }
}
