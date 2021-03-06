package hu.sapka12.mozaik.maker.bufferedimage;

import hu.sapka12.mozaik.maker.IMozaikBuilder;
import java.awt.image.BufferedImage;
import hu.sapka12.mozaik.maker.ITile;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MozaikBuilder implements IMozaikBuilder<BufferedImage>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MozaikBuilder.class);
    private final Map<Pair<Integer, Integer>, ITile<BufferedImage>> map = new HashMap<>();

    @Override
    public BufferedImage build()
    {
        int numberOfRows = 0;
        int numberOfCoulumns = 0;
        int tileSize = 0;

        for (Map.Entry<Pair<Integer, Integer>, ITile<BufferedImage>> entry : map.entrySet())
        {
            Pair<Integer, Integer> coord = entry.getKey();
            if (coord.getLeft() > numberOfRows)
            {
                numberOfRows = coord.getLeft();
            }
            if (coord.getRight() > numberOfCoulumns)
            {
                numberOfCoulumns = coord.getRight();
            }

            ITile<BufferedImage> tile = entry.getValue();

            if (tileSize == 0)
            {
                tileSize = tile.get().getHeight();
            }
        }

        LOGGER.info("-----------------------------------------");
        LOGGER.info("-----------------------------------------");
        LOGGER.info("numberOfRows: {}", numberOfRows);
        LOGGER.info("numberOfCoulumns: {}", numberOfCoulumns);
        LOGGER.info("tileSize: {}", tileSize);

        LOGGER.info("image: {}x{}", numberOfCoulumns * tileSize, numberOfRows * tileSize);

        BufferedImage out = new BufferedImage(numberOfCoulumns * tileSize, numberOfRows * tileSize, BufferedImage.TYPE_INT_RGB);
        map.entrySet().stream().forEach((entry) ->
        {
            Pair<Integer, Integer> coord = entry.getKey();
            ITile<BufferedImage> tile = entry.getValue();
            setSubImage(out, tile.get(), coord);
        });

        return out;
    }

    @Override
    public void append(ITile<BufferedImage> substituteTile, int row, int column)
    {
        Assert.isTrue(containsOnlySameSize(substituteTile));
        Pair<Integer, Integer> coord = new ImmutablePair(row, column);
        map.put(coord, substituteTile);
    }

    private boolean containsOnlySameSize(ITile<BufferedImage> substituteTile)
    {
        if (map.isEmpty())
        {
            return true;
        }

        BufferedImage anElement = map.values().iterator().next().get();

        return anElement.getHeight() == substituteTile.get().getHeight()
                && anElement.getWidth() == substituteTile.get().getWidth();
    }

    private void setSubImage(BufferedImage out, BufferedImage tile, Pair<Integer, Integer> coord)
    {
        int row = coord.getLeft();
        int column = coord.getRight();

        for (int x = 0; x < tile.getWidth(); x++)
        {
            for (int y = 0; y < tile.getHeight(); y++)
            {
                int rgb = tile.getRGB(x, y);
                int outX = column * tile.getHeight() + x;
                int outY = row * tile.getWidth() + y;
                
                try
                {
                    out.setRGB(outX, outY, rgb);
                } catch (Exception e)
                {
//                    LOGGER.error("X{} Y{}", outX, outX);
                }
            }
        }
    }
}
