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
	
	private ArrayList<Point> mids;
	private ArrayList<Point> ends;
	private ArrayList<Point> intersects;
	private double[] slopes;
	private double[] bVals;
	private Line2D[] wallCopy;
	
	private final double SIG_LEN = 10.0;
	private Logger log = Logger.getLogger(this.getClass()); // for logging
	
	/*
	 * This is used to display the name in the UI.
	 */
	@Override
	public String getName() {
		return "Critical Points Player";
	}

	/*
	 * This is called when a new game starts. It is passed the set
	 * of lines that comprise the different walls, as well as the 
	 * maximum number of lights you are allowed to use.
	 */
	@Override
	public void startNewGame(Set<Line2D> walls, int numLights) {
		log.trace("Starting new game: numLights=" + numLights);
		
		
		if(!walls.isEmpty()){
			
			wallCopy = new Line2D[1];
			
			wallCopy = (Line2D[]) walls.toArray(wallCopy);
			
			initSlopes();
			initBVals();
			initMids();
			initEnds();
			initIntersections();
			
			// this may seem redundant, but it gets awfully modern if I put this
			// after the if/else block
			mids.add( new Point( 0, 50 ) );
			mids.add( new Point( 50, 100 ) );
			mids.add( new Point( 50, 0 ) );
			mids.add( new Point( 100, 50 ) );
			intersects.add( new Point( 0, 0) );
			intersects.add( new Point( 100, 0) );
			intersects.add( new Point( 0, 100) );
			intersects.add( new Point( 100, 100) );
			
		}else{
			
			// blank board!
			mids = new ArrayList<Point>();
			mids.add( new Point( 0, 50 ) );
			mids.add( new Point( 50, 100 ) );
			mids.add( new Point( 50, 0 ) );
			mids.add( new Point( 100, 50 ) );
			
			ends = new ArrayList<Point>();
			
			intersects = new ArrayList<Point>();
			intersects.add( new Point( 0, 0) );
			intersects.add( new Point( 100, 0) );
			intersects.add( new Point( 0, 100) );
			intersects.add( new Point( 100, 100) );
			
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
		
		for( int i=0; i < ends.size(); i++){
			
			Light endLight = pointToLight(ends.get(i), 5000, 1, 0);
			ret.add(endLight);
			
		}// end end point population
		
		for( int i=0; i < intersects.size(); i++){
			
			Light cornerLight = pointToLight(intersects.get(i), 5000, 1, 0);
			ret.add(cornerLight);
			
		}// end end point population
		
		for( int i=0; i < mids.size(); i++){
			
			Light midLight = pointToLight( (Point) mids.get(i), 5000, 1, 0);
			ret.add(midLight);
			
		}// end end point population
		
		System.out.println("There are " + ret.size() + " lights.");
		
		return ret;
	}

	/*
	 * This is called after getLights.
	 */
	@Override
	public Collector getCollector() {
		// place the Collector just to the right of and below the last Light
		//Collector c = new Collector(mainLight.getX()+1,mainLight.getY() +1);

		Collector c = new Collector(1,1);
		
		// log.debug("Positioned Collector at (" + (mainLight.getX()+1) + ", " + (mainLight.getY()+1) + ")");
		return c;
	}
	
	private void initMids(){
		
		mids = new ArrayList<Point>();
		
		for(int i = 0; i < wallCopy.length; i++){
			
			// checks to see if the wall is significant
			if(isSignificant(wallCopy[i])){
				
				double newX = ( wallCopy[i].getX1() + wallCopy[i].getX2() ) / 2.0;
				double newY = ( wallCopy[i].getY1() + wallCopy[i].getY2() ) / 2.0;
				
				Point midpoint = new Point();
				midpoint.setLocation(newX, newY);
				
				mids.add(midpoint);
				
			}// end significance check
			
		}// end list population
		
	}
	
	private boolean isValidPath(Line2D path){
		
		for(int i=0; i < wallCopy.length; i++){
			
			// checks to see if light path intersects any walls
			if( wallCopy[i].intersectsLine( path ) ){
				
				return false;
				
			}// end blocked check
			
		}// end intersection check loop
		
		// if you're here, you're in the clear
		return true;
		
	}// end isValidPath method
	
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
	
	private void initSlopes(){
		
		slopes = new double[wallCopy.length];
		
		for(int i=0; i < wallCopy.length; i++){
			
			log.trace("Wall " + i + " of " + wallCopy.length);
			
			double denom = ( wallCopy[i].getX2() - wallCopy[i].getX1() );
			
			// checks for perfectly vertical line
			if(denom != 0.0){
				
				slopes[i] = ( wallCopy[i].getY2() - wallCopy[i].getY1() ) / denom;
			
			}else{
				
				slopes[i] = Double.NaN;
			
			}
			
		}// end slope initialization
		
	}// end initSlopes method
	
	private void initBVals(){
		
		bVals = new double[wallCopy.length];
		
		for(int i=0; i < bVals.length; i++){
			
			if(slopes[i] != Double.NaN){
				
				bVals[i] = wallCopy[i].getY1() - ( slopes[i] * wallCopy[i].getX1() ); 
			
			}else{
				
				bVals[i] = Double.NaN;
				
			}
			
		}// end bVal population loop
		
	}// end initBVals method
	
	private void initEnds(){
		
		ends = new ArrayList<Point>();
		
		for(int i=0; i < wallCopy.length; i++){
			
			// only considers significant lines
			if( isSignificant(wallCopy[i])){
				
				// starts are evens, ends are odds
				ends.add( new Point( (int) wallCopy[i].getX1(), (int) wallCopy[i].getY1() ) );
				ends.add( new Point( (int) wallCopy[i].getX2(), (int) wallCopy[i].getY2() ) );
			
			}
			
		}// end endpoint generation
		
	}// end initEnds method
	
	private void initIntersections(){
		
		// initialize the ArrayList
		intersects = new ArrayList<Point>();
		
		for(int i=0; i < wallCopy.length; i++){
			
			for(int j=i+1; j < wallCopy.length; j++){
				
				if( wallCopy[i].intersectsLine(wallCopy[j]) ){
					
					double newX = 0.0;
					double newY = 0.0;
					
					if(bVals[i] != Double.NaN && bVals[j] != Double.NaN){
						
						newX = ( bVals[j] - bVals[i] ) / ( slopes[i] - slopes[j] );
						newY = slopes[i] * newX + bVals[i];
						
					}else{
						
						// only happens if either of the lines are perfectly vertical
						if(bVals[i] == Double.NaN){
							newX = wallCopy[i].getX1();
							newY = slopes[j] * newX + bVals[j];
						}else{
							newX = wallCopy[j].getX1();
							newY = slopes[i] * newX + bVals[i];
						}
						
					}// end intersection point assignment
					
					intersects.add(new Point((int) newX, (int) newY));
					
				}// end line intersection point creation
				
			}// end line intersection discovery and population
			
		}// end exhaustive line intersection check
		
	}// end initIntersections method

}
