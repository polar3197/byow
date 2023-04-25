package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import com.github.javaparser.utils.Pair;

import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;

import static java.lang.Math.abs;

public class World {
    private TETile[][] world;
    private TETile[][] darkWorld;
    private RoomTree roomTree;
    private ArrayList<Room> rooms;
    private int worldWidth;
    private int worldHeight;
    final int NUM_ROOMS;
    private int RADIUS_OF_SIGHT = 6;
    public Pair<Integer, Integer> avatarPos;
    private Random random;

    public static void main(String args[]) {
        World world = new World(System.currentTimeMillis(), 80, 30);
        world.createWorld();
        TERenderer renderer = new TERenderer();
        renderer.initialize(80, 30);
        renderer.renderFrame(world.world);
    }

    public World(Long seed, int width, int height) {
        worldWidth = width;
        worldHeight = height;
        random = new Random(seed);
        int worldSize = worldWidth * worldHeight;
        int maxRooms = (int) Math.floor((worldSize / 75.0) * (14.0/15.0));
        int minRooms = (int) Math.floor((worldSize / 75.0) * (1.0/2.0));
        NUM_ROOMS = random.nextInt(minRooms, maxRooms);
        roomTree = new RoomTree(NUM_ROOMS);
        rooms = new ArrayList<>(NUM_ROOMS);

        world = new TETile[worldWidth][worldHeight];
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                setTile(i, j, Tileset.WALL);
            }
        }
    }

    public TETile[][] getWorld(boolean light) {
        if (light) {
            return world;
        } else {
            return darkWorld;
        }
    }
    public void setRADIUS_OF_SIGHT(int newROS) {
        this.RADIUS_OF_SIGHT = newROS;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public void createWorld() {
        // determine number of rooms to make using width & height of world
        for (int i = 0; i < NUM_ROOMS; i++) {
            Room rm = null;
            while (rm == null) {
                rm = chooseLocation(random);
            }
            addRoom2World(rm);
        }
//        this.roomTree = new RoomTree(rooms.size());
//        while (!roomTree.fullyConnected()) {
//            Pair<Integer, Integer> roomPair = roomTree.pickDisjointRooms(this.random);
//            connectRooms(roomPair.a, roomPair.b, this.random);
//        }
        Comparator c = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Room r1 = (Room) o1;
                Room r2 = (Room) o2;
                return r1.getX() - r2.getX();
            }
        };
        rooms.sort(c);
        for (int i = 0; i < rooms.size() - 1; i++) {
            connectRooms(i, i + 1, random);
        }
        removeWalls();
        Room rm = rooms.get(0);
        int x = rm.getX();
        int y = rm.getY();
        avatarPos = new Pair<>(x + 1, y + 1);
        world[avatarPos.a][avatarPos.b] = Tileset.AVATAR;

        darkWorld = new TETile[worldWidth][worldHeight];
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                if (abs((i - avatarPos.a)) + abs((j - avatarPos.b)) <= RADIUS_OF_SIGHT && inBounds(i, j)) {
                    darkWorld[i][j] = world[i][j];
                } else {
                    darkWorld[i][j] = Tileset.NOTHING;
                }
            }
        }
        return;
    }

    public Pair<Integer, Integer> addPairs(Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
        return new Pair<>(first.a + second.a, first.b + second.b);
    }

    public boolean move(Pair<Integer, Integer> dir) {
        Pair<Integer, Integer> temp = addPairs(dir, avatarPos);
        if (inBounds(temp.a, temp.b) && world[temp.a][temp.b] == Tileset.FLOOR) {
            world[avatarPos.a][avatarPos.b] = Tileset.FLOOR;
            darkWorld[avatarPos.a][avatarPos.b] = Tileset.FLOOR;
            avatarPos = temp;
            world[avatarPos.a][avatarPos.b] = Tileset.AVATAR;
            darkWorld[avatarPos.a][avatarPos.b] = Tileset.AVATAR;
            shiftLight();
            return true;
        }
        return false;
    }

    private void shiftLight() {
        for (int i = -(RADIUS_OF_SIGHT + 1); i <= RADIUS_OF_SIGHT + 1; i++) {
            for (int j = -(RADIUS_OF_SIGHT + 1) + abs(i); j <= RADIUS_OF_SIGHT + 1 - abs(i); j ++) {
                if (inBounds(avatarPos.a + i, avatarPos.b + j)) {
                    darkWorld[avatarPos.a + i][avatarPos.b + j] = Tileset.NOTHING;
                }
            }
        }
        for (int i = -RADIUS_OF_SIGHT; i <= RADIUS_OF_SIGHT; i++) {
            for (int j = -(RADIUS_OF_SIGHT) + abs(i); j <= RADIUS_OF_SIGHT - abs(i); j++) {
                if (inBounds(avatarPos.a + i, avatarPos.b + j)) {
                    darkWorld[avatarPos.a + i][avatarPos.b + j] = world[avatarPos.a + i][avatarPos.b + j];
                }
            }
        }
    }

    public boolean inBounds(int i, int j) {
        return ((0 <= i && i < worldWidth) && (0 <= j && j < worldHeight));
    }
        /*
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                if (darkWorld[i][j] != Tileset.NOTHING) {
                    if ((i + dir.a < worldWidth) && (j + dir.b < worldHeight)) {
                        darkWorld[i + dir.a][j + dir.b] = world[i + dir.a][j + dir.b];
                    }
                } else if ((i - dir.a < worldWidth) && (j - dir.b < worldHeight)) {
                    if (darkWorld[i - dir.a][j - dir.b] != Tileset.NOTHING) {
                        darkWorld[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
         */

                /*
                if ((i + dir.a < worldWidth) && (j + dir.b < worldHeight)) {
                    if (darkWorld[i][j] != Tileset.NOTHING) {
                        darkWorld[i + dir.a][j + dir.b] = world[i + dir.a][j + dir.b];
                    } else {
                        if (darkWorld[i - dir.a][j - dir.b] != Tileset.NOTHING) {
                            darkWorld[i - dir.a][j - dir.b] = Tileset.NOTHING;
                        }
                    }
                }
                */

    /*** does a preliminary check of the proposed room before creating it
     * @param x = x coordinate of bottom left
     *        y = y coordinate of bottom left
     *        width = proposed width of room
     *        height = proposed height of room
     * @return true if an overlap is detected, false otherwise
     */
    public boolean detectOverlap(int x, int y, int width, int height) {
        for (int i = x - 1; i < x + width; i++) {
            for (int j = y - 1; j < y + height; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) return true;
            }
        }
        return false;
    }

    private void removeWalls() {
        for (int k = 0; k < worldHeight; k++) {
            world[worldWidth - 1][k] = Tileset.NOTHING;
            world[0][k] = Tileset.NOTHING;
        }
        for (int i = 0; i < worldWidth; i++) {
            world[i][worldHeight - 1] = Tileset.NOTHING;
            world[i][0] = Tileset.NOTHING;
            for (int j = 1; j < worldHeight - 1; j++) {
                if (world[i][j].equals(Tileset.WALL) && !neighborHasFloor(i, j)) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    private boolean neighborHasFloor(int x, int y) {
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (world[i][j] == Tileset.FLOOR) {
                    return true;
                }
            }
        }
        return false;
    }

    public Room chooseLocation(Random random) {
        int x = random.nextInt(1, this.worldWidth - 5);
        int y = random.nextInt(1, this.worldHeight - 5);
        if (world[x][y] == Tileset.FLOOR) {
            return null;
        }
        int width = random.nextInt(5, Math.min(this.worldWidth - x, 11));
        int height = random.nextInt(5, Math.min(this.worldHeight - y, 11));
        if (detectOverlap(x, y, width, height) == true) {
            return null;
        }
        Room rm = new Room(random, x, y, width, height);
        return rm;
    }

    public void addRoom2World(Room newRm) {
        int x = newRm.getX();
        int y = newRm.getY();
        for (int i = x + 1; i < x + newRm.getwidth() - 1; i++) {
            for (int j = y + 1; j < y + newRm.getHeight() - 1; j++) {
                setTile(i, j, Tileset.FLOOR);
            }
        }
        this.rooms.add(newRm);

    }

    /**
     * Connect two rooms with a hallway and update
     * the RoomTree
     *
     * @param a index of first room
     * @param b index of second room
     * @param random Random object
     */
    public void connectRooms(int a, int b, Random random) {
        // find closest potential doors
        Pair<Integer, Integer> minDoorA = null;
        Pair<Integer, Integer> minDoorB = null;
        double minDist = Double.MAX_VALUE;
        for (Pair<Integer, Integer> doorA : rooms.get(a).getPotentialDoors()) {
            for (Pair<Integer, Integer> doorB : rooms.get(b).getPotentialDoors()) {
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
        roomTree.union(a, b);
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
                setTile(i, j, tile);
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

