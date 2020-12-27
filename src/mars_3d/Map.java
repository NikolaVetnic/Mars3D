package mars_3d;

import mars.geometry.Vector;

public class Map {

	public final double[][] levels;
	private final Vector size;
	
	
	// =-=-=
	
	
	public Vector size() {
		return this.size;
	}
	
	
	public int sizeX() {
		return (int) size.x;
	}
	
	
	public int sizeY() {
		return (int) size.y;
	}
	
	
	public Map(double[][] levels) {
		int sizeY = levels.length;
		if (sizeY == 0) {
			throw new IllegalArgumentException("Map height cannot be 0.");
		}
		
		int sizeX = levels[0].length;
		for (int y = 0; y < sizeY; y++) {
			if (levels[y].length != sizeX) {
				throw new IllegalArgumentException("Map rows must have equal length.");
			}
		}
		
		this.levels = levels;
		this.size = new Vector(sizeX, sizeY);
	}
	
	
	private int xToIndex(double x) {
		return (int) (sizeX() / 2.0 - 1.0 + x);
	}
	
	
	private int yToIndex(double y) {
		return (int) (sizeY() / 2.0 - 1.0 + y);
	}
	
	
	public double levelAt(Vector p) {
		return levels[xToIndex((int) p.x)][yToIndex((int) p.y)];
	}
	
	
	public double levelAt(double x, double y) {
		return levels[xToIndex((int) x)][yToIndex((int) y)];
	}
	
	
	public double levelAvg(Vector p) {
		
		int xCurr = xToIndex((int) p.x);
		int xNext = xCurr >= sizeX() ? sizeX() : xCurr;
		
		int yCurr = yToIndex((int) p.y);
		int yNext = yCurr >= sizeY() ? sizeY() : yCurr;
		
		return (
				levels[xCurr][yCurr] + 
				levels[xCurr][yNext] +
				levels[xNext][yCurr] +
				levels[xNext][yNext]
			   ) / 4.0;
	}
	
	
	public double levelAvg(double x, double y) {
		
		int xCurr = xToIndex((int) x);
		int xNext = xCurr >= sizeX() ? sizeX() : xCurr;
		
		int yCurr = yToIndex((int) y);
		int yNext = yCurr >= sizeY() ? sizeY() : yCurr;
				
		
		return (
				levels[xCurr][yCurr] + 
				levels[xCurr][yNext] +
				levels[xNext][yCurr] +
				levels[xNext][yNext]
			   ) / 4.0;
	}
	
	
	public double brightnessAt(int x, int y) {
		
		double level = (levelAt(x, y) + 0.75) / 1.5;
		
		if (level < 0.05) level = 0.05;
		if (level > 0.95) level = 0.95;
		
		return Math.pow(level, 4);
	}	// calculates color at position (x, y) with water considered
}
