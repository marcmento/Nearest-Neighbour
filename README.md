# Nearest Neighbour
In this project I implemented algorithms and data structures for k-nearest neighbour searches, using a naive and Kd-tree based approaches. In my implementation of each approach the following operations can be done:
* Create an empty algorithm/data structure
* Search for the k-nearest neighbours of a query of coordinates and category.
* Add a point to the set of points.
* Remove a point from the set of points.

## Naive Implementation 
In the naive implementation, no data structure are used to assist searches. Each time there is a neighbour neighbour query, the naive algorithm will compute all pairwise distances from query, then choose the (k) nearest ones.

## KD-Trees Implementation
In this implementation a Kd-tree was used to answer nearest neighbour searches.

## Commands
* `S <category> <x-coordindates> <y-coordinates> <k>` – searches for the k nearest neigh-
bours to query (x,y), for the specified category. If no nearest neighbours, return empty list.
* `A <id> <category> <x-coordinates> <y-coordinates>` – adds a new point (x,y) to the point
set of specified category. If point exists already (id, category, both coordinates all match), return
false.
* `D <id> <category> <x-coordinates> <y-coordinates>` – delete a point (x,y) with id from
the point set of specified category. Id, category and coordinates must match for delete to proceed.
If fail to delete, return false.
* `C <id> <category> <x-coordinates> <y-coordinates>` – check if a point (x,y) is in the point
set of specified category. Id, category and coordinates must match for check to be success.

## Run
To compile, from the directory where NearestNeighFileBased.java is, execute:
```
javac *.java
```
To run, from the directory where NearestNeighFileBased.java is, execute:
```
java NearestNeighFileBased [approach] [data filename] [command filename] [output filename]
```
where
* approach is one of “naive” or “kdtree”;
* data filename is the name of the file containing the intial set of points;
* command filename is the name of the file with the commands/operations;
* output filename is where to store the output of program.
