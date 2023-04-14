package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;

public class World {
    private TETile[][] world;
    private RoomTree roomTree;
    private ArrayList<Room> rooms;
    int worldWidth;
    int worldHeight;
    int FIVE = 5;

    public void createWorld(Long seed) {
        this roomTree = new RoomTree();
        Random random = new Random(seed);
        // determine number of rooms to make using width & height of world
        for (int i = 0; i < NUM_ROOMS; i++) {
            Room rm = null;
            while (rm == null) {
                rm = chooseLocation(random);
            }
            addRoom2World(rm);
        }



        return;
    }

    /*** does a preliminary check of the proposed room before creating it
     * @param x = x coor of bottom left
     *        y = y coor of bottom left
     *        width = proposed width of room
     *        height = proposed height of room
     * @return true if an overlap is detected, false otherwise
     */
    public boolean detectOverlap(int x, int y, int width, int height) {
        for (int i = x; i < x + width - 1; i++) {
            for (int j = y; j < y + height - 1; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) return true;
            }
        }
        return false;
    }

    public Room chooseLocation(Random random) {
        int x = random.nextInt(0, this.worldWidth - 2);
        int y = random.nextInt(0, this.worldHeight - 2);
        if (world[x][y] == Tileset.FLOOR || world[x][y] == Tileset.WALL) {
            return null;
        }
        int width = random.nextInt(0, this.worldWidth - x + 1);
        int height = random.nextInt(0, this.worldHeight - y + 1);
        if (detectOverlap(x, y, width, height) == true) {
            return null;
        }
        Room rm = new Room(random, width, height);
        return rm;
    }

    public void addRoom2World(Room newRm) {
        int x = newRm.getX();
        int y = newRm.getY();
        for (int i = x; i < newRm.getwidth(); i++) {
            for (int j = y; j < newRm.getwidth(); j++) {
                this.world[i][j] = newRm.getTile(i - x - 1, j - y - 1);
            }
        }

    }

}