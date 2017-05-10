package hu.sapka12.mozaik.tile;

import hu.sapka12.mozaik.TileFinderStrategy;
import hu.sapka12.mozaik.maker.bufferedimage.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TileChanger {

    private static final Logger LOGGER = LoggerFactory.getLogger(TileChanger.class);
    private final TileFinderStrategy tileFinderStrategy;

    @Autowired
    public TileChanger(TileFinderStrategy tileFinderStrategy)
    {
        this.tileFinderStrategy = tileFinderStrategy;
    }
    
    public Tiles change(Tiles tiles) {
        Tile[][] tileMatrix = tiles.getTiles();
        
        int counter = 0;
        
        for (Tile[] tileRow : tileMatrix)
        {
            for (int j = 0; j < tileRow.length; j++)
            {
                LOGGER.info("Looking for subtitute {}", counter++);
                
                Tile tile = tileRow[j];
                tileRow[j] = tileFinderStrategy.findSubstitute(tile);
            }
        }
        return tiles;
    }
}
