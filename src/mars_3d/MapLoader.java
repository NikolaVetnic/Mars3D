package mars_3d;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class MapLoader {
	
	
	Image image;
	
	
	public MapLoader(Image image) {
		this.image = image;
	}

	
	public Map load() {
		
		double[][] levels = new double[(int) image.getWidth()][(int) image.getHeight()];
		
		PixelReader pr = image.getPixelReader();
		
		for (int y = 0; y < levels[0].length; y++) {
			for (int x = 0; x < levels.length; x++) {
				levels[x][(int) image.getHeight() - y - 1] = pr.getColor(x, y).getBrightness();
			}
		}
		return new Map(levels);
	}
}
