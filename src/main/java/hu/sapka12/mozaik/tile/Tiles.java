package hu.sapka12.mozaik.tile;

import hu.sapka12.mozaik.maker.bufferedimage.Tile;
import java.awt.image.BufferedImage;

public class Tiles {

    private final Tile[][] tiles;

    public Tiles(Tile[][] tileArray) {
        this.tiles = tileArray;
    }

    public int numberOfRows() {
        return tiles.length;
    }

    public int numberOfColumns() {
        return tiles[0].length;
    }
    
    public BufferedImage buildImage() {
        BufferedImage out = new BufferedImage(
                getWidth(), 
                getHeight(), 
                BufferedImage.TYPE_INT_RGB);
        
        return out;
    }

    private int getWidth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int getHeight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
