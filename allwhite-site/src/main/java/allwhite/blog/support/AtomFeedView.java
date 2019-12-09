package allwhite.blog.support;

import allwhite.support.DateFactory;
import com.rometools.rome.feed.atom.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    protected List<Entry> buildFeedEntries(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
