package hu.sapka12.mozaik.maker.bufferedimage;

import java.awt.image.BufferedImage;
import org.springframework.util.Assert;
import hu.sapka12.mozaik.maker.ITile;
import hu.sapka12.mozaik.maker.IInputImage;

public class InputImage implements IInputImage<BufferedImage>
{
    private final BufferedImage input;
    private final int tileSizeInPx;

    public InputImage(BufferedImage input, int tileSizeInPx)
    {
        this.input = input;
        this.tileSizeInPx = tileSizeInPx;
    }

    @Override
    public int getTilesInARow()
    {
        Assert.isTrue(input.getWidth() % tileSizeInPx == 0);
        return input.getWidth() / tileSizeInPx;
    }

    @Override
    public int getTilesInColumn()
    {
        Assert.isTrue(input.getHeight() % tileSizeInPx == 0);
        return input.getHeight() / tileSizeInPx;
    }

    @Override
    public ITile<BufferedImage> getTile(int row, int column)
    {
        int x = tileSizeInPx * column;
        int y = tileSizeInPx * row;
        
        BufferedImage subImage = input.getSubimage(x, y, tileSizeInPx, tileSizeInPx);
        
        return new Tile(subImage);
    }    
}
