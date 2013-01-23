package mosquito.g3;

/*
 * This is a very simple Player that just places the Lights
 * randomly and then places the Collector next to the last Light
 */

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class MidpointPlayerG3 extends Player {
	private int numLights;
	private Point2D mainLight;
	private ArrayList<Point2D> mids;
	private Set<Line2D> wallCopy;
	private final double SIG_LEN = 10.0;
	private Logger log = Logger.getLogger(this.getClass()); // for logging
	
	/*
	 * This is used to display the name in the UI.
	 */
	@Override
	public String getName() {
		return "Random Player";
	}

	/*
	 * This is called when a new game starts. It is passed the set
	 * of lines that comprise the different walls, as well as the 
	 * maximum number of lights you are allowed to use.
	 */
	@Override
	public void startNewGame(Set<Line2D> walls, int numLights) {
		log.trace("Starting new game: numLights=" + numLights);
		
		wallCopy = walls;
		
		if(!walls.isEmpty()){
			
			
			
		}
		
		this.numLights = numLights;
	}

	
	/*
	 * This is called after startNewGame. If your Set contains more
	 * than the maximum allowed number of Lights, an error will occur.
	 */
	@Override
	public Set<Light> getLights() {
		HashSet<Light> ret = new HashSet<Light>();
		
		return ret;
	}

	/*
	 * This is called after getLights.
	 */
	@Override
	public Collector getCollector() {
		// place the Collector just to the right of and below the last Light
		Collector c = new Collector(mainLight.getX()+1,mainLight.getY() +1);
		log.debug("Positioned Collector at (" + (mainLight.getX()+1) + ", " + (mainLight.getY()+1) + ")");
		return c;
	}
	
	private ArrayList<Point> generateMids(Set<Line2D> walls){
		
		ArrayList<Point> mids = new ArrayList<Point>();
		Line2D[] wallList = (Line2D[]) walls.toArray();
		
		for(int i = 0; i < walls.size(); i++){
			
			// checks to see if the wall 
			if(isSignificant(wallList[i])){
				
				double newX = ( wallList[i].getX1() + wallList[i].getX2() ) / 2.0;
				double newY = ( wallList[i].getY1() + wallList[i].getY2() ) / 2.0;
				
				Point midpoint = new Point();
				midpoint.setLocation(newX, newY);
				
				mids.add(midpoint);
				
			}// end significance check
			
		}// end list population
		
		return mids;
		
	}
	
	private boolean isSignificant(Line2D wall){
		
		if(wallLength(wall) < SIG_LEN){
			return false;
		}
		
		return true;
		
	}// end isSignificant method
	
	private double wallLength(Line2D wall){
		
		// Euclidean distance. Why is this not in the class?
		return Math.sqrt(Math.pow(wall.getY2() - wall.getY1(), 2.0) + Math.pow(wall.getX2() - wall.getX1(), 2.0));
	
	} // end wallLength method
	
	private Light pointToLight(Point p, int d, int s, int t){
		
		Light out = new Light(p.getX(), p.getY(), d, s, t);
		
		return out;
		
	}// end pointToLight method
	
	private Point nearestPoint(Point target, ArrayList<Point> mids){
		
		// starts arbitrarily high
		double shortestDistance = 10000.0;
		
		// initially the first midpoint. 
		int index = 0;
		
		// iterates over the set of midpoints, calculating distance between all of them
		for(int i=0; i < mids.size(); i++){
			
			double dist = target.distance(mids.get(i));
			
			// checks to see if new shortest distance has been found
			if(dist < shortestDistance){
				
				// updates nearest neighbor information
				shortestDistance = dist;
				index = i;
				
			}// end shortness check
			
		}// end shortest search
		
		return mids.get(index);
		
	}// end nearestPoint method

}
