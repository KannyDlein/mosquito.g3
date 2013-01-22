package mosquito.g3;

import java.awt.geom.Line2D;
//import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class PlayerG3Earn extends Player{
	
	private int numLights;
	private Logger log = Logger.getLogger(this.getClass()); // for logging
	
	/*
	 * This is used to display the name in the UI.
	 */
	@Override
	public String getName() {
		return "G3 Player - Earnest";
	}

	@Override
	public void startNewGame(Set<Line2D> walls, int numLights) {
		log.trace("Starting new game: numLights=" + numLights);
		// this player doesn't use the walls
		this.numLights = numLights;
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
		Light center = new Light(50, 50, 30, 27, 1);

		log.trace("Positioned center light at (" + center.getX() + ", " + center.getY() + ")");
		ret.add(center);
		

		/** 5 light configuration
		
		Light aux1 = new Light(32, 50, 80, 40, 41);
		Light aux2 = new Light(68, 50, 80, 40, 41);
		Light aux3 = new Light(50, 32, 80, 40, 41);
		Light aux4 = new Light(50, 68, 80, 40, 41); */
		
		/*
		if (numLights <= 5) {
			for (int i = 0; i < numLights - 1; i++) {
				double angle = i*90;
				double distance = 18;
				Light light = vectorLightBuilder(center, angle, distance,
						80, 40, 41);
				ret.add(light);
				log.trace("Positioned aux light at (" + light.getX() + ", " + light.getY() + ")");
			}
		}
		*/
		
		// 9 light configuration
		
		Light aux1 = new Light(32, 50, 90, 30, 31);
		Light aux2 = new Light(68, 50, 90, 30, 31);
		Light aux3 = new Light(50, 32, 90, 30, 31);
		Light aux4 = new Light(50, 68, 90, 30, 31);
		
		Light aux5 = new Light(68, 68, 90, 30, 1);
		Light aux6 = new Light(32, 68, 90, 30, 1);
		Light aux7 = new Light(32, 32, 90, 30, 1);
		Light aux8 = new Light(68, 32, 90, 30, 1); 
		/*
		if (numLights > 5 && numLights <= 9) {
			for (int i = 1; i < numLights; i++) {
				if (i < 5) {
					double angle = (i-1) * 90;
					double distance = 18;
					Light light = vectorLightBuilder(center, angle, distance, 90, 30, 31);
					ret.add(light);
					log.trace("Positioned aux light at (" + light.getX() + ", "
							+ light.getY() + ")");
				}
				else if (i >= 5) {
					double angle = ((i-4) * 90) - 45;
					double distance = 18*Math.sqrt(2);
					Light light = vectorLightBuilder(center, angle, distance, 90, 30, 1);
					ret.add(light);
					log.trace("Positioned aux light at (" + light.getX() + ", "
							+ light.getY() + ")");
				}
			}
		}
		*/
		
		ret.add(aux1);
		ret.add(aux2);
		ret.add(aux3);
		ret.add(aux4);
		
		ret.add(aux5);
		ret.add(aux6);
		ret.add(aux7);
		ret.add(aux8);
		
		return ret;
	}

	@Override
	public Collector getCollector() {
		Collector c = new Collector(51, 49);
		log.debug("Positioned Collector at (" + (51) + ", " + (49) + ")");
		return c;
	}
	
	// Constructs a Light at a particular angle and distance away from a specified central light
	// Angle is in degrees (and gets converted to radians)
	// 0 degrees is directly to the right, and increases clockwise
	private Light vectorLightBuilder(Light centralLight, double angle, double distance,
			double d, double s, double t) {
		double radians = angle*Math.PI/180;
		double x = centralLight.getX() + distance*Math.cos(radians);
		double y = centralLight.getY() + distance*Math.sin(radians);
		
		return new Light(x, y, d, s, t);
	}
	
}
