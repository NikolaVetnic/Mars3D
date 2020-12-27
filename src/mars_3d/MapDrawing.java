package mars_3d;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;

public class MapDrawing {
	
	// level 		- value in 2d array read from image
	// elevation 	- variable set through gadget
	// height 		- final terrain height calculated using above
	
	Image image;
	
	
	Map map;
	
	
	private double zoom = 10.0;
	private double tilt = 2.75;
	private double turn = 0.125;
	private double rise = 5.0;
	private double elev = 0.0;
	
	
	private Palette schm;
	
	
	Vector3d lght = new Vector3d(0, 0, 100);
	
	
	boolean usePhotoBrightness = true;			// mixes the original photo with the generated palette
	boolean useTris = true;						// changes view mode between triangles and raised tiles

	
	private Vector MAP_POS = new Vector(150);
	
	
	// =-=-=
	
	
	public MapDrawing() {
		return;
	}
	
	
	public MapDrawing(Image 	image, 
					  Vector 	mapPos, 
					  double 	zoom, 
					  double 	tilt, 
					  double 	turn, 
					  double 	rise, 
					  double 	elev, 
					  int 		pInd,
					  Vector3d 	lght) {
		this.image 		= image;
		this.MAP_POS 	= mapPos;
		this.zoom 		= zoom;
		this.tilt 		= tilt;
		this.turn 		= turn;
		this.rise 		= rise;
		this.schm 		= new Palette(pInd);
		this.elev 		= elev - 0.25;
		this.lght 		= lght;
	}
	
	
	// =-=-=
	
	
	private double heightAt(Map map, int x, int y) {
		
		double hDry = map.levelAvg(x, y) + elev;
		
		if (schm.arid) return hDry;
		else return hDry < 0.0 ? 0.0 : hDry;
	}	// calculates height at position (x, y) with water considered
	
	
	public Color colorAt(Map map, int x, int y) {
		
		double hghtsAvg = (map.levelAvg(x, y) + 1) * 0.5 + elev;
		
		int i = hghtsAvg > schm.lyrs[4] ? 5 :
				hghtsAvg > schm.lyrs[3] ? 4 :
				hghtsAvg > schm.lyrs[2] ? 3 :
				hghtsAvg > schm.lyrs[1] ? 2 :
				hghtsAvg > schm.lyrs[0] ? 1 : 0;
		
		int j = i == 0 ? 0 : i - 1;
		
		Color c0 = j == 5 ? schm.landAtIndex(5) : schm.landAtIndex(j);
		Color c1 = i == 5 ? schm.landAtIndex(5) : schm.landAtIndex(i);
		
		double k = (hghtsAvg - schm.lyrs[j]) / (schm.lyrs[i] - schm.lyrs[j]);
		
		return c0.interpolate(c1, k);
	}
	
	
	public Color waterAt(Map map, int x, int y) {
		
		double hghtsAvg = (map.levelAvg(x, y) + 1) * 0.5 + elev;
		
		int i = hghtsAvg > schm.lyrs[0] ? 1 : 0;
		int j = i == 0 ? 0 : i - 1;
		
		Color c0 = j == 5 ? schm.aquaAtIndex(5) : schm.aquaAtIndex(j);
		Color c1 = i == 5 ? schm.aquaAtIndex(5) : schm.aquaAtIndex(i);
		
		double k = (hghtsAvg - schm.lyrs[j]) / (schm.lyrs[i] - schm.lyrs[j]);
		
		return c0.interpolate(c1, k);
	}
	
	
	public double[] lightAt(Map map, int x, int y) {
		
		double h0 = 		  heightAt(map, x  , y  );
		double h1 = useTris ? heightAt(map, x+1, y  ) : heightAt(map, x  , y  );
		double h2 = useTris ? heightAt(map, x  , y+1) : heightAt(map, x  , y  );
		double h3 = useTris ? heightAt(map, x+1, y+1) : heightAt(map, x  , y  );

		Vector3d[] triSides = new Vector3d[] {
				(new Vector3d(x+1, y  , h1)).sub(new Vector3d(x  , y  , h0)),
				(new Vector3d(x  , y+1, h2)).sub(new Vector3d(x  , y  , h0)),
				(new Vector3d(x+1, y  , h1)).sub(new Vector3d(x  , y+1, h2)),
				(new Vector3d(x+1, y+1, h3)).sub(new Vector3d(x  , y+1, h2))
		};
		
		double light1 = (triSides[0].cross(triSides[1]).norm().dot(lght) + 100.0) / 200.0;
			   light1 = light1 > 0.9 ? 0.9 :
				   		light1 < 0.1 ? 0.1 : light1;
		
		double light2 = (triSides[2].cross(triSides[3]).norm().dot(lght) + 100.0) / 200.0;
			   light2 = light2 > 0.9 ? 0.9 : 
						light2 < 0.1 ? 0.1 : light2;
			   
		return new double[] { light1, light2 };
	}
	
	
	private void fillMapPoly(View view, Map map, int x, int y, boolean withFill) {
		
		double h0 = 		  heightAt(map, x  , y  );
		double h1 = useTris ? heightAt(map, x+1, y  ) : heightAt(map, x  , y  );
		double h2 = useTris ? heightAt(map, x  , y+1) : heightAt(map, x  , y  );
		double h3 = useTris ? heightAt(map, x+1, y+1) : heightAt(map, x  , y  );
		
		double[] l = lightAt(map, x, y);
		
		Color c = usePhotoBrightness ? 
					Palette.multiply(Color.gray(map.brightnessAt(x, y)), colorAt(map, x, y)) : 
					colorAt(map, x, y);
					
		Color w = usePhotoBrightness ? 
					Palette.multiply(Color.gray(map.brightnessAt(x, y)), waterAt(map, x, y)) : 
					waterAt(map, x, y);
		
		Vector[] verts = {
			(new Vector3d(x  , y  , h0)).plot3d(view, zoom, tilt, turn, rise),
			(new Vector3d(x+1, y  , h1)).plot3d(view, zoom, tilt, turn, rise),
			(new Vector3d(x  , y+1, h2)).plot3d(view, zoom, tilt, turn, rise),
			(new Vector3d(x+1, y+1, h3)).plot3d(view, zoom, tilt, turn, rise)
		};

		if (heightAt(map, x  , y  ) != 0 ||
			heightAt(map, x+1, y  ) != 0 ||
			heightAt(map, x  , y+1) != 0 ||
			heightAt(map, x+1, y+1) != 0) {
				
				// drawing land tiles
				view.setStroke(Color.hsb(c.getHue(), c.getSaturation(), c.getBrightness() * l[0]));
				view.strokePolygon (verts[0], verts[1], verts[2]);
				
				view.setStroke(Color.hsb(c.getHue(), c.getSaturation(), c.getBrightness() * l[1]));
				view.strokePolygon (verts[3], verts[1], verts[2]);
				
			if (withFill) {
				
				view.setFill(Color.hsb(c.getHue(), c.getSaturation(), c.getBrightness() * l[0]));
				view.fillPolygon (verts[0], verts[1], verts[2]);
				
				view.setFill(Color.hsb(c.getHue(), c.getSaturation(), c.getBrightness() * l[1]));
				view.fillPolygon (verts[3], verts[1], verts[2]);
			}
		} else {
			
				// drawing sea tiles
				view.setStroke(w);
				view.strokePolygon	(verts[0], verts[1], verts[2]);
				view.strokePolygon	(verts[3], verts[1], verts[2]);
			
			if (withFill) {
				
				view.setFill(w);
				view.fillPolygon	(verts[0], verts[1], verts[2]);
				view.fillPolygon	(verts[3], verts[1], verts[2]);
			}
		}
	}
	
	
	public void drawMapTris(View view, Map map, boolean fill) {
		
		Vector str = new Vector(1 - map.sizeX() / 2, 1 - map.sizeY() / 2);
		Vector end = new Vector(map.sizeX() / 2 - 1, map.sizeY() / 2 - 1);
		
		if ((0.0 <= turn && turn <= 0.25) || (0.75 <= turn && turn <= 1.00)) {
			for (int y = (int) str.y; y <= (int) end.y - (int) (map.sizeY() / MAP_POS.y); y++)
				for (int x = (int) str.x; x <= (int) end.x - (int) (map.sizeX() / MAP_POS.x); x++)
					fillMapPoly(view, map, x, y, fill);
		} else {
			for (int y = (int) end.y - (int) (map.sizeY() / MAP_POS.y); y >= (int) str.y; y--)
				for (int x = (int) end.x - (int) (map.sizeX() / MAP_POS.x); x >= (int) str.x; x--)
					fillMapPoly(view, map, x, y, fill);
		}
	}	// renders map as wireframe or solid polygons	
}