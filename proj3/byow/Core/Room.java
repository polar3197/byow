package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Room {
    private int width;
    private int height;
    private int x;
    private int y;

    private ArrayList<Pair<Integer, Integer>> potentialDoors; // potential hallway entry points in (x, y) order
    private TETile roomGrid[][];

    public TETile getTile(int x, int y) {
        return roomGrid[x][y];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getwidth() {
        return width;
    }

    /*
    grid rotated 90 degrees clockwise such that
    (0, 0) corresponds to top left of the grid and
    (width - 1, height - 1) corresponds to bottom right of the grid

    Example 4(width)x5(height) grid:
    #####
    #...#
    #...#
    #####
    (0, 0) is top left corner
    (3, 4) is bottom right corner
     */
    public Room(Random random, int x, int y, int newWidth, int newHeight) {
        this.x = x;
        this.y = y;
        width = newWidth;
        height = newHeight;
        roomGrid = new TETile[this.width][this.height];
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (i > 0 && i < this.width - 1 && j > 0 && j < this.height - 1) {
                    roomGrid[i][j] = Tileset.FLOOR;
                } else {
                    roomGrid[i][j] = Tileset.WALL;
                }
            }
        }

        potentialDoors = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> startPoints = new ArrayList<>();
        startPoints.add(new Pair<>(0, 0)); // left
        startPoints.add(new Pair<>(0, 0)); // bot
        startPoints.add(new Pair<>(width - 1, 0)); // right
        startPoints.add(new Pair<>(0, height - 1)); // top
        ArrayList<Pair<Integer, Integer>> endPoints = new ArrayList<>();
        endPoints.add(new Pair<>(0, height - 1));
        endPoints.add(new Pair<>(width - 1, 0));
        endPoints.add(new Pair<>(width - 1, height - 1));
        endPoints.add(new Pair<>(width - 1, height - 1));
        for (int i = 0; i < 4; i++) {
            int x1;
            int y1;
            if (startPoints.get(i).a == endPoints.get(i).a) {
                x1 = startPoints.get(i).a;
                int y_start = startPoints.get(i).b;
                int y_end = endPoints.get(i).b;
                y1 = random.nextInt(y_start + 1, y_end);

            } else {
                x1 = random.nextInt(startPoints.get(i).a + 1, endPoints.get(i).a);
                y1 = startPoints.get(i).b;
            }
            potentialDoors.add(new Pair<>(x1 + x, y1 + y));
        }
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ArrayList<Pair<Integer, Integer>> getPotentialDoors() {
        return potentialDoors;
    }
}
