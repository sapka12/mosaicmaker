package hu.sapka12.mozaik;

import hu.sapka12.mozaik.maker.bufferedimage.InputImage;
import hu.sapka12.mozaik.maker.bufferedimage.MozaikBuilder;
import hu.sapka12.mozaik.maker.IMozaikBuilder;
import hu.sapka12.mozaik.maker.ITileFinderStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import hu.sapka12.mozaik.maker.ITile;
import hu.sapka12.mozaik.maker.IInputImage;
import hu.sapka12.mozaik.maker.bufferedimage.EasyStrategy;

public class Maker<T>
{
    private final IInputImage<T> inputImage;
    private final ITileFinderStrategy<T> tileFinderStrategy;
    private final IMozaikBuilder<T> mozaikBuilder;

    public static void main(String[] args) throws IOException
    {
        BufferedImage inputImage = ImageIO.read(new File("input.jpg"));
        IInputImage<BufferedImage> input = new InputImage(inputImage, 64);
        ITileFinderStrategy<BufferedImage> tileFinderStrategy = new EasyStrategy(64);
        IMozaikBuilder<BufferedImage> mozaikBuilder = new MozaikBuilder();

        Maker<BufferedImage> maker = new Maker<>(input, tileFinderStrategy, mozaikBuilder);
        BufferedImage image = maker.make();

        ImageIO.write(image, "jpg", new File("out.jpg"));
    }

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
