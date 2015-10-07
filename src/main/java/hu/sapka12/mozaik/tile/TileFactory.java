package hu.sapka12.mozaik.tile;

import hu.sapka12.mozaik.maker.bufferedimage.Tile;
import java.awt.image.BufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TileFactory {

    private final Logger LOGGER = LoggerFactory.getLogger(TileFactory.class);
    
    public Tiles getTiles(BufferedImage image, int tileSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        Assert.isTrue(width % tileSize == 0);
        Assert.isTrue(height % tileSize == 0);

        Tile[][] tileArray = new Tile[height / tileSize][width / tileSize];

        for (int x = 0; x < tileArray.length; x++) {
            Tile[] row = tileArray[x];
            for (int y = 0; y < row.length; y++) {
                int subImageX = y * tileSize;
                int subImageY = x * tileSize;
                
                LOGGER.trace("SUB[{}][{}]", subImageX, subImageY);
                
                row[y] = new Tile(image.getSubimage(subImageX, subImageY, tileSize, tileSize));
            }
        }
        
        Tiles tiles = new Tiles(tileArray);

        return tiles;
    }

}
