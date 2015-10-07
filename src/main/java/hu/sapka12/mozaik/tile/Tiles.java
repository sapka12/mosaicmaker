package hu.sapka12.mozaik.tile;

import hu.sapka12.mozaik.maker.bufferedimage.Tile;
import java.awt.image.BufferedImage;
import org.springframework.util.Assert;

public class Tiles
{

    private final Tile[][] tiles;
    private final int tileSize;

    public Tiles(Tile[][] tileArray)
    {
        Assert.notEmpty(tileArray);
        Assert.notEmpty(tileArray[0]);

        this.tiles = tileArray;
        this.tileSize = tileArray[0][0].get().getHeight();
    }

    public int numberOfRows()
    {
        return tiles.length;
    }

    public int numberOfColumns()
    {
        return tiles[0].length;
    }

    public Tile[][] getTiles()
    {
        return tiles;
    }
    
    public BufferedImage buildImage()
    {
        BufferedImage out = new BufferedImage(
                getWidth(),
                getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int columnIdx = 0; columnIdx < numberOfColumns(); columnIdx++)
        {
            for (int rowIdx = 0; rowIdx < numberOfRows(); rowIdx++)
            {
                setTile(out, rowIdx, columnIdx);
            }
        }

        return out;
    }

    private int getWidth()
    {
        return numberOfColumns() * tileSize;
    }

    private int getHeight()
    {
        return numberOfRows() * tileSize;
    }

    private void setTile(BufferedImage out, int rowIdx, int columnIdx)
    {
        Tile tile = tiles[rowIdx][columnIdx];
        BufferedImage tileImage = tile.get();
        
        int xOffset = columnIdx * tileSize;
        int yOffset = rowIdx * tileSize;
        
        for (int x = 0; x < tileSize; x++)
        {
            for (int y = 0; y < tileSize; y++)
            {
                out.setRGB(xOffset + x, yOffset + y, tileImage.getRGB(x, y));
            }
        }
    }
}
