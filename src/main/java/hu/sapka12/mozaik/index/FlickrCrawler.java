package hu.sapka12.mozaik.index;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import hu.sapka12.mozaik.index.model.Color;
import hu.sapka12.mozaik.index.model.Identifier;
import hu.sapka12.mozaik.index.model.IndexData;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.scribe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlickrCrawler implements InitializingBean
{

    private static final Logger LOGGER = LoggerFactory.getLogger(FlickrCrawler.class);

    private String userId;
    private Flickr flickr;

    @Value("${flickr.apiKey}")
    private String apiKey;
    @Value("${flickr.sharedSecret}")
    private String sharedSecret;
    @Value("${flickr.token}")
    private String token;
    @Value("${flickr.tokenSecret}")
    private String tokenSecret;

    public Set<IndexData> crawl(int year)
    {
        try
        {
            Set<IndexData> indexDataSet = new HashSet<>();

            Set<String> pictureIds = findAllPictureId(year);

            LOGGER.info("all pictures: {}", pictureIds.size());
            int counter = 0;
            for (String pictureId : pictureIds)
            {
                counter++;

                LOGGER.info("pic [{}] {}/{}", year, counter, pictureIds.size());
                indexDataSet.add(createIndexData(pictureId));
            }

            return indexDataSet;
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private Set<String> findAllPictureId(int year) throws FlickrException
    {
        PhotosetsInterface photosetsInterface = flickr.getPhotosetsInterface();
        Collection<Photoset> photosets = photosetsInterface.getList(userId).getPhotosets();

        Set<String> ids = new HashSet<>();

        int counter = 0;
        for (Photoset photoset : photosets)
        {
            counter++;
            if (!photoset.getTitle().contains(year + ""))
            {
                continue;
            }

            LOGGER.info("PHOTOSET[{}]: {}/{}", photoset.getTitle(), counter, photosets.size());
            ids.addAll(idsFromPhotoset(photoset));
        }

        return ids;
    }

    private IndexData createIndexData(String pictureId) throws FlickrException
    {
        BufferedImage image = getImage(pictureId);
        Color averageColor = findAverageColor(image);

        Identifier id = new Identifier(userId, pictureId);

        return new IndexData(id, averageColor);
    }

    private BufferedImage getImage(String pictureId) throws FlickrException
    {
        final PhotosInterface photosInterface = flickr.getPhotosInterface();
        Photo photo = photosInterface.getPhoto(pictureId);
        return photosInterface.getImage(photo, Size.SQUARE);
    }

    private Color findAverageColor(BufferedImage image)
    {

        image = subImage(image);

        double sumRed = 0;
        double sumGreen = 0;
        double sumBlue = 0;

        final int width = image.getWidth();
        final int height = image.getHeight();

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int rgb = image.getRGB(x, y);
                java.awt.Color color = new java.awt.Color(rgb);

                sumRed += color.getRed();
                sumGreen += color.getGreen();
                sumBlue += color.getBlue();
            }
        }

        double pixels = width * height;

        return new Color(
                createInt(sumRed / pixels),
                createInt(sumGreen / pixels),
                createInt(sumBlue / pixels));
    }

    private int createInt(double d)
    {
        return new Double(d).intValue();
    }

    private BufferedImage subImage(BufferedImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();

        int min = Math.min(height, width);
        int max = Math.max(height, width);
        int diffPer2 = (max - min) / 2;
        int x = width > height ? diffPer2 : 0;
        int y = width > height ? 0 : diffPer2;
        image = image.getSubimage(x, y, min, min);
        return image;
    }

    private Collection<String> idsFromPhotoset(Photoset photoset) throws FlickrException
    {
        PhotoList<Photo> photos = flickr.getPhotosetsInterface().getPhotos(photoset.getId(), photoset.getPhotoCount(), 1);

        Set<String> ids = new HashSet<>();
        photos.stream().forEach((photo) ->
        {
            ids.add(photo.getId());
        });

        return ids;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        flickr = new Flickr(apiKey, sharedSecret, new REST());

        final AuthInterface authInterface = flickr.getAuthInterface();
        Token requestToken = new Token(token, tokenSecret);
        Auth auth = authInterface.checkToken(requestToken);
        flickr.setAuth(auth);

        auth.getUser().setFamily(true);

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);

        Flickr.debugRequest = false;
        Flickr.debugStream = false;

        final User user = auth.getUser();
        userId = user.getId();
    }

}
