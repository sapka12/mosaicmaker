package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.IMozaikBuilder;
import java.awt.image.BufferedImage;
import hu.sapka12.mozaik.maker.ITile;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class MozaikBuilder implements IMozaikBuilder<BufferedImage>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MozaikBuilder.class);
    private final Map<Pair<Integer, Integer>, ITile<BufferedImage>> map = new HashMap<>();

    @Override
    public BufferedImage build()
    {
        int maxRow = 0;
        int maxColumn = 0;
        int tileSize = 0;

        for (Map.Entry<Pair<Integer, Integer>, ITile<BufferedImage>> entry : map.entrySet())
        {
            Pair<Integer, Integer> coord = entry.getKey();
            if (coord.getLeft() > maxRow)
            {
                maxRow = coord.getLeft();
            }
            if (coord.getRight() > maxColumn)
            {
                maxColumn = coord.getRight();
            }

            ITile<BufferedImage> tile = entry.getValue();

            if (tileSize == 0)
            {
                tileSize = tile.get().getHeight();
            }
        }

        BufferedImage out = new BufferedImage(maxColumn * tileSize, maxRow * tileSize, BufferedImage.TYPE_INT_RGB);
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
                try
                {
                    out.setRGB(row * tile.getHeight() + y, column * tile.getWidth() + x, rgb);

                } catch (Exception e)
                {
//                    LOGGER.error("ROW{} COL{}", row, column);
//            throw new RuntimeException(e);
                }
            }
        }

    }

}
