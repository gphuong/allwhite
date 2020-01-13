package allwhite.support.nav;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationInfo {
    private final long currentPage;
    private final long totalPages;

    public PaginationInfo(Page<?> page) {
        currentPage = page.getNumber() + 1;
        totalPages = page.getTotalPages();
    }

    public boolean isPreviousVisible() {
        return currentPage > 1;
    }

    public boolean isNextVisible() {
        return currentPage < totalPages;
    }

    public long getNextPageNumber() {
        return currentPage + 1;
    }

    public long getPreviousPageNumber() {
        return currentPage - 1;
    }

    public List<PageElement> getPageElements(){
        return new PageElementsBuilder(currentPage, totalPages).build();
    }

    public boolean isVisible() {
        return isPreviousVisible() || isNextVisible();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PaginationInfo that = (PaginationInfo) o;

        if (currentPage != that.currentPage)
            return false;
        if (totalPages != that.totalPages)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (currentPage ^ (currentPage >>> 32));
        result = 31 * result + (int) (totalPages ^ (totalPages >>> 32));
        return result;
    }
}
