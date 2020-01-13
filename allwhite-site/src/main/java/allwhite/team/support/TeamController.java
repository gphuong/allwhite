package allwhite.team.support;

import allwhite.site.blog.BlogService;
import allwhite.support.DateFactory;
import allwhite.team.MemberProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@Controller
@RequestMapping("/team")
class TeamController {
    private final TeamService teamService;
    private final BlogService blogService;
    private final DateFactory dateFactory;

    @Autowired
    public TeamController(TeamService teamService, BlogService blogService, DateFactory dateFactory) {
        this.teamService = teamService;
        this.blogService = blogService;
        this.dateFactory = dateFactory;
    }

    @RequestMapping(method = {GET, HEAD})
    public String showTeam(Model model) {
        List<MemberProfile> profiles = teamService.fetchActiveMembers();
        model.addAttribute("profiles", profiles);
        model.addAttribute("teamLocations", profiles.stream()
                .filter(profile -> profile.getTeamLocation() != null)
                .map(MemberProfile::getTeamLocation)
                .collect(Collectors.toList()));
        return "team/index";
    }
}
