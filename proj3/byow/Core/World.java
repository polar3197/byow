package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import com.github.javaparser.utils.Pair;

import java.util.Random;
import java.util.Set;

public class World {
    private TETile[][] world;

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
