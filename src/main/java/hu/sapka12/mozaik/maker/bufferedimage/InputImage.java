package hu.sapka12.mozaik.maker.bufferedimage;

import java.awt.image.BufferedImage;
import org.springframework.util.Assert;
import hu.sapka12.mozaik.maker.ITile;
import hu.sapka12.mozaik.maker.IInputImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class InputImage implements IInputImage<BufferedImage>
{
    private final BufferedImage input;
    private final int tileSizeInPx;

    public InputImage()
    {
        try {
            this.input = ImageIO.read(new File("input.jpg"));
            this.tileSizeInPx = 16;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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
