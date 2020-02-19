package simulator;

import animals.*;
import animals.prey.day_eaters.Deer;
import animals.prey.Plant;
import animals.prey.day_eaters.Rabbit;
import animals.prey.Rat;
import field.Field;
import field.Location;
import utils.Randomizer;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.05;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.06;
    // The probability that a deer will be created in any given grid position.
    private static final double DEER_CREATION_PROBABILITY = 0.06;
    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.04;
    // The probability that a tiger will be created in any given grid position.
    private static final double RAT_CREATION_PROBABILITY = 0.15;
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.1;
    // List of animals in the field.
    private List<Creature> creatures;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // Random object
    private Random rand = Randomizer.getRandom();
    // Boolean value which indicates the current state of the time
    private boolean isNight;
    private boolean isRaining;
    private int rainTime;
    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        creatures = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.MAGENTA);
        view.setColor(Deer.class, Color.YELLOW);
        view.setColor(Rat.class, Color.GRAY);
        view.setColor(Fox.class, Color.RED);
        view.setColor(Tiger.class, Color.ORANGE);
        view.setColor(Plant.class, Color.GREEN);

        isNight = false;
        rainTime = 15;
        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation() {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     *
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
//            delay(500);   // uncomment this to run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep() {
        step++;

        // Provide space for newborn animals.
        List<Creature> newCreatures = new ArrayList<>();

        // On every 20th run change the day time
        if (step % 20 == 0) {
            isNight = !isNight;
            view.setNight(isNight);
        }
        // Rains for given raintime
        if (step % rainTime == 0){
            isRaining = !isRaining;
            if (!isRaining) {
                do {
                    // rain time is random
                    rainTime = rand.nextInt(15);
                }
                while (rainTime == 0);// prevents zero division error
            }
        }

        // Display is raining message.
        if (isRaining) {
            view.setInfoText("RAINING!");
        }
        // Set to null when weather is normal.
        else
            view.setInfoText("");
        // Let all creatures act.
        for (Iterator<Creature> it = creatures.iterator(); it.hasNext(); ) {
            Creature creature = it.next();

            creature.setDayTime(isNight);
            creature.setRain(isRaining);
            creature.act(newCreatures);
            if (!creature.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born foxes and rabbits to the main lists.
        creatures.addAll(newCreatures);

        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        creatures.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                boolean isMale = rand.nextBoolean();
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location, isMale);
                    creatures.add(fox);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Creature rabbit = new Rabbit(true, field, location, isMale);
                    creatures.add(rabbit);
                } else if (rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Creature deer = new Deer(true, field, location, isMale);
                    creatures.add(deer);
                } else if (rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Creature tiger = new Tiger(true, field, location, isMale);
                    creatures.add(tiger);
                } else if (rand.nextDouble() <= RAT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Creature rat = new Rat(true, field, location, isMale);
                    creatures.add(rat);
                } else if(rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Creature plant = new Plant(true, field, location, isMale);
                    creatures.add(plant);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     *
     * @param millisec The time to pause for, in milliseconds
     */
    private void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ie) {
            // wake up
        }
    }
}
