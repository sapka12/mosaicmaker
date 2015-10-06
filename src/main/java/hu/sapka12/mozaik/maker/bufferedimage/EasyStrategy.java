package hu.sapka12.mozaik.maker.bufferedimage;

import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import java.awt.image.BufferedImage;
import hu.sapka12.mozaik.maker.ITile;
import java.awt.Color;
import org.springframework.stereotype.Component;

@Component
public class EasyStrategy implements ITileFinderStrategy<BufferedImage>
{
    private final int tileSize;

    public EasyStrategy()
    {
        this.tileSize = 64;
    }

    @Override
    public ITile<BufferedImage> findSubstitute(ITile<BufferedImage> tile)
    {
        BufferedImage input = tile.get();

        int rgb = getAverageColor(input);

        return new Tile(getTileWithColor(rgb));
    }

    private int getAverageColor(BufferedImage input)
    {
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;
        for (int i = 0; i < input.getWidth(); i++)
        {
            for (int j = 0; j < input.getHeight(); j++)
            {
                int rgb = input.getRGB(i, j);
                Color c = new Color(rgb);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        
        
        return new Color(
                new Double(r/input.getWidth()/input.getHeight()).intValue(), 
                new Double(g/input.getWidth()/input.getHeight()).intValue(), 
                new Double(b/input.getWidth()/input.getHeight()).intValue())
                .getRGB();
    }

    private BufferedImage getTileWithColor(int rgb)
    {
        BufferedImage output = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < output.getWidth(); i++)
        {
            for (int j = 0; j < output.getHeight(); j++)
            {
                output.setRGB(i, j, rgb);
            }
        }
        return output;
    }

}
