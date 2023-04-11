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

    public Room(Random random) {
        x = Integer.MAX_VALUE;
        y = Integer.MAX_VALUE;
        width = random.nextInt(3, 11);
        height = random.nextInt(3, 11);
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
     *
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
//    public Room(int width, int height, int x, int y) {
//        this.width = width;
//        this.height = height;
//        this.x = x;
//        this.y = y;
//        neighbors = new ArrayList<>();
//        ArrayList<Integer> left_wall = new ArrayList<>(List.of(this.x, this.y, this.x, this.y + this.height - 1));
//        ArrayList<Integer> top_wall = new ArrayList<>(List.of(this.x, this.y + this.height - 1, this.x + this.width - 1, this.y + this.height - 1));
//        ArrayList<Integer> right_wall = new ArrayList<>(List.of(this.x + this.width - 1, this.y, this.x + this.width - 1, this.y + this.height - 1));
//        ArrayList<Integer> bot_wall = new ArrayList<>(List.of(this.x, this.y, this.x + this.width - 1, this.y));
//        this.walls.add(left_wall);
//        this.walls.add(top_wall);
//        this.walls.add(right_wall);
//        this.walls.add(bot_wall);
//    }
//
//    /**
//     * Computes the closest walls of this Room and the given Room and returns the
//     * coordinates of their midpoints.
//     *
//     * @param other other Room to connect to
//     * @param random Random object
//     * @return an ArrayList of Pairs containing the coordinates of walls to connect
//     */
//    public ArrayList<Pair<Integer, Integer>> connect(Room other, Random random) {
//        int wall1x = 0;
//        int wall1y = 0;
//        int wall2x = 0;
//        int wall2y = 0;
//        double min_dist = Float.MAX_VALUE;
//        for (ArrayList<Integer> wall : this.walls) {
//            for (ArrayList<Integer> other_wall : other.walls) {
//                float mid1x = (wall.get(0) + wall.get(2)) / 2;
//                float mid1y = (wall.get(1) + wall.get(3)) / 2;
//                float mid2x = (other_wall.get(0) + other_wall.get(2)) / 2;
//                float mid2y = (other_wall.get(1) + other_wall.get(3)) / 2;
//                double cur_dist = Math.sqrt((mid2x - mid1x)*(mid2x - mid1x) + (mid2y - mid1y)*(mid2y - mid1y));
//                if (cur_dist < min_dist) {
//                    min_dist = cur_dist;
//                    wall1x = (int) mid1x;
//                    wall1y = (int) mid1y;
//                    wall2x = (int) mid2x;
//                    wall2y = (int) mid2y;
//                }
//            }
//        }
//        return new ArrayList<>(List.of(new Pair(wall1x, wall1y), new Pair(wall2x, wall2y)));
//    }
}
