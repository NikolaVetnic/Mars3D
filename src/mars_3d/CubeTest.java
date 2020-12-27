package mars_3d;

import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.geometry.Vector;

public class CubeTest implements Drawing {

	
	@GadgetDouble(min = 10.0, max = 300.0)
	double s = 1000.0;
	
	
	Vector3d[] verts3d = {
			new Vector3d(-30.0, -20.0, -30.0),
			new Vector3d(-30.0, -20.0, +30.0),
			new Vector3d(+30.0, -20.0, -30.0),
			new Vector3d(+30.0, -20.0, +30.0),
			
			new Vector3d(-30.0, +20.0, -30.0),
			new Vector3d(-30.0, +20.0, +30.0),
			new Vector3d(+30.0, +20.0, -30.0),
			new Vector3d(+30.0, +20.0, +30.0),
	};
	
	
	Vector[] verts2d = new Vector[verts3d.length];
	
	@GadgetDouble(min = -200.0, max = 200.0)
	double f = 3.0;
	
	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.GRAY);
		

		double g = 1.0;
		
		for (int i = 0; i < verts2d.length; i++)
			verts2d[i] = new Vector(verts3d[i].getX() / (f - verts3d[i].getZ()) * s, verts3d[i].getY() / (f - verts3d[i].getZ() * s));
		
		view.setStroke(Color.GREENYELLOW);
		view.strokePolygon(verts2d[0], verts2d[1], verts2d[2], verts2d[3]);
		view.strokePolygon(verts2d[2], verts2d[3], verts2d[6], verts2d[7]);
		
	}
	
	public static void main(String[] args) {
		DrawingApplication.launch(600, 400);
	}

}
