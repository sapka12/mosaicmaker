package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import hu.sapka12.mozaik.tile.TileFactory;
import java.awt.image.BufferedImage;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MozaikMakerTest {

    private MozaikMaker maker;
    private BufferedImage input;
    private final int tileSize = 10;
    private BufferedImage output;

    public MozaikMakerTest() {
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void makerSmallInput() {
        givenMaker();
        givenInputWithSize(7, 23);
        whenCallMaker();
    }

    @Test
    public void makerInput() {
        givenMaker();
        givenInputWithSize(202, 155);
        whenCallMaker();
        assertOutputSize(200, 150);
    }

    private void givenMaker() 
    {    
        ITileFinderStrategy<BufferedImage> finderStrategy = EasyMock.createMock(ITileFinderStrategy.class);   
        TileFactory tileFactory = EasyMock.createMock(TileFactory.class);
        maker = new MozaikMaker(finderStrategy, tileFactory);
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

}
