package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.support.DateFactory;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class AtomFeedView extends AbstractAtomFeedView {
    private final SiteUrl siteUrl;
    private final DateFactory dateFactory;

    @Autowired
    public AtomFeedView(SiteUrl siteUrl, DateFactory dateFactory) {
        this.siteUrl = siteUrl;
        this.dateFactory = dateFactory;
    }

    @Override
    protected List<Entry> buildFeedEntries(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        List<Post> posts = (List<Post>) model.get("posts");
        List<Entry> entries = new ArrayList<>(posts.size());
        return null;
    }

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
        String feedPath = (String) model.get("feed-path");
        feed.setTitle((String) model.get("feed-title"));
        feed.setId(String.format("http://spring.io%s", feedPath));
        feed.setIcon(siteUrl.get);
    }
}
