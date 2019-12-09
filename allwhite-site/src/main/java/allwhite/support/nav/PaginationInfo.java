package allwhite.support.nav;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationInfo {
    private final long currentPage;
    private final long totalPage;

    public PaginationInfo(Page<?> page) {
        currentPage = page.getNumber() + 1;
        totalPage = page.getTotalPages();
    }

    public boolean isPreviousVisible() {
        return currentPage > 1;
    }

    public boolean isNextVisible() {
        return currentPage < totalPage;
    }

    public long getNextPageNumber() {
        return currentPage + 1;
    }

    public long getPreviousPageNumber() {
        return currentPage - 1;
    }

    public List<PageElement> getPageElements(){
        return  new PageElementsBuilder(currentPage, totalPage).build();
    }
}
