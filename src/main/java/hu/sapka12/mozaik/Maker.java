package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.IMozaikBuilder;
import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import hu.sapka12.mozaik.maker.ITile;
import hu.sapka12.mozaik.maker.IInputImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Maker<T>
{
    private final IInputImage<T> inputImage;
    private final ITileFinderStrategy<T> tileFinderStrategy;
    private final IMozaikBuilder<T> mozaikBuilder;

    public static void main(String[] args) throws IOException
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        Maker<BufferedImage> maker = (Maker<BufferedImage>) ctx.getBean("maker");
        
        BufferedImage image = maker.make();

        ImageIO.write(image, "jpg", new File("out.jpg"));
    }

    @Autowired
    public Maker(IInputImage<T> inputImage, ITileFinderStrategy<T> tileFinderStrategy, IMozaikBuilder<T> mozaikBuilder)
    {
        this.inputImage = inputImage;
        this.tileFinderStrategy = tileFinderStrategy;
        this.mozaikBuilder = mozaikBuilder;
    }

    public T make()
    {
        for (int column = 0; column < inputImage.getTilesInARow(); column++)
        {
            for (int row = 0; row < inputImage.getTilesInColumn(); row++)
            {
                ITile tile = inputImage.getTile(row, column);
                ITile substituteTile = tileFinderStrategy.findSubstitute(tile);
                mozaikBuilder.append(substituteTile, row, column);
            }
        }

        return mozaikBuilder.build();
    }
}
