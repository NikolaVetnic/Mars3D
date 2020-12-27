package mars_3d;

import javafx.scene.paint.Color;

public class Palette {
	
	int pIndex;
	
	
	boolean arid;
	
	Color[] land;
	Color[] aqua;
	double[] lyrs;
	
	
	// =-=-=
	
	
	int[][] presets = new int[][] {
		{ 0,  0},
		{ 1,  1},
		{ 2, -1},
		{ 3, -1}
	};
	
	
	public Color[][] aquaPalletes = {
			{
				Color.TRANSPARENT,
				Color.TRANSPARENT,
			},
			
			{
//				Color.TRANSPARENT,
				Color.hsb(180, 0.7, 0.5, 0.7),	// duboka voda
				Color.hsb(210, 0.7, 0.9, 0.5)	// voda
			},
			
			{
//				Color.TRANSPARENT,
				Color.hsb(155, 0.50, 0.55),		// duboka voda
				Color.hsb(155, 0.50, 0.60),		// voda
			},
	};
	
	
	public Color[][] landPalletes = {
			{
//				Color.TRANSPARENT,
				Color.hsb( 20, 0.1, 0.1),		// duboko dno
				Color.hsb( 30, 0.5, 0.5),		// plitko dno
				Color.hsb( 50, 0.5, 1.0),		// pesak
				Color.hsb(110, 0.6, 0.8),		// trava
				Color.hsb( 30, 0.6, 0.5),		// planina
				Color.hsb(  0, 0.0, 1.0)		// sneg
			},
			
			{
//				Color.TRANSPARENT,
				Color.hsb( 30, 0.45, 0.65),		// duboko dno
				Color.hsb( 30, 0.45, 0.50),		// plitko dno
				Color.hsb( 30, 0.45, 0.30),		// blatnjava obala
				Color.hsb(210, 0.30, 0.90),		// ravnica
				Color.hsb(210, 0.15, 0.95),		// bregovi
				Color.hsb(210, 0.05, 0.95)		// planine
			},
			
			{
//				Color.TRANSPARENT,
				Color.hsb( 47, 0.06, 0.91),		// svetli erg
				Color.hsb( 35, 0.24, 0.92),		// tamniji erg
				Color.hsb( 26, 0.41, 0.94),		// niske stene
				Color.hsb( 21, 0.44, 0.86),		// visoke stene
				Color.hsb( 19, 0.49, 0.78),		// visoravan
				Color.hsb(  0, 0.05, 0.46)		// vrhovi
			},
			
			{
//				Color.TRANSPARENT,
				Color.hsb( 16, 0.30, 0.76),		// 
				Color.hsb( 18, 0.39, 0.67),		// 
				Color.hsb( 19, 0.49, 0.61),		// 
				Color.hsb( 18, 0.48, 0.48),		// 
				Color.hsb( 15, 0.52, 0.38),		// 
				Color.hsb( 16, 0.46, 0.22)		// 
			},
	};
	
	
	public double[][] layers = {
			{
				0.25, 							// duboko dno - plitko dno
				0.50, 							// plitko dno - pesak
				0.55, 							// pesak - trava
				0.70, 							// trava - planina
				0.90,							// planina - sneg
				Double.POSITIVE_INFINITY
			},
			
			{
				0.25, 							// duboko dno - plitko dno
				0.50, 							// plitko dno - blatnjava obala
				0.55, 							// blatnjava obala - ravnica
				0.70, 							// ravnica - bregovi
				0.90,							// bregovi - planine
				Double.POSITIVE_INFINITY
			},
			
			{
				0.25, 							// svetliji erg - tamniji erg
				0.50, 							// tamniji erg - niske stene
				0.55, 							// niske stene - visoke stene
				0.70, 							// visoke stene - visoravan
				0.90,							// visoravan - vrhovi
				Double.POSITIVE_INFINITY
			},
			
			{
				0.25, 							// sneg - planina
				0.50, 							// planina - trava
				0.55, 							// trava - pesak
				0.70, 							// pesak - plitko dno
				0.90,							// plitko dno - duboko dno
				Double.POSITIVE_INFINITY
			},
	};
	

	// =-=-=
	
	
	public Palette(int preset) {
		this.pIndex = presets[preset][0];
		this.land 	= landPalletes[presets[preset][0]];
		this.aqua	= aquaPalletes[presets[preset][1] + 1];
		this.arid	= presets[preset][1] == -1;
		this.lyrs	= layers[presets[preset][0]];
	}
	
	
	public int index() {
		return pIndex;
	}
	
	
	public Color landAtIndex(int index) {
		return land[index];
	}
	
	
	public Color aquaAtIndex(int index) {
		return aqua[index];
	}


	public static Color multiply(Color c1, Color c2) {
		
		Color c;
		
		if (c1.getBrightness() < 0.5) {
			c = new Color(
					2 * c1.getRed() 	* c2.getRed(),
					2 * c1.getGreen() 	* c2.getGreen(),
					2 * c1.getBlue()	* c2.getBlue(),
					1.0);
		} else {
			c = new Color(
					1 - 2 * (1 - c1.getRed()) 	* (1 - c2.getRed()),
					1 - 2 * (1 - c1.getGreen()) * (1 - c2.getGreen()),
					1 - 2 * (1 - c1.getBlue()) 	* (1 - c2.getBlue()),
					1.0);		
		}
		
		return c;
	}	// multiply blend mode
}
