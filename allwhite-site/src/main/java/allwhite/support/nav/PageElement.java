package allwhite.support.nav;

class PageElement {
    private final String label;
    private final boolean isNavigable;
    private final boolean isCurrentPage;

    public PageElement(long pageNumber, boolean isNavigable, boolean isCurrentPage) {
        this(pageNumber + "", isNavigable, isCurrentPage);
    }

    public PageElement(String label, boolean isNavigable, boolean isCurrentPage) {
        this.label = label;
        this.isNavigable = isNavigable;
        this.isCurrentPage = isCurrentPage;
    }

    public String getLabel() {
        return label;
    }

    public boolean isNavigable() {
        return isNavigable;
    }

    public boolean isCurrentPage() {
        return isCurrentPage;
    }
}
