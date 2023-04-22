package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private MouseAdapter mouseAdapter;
    private World world;


    public Engine() {
        System.out.println("Engine constructor called");
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        world = new World(System.currentTimeMillis(), WIDTH, HEIGHT);
        world.createWorld();
        TETile[][] finalWorldFrame = world.getWorld();
        ter.initialize(WIDTH, HEIGHT);
        while (true) {
            ter.renderFrame(finalWorldFrame);
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
        StringBuffer seedBuf = new StringBuffer();
        long seed = Long.MAX_VALUE;
        String savePath = "./out/production/proj3/last_save.SAV";
        if (input.charAt(0) == 'N') {
            int seedLength = 0;
            while (input.charAt(1 + seedLength) != 'S') {
                seedBuf.append(input.charAt(1 + seedLength));
                seedLength++;
            }
            seed = Long.valueOf(seedBuf.toString());
            if (input.substring(input.length() - 2, input.length()).equals(":Q")) { // save world and terminate
                try {
                    FileWriter saveFile = new FileWriter(savePath);
                    saveFile.write(String.valueOf(seed) + '\n');
                    saveFile.close();
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        } else if (input.charAt(0) == 'L') {   // load previous save
            File saveFile = new File(savePath);
            if (!saveFile.exists()) {
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
            In fileReader = new In(savePath);
            seed = Long.valueOf(fileReader.readLine().trim());
            fileReader.close();
        }
        world = new World(seed, WIDTH, HEIGHT);
        world.createWorld();
        TETile[][] finalWorldFrame = world.getWorld();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
}
