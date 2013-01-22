package mosquito.g0;

/*
 * This is a very simple Player that just places the Lights
 * randomly and then places the Collector next to the last Light
 */

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class RandomPlayer extends Player {
	private int numLights;
	private Point2D lastLight = null; // to keep track of the last light created
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
		// this player doesn't use the walls
		this.numLights = numLights;
	}

	
	/*
	 * This is called after startNewGame. If your Set contains more
	 * than the maximum allowed number of Lights, an error will occur.
	 */
	@Override
	public Set<Light> getLights() {
		HashSet<Light> ret = new HashSet<Light>();
		Random r = new Random();
		for(int i = 0; i< numLights; i++)
		{
			// this player just picks random points for the Light
			lastLight = new Point2D.Double(r.nextInt(100), r.nextInt(100));
			
			/*
			 * The arguments to the Light constructor are: 
			 * - X coordinate
			 * - Y coordinate
			 * - blinking interval
			 * - how long it stays on for in each interval
			 * - time at which to turn on
			 */
			Light l = new Light(lastLight.getX(), lastLight.getY(), 10, r.nextInt(10), 1);

			log.trace("Positioned a light at (" + lastLight.getX() + ", " + lastLight.getY() + ")");
			ret.add(l);
		}
		return ret;
	}

	/*
	 * This is called after getLights.
	 */
	@Override
	public Collector getCollector() {
		// place the Collector just to the right of and below the last Light
		Collector c = new Collector(lastLight.getX()+1,lastLight.getY() +1);
		log.debug("Positioned Collector at (" + (lastLight.getX()+1) + ", " + (lastLight.getY()+1) + ")");
		return c;
	}


}
