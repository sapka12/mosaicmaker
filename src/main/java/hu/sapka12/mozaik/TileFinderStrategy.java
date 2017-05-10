package hu.sapka12.mozaik;

import hu.sapka12.mozaik.index.FlickrFactory;
import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import hu.sapka12.mozaik.maker.bufferedimage.EasyStrategy;
import java.awt.image.BufferedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class TileFinderStrategy extends EasyStrategy implements ITileFinderStrategy<BufferedImage>{

    @Autowired
    public TileFinderStrategy(MongoOperations mongo, FlickrFactory flickrFactory)
    {
        super(mongo, flickrFactory);
    }
}
