package allwhite.blog;


import allwhite.support.ResourceNotFoundException;

public class PostNotFoundException extends ResourceNotFoundException {
    public PostNotFoundException(long id) {
        super("Could not find blog post with id " + id);
    }

    public PostNotFoundException(String slug) {
        super("Could not find blog post with slug '" + slug + "'");
    }
}
