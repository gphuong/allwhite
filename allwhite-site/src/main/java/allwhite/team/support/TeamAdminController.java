package allwhite.team.support;

import allwhite.team.MemberProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@Controller
public class TeamAdminController {
    private final TeamService teamService;
    private final TeamImporter teamImporter;
    private final InMemoryUsersConnectionRepository usersConnectionRepository;

    @Autowired
    public TeamAdminController(TeamService teamService,
                               TeamImporter teamImporter,
                               InMemoryUsersConnectionRepository usersConnectionRepository) {
        this.teamService = teamService;
        this.teamImporter = teamImporter;
        this.usersConnectionRepository = usersConnectionRepository;
    }

    @RequestMapping(value = "/admin/team/{username}", method = {GET, HEAD})
    public String editTeamMemberForm(@PathVariable("username") String username, Model model) {
        MemberProfile profile = teamService.fetchMemberProfileUsername(username);
        if (profile == MemberProfile.NOT_FOUND) {
            throw new MemberNotFoundException(username);
        }
        model.addAttribute("profile", profile);
        model.addAttribute("formAction", "/admin/team/" + username);
        return "admin/team/edit";
    }

    @RequestMapping(value = "/admin/team", method = {GET, HEAD})
    public String getTeamAdminPage(Model model) {
        model.addAttribute("activeMembers", teamService.fetchActiveMembers());
        model.addAttribute("hiddenMembers", teamService.fetchHiddenMembers());
        return "admin/team/index";
    }

}
