package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.ITile;
import java.awt.image.BufferedImage;

public class Tile implements ITile<BufferedImage>
{
    private final BufferedImage image;

    public Tile(BufferedImage image)
    {
        this.image = image;
    }

    @Override
    public BufferedImage get()
    {
        return image;
    }
    
}
