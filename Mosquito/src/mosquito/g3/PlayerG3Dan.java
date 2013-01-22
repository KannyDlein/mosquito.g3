package mosquito.g3;

import java.awt.geom.Line2D;
//import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class PlayerG3Dan extends Player{
	
	private Logger log = Logger.getLogger(this.getClass()); // for logging
	
	private static double INTERVAL_TIME = 150;
	private static double ON_TIME = 30;
	private static double START_TIME = 0;
	
	
	/*
	 * This is used to display the name in the UI.
	 */
	@Override
	public String getName() {
		return "G3 Player - Dan";
	}

	@Override
	public void startNewGame(Set<Line2D> walls, int numLights) {
		log.trace("Starting new game: numLights=" + numLights);
		// this player doesn't use the walls
		//this.numLights = numLights;
	}

	@Override
	public Set<Light> getLights() {
		HashSet<Light> ret = new HashSet<Light>();
			
		/*
		 * The arguments to the Light constructor are: 
		 * - X coordinate
		 * - Y coordinate
		 * - blinking interval
		 * - how long it stays on for in each interval
		 * - time at which to turn on
		 */
		
		// Pulse harvesting technique
		//Light center1 = new Light(51, 50, 60, 30, 30);
		//Light center2 = new Light(49, 50, 60, 30, 60);
		
		// Ping-pong harvesting technique
		Light center1 = new Light(51, 50, 30, 15, ON_TIME * 4);
		Light center2 = new Light(49, 50, 30, 15, (ON_TIME * 4) + 15);
		
		log.trace("Positioned a light at (" + center1.getX() + ", " + center1.getY() + ")");
		ret.add(center1);
		log.trace("Positioned a light at (" + center2.getX() + ", " + center2.getY() + ")");
		ret.add(center2);
		
		/*
		Light aux1 = new Light(32, 50, INTERVAL_TIME, ON_TIME, START_TIME);
		Light aux2 = new Light(68, 50, INTERVAL_TIME, ON_TIME, START_TIME);
		Light aux3 = new Light(50, 32, INTERVAL_TIME, ON_TIME, START_TIME);
		Light aux4 = new Light(50, 68, INTERVAL_TIME, ON_TIME, START_TIME);
		*/
		
		
		Light aux1 = new Light(32, 50, INTERVAL_TIME, ON_TIME, ON_TIME * 3);
		Light aux2 = new Light(68, 50, INTERVAL_TIME, ON_TIME, ON_TIME * 3);
		Light aux3 = new Light(50, 32, INTERVAL_TIME, ON_TIME, ON_TIME * 3);
		Light aux4 = new Light(50, 68, INTERVAL_TIME, ON_TIME, ON_TIME * 3);
		
		
		ret.add(aux1);
		ret.add(aux2);
		ret.add(aux3);
		ret.add(aux4);
		
		
		// The code below implements 12 additional lights to pull in corner mosquitos better.
		// It also gets 50% in under 200 steps.
		
		
		
		Light newRightLight = new Light(10 * Math.sqrt(2.0), 10 * Math.sqrt(2.0), INTERVAL_TIME, ON_TIME, START_TIME);
		Light newDownLight = new Light(100 - (10 * Math.sqrt(2.0)), 10 * Math.sqrt(2.0), INTERVAL_TIME, ON_TIME, START_TIME);
		Light newLeftLight = new Light(100 - (10 * Math.sqrt(2.0)), 100 - (10 * Math.sqrt(2.0)), INTERVAL_TIME, ON_TIME, START_TIME);
		Light newUpLight = new Light(10 * Math.sqrt(2.0), 100 - (10 * Math.sqrt(2.0)), INTERVAL_TIME, ON_TIME, START_TIME);
		
		for( int i = 0; i < 3; i++ ){

			ret.add(newRightLight);
			log.trace("Positioned a light at (" + newRightLight.getLocation() + ")");
			newRightLight = nextLight(newRightLight, (i * ON_TIME), 0);

			ret.add(newDownLight);
			log.trace("Positioned a light at (" + newDownLight.getLocation() + ")");
			newDownLight = nextLight(newDownLight, (i * ON_TIME), 1);

			ret.add(newLeftLight);
			log.trace("Positioned a light at (" + newLeftLight.getLocation() + ")");
			newLeftLight = nextLight(newLeftLight, (i * ON_TIME), 2);

			ret.add(newUpLight);
			log.trace("Positioned a light at (" + newUpLight.getLocation() + ")");
			newUpLight = nextLight(newUpLight, (i * ON_TIME), 3);
			
		}
		
		
		
		return ret;
	}

	@Override
	public Collector getCollector() {
		Collector c = new Collector(50, 50);
		log.debug("Positioned Collector at (" + 51 + ", " + 49 + ")");
		return c;
	}
	
	private Light nextLight(Light oldCenter, double startTime, int direction){
		
		Light newCenter = null;
		
		switch(direction){
		
		case 0:
			newCenter = new Light(oldCenter.getX() + 18, oldCenter.getY(), INTERVAL_TIME, ON_TIME, startTime + ON_TIME);
			break;
		
		case 1:
			newCenter = new Light(oldCenter.getX(), oldCenter.getY() + 18, INTERVAL_TIME, ON_TIME, startTime + ON_TIME);
			break;
			
		case 2:
			newCenter = new Light(oldCenter.getX() - 18, oldCenter.getY(), INTERVAL_TIME, ON_TIME, startTime + ON_TIME);
			break;
			
		default:
			newCenter = new Light(oldCenter.getX(), oldCenter.getY() - 18, INTERVAL_TIME, ON_TIME, startTime + ON_TIME);
			break;
		
		}
		
		return newCenter;
		
	}
	
}
