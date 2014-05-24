package sk.hackcraft.bwu;

import java.util.Random;

import jnibwapi.Map;
import jnibwapi.Position;

/**
 * Class representing two dimensional vector and operations defined on it.
 * This class is immutable.
 * 
 * @author nixone
 *
 */
public class Vector2D {
	/**
	 * Classic zero vector
	 */
	static final public Vector2D ZERO = new Vector2D(0, 0);
	
	/**
	 * Vector pointing to negative y.
	 */
	static final public Vector2D NORTH = new Vector2D(0, -1);
	
	/**
	 * Vector pointing to positive x.
	 */
	static final public Vector2D EAST = new Vector2D(1, 0);
	
	/**
	 * Vector pointing to positive y.
	 */
	static final public Vector2D SOUTH = new Vector2D(0, 1);
	
	/**
	 * Vector pointing to negative x.
	 */
	static final public Vector2D WEST = new Vector2D(-1, 0);
	
	static private Random random = new Random();
	
	/**
	 * Creates a vector with random distribution between (0, 0) and (1, 1)
	 * @return
	 */
	static public Vector2D random() {
		return new Vector2D(random.nextDouble(), random.nextDouble());
	}
	
	/**
	 * Calculates a dot product of two vectors.
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @return dot product
	 */
	static public double dotProduct(Vector2D vectorA, Vector2D vectorB) {
		return vectorA.x*vectorB.x+vectorA.y*vectorB.y;
	}
	
	/**
	 * Component.
	 */
	final public double x, y;
	
	/**
	 * Length of the vector, or the distance of components from the origin (0, 0)
	 */
	final public double length;
	
	public Vector2D(Position position) {
		this(position.getPX(), position.getPY());
	}
	
	/**
	 * Creates a vector.
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) {
		if(Double.isNaN(x) || Double.isNaN(y)) {
			throw new IllegalArgumentException("Cannot crate a NaN vector!");
		}
		
		this.x = x;
		this.y = y;
		
		double lng = Math.sqrt(x*x+y*y);	
		this.length = Double.isNaN(lng) ? 0 : lng;
	}
	
	/**
	 * Scales this vector by a specific aspect.
	 * 
	 * @param aspect
	 * @return
	 */
	public Vector2D scale(double aspect) {
		return scale(aspect, aspect);
	}
	
	/**
	 * Scales this vector by specific aspects in specific components. Be careful, this method does change the vector heading if the aspects
	 * are different.
	 * 
	 * @param aspectX
	 * @param aspectY
	 * @return new vector
	 */
	public Vector2D scale(double aspectX, double aspectY) {
		return new Vector2D(x*aspectX, y*aspectY);
	}
	
	/**
	 * Scale this vector by specific aspects in specific components. Be careful, this method does change the vector heading if the aspects
	 * are different.
	 * 
	 * @param aspect
	 * @return
	 */
	public Vector2D scale(Vector2D aspect) {
		return scale(aspect.x, aspect.y);
	}
	
	public Vector2D scale(Map map) {
		return scale(map.getWidth()*Game.TILE_SIZE, map.getHeight()*Game.TILE_SIZE);
	}
	
	/**
	 * Normalizes this vector.
	 * @return new vector
	 */
	public Vector2D normalize() {
		if(length == 0) {
			return this;
		}
		return new Vector2D(x/length, y/length);
	}
	
	/**
	 * Adds vector to this vector.
	 * @param v vector to be added
	 * @return new vector
	 */
	public Vector2D add(Vector2D v) {
		return new Vector2D(x+v.x, y+v.y);
	}
	
	/**
	 * Substracts a specified vector from this vector.
	 * @param v vector to be substracted
	 * @return new vector
	 */
	public Vector2D sub(Vector2D v) {
		return add(v.scale(-1));
	}
	
	/**
	 * Inverts this vector (scales it by aspect of -1)
	 * @return
	 */
	public Vector2D invert() {
		return scale(-1);
	}
	
	/**
	 * Returns two trivial orthogonal vectors to this vector.
	 * @return
	 */
	public Vector2D [] getOrthogonal() {
		return new Vector2D[]{
			new Vector2D(-y, x),
			new Vector2D(y, -x)
		};
	}
	
	/**
	 * Prints this vector.
	 */
	@Override
	public String toString() {
		return String.format("(%.2f; %.2f)", x, y);
	}
	
	/**
	 * Decides if these two vectors are the same.
	 * @param v
	 * @return
	 */
	public boolean sameAs(Vector2D v) {
		return v.x==x && v.y==y;
	}
	
	public Position toPosition() {
		return new Position((int)Math.round(x), (int)Math.round(y));
	}
}
