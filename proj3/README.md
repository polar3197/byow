# Build Your Own World Design Document

**Partner 1:** Jesse Lutan

**Partner 2:** Charlie Cooper

## Classes and Data Structures

World {  
  private DisjointSet chunks  
  private Chunk[][] world  
    
  public connectChunks() # connects all chunks; calls DisjoinSet.weightedQuickUnion()  
  public addWalls() # calls Chunk.addWalls()  
}  
  
Chunk {  
  private size # the length of a "wall" of the chunk  
  private coordinates # bottom left corner  
  private TETile[][]  
  private Room roomTree  
    
  public populateTree() # populate tree with rooms and hallways  
  public connect(Chunk)  
  public addWalls() # transforms floors with empty neighboring tiles to walls  
}  
  
Room {  
  private width, height  
  private coordinates  
  private neighbors[]  
    
  public connect(Room)  
}  
  
## Algorithms

World Generation:
1. Generate number of rows and columns of chunks between 3-6
2. ceiling(2/3 * world_size) = # populated chunks, world_size = # chunks
3. Generating disjoint sets: loop over # populated chunks, pick a random chunk and generate a tree of rooms and hallways
4. Store each populated chunk in a DisjointSet
5. connect sets together with weighted quick union
6. add walls to rooms and hallways

## Persistence
