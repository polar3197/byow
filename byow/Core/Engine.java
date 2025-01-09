package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import com.github.javaparser.utils.Pair;

import java.awt.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import java.io.FileWriter;
import java.io.IOException;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WHITE = 255;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static final int LSWIDTH = 40;
    public static final int LSHEIGHT = 40;
    public static final String SAVEPATH = "./last_save.txt";
    private final int QUIT_FLAG = -1;
    private final int MENU_FLAG = -2;
    private final int LOAD_FLAG = -3;
    private final char ESC_CHAR = (char) 27;
    private final char BSPACE_CHAR = (char) 8;
    private World world;
    private String avatarName = "default";


    public Engine() {
        /*System.out.println("Engine constructor called");*/
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(LSWIDTH, LSHEIGHT);
        long longSeed = MENU_FLAG; // menuExec(StdDraw.nextKeyTyped());
        Pair<Long, Pair<Integer, Integer>> loadData = null;
        while (longSeed == MENU_FLAG || (longSeed == LOAD_FLAG && loadData == null)) {
            ter.loadScreen();
            while (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            longSeed = menuExec(StdDraw.nextKeyTyped());
            if (longSeed == LOAD_FLAG) { // user chose LOAD
                loadData = loadSave();
                if (loadData != null) {
                    longSeed = loadData.a;
                }
            }
        }
        if (longSeed == QUIT_FLAG) { // user chose QUIT
            return;
        }
        ter.setAvatarName(avatarName);
        world = new World(longSeed, WIDTH, HEIGHT);
        if (loadData != null) {
            world.createWorld(loadData.b);
        } else {
            world.createWorld();
        }
        boolean light = false;
        ter.initialize(WIDTH, HEIGHT);

        boolean colon = false;
        while (!StdDraw.hasNextKeyTyped()) {
            ter.renderFrame(world.getWorld(light));
        }
        char key = StdDraw.nextKeyTyped();
        while (true) {
            Pair<Integer, Integer> dir = new Pair<Integer, Integer>(0, 0);
            if ((key == 'Q' || key == 'q') && colon) { // save and quit
                try {
                    FileWriter saveFile = new FileWriter(SAVEPATH);
                    saveFile.write(String.valueOf(longSeed) + '\n'); // save seed
                    Pair<Integer, Integer> avatarPos = world.getAvatarPos();
                    saveFile.write(String.valueOf(avatarPos.a) + ' ' + avatarPos.b + '\n'); // save avatar position
                    saveFile.write(avatarName);
                    saveFile.close();
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }
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
            while (!StdDraw.hasNextKeyTyped()) {
                ter.renderFrame(world.getWorld(light));
            }
            key = StdDraw.nextKeyTyped();
        }
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
        char command = input.charAt(0);
        if (command == 'q' || command == 'Q') { // user chose QUIT
            return null;
        }

        StringBuffer seedBuf = new StringBuffer();
        long longSeed = Long.MAX_VALUE;
        if (command == 'N' || command == 'n') {
            int seedLength = 0;
            char nextChar = input.charAt(1 + seedLength);
            while (nextChar != 'S' && nextChar != 's') {
                seedBuf.append(nextChar);
                seedLength++;
                nextChar = input.charAt(1 + seedLength);
            }
            longSeed = Long.valueOf(seedBuf.toString());
            world = new World(longSeed, WIDTH, HEIGHT);
            world.createWorld();
            input = input.substring(2 + seedLength);
        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {   // load previous save
            Pair<Long, Pair<Integer, Integer>> loadData = loadSave();
            if (loadData == null) {
                return null;
            }
            world = new World(loadData.a, WIDTH, HEIGHT);
            world.createWorld(loadData.b);
            input = input.substring(1);
        }

        // process movement commands
        StringBuffer inputReader = new StringBuffer(input);
        Pair<Integer, Integer> dir = new Pair<>(0, 0);
        while (!inputReader.isEmpty()) {
            char key = inputReader.charAt(0);
            if (key == 'A' || key == 'a') {
                dir = new Pair<Integer, Integer>(-1, 0);
            } else if (key == 'S' || key == 's') {
                dir = new Pair<Integer, Integer>(0, -1);
            } else if (key == 'W' || key == 'w') {
                dir = new Pair<Integer, Integer>(0, 1);
            } else if (key == 'D' || key == 'd') {
                dir = new Pair<Integer, Integer>(1, 0);
            } else if (key == ':'
                       && inputReader.length() > 1
                       && (inputReader.charAt(1) == 'q' || inputReader.charAt(1) == 'Q')) {
                try {
                    FileWriter saveFile = new FileWriter(SAVEPATH);
                    saveFile.write(String.valueOf(longSeed) + '\n'); // save seed
                    Pair<Integer, Integer> avatarPos = world.getAvatarPos();
                    saveFile.write(String.valueOf(avatarPos.a) + ' ' + avatarPos.b); // save avatar position
                    saveFile.close();
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
            world.move(dir);
            inputReader.deleteCharAt(0);
        }
        TETile[][] finalWorldFrame = world.getWorld();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    // returns seed (-1 if command is QUIT, -3 if LOAD)
    public long menuExec(char command) {
        if (command == 'n' || command == 'N') {
            String seed = "";
            ter.prompt(seed, "Enter random seed:");
            while (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            while (true) {
                if ((key == 's' || key == 'S') && seed.length() != 0) {
                    break;
                }
                if ('0' <= key && key <= '9') {
                    seed += key;
                }
                ter.prompt(seed, "Enter random seed:");
                while (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                key = StdDraw.nextKeyTyped();
            }
            return Long.valueOf(seed);
        } else if (command == 'l' || command == 'L') {
            return LOAD_FLAG;
        } else if (command == 'c' || command == 'C') {
            avatarName = "";
            char key = ' ';
            while (true) {
                ter.prompt(avatarName, "Enter avatar name (Esc to confirm):");
                while (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                key = StdDraw.nextKeyTyped();
                if (key == ESC_CHAR && avatarName.length() > 0) {
                    return MENU_FLAG;
                } else if (key == BSPACE_CHAR) {
                    if (avatarName.length() > 0) {
                        avatarName = avatarName.substring(0, avatarName.length() - 1);
                    }
                } else if (key != ' ') {
                    avatarName += key;
                }
            }
        } else if (command == 'q' || command == 'Q') {
            StdDraw.clear(new Color(WHITE, WHITE, WHITE));
            StdDraw.show();
            return QUIT_FLAG;
        }
        return MENU_FLAG;
    }

    private Pair<Long, Pair<Integer, Integer>> loadSave() {
        try {
            In reader = new In(SAVEPATH);
            if (!reader.exists()) { // no save file detected
                return null;
            }
            long longSeed = Long.valueOf(reader.readLine());
            String[] avatarPosStr = reader.readLine().split(" ");
            Pair<Integer, Integer> avatarPos = new Pair<>(
                    Integer.valueOf(avatarPosStr[0]),
                    Integer.valueOf(avatarPosStr[1]));
            if (reader.hasNextLine() && avatarName.equals("default")) {
                avatarName = reader.readLine();
            }
            return new Pair<>(longSeed, avatarPos);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;
    }
}
