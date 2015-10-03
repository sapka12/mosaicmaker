package hu.sapka12.mozaik;

import com.flickr4java.flickr.FlickrException;
import hu.sapka12.mozaik.index.Identifier;
import hu.sapka12.mozaik.index.IndexData;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

public class Indexer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);

    private final MongoOperations mongo;
    private final FlickrCrawler crawler;

    public static void main(String[] args) throws FlickrException
    {
        Indexer mozaikMaker = new Indexer();
        mozaikMaker.indexPicturesInYear(2015);
    }

    public Indexer() throws FlickrException
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);

        mongo = (MongoOperations) ctx.getBean("mongoTemplate");
        crawler = (FlickrCrawler) ctx.getBean("flickrCrawler");
    }

    public void indexPicturesInYear(int year) throws FlickrException
    {
        LOGGER.info("Start " + year);

        Set<IndexData> datas = crawler.crawl(year);

        LOGGER.info("INDEX:");

        persist(datas);
    }

    private void persist(Set<IndexData> datas)
    {

        List<IndexData> alreadyInsertedData = mongo.findAll(IndexData.class);
        for (Iterator<IndexData> iterator = datas.iterator(); iterator.hasNext();)
        {
            IndexData data = iterator.next();
            for (IndexData indexData : alreadyInsertedData)
            {
                final Identifier id = indexData.getId();
                final Identifier actualId = data.getId();
                if (id.getPictureId().equals(actualId.getPictureId())
                        && id.getUserId().equals(actualId.getUserId()))
                {
                    iterator.remove();
                    break;
                }
            }
        }

        mongo.insertAll(datas);

        for (IndexData data : datas)
        {
            LOGGER.info("{}", data);
        }
    }
}
