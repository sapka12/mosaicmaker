package hu.sapka12.mozaik;

import hu.sapka12.mozaik.tile.TileFactory;
import hu.sapka12.mozaik.tile.Tiles;
import java.awt.image.BufferedImage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TileFactoryTest {

    @Test
    public void testImageSize() {
        BufferedImage image = new BufferedImage(14 * 15, 10 * 15, BufferedImage.TYPE_INT_RGB);
        int tileSize = 15;
        TileFactory tileFactory = new TileFactory();
        Tiles tiles = tileFactory.getTiles(image, tileSize);

        Assert.assertEquals(tiles.numberOfRows(), 10);
        Assert.assertEquals(tiles.numberOfColumns(), 14);
    }   
    
}
