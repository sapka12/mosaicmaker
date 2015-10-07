package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.ITile;
import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import hu.sapka12.mozaik.maker.bufferedimage.Tile;
import java.awt.image.BufferedImage;

public class TileFinderStrategy implements ITileFinderStrategy<BufferedImage>{

    @Override
    public Tile findSubstitute(ITile<BufferedImage> tile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
