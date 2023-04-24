package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import com.github.javaparser.utils.Pair;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int LSWIDTH = 40;
    public static final int LSHEIGHT = 40;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(LSWIDTH, LSHEIGHT);
        ter.loadScreen();
        while (!StdDraw.hasNextKeyTyped()) {}
        long longSeed = menuExec(StdDraw.nextKeyTyped());
        while (longSeed == -2) { // -2 indicates an INVALID MENU CHOICE
            while (!StdDraw.hasNextKeyTyped()) {}
            longSeed = menuExec(StdDraw.nextKeyTyped());
        }
        if (longSeed == -1) { // user chose QUIT
            return;
        }
        World world = new World(longSeed, WIDTH, HEIGHT);
        world.createWorld();
        boolean light = false;
        TETile[][] finalWorldFrame = world.getWorld(light);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);

        boolean colon = false;
        while (!StdDraw.hasNextKeyTyped()) {}
        char key = StdDraw.nextKeyTyped();
        while (true) {
            Pair<Integer, Integer> dir = new Pair<Integer, Integer>(0, 0);
            if ((key == 'Q' || key == 'q') && colon) {
                break;
            }
            colon = false;
            if (key == 'o' || key == 'O') {
                light = !light;
                ter.renderFrame(world.getWorld(light));
            } else if (key == 'A' || key == 'a') {
                dir = new Pair<Integer, Integer>(-1, 0);
            } else if (key == 'S' || key == 's') {
                dir = new Pair<Integer, Integer>(0, -1);
            } else if (key == 'W' || key == 'w') {
                dir = new Pair<Integer, Integer>(0, 1);
            } else if (key == 'D' || key == 'd') {
                dir = new Pair<Integer, Integer>(1, 0);
            } else if (key == ':') {
                colon = true;
            }
            if (world.move(dir)) {
                ter.renderFrame(world.getWorld(light));
            }
            while (!StdDraw.hasNextKeyTyped()) {}
            key = StdDraw.nextKeyTyped();
        }
        ter.prompt("Game Over");
    }

    // returns seed (-1 if command is QUIT)
    public long menuExec(char command) {
        if (command == 'n' || command == 'N') {
            String seed = "";
            ter.prompt(seed, "Enter random seed:");
            while (!StdDraw.hasNextKeyTyped()) {}
            char key = StdDraw.nextKeyTyped();
            while (true) {
                if ((key == 's' || key == 'S') && seed != "") {
                    break;
                }
                if ('0' <= key && key <= '9') {
                    seed += key;
                }
                ter.prompt(seed, "Enter random seed:");
                while (!StdDraw.hasNextKeyTyped()) {}
                key = StdDraw.nextKeyTyped();
            }
            return Long.valueOf(seed);
        } else if (command == 'l' || command == 'L') {

            // return loaded seed;
        } else if (command == 'q' || command == 'Q') {
            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.show();
            return -1;
        }
        return -2;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        char[] seedString = new char[input.length() - 2];
        for (int i = 1; i < input.length() - 1; i++) {
            seedString[i - 1] = input.charAt(i);
        }
        long seed = Long.valueOf(new String(seedString));
        World world = new World(seed, WIDTH, HEIGHT);
        world.createWorld();
        TETile[][] finalWorldFrame = world.getWorld();
        return finalWorldFrame;
    }
}
