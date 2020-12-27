package mars_3d;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.application.Options;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetDouble;
import mars.drawingx.gadgets.annotations.GadgetInteger;
import mars.drawingx.gadgets.annotations.GadgetVector;
import mars.geometry.Vector;

public class Mars3D implements Drawing {
	
	
	static final Vector APP_DIM = new Vector(1160, 870);
	static final Vector MAP_DIM = new Vector(150);
	static final Vector MAP_POS  = new Vector(450, 100);
	
	
	Image image;
	
	
	@GadgetDouble(min = 1.0, max = 20.0)
	double zoom = 10.0;
	
	@GadgetDouble(min = 0.0001, max = 5.0)
	double tilt = 2.5;
	
	@GadgetDouble(min = -150.0, max = 150.0)
	double rise = 100.0;
	
	@GadgetDouble(min = 0.0, max = 1.00)
	double turn = 0.875;
	
	@GadgetDouble(min = -0.50, max = 0.25)
	double elev = -0.05; 
	
	@GadgetInteger(min = 0, max = 3)
	int pInd = 3;
	
	
//	@GadgetDouble(min = 0.05, max = 2.0)
	double z = 1.0;
	
	
//	@GadgetVector
	Vector c = new Vector(1, 1);
	
	
	@GadgetVector
	Vector boxPos = MAP_POS;
	
	@GadgetVector
	Vector lght = Vector.ZERO;
	
//	@GadgetDouble(min = 0.001, max = 1.0)
	double boxDim = 0.75;
	
	
	@GadgetInteger(min = 0, max = 4)
	int file = 0;
	
	
	String[] fileNames = {
			"ares-vallis.jpg",
			"ares-vallis-wide.jpg",
			"deuteronilus-mensae.jpg",
			"mellas-candor-chasma.jpg",
			"noctis-labyrinthus.jpg",
	};
	
	
	// =-=-=
	
	
	@Override
	public void draw(View view) {
		
		DrawingUtils.clear(view, Color.gray(0.125));
		
		image = new Image("images/" + fileNames[file]);
		
		MiniMap miniMap = new MiniMap(
								image, MAP_DIM, MAP_POS, (double) ((int) (boxDim * 100.0) / 100.0), boxPos);
		
		MapDrawing mapDrawing = new MapDrawing(
								image, MAP_DIM, 
								zoom, tilt, turn, rise, elev, pInd,  
								new Vector3d(lght, 50));
		mapDrawing.drawMapTris(view, miniMap.inFocus(), true);
		
		miniMap.draw(view);
	}

	
	public static void main(String[] args) {
		
		Options options = Options.redrawOnEvents();
		options.drawingSize = APP_DIM;
		DrawingApplication.launch(options);
	}
	
}
