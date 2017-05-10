package hu.sapka12.mozaik;

import hu.sapka12.mozaik.tile.TileChanger;
import hu.sapka12.mozaik.tile.TileFactory;
import hu.sapka12.mozaik.tile.Tiles;
import java.awt.image.BufferedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MozaikMaker {

    private final TileChanger tileChanger;
    private final TileFactory tileFactory;
    
    @Autowired
    public MozaikMaker(TileFactory tileFactory, TileChanger tileChanger) {
        this.tileChanger = tileChanger;
        this.tileFactory = tileFactory;
    }

    public BufferedImage make(BufferedImage input, int tileSize) {
        assertImageSize(input, tileSize);

        BufferedImage subimage = input.getSubimage(0, 0, 
                normalizeSize(input.getWidth(), tileSize), 
                normalizeSize(input.getHeight(), tileSize));
        
        Tiles tiles = tileFactory.getTiles(subimage, tileSize);
        tiles = tileChanger.change(tiles);
        
        return tiles.buildImage();
    }

    private static int normalizeSize(int size, int tileSize) {
        return size - size % tileSize;
    }

    private void assertImageSize(BufferedImage image, int tileSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (width < tileSize) {
            throw new IllegalArgumentException("Width[" + width + "] is smaller than tileSize[" + tileSize + "]");
        }

        if (height < tileSize) {
            throw new IllegalArgumentException("Height[" + height + "] is smaller than tileSize[" + tileSize + "]");
        }
    }
}
