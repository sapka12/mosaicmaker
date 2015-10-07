package hu.sapka12.mozaik;

import hu.sapka12.mozaik.tile.TileChanger;
import hu.sapka12.mozaik.tile.TileFactory;
import hu.sapka12.mozaik.tile.Tiles;
import java.awt.image.BufferedImage;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MozaikMakerTest {

    private MozaikMaker maker;
    private BufferedImage input;
    private int tileSize;
    private BufferedImage output;
    private BufferedImage expactedOutput;
    private TileChanger tileChanger;

    public MozaikMakerTest() {
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void makerSmallInput() {
        givenMaker();
        givenTileSize(10);
        givenInputWithSize(7, 23);
        whenCallMaker();
    }

    @Test
    public void makerInput() {
        givenMaker();
        givenTileSize(10);
        givenInputWithSize(202, 155);
        whenChangeTiles();
        whenCallMaker();
        assertOutputSize(200, 150);
    }

    @Test
    public void maker() {
        givenMaker();
        givenTileSize(2);
        givenInputWithSize(2, 2);

        whenChangeTiles();
        
        whenCallMaker();
        
        Assert.assertEquals(output, expactedOutput);
    }

    private void givenMaker() {
        tileChanger = EasyMock.createMock(TileChanger.class);
        TileFactory tileFactory = EasyMock.createMock(TileFactory.class);
        
        maker = new MozaikMaker(tileFactory, tileChanger);
    }

    private void givenInputWithSize(int width, int height) {
        input = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private void whenCallMaker() {
        output = maker.make(input, tileSize);
    }

    private void assertOutputSize(int expectedWidth, int expectedHeight) {
        Assert.assertEquals(output.getWidth(), expectedWidth);
        Assert.assertEquals(output.getHeight(), expectedHeight);
    }

    private void givenTileSize(int size) {
        tileSize = size;
    }

    private void whenChangeTiles() {
        
        expactedOutput = new BufferedImage(200, 150, BufferedImage.TYPE_INT_RGB);
        
        Tiles tiles = EasyMock.createMock(Tiles.class);
        EasyMock.expect(tiles.buildImage()).andStubReturn(expactedOutput);
        
        EasyMock.expect(tileChanger.change(EasyMock.anyObject(Tiles.class))).andStubReturn(tiles);
        
        EasyMock.replay(tiles, tileChanger);
    }

}
