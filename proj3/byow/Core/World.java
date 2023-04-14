package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import com.github.javaparser.utils.Pair;
import java.util.Random;
import java.util.ArrayList;

public class World {
    private TETile[][] world;
    private RoomTree roomTree;
    private ArrayList<Room> rooms;
    int worldWidth;
    int worldHeight;
    int FIVE = 5;

    public void createWorld(Long seed, int wrldWidth, int wrldHeight) {
        Random random = new Random(seed);
        int maxRooms = ((wrldWidth * wrldHeight) / 100) * (2/3);
        int minRooms = ((wrldWidth * wrldHeight) / 100) * (1/3);
        int NUM_ROOMS = random.nextInt(minRooms, maxRooms);
        // determine number of rooms to make using width & height of world
        for (int i = 0; i < NUM_ROOMS; i++) {
            Room rm = null;
            while (rm == null) {
                rm = chooseLocation(random);
            }
            addRoom2World(rm);
        }
        this.roomTree = new RoomTree();


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
        this.rooms.add(newRm);
    }
                

    public void connectRooms(Room a, Room b, Random random) {
        // find closest potential doors
        Pair<Integer, Integer> minDoorA = null;
        Pair<Integer, Integer> minDoorB = null;
        double minDist = Double.MAX_VALUE;
        for (Pair<Integer, Integer> doorA : a.getPotentialDoors()) {
            for (Pair<Integer, Integer> doorB : b.getPotentialDoors()) {
                double curDist = Math.sqrt((doorA.a - doorB.a)*(doorA.a - doorB.a)
                        + (doorA.b - doorB.b)*(doorA.b - doorB.b));
                if (curDist < minDist) {
                    minDoorA = doorA;
                    minDoorB = doorB;
                    minDist = curDist;
                }
            }
        }

        // generate hallway
        Pair<Integer, Integer>[] joints = new Pair[]{new Pair<>(minDoorA.a, minDoorB.b),
                                                     new Pair<>(minDoorB.a, minDoorA.b)};
        Pair<Integer, Integer> joint = joints[random.nextInt(2)];
        setArea(minDoorA.a, minDoorA.b, joint.a, joint.b, Tileset.FLOOR);
        setArea(minDoorB.a, minDoorB.b, joint.a, joint.b, Tileset.FLOOR);
    }

    /**
     * Sets the specified area of the world to the specified tile
     *
     * @param x1 first x coordinate
     * @param y1 first y coordinate
     * @param x2 first y coordinate
     * @param y2 first x coordinate
     * @param tile TETile to fill with
     */
    public void setArea(int x1, int y1, int x2, int y2, TETile tile) {
        int x_start;
        int y_start;
        int x_end;
        int y_end;

        if (x1 < x2) {
            x_start = x1;
            x_end = x2;
        } else {
            x_start = x2;
            x_end = x1;
        } if (y1 < y2) {
            y_start = y1;
            y_end = y2;
        } else {
            y_start = y2;
            y_end = y1;
        }
        for (int i = x_start; i <= x_end; i++) {
            for (int j = y_start; j <= y_end; j++) {
                world[i][j] = tile;
            }
        }
    }

    /**
     * Sets the cell at the specified coordinate to the specified tile
     * @param x x coordinate of cell
     * @param y y coordinate of cell
     * @param tile TETile to set cell to
     */
    public void setTile(int x, int y, TETile tile) {
        world[x][y] = tile;
    }
}

