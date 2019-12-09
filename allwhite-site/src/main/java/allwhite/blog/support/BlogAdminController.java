package allwhite.blog.support;

import allwhite.blog.Post;
import allwhite.blog.PostCategory;
import allwhite.blog.PostFormat;
import allwhite.site.blog.BlogService;
import allwhite.site.blog.PostForm;
import allwhite.support.DateFactory;
import allwhite.support.nav.PageableFactory;
import allwhite.support.nav.PaginationInfo;
import allwhite.team.MemberProfile;
import allwhite.team.support.TeamRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {
    private BlogService service;
    private TeamRepository teamRepository;
    private DateFactory dateFactory;

    public BlogAdminController(BlogService service, TeamRepository teamRepository, DateFactory dateFactory) {
        this.service = service;
        this.teamRepository = teamRepository;
        this.dateFactory = dateFactory;
    }

    @RequestMapping(value = "", method = {GET, HEAD})
    public String dashboard(Model model, @RequestParam(defaultValue = "1") int page) {
        Page<PostView> postViewPage = PostView.pageOf(service.getPublishedPosts(PageableFactory.forDashboard(page)), dateFactory);
        model.addAttribute("posts", postViewPage);
        model.addAttribute("paginationInfo", new PaginationInfo(postViewPage));

        if (page == 1) {
            model.addAttribute("drafts", PostView.pageOf(service.getDraftPosts(PageableFactory.all()), dateFactory));
            model.addAttribute("scheduled", PostView.pageOf(service.getScheduledPosts(PageableFactory.all()), dateFactory));
        } else {
            Page<PostView> emptyPage = new PageImpl<>(Collections.emptyList(), PageableFactory.all(), 0);
            model.addAttribute("drafts", emptyPage);
            model.addAttribute("scheduled", emptyPage);
        }
        return "admin/blog/index";
    }

    @RequestMapping(value = "/{postId:[0-9]+}{slug:.*}", method = {GET, HEAD})
    public String showPost(@PathVariable Long postId, @PathVariable String slug, Model model) {
        model.addAttribute("post", PostView.of(service.getPost(postId), dateFactory));
        return "admin/blog/show";
    }

    @RequestMapping(value = "", method = {POST})
    public String createPost(Principal principal, @Valid PostForm postForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", PostCategory.values());
            model.addAttribute("formats", PostFormat.values());
            return "admin/blog/new";
        } else {
            MemberProfile memberProfile = teamRepository.findById(new Long(principal.getName()));
            try {
                Post post = service.addPost(postForm, memberProfile.getUsername());
                PostView postView = PostView.of(post, dateFactory);
                return "redirect:" + postView.getPath();
            } catch (DataIntegrityViolationException ex) {
                model.addAttribute("categories", PostCategory.values());
                model.addAttribute("postForm", postForm);
                bindingResult.rejectValue("title", "duplicate_post",
                        "A blog post with this publication date and title already exists");
                return "admin/blog/new";
            }
        }
    }
}
