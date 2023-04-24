# Build Your Own World Design Document

**Partner 1:** Jesse Lutan

**Partner 2:** Charlie Cooper

## Classes and Data Structures

World {  
  private DisjointSet<RoomTree> roomSet  
  private TETile[][] world  
    
  public connectRooms()  
  public connect(Room, Room)  
}  
  
RoomTree {  
  private Room root  
  private int size  
  
}  
  
<!-- Chunk {  
  private size # the length of a "wall" of the chunk  
  private coordinates # bottom left corner  
  private TETile[][]  
  private Room roomTree  
    
  public populateTree() # populate tree with rooms and hallways  
  public connect(Chunk)  
  public addWalls() # transforms floors with empty neighboring tiles to walls  
}   -->
  
Room {  
  private width, height  
  private coordinates  
  private TETile map[][]  
  private neighbors[]  
    
  public Room(Random random) # generate random room dimensions  
  public bool detectOverlap(TETile[][] map)
}  
  
## Algorithms


## Persistence
