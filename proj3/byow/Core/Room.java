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
    private ArrayList<Pair<Integer, Integer>> potentialDoors;
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

    public Room(Random random, int newWidth, int newHeight) {
        x = Integer.MAX_VALUE;
        y = Integer.MAX_VALUE;
        width = newWidth; // .nextInt(3, maxWidth);
        height = newHeight; // random.nextInt(3, maxHeight);
        roomGrid = new TETile[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (i > 0 && i < this.height - 1 && j > 0 && j < this.width - 1) {
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
            int x;
            int y;
            if (startPoints.get(i).a == endPoints.get(i).a) {
                x = startPoints.get(i).a;
                y = random.nextInt(startPoints.get(i).b + 1, endPoints.get(i).b);
            } else {
                x = random.nextInt(startPoints.get(i).a + 1, endPoints.get(i).a);
                y = startPoints.get(i).b;
            }
            potentialDoors.add(new Pair<>(x, y));
        }
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ArrayList<Pair<Integer, Integer>> getPotentialDoors() {
        return potentialDoors;
    }

    /**
     * Detects if this Room with its current coordinates overlaps with
     * other Rooms in a given map
     * <p>
     * Precondition: this Room's coordinates are set
     *
     * @param world 2D TETile array representing the world
     * @return true if an overlap is detected, false otherwise
     */
    public boolean detectOverlap(TETile world[][]) {
        for (int i = x; i < x + this.width - 1; i++) {
            for (int j = y; j < y + this.height - 1; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) return true;
            }
        }
        return false;
    }
}