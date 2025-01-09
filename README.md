# Build Your Own World Design Document

**Partner 1:** Jesse Lutan

**Partner 2:** Charlie Cooper

## Setup
1. Navigate to root and run 'java -cp "lib/algs4.jar:lib/javaparser-core-3.24.0.jar:." byow.Core.Main'

## How To Use
1. Use keys to select menu options 
2. To sumbit a seed value press 's' or 'S'
3. To save your map and quit the program while it is running, press ":q" or ":Q"
4. To remove fog-of-war effect, press "o" or "O"

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
  
Room {  
  private width, height  
  private coordinates  
  private TETile map[][]  
  private neighbors[]  
    
  public Room(Random random) # generate random room dimensions  
  public bool detectOverlap(TETile[][] map)
}  
  
