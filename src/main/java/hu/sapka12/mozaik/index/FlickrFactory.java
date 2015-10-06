package hu.sapka12.mozaik.index;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.people.User;
import org.scribe.model.Token;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlickrFactory implements InitializingBean {

    @Value("${flickr.apiKey}")
    private String apiKey;
    @Value("${flickr.sharedSecret}")
    private String sharedSecret;
    @Value("${flickr.token}")
    private String token;
    @Value("${flickr.tokenSecret}")
    private String tokenSecret;

    private Flickr flickr;
    private String userId;

    @Override
    public void afterPropertiesSet() throws Exception {
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

    public Flickr getFlickr() {
        return flickr;
    }

    public String getUserId() {
        return userId;
    }
}
