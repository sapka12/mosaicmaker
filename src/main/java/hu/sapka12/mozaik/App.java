package hu.sapka12.mozaik;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);

        MozaikMaker mozaikMaker = (MozaikMaker) ctx.getBean("mozaikMaker");
        
        BufferedImage input = ImageIO.read(new File("input.jpg"));
        int tileSize = 128;
        
        BufferedImage out = mozaikMaker.make(input, tileSize);
        
        ImageIO.write(out, "jpg", new File("out.jpg"));
    }
}
