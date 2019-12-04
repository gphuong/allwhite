package allwhite.search;

import allwhite.search.types.SearchEntry;

public interface SearchEntryMapper<T> {
    <R extends SearchEntry> R map(T item);
}
