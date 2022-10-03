package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * 
 */
public class NaiveNN implements NearestNeigh{

    ArrayList<Point> naive = new ArrayList<Point>();

    @Override
    public void buildIndex(List<Point> points) {
        // Add all points to array
        for (int i = 0; i < points.size(); i++) {
            naive.add(points.get(i));
        }
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        ArrayList<Point> pointdist = new ArrayList<Point>();
        Double distance;
        if(searchTerm != null && k > 0){
            Category categorysearch = searchTerm.cat;
            for (int i = 0; i < naive.size(); i++) {
                //Check if category is the same
                if (naive.get(i).cat == categorysearch){
                    distance = searchTerm.distTo(naive.get(i));
                    if (pointdist.size() == 0){
                        pointdist.add(naive.get(i));
                    } else {
                        int j = 0;
                        boolean added = false;
                        // Goes through all points in list and compares them to see where to place this point
                        while (j < pointdist.size() && added == false){
                            if (searchTerm.distTo(pointdist.get(j)) >= distance){
                                pointdist.add(j, naive.get(i));
                                added = true;
                            } else if (j + 1 == pointdist.size()){
                                pointdist.add(naive.get(i));
                                added = true;
                            }
                            j = j +1;
                        }
                    }
                }
            }
            // Removing extra points so that we only return k amount of points
            while (pointdist.size() > k){
                pointdist.remove(pointdist.size()-1);
            }
        }
        return pointdist;
    }

    @Override
    public boolean addPoint(Point point) {
        boolean added = false;
        if(point != null){
            if(isPointIn(point) == false){
                added = true;
                naive.add(point);
            }
        }    
        return added;
    }

    @Override
    public boolean deletePoint(Point point) {
        boolean deleted = false;
        if(point != null){
            for (int i = 0; i < naive.size(); i++) {
                if (point.equals(naive.get(i))) {
                    deleted = true;
                    naive.remove(i);
                }
            }
        }
        return deleted;
    }

    @Override
    public boolean isPointIn(Point point) {
        boolean pointin = false;
        if(point != null){
            for (int i = 0; i < naive.size(); i++) {
                if (point.equals(naive.get(i))) {
                    pointin = true;
                }
            }
        }
        return pointin;
    }

}
