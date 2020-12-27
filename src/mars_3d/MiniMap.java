package mars_3d;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Transformation;
import mars.geometry.Vector;

public class MiniMap {
	
	Vector mapDim;
	Vector mapPos;
	Vector boxDim;
	Vector boxPos;

	Image image;
	
	
	// =-=-=
	
	
	public MiniMap(Image image, Vector mapDim, Vector mapPos, double boxDim, Vector boxPos) {
		this.image = image;
		this.mapDim = mapDim;
		this.mapPos = mapPos;
		this.boxDim = new Vector(boxDim / 2);
		this.boxPos = boxPos;
	}
	
	
	public MiniMap(Image image, Vector boxPos) {
		this.image = image;
		this.mapDim = new Vector(150);
		this.mapPos = new Vector(450, 100);
		this.boxDim = new Vector(0.25);
		this.boxPos = boxPos;
	}
	
	
	private Vector clampedBoxCenter() {
		Vector c = mapDim.div(2).sub(mapDim.mul(boxDim));
		return boxPos.sub(mapPos).clampTo(c.inverse(), c.mul(2));
	}	// restricts scroll box to minimap area
	
	
	private Vector size(Image image) {
		return new Vector(image.getWidth(), image.getHeight());
	}	// image size as vector
	
	
	private Image flipV() {
		
		WritableImage flip = new WritableImage((int) image.getWidth(), (int) image.getHeight());
		PixelWriter pw = flip.getPixelWriter();
		PixelReader pr = image.getPixelReader();
		
		for (int y = 0; y < (int) image.getHeight(); y++) {
			for (int x = 0; x < (int) image.getWidth(); x++) {
				pw.setColor(x, (int) image.getHeight() - y - 1, pr.getColor(x, y));
			}
		}
		return flip;
	}	
	
	
	private void drawImage(View view) {
		Transformation t = new Transformation().scale(mapDim.div(size(image)));
		view.stateStore();
		view.addTransformation(t);
		view.drawImageCentered(Vector.ZERO, flipV());
		view.stateRestore();
	}	// draws image and scales it to desired size
	
	
	private void drawScrollBox(View view) {
		view.setStroke(Color.GREENYELLOW);
		view.setLineWidth(2);
		view.strokeRectCentered(clampedBoxCenter(), mapDim.mul(boxDim));
	}	// draws clamped scrollbox
	
	
	private void drawBoundingBox(View view) {
		view.setStroke(Color.INDIANRED);
		view.setLineWidth(2);
		view.strokeRectCentered(Vector.ZERO, mapDim.div(2));
	}	// draws minimap bounding box
	
	
	public void draw(View view) {

		Transformation t = new Transformation().translate(mapPos);
		
		view.stateStore();
		
		view.addTransformation(t);
		
		drawImage(view);
		drawScrollBox(view);
		drawBoundingBox(view);
		
		view.stateRestore();
	}
	
	
	public Map inFocus() {
		
		Vector str = clampedBoxCenter().sub(((new Vector(image.getWidth(), image.getHeight())).mul(boxDim)));
		Vector end = clampedBoxCenter().add(((new Vector(image.getWidth(), image.getHeight())).mul(boxDim)));
		
		PixelReader pr = image.getPixelReader();
		
		double[][] inFocus = new double[(int) Math.abs(end.x - str.x) + 1][(int) Math.abs(end.y - str.y) + 1];
		
		for (int y = (int) str.y; y < (int) end.y; y++) {
			for (int x = (int) str.x; x < (int) end.x; x++) {
				int cx = str.x < 0 ? x + (int) Math.abs(str.x) : x - (int) Math.abs(str.x);
				int cy = str.y < 0 ? y + (int) Math.abs(str.y) : y - (int) Math.abs(str.y);
				
				inFocus[cx][cy] = pr.getColor(x + (int) image.getWidth() / 2, y + (int) image.getHeight() / 2).getBrightness();
			}
		}
		return new Map(inFocus);
	}	// returns map of the area in focus of minimap scroll box
	
}
