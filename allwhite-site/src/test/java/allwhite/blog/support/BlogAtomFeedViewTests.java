package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.support.DateFactory;
import com.rometools.rome.feed.atom.Feed;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BlogAtomFeedViewTests {
    private ExtendedModelMap model = new ExtendedModelMap();
    private SiteUrl siteUrl;
    private AtomFeedView atomFeedView;
    private Feed feed = new Feed();
    private Calendar calendar = Calendar.getInstance(DateFactory.DEFAULT_TIME_ZONE);
    private HttpServletRequest request = mock(HttpServletRequest.class);

    @Before
    public void setUp() {
        siteUrl = mock(SiteUrl.class);
        atomFeedView = new AtomFeedView(siteUrl, new DateFactory());
        given(request.getServerName()).willReturn("springsource.org");
        model.addAttribute("posts", new ArrayList<Post>());
    }

    @Test
    public void hasFeedTitleFromModel(){
        model.addAttribute("feed-title", "Spring Engineering");
        atomFeedView.buildFeedMetadata(model, feed, mock(HttpServletRequest.class));
        assertThat(feed.getTitle(), is("Spring Engineering"));

    }
}
