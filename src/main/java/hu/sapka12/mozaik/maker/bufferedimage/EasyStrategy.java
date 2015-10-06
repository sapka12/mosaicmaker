package hu.sapka12.mozaik.maker.bufferedimage;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import hu.sapka12.mozaik.index.FlickrFactory;
import hu.sapka12.mozaik.index.model.IndexData;
import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import java.awt.image.BufferedImage;
import hu.sapka12.mozaik.maker.ITile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class EasyStrategy implements ITileFinderStrategy<BufferedImage> {

    private final int tileSize;
    private final List<IndexData> allIndexData;
    private final FlickrFactory flickrFactory;

    @Autowired
    public EasyStrategy(MongoOperations mongo, FlickrFactory flickrFactory) {
        this.tileSize = 64;
        this.flickrFactory = flickrFactory;
        allIndexData = mongo.findAll(IndexData.class);
    }

    @Override
    public ITile<BufferedImage> findSubstitute(ITile<BufferedImage> tile) {
        BufferedImage input = tile.get();

        ITile<BufferedImage> flickrTile = getFromFlickr(tile);

        if (flickrTile == null) {
            int rgb = getAverageColor(input);

            return new Tile(getTileWithColor(rgb));
        }
        return flickrTile;
    }

    private int getAverageColor(BufferedImage input) {
        double r = 0.0;
        double g = 0.0;
        double b = 0.0;
        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int rgb = input.getRGB(i, j);
                Color c = new Color(rgb);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }

        return new Color(
                new Double(r / input.getWidth() / input.getHeight()).intValue(),
                new Double(g / input.getWidth() / input.getHeight()).intValue(),
                new Double(b / input.getWidth() / input.getHeight()).intValue())
                .getRGB();
    }

    private BufferedImage getTileWithColor(int rgb) {
        BufferedImage output = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                output.setRGB(i, j, rgb);
            }
        }
        return output;
    }

    private ITile<BufferedImage> getFromFlickr(ITile<BufferedImage> tile) {
        BufferedImage tileImage = tile.get();
        int rgb = getAverageColor(tileImage);
        IndexData indexData = findClosestIndexData(rgb);
        BufferedImage flickrImage = getBy(indexData);
        return new Tile(resize(flickrImage, tileImage.getWidth(), tileImage.getHeight()));
    }

    private IndexData findClosestIndexData(int rgb) {
        Color inputColor = new Color(rgb);

        IndexData closestIndexData = null;
        for (IndexData actualIndexData : allIndexData) {
            if (closestIndexData == null) {
                closestIndexData = actualIndexData;
            } else {

                if (distance(inputColor, getColor(actualIndexData)) < distance(inputColor, getColor(closestIndexData))) {
                    closestIndexData = actualIndexData;
                }
            }
            return closestIndexData;
        }
        return null;
    }

    private BufferedImage getBy(IndexData indexData) {
        try {
            PhotosInterface photosInterface = flickrFactory.getFlickr().getPhotosInterface();
            Photo photo = photosInterface.getPhoto(indexData.getId().getPictureId());
            return photosInterface.getImage(photo, Size.SQUARE);
        } catch (FlickrException e) {
            throw new RuntimeException(e);
        }
    }

    private Color getColor(IndexData indexData) {
        return new Color(
                indexData.getColor().getRed(),
                indexData.getColor().getGreen(),
                indexData.getColor().getBlue());
    }

    private double distance(Color c1, Color c2) {
        return Math.sqrt(square(c1.getRed() - c2.getRed()) + square(c1.getGreen() - c2.getGreen()) + square(c1.getBlue() - c2.getBlue()));
    }

    private double square(int num) {
        return (double) num * num;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
}
