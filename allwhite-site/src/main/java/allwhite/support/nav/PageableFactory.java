package allwhite.support.nav;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class PageableFactory {
    public static Pageable all() {
        return build(0, Integer.MAX_VALUE);
    }

    public static Pageable forLists(int page) {
        return build(page - 1, 10);
    }

    public static Pageable forDashboard(int page) {
        return build(page - 1, 30);
    }

    private static Pageable build(int page, int pageSize) {
        return new PageRequest(page, pageSize, Sort.Direction.DESC, "publishAt");
    }


    public static Pageable forFeeds() {
        return build(0, 20);
    }
}
