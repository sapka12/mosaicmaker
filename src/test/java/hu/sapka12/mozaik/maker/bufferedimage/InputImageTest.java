package hu.sapka12.mozaik.maker.bufferedimage;

import hu.sapka12.mozaik.maker.ITile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import org.junit.Assert;
import org.junit.Test;

public class InputImageTest
{

    public InputImageTest()
    {
    }

//    @Test
    public void testInputNumberOfTiles()
    {
        final int expectedTilesInARow = 100;
        final int expectedTilesInAColumn = 75;

        InputImage inputImage = new InputImage();

        Assert.assertEquals(expectedTilesInARow, inputImage.getTilesInARow());
        Assert.assertEquals(expectedTilesInAColumn, inputImage.getTilesInColumn());
    }

//    @Test
    public void testTilePosition()
    {
        final int expectedTilesInARow = 30;
        final int expectedTilesInAColumn = 20;

        int tileSizeInPx = 10;
        int width = expectedTilesInARow * tileSizeInPx;
        int height = expectedTilesInAColumn * tileSizeInPx;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        InputImage inputImage = new InputImage();

        final int row = 3;
        final int column = 5;

        paintSubImage(image, tileSizeInPx, row, column, Color.YELLOW);

        ITile<BufferedImage> tile = inputImage.getTile(row, column);

        for (int actualRowInTile = 0; actualRowInTile < tileSizeInPx; actualRowInTile++)
        {
            for (int actualColumnInTile = 0; actualColumnInTile < tileSizeInPx; actualColumnInTile++)
            {
                int rgbInTile = tile.get().getRGB(
                        actualColumnInTile, 
                        actualRowInTile);
                
                int rgbInImage = image.getRGB(
                        column * tileSizeInPx + actualColumnInTile, 
                        row * tileSizeInPx + actualRowInTile);
                
                Assert.assertEquals(rgbInImage, rgbInTile);
            }
        }
    }

    private void paintSubImage(BufferedImage image, int tileSizeInPx, int tileRow, int tileColumn, Color color)
    {
        int baseRow = tileRow * tileSizeInPx;
        int baseColumn = tileColumn * tileSizeInPx;

        for (int i = 0; i < tileSizeInPx; i++)
        {
            for (int j = 0; j < tileSizeInPx; j++)
            {
                image.setRGB(baseColumn + i, baseRow + j, color.getRGB());
            }
        }
    }
}
