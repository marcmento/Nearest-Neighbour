package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh{
    // Seperate trees for each category
    private Node root_RES;
    private Node root_EDU;
    private Node root_HOS;

    @Override
    public void buildIndex(List<Point> points) {
        ArrayList<Point> res = new ArrayList<Point>();
        ArrayList<Point> hos = new ArrayList<Point>();
        ArrayList<Point> edu = new ArrayList<Point>();
        for (int i = 0; i < points.size(); i++) {
            if(points.get(i).cat == Category.RESTAURANT){
                res.add(points.get(i));
            }
        }
        for (int i = 0; i < points.size(); i++) {
            if(points.get(i).cat == Category.EDUCATION){
                edu.add(points.get(i));
            }
        }
        for (int i = 0; i < points.size(); i++) {
            if(points.get(i).cat == Category.HOSPITAL){
                hos.add(points.get(i));
            }
        }
        root_RES = subset(res, 0, res.size() -1, 0);
        root_HOS = subset(hos, 0, hos.size() -1, 0);
        root_EDU = subset(edu, 0, edu.size() -1, 0);
    }

    public Node subset(List<Point> points, int startpoint, int endpoint, int depth) {
        // Check for empty list
        if (startpoint > endpoint) {
            return null;
        }
        // Sort by X or Y
        if(depth % 2 == 0){ 
            //Sort X
            points = Xsort(points);
        } else {
            //Sort Y
            points = Ysort(points);
        }

        int median = (startpoint + endpoint) / 2;

        // Multiple of X or Y check
        if(depth % 2 == 0 && points.size() > 1 && median > 0){
            while(points.get(median).lat == points.get(median-1).lat){
                median = median-1;
            }
        } else if(depth % 2 != 0 && points.size() >1 && median > 0) {
            while(points.get(median).lon == points.get(median-1).lon){
                median = median-1;
            }
        }
        
        Node node = new Node(points.get(median));
        // This array gets points lower then the medium
        ArrayList<Point> lower = new ArrayList<Point>();
        for (int i = 0; i < median; i++) {
            lower.add(points.get(i));
        }
        // This array gets the points higher then the medium
        ArrayList<Point> upper = new ArrayList<Point>();
        for (int i = median + 1; i < points.size(); i++) {
            upper.add(points.get(i));
        }
        // Left side gets lower numbers, right side gets higher
        node.nLeftChild = subset(lower, 0, lower.size()-1, depth +1);
        node.nRightChild = subset(upper, 0 , upper.size()-1, depth +1);
        return node;
    }

    // Sorting methods
    public List<Point> Xsort(List<Point> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = 0; j < points.size()- 1 - i; j++) {
                if (points.get(j+1).lat < points.get(j).lat) {
                    Point temp = points.get(j);
                    Point temp1 = points.get(j+1);
                    points.remove(j+1);
                    points.remove(j);
                    points.add(j, temp1);
                    points.add(j+1, temp);
                }
            }
        }
        return points;
    } 

    public List<Point> Ysort(List<Point> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = 0; j < points.size()- 1 - i; j++) {
                if (points.get(j+1).lon < points.get(j).lon) {
                    Point temp = points.get(j);
                    Point temp1 = points.get(j+1);
                    points.remove(j+1);
                    points.remove(j);
                    points.add(j, temp1);
                    points.add(j+1, temp);
                }
            }
        }
        return points;
    } 

    @Override
    public List<Point> search(Point searchTerm, int k) {
        ArrayList<Point> pointdist = new ArrayList<Point>();
        Node min = null;
        Node check = null;
        if(searchTerm == null){
            return pointdist;
        }
        // Setting some parameters based on category of search term
        if(searchTerm.cat == Category.RESTAURANT){
             if (root_RES == null) {
                return pointdist;
             } else {
                min = root_RES;
                check = root_RES;
             }
        } else if (searchTerm.cat == Category.EDUCATION){
            if (root_EDU == null) {
                return pointdist;
            } else {
               min = root_EDU;
               check = root_EDU;
            }
        } else if (searchTerm.cat == Category.HOSPITAL){
            if (root_HOS == null) {
                return pointdist;
             } else {
                min = root_HOS;
                check = root_HOS;
             }
        }
        int count = 0;
        Boolean leafFound = false;
        // Finding min
        while(leafFound == false){
            if (count % 2 == 0) {
                if (searchTerm.lat < min.nKey.lat ) {
                    if(min.nLeftChild != null){
                        min = min.nLeftChild;
                        count++;
                    } else {
                        leafFound = true;
                    }
                }
                else if (searchTerm.lat >= min.nKey.lat) {
                    if(min.nRightChild != null){
                        min = min.nRightChild;
                        count++;
                    } else {
                        leafFound = true;
                    }
                }    
            } else {
                if (searchTerm.lon < min.nKey.lon ) {
                    if(min.nLeftChild != null){
                        min = min.nLeftChild;
                        count++;
                    } else {
                        leafFound = true;
                    }
                }
                else if (searchTerm.lon >= min.nKey.lon) {
                    if(min.nRightChild != null){
                        min = min.nRightChild;
                        count++;
                    } else {
                        leafFound = true;
                    }
                }   
            }
        }
        pointdist.add(min.nKey);
        int depth = 0;
        // Recursive method used.
        pointdist = TreeSearch(pointdist, searchTerm, check, k, depth);
        return pointdist;
    }

    public  ArrayList<Point> TreeSearch(ArrayList<Point> pointdist, Point searchTerm, Node check, int k, int depth){
        // If NN list is smaller then k then point is added regardless
        if(pointdist.size() < k){
            Boolean alreadyin = false;
            for(int i = 0; i < pointdist.size(); i++){
                if (pointdist.get(i).equals(check.nKey)){
                    alreadyin = true;
                }
            }
            if(alreadyin == false){
                pointdist.add(check.nKey);
                pointdist = DistSort(pointdist, searchTerm);
            }
        // If not then we comapre current point to last item in nearest neighbour list
        } else if(searchTerm.distTo(check.nKey) < searchTerm.distTo(pointdist.get(pointdist.size()-1))) {
            Boolean alreadyin = false;
            for(int i = 0; i < pointdist.size(); i++){
                if (pointdist.get(i).equals(check.nKey)){
                    alreadyin = true;
                }
            }
            if(alreadyin == false){
                pointdist.remove(pointdist.size()-1);
                pointdist.add(check.nKey);
                pointdist = DistSort(pointdist, searchTerm);
            }
        }
        // SPLIT
        if (depth % 2 == 0) {
            if (searchTerm.lat < check.nKey.lat) {
                // TESTING RIGHT TREE
                if(pointdist.size() < k && check.nRightChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nRightChild, k, depth+1);
                    // GO DOWN RIGHT SUBTREE
                } else if(check.nRightChild != null){
                    Point dummy = new Point("DUMMY", Category.RESTAURANT, check.nKey.lat, searchTerm.lon);
                    if(searchTerm.distTo(dummy) < searchTerm.distTo(pointdist.get(pointdist.size()-1))){
                        pointdist = TreeSearch(pointdist, searchTerm, check.nRightChild, k, depth+1);
                    } 
                } 
                if(check.nLeftChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nLeftChild, k, depth+1);
                    // GO DOWN LEFT TREE
                }
            }
            else if (searchTerm.lat >= check.nKey.lat) {
                // TESTING LEFT TREE
                if(pointdist.size() < k && check.nLeftChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nLeftChild, k, depth+1);
                    // GO DOWN LEFT SUBTREE
                } else if(check.nLeftChild != null){
                    Point dummy = new Point("DUMMY", Category.RESTAURANT, check.nKey.lat, searchTerm.lon);
                    if(searchTerm.distTo(dummy) < searchTerm.distTo(pointdist.get(pointdist.size()-1))){
                        pointdist = TreeSearch(pointdist, searchTerm, check.nLeftChild, k, depth+1);
                        // SEARCH LEFT TREE 
                    } 
                }
                if(check.nRightChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nRightChild, k, depth+1);
                    // GO DOWN RIGHT TREE
                }
            }    
        } else {
            if (searchTerm.lon < check.nKey.lon ) {
                // TESTING RIGHT TREE
                if(pointdist.size() < k && check.nRightChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nRightChild, k, depth+1);
                    // GO DOWN RIGHT SUBTREE
                } else if(check.nRightChild != null){
                    Point dummy = new Point("DUMMY", Category.RESTAURANT, searchTerm.lat, check.nKey.lon);
                    if(searchTerm.distTo(dummy) < searchTerm.distTo(pointdist.get(pointdist.size()-1))){
                        pointdist = TreeSearch(pointdist, searchTerm, check.nRightChild, k, depth+1);
                        // SEARCH RIGHT TREE 
                    } 
                } 
                if(check.nLeftChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nLeftChild, k, depth+1);
                    // GO DOWN LEFT TREE
                } 
            }
            else if (searchTerm.lon >= check.nKey.lon) {
                // TESTING LEFT TREE
                if(pointdist.size() < k && check.nLeftChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nLeftChild, k, depth+1);
                    // GO DOWN LEFT SUBTREE
                } else if(check.nLeftChild != null){
                    Point dummy = new Point("DUMMY", Category.RESTAURANT, searchTerm.lat, check.nKey.lon);
                    if(searchTerm.distTo(dummy) < searchTerm.distTo(pointdist.get(pointdist.size()-1))){
                        pointdist = TreeSearch(pointdist, searchTerm, check.nLeftChild, k, depth+1);
                        // SEARCH LEFT TREE 
                    } 
                } 
                if(check.nRightChild != null){
                    pointdist = TreeSearch(pointdist, searchTerm, check.nRightChild, k, depth+1);
                    // GO DOWN RIGHT TREE
                } 
            }   
        }
        return pointdist;
    }
    // Sorting nearest neighbour list when new point is added
    public ArrayList<Point> DistSort(ArrayList<Point> points, Point searchTerm) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = 0; j < points.size()- 1 - i; j++) {
                if (searchTerm.distTo(points.get(j+1)) < searchTerm.distTo(points.get(j))) {
                    Point temp = points.get(j);
                    Point temp1 = points.get(j+1);
                    points.remove(j+1);
                    points.remove(j);
                    points.add(j, temp1);
                    points.add(j+1, temp);
                }
            }
        }
        return points;
    } 

    @Override
    public boolean addPoint(Point point) {
        boolean added = false;
        Node check = null;
        if(point == null){
            return added;
        }
        if(point.cat == Category.RESTAURANT){
             if (root_RES == null) {
                root_RES = new Node(point);
                added = true;
                return added;
             } else {
                check = root_RES;
             }
        } else if (point.cat == Category.EDUCATION){
            if (root_EDU == null) {
                root_EDU = new Node(point);
                added = true;
                return added;
             }else {
                check = root_EDU;
             }
        } else if (point.cat == Category.HOSPITAL){
            if (root_HOS == null) {
                root_HOS = new Node(point);
                added = true;
                return added;
             } else {
                check = root_HOS;
             }
        }
        if(isPointIn(point) == false){
            Node newpoint = new Node(point);
            int count = 0;
            // Search down tree till leaf node is found
            while(added == false){
                if (count % 2 == 0) {
                    if (point.lat < check.nKey.lat ) {
                        if(check.nLeftChild != null){
                            check = check.nLeftChild;
                            count++;
                        } else {
                            check.nLeftChild = newpoint;
                            added = true;
                        }
                    }
                    else if (point.lat >= check.nKey.lat) {
                        if(check.nRightChild != null){
                            check = check.nRightChild;
                            count++;
                        } else {
                            check.nRightChild = newpoint;
                            added = true;
                        }
                    }    
                } else {
                    if (point.lon < check.nKey.lon ) {
                        if(check.nLeftChild != null){
                            check = check.nLeftChild;
                            count++;
                        } else {
                            check.nLeftChild = newpoint;
                            added = true;
                        }
                    }
                    else if (point.lon >= check.nKey.lon) {
                        if(check.nRightChild != null){
                            check = check.nRightChild;
                            count++;
                        } else {
                            check.nRightChild = newpoint;
                            added = true;
                        }
                    }   
                }
            }
        }
        return added;
    }

    @Override
    public boolean deletePoint(Point point) {
        boolean pointdelted = false;
        if(isPointIn(point) == true){
            if(point.cat == Category.RESTAURANT){
                root_RES = deletepoint(root_RES, point, 0);
            } else if (point.cat == Category.EDUCATION){
                root_EDU = deletepoint(root_EDU, point, 0);
            } else if (point.cat == Category.HOSPITAL){
                root_HOS = deletepoint(root_HOS, point, 0);
            }
            pointdelted = true;
        }
        return pointdelted;
    }

    public Node deletepoint(Node check, Point pointremove, int depth) {
        // Given point is not present
        if (check == null){
            return null;
        }
        // If the point to be deleted is present at check
        if (check.nKey.equals(pointremove) == true) {
            if (check.nRightChild != null) {
                // Find minimum in right subtree
                Node min = FindMin(check.nRightChild, depth, 0);
                // Copy the minimum to the current point
                check.nKey = min.key();
                // Recursively delete
                check.nRightChild = deletepoint(check.nRightChild, min.nKey, depth +1);
            }
            else if (check.nLeftChild != null) // same as above 
            {
                // Make left side right side
                check.nRightChild = check.nLeftChild;
                check.nLeftChild = null;
                // Find minimum in right subtree
                Node min = FindMin(check.nRightChild, depth, 0);
                // Copy the minimum to current point
                check.nKey = min.key();
                // Recursively delete
                check.nRightChild = deletepoint(check.nRightChild, min.nKey, depth +1);
            }
            else 
            {
                return null;
            }
            return check;
        }
        // Keep earching
        if (depth % 2 == 0){
            if (pointremove.lat < check.nKey.lat){
                check.nLeftChild = deletepoint(check.nLeftChild, pointremove, depth+1);
            } else {
                check.nRightChild = deletepoint(check.nRightChild, pointremove, depth+1);
            }
        } else {
            if (pointremove.lon < check.nKey.lon){
                check.nLeftChild = deletepoint(check.nLeftChild, pointremove, depth+1);
            } else {
                check.nRightChild = deletepoint(check.nRightChild, pointremove, depth+1);
            }
        }
        
        return check;
    }

    public Node FindMin(Node check, int checkdepth, int depth)
    {   
        // Checking current depth
        if (checkdepth % 2 == depth % 2)
        {
            if (check.nLeftChild == null){
                return check;
            }   
            return FindMin(check.nLeftChild, checkdepth, depth+1);
        }
    
        // If current depth is different to the one we are replacing then the minimum can be anywhere
        return minNode(check,FindMin(check.nLeftChild , checkdepth, depth+1), FindMin(check.nRightChild , checkdepth, depth+1), checkdepth);
    }
    
    //Minimum of points finder
    public Node minNode(Node x, Node y, Node z, int depth)
    {
        Node res = x;
        if(depth % 2 == 0){
            if (y != null && y.nKey.lat < res.nKey.lat){
                res = y;
            }    
            if (z !=  null && z.nKey.lat < res.nKey.lat){
                res = z;
            }
        } else {
            if (y != null && y.nKey.lon < res.nKey.lon){
                res = y;
            }    
            if (z !=  null && z.nKey.lon < res.nKey.lon){
                res = z;
            }
        }
        return res; 
    }

    @Override
    public boolean isPointIn(Point point) {
        boolean pointThere = false;
        Node check = null;
        if(point.cat == Category.RESTAURANT){
            if (root_RES == null) {
                return pointThere;
            } else {
                check = root_RES;
            }
        } else if (point.cat == Category.EDUCATION){
            if (root_EDU == null) {
                return pointThere;
            } else {
                check = root_EDU;
            }
        } else if (point.cat == Category.HOSPITAL){
            if (root_HOS == null) {
                return pointThere;
            } else {
                check = root_HOS;
            }
        }
        int count = 0;
        // See if point is where it should be 
        while(check != null && pointThere == false ){
            if (point.equals(check.nKey)){
                pointThere = true;
            } else if (count % 2 == 0) {
                if (point.lat < check.nKey.lat ) {
                    check = check.nLeftChild;
                    count++;
                }
                else if (point.lat >= check.nKey.lat) {
                    check = check.nRightChild;
                    count++;
                }    
            } else {
                if (point.lon < check.nKey.lon ) {
                    check = check.nLeftChild;
                    count++;
                }
                else if (point.lon >= check.nKey.lon) {
                    check = check.nRightChild;
                    count++;
                }   
            }
        }
        return pointThere;
    }

}
// Node class
class Node
{
        public Point nKey;
        public Node nLeftChild;
        public Node nRightChild;

        public Node(Point key) {
                nKey = key;
                nLeftChild = null;
                nRightChild = null;
        }

        public Point key() {
            return nKey;
        } 

        public Node leftChild() {
            return nLeftChild;
        } 

        public Node rightChild() {
            return nRightChild;
        } 
}