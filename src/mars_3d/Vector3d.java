package mars_3d;

import mars.drawingx.drawing.View;
import mars.geometry.Transformation;
import mars.geometry.Vector;

public class Vector3d {
	
	private double x;
	private double y;
	private double z;
	
	
	// =-=-=
	
	
	public Vector3d(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	
	public Vector3d(Vector p, double z) {
		this.setX(p.x);
		this.setY(p.y);
		this.setZ(z);
	}
	
	
	public Vector3d(Vector p) {
		this.setX(p.x);
		this.setY(p.y);
		this.setZ(0);
	}

	
	public Vector3d(double x, double y) {
		this.setX(x);
		this.setY(y);
		this.setZ(0);
	}
	
	
	public Vector3d() {
		this.setX(0);
		this.setY(0);
		this.setZ(0);
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public double getZ() {
		return z;
	}


	public void setZ(double z) {
		this.z = z;
	}

	
	// =-=-=
	
	
	public Vector3d neg() {
		return new Vector3d(-x, -y, -z);
	}
	
	
	public Vector3d add(Vector3d addend) {
		return new Vector3d(x + addend.x, y + addend.y, z + addend.z);
	}
	
	
	public Vector3d add(Vector addend) {
		return new Vector3d(x + addend.x, y + addend.y, z);
	}

	
	public Vector3d sub(Vector3d addend) {
		return new Vector3d(x - addend.x, y - addend.y, z - addend.z);
	}
	
	
	public Vector3d sub(Vector addend) {
		return new Vector3d(x - addend.x, y - addend.y, z);
	}
	
	
	public Vector3d mul(double a) {
		return new Vector3d(x * a, y * a, z * a);
	}
	
	
	public Vector3d cross(Vector3d multiplicand) {
		return new Vector3d(
				y * multiplicand.z - z * multiplicand.y,
				z * multiplicand.x - x * multiplicand.z,
				x * multiplicand.y - y * multiplicand.x
				);
	}
	
	
	public Vector3d cross(Vector multiplicand) {
		return this.cross(new Vector3d(multiplicand));
	}
	
	
	public Vector3d norm() {
		return new Vector3d(x / this.mag(), 
							y / this.mag(), 
							z / this.mag());
	}	// normalization
	
	
	public double dot(Vector3d multiplicand) {
		return x * multiplicand.x + 
			   y * multiplicand.y + 
			   z * multiplicand.z;
	}
	
	
	public double dot(Vector multiplicand) {
		return this.dot(new Vector3d(multiplicand));
	}
	
	
	public double mag() {
		return Math.sqrt(x * x + y * y + z * z);
	}	// magnitude
	
	
	public double dist(Vector3d q) {
		return Math.sqrt(
				Math.pow(x - q.x, 2) +
				Math.pow(y - q.y, 2) +
				Math.pow(z - q.z, 2));
	}
	
	
	// =-=-=
	
	
	private Vector[] tParam(double zoom, double tilt, double rise) {
		return new Vector[] {
				new Vector(x * zoom, y * zoom),
				new Vector(1.0, 1.0 / tilt),
				new Vector(0, z * rise)
		};
	}	// calculate transformation vector parameters
	
	
	private Vector[] tParam(double zoom, double tilt) {
		return new Vector[] {
				new Vector(x * zoom, y * zoom),
				new Vector(1.0, 1.0 / tilt)
		};
	}	// calculate transformation vector parameters, height disregarded
	
	
	public Vector plot3d(View view, double zoom, double tilt, double turn, double rise) {
		
		Vector[] tParam = tParam(zoom, tilt, rise);
		
		Transformation t = new Transformation()
				.translate	(tParam[0])
				.rotate		(turn)
				.scale		(tParam[1])
				.translate	(tParam[2])
				;
		
		return new Vector(x, y).transform(t);
	}	// plots 3d vector onto screen
	
	
	public Vector plot2d(View view, double zoom, double tilt, double turn) {
		
		Vector[] tParam = tParam(zoom, tilt);
		
		Transformation t = new Transformation()
				.translate	(tParam[0])
				.rotate		(turn)
				.scale		(tParam[1])
				;
		
		return new Vector(x, y).transform(t);
	}	// plots 3d vector onto screen disregarding height
	
	
	public String toString() {
		return "( " + x + ", " + y + ", " + z + " )";
	}
}
