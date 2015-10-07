package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.bufferedimage.Tile;
import hu.sapka12.mozaik.tile.Tiles;
import java.awt.image.BufferedImage;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TilesTest {

    @Test(enabled = false)
    public void testbuildImage() throws CloneNotSupportedException {

        BufferedImage input = new BufferedImage(10, 21, BufferedImage.TYPE_INT_RGB);

        Tile tile = EasyMock.createMock(Tile.class);
        EasyMock.expect(tile.get()).andStubReturn(input);

        Tile[][] tileArray = new Tile[][]{
            new Tile[]{tile}
        };

        Tiles tiles = new Tiles(tileArray);
        BufferedImage outImage = tiles.buildImage();

        assertEquals(outImage, input);
    }

    private boolean assertEquals(BufferedImage actual, BufferedImage expected) {

        if (actual.getWidth() != expected.getWidth()) {
            return false;
        }

        if (actual.getHeight() != expected.getHeight()) {
            return false;
        }

        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                if (expected.getRGB(x, y) != actual.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

}
