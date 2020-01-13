package allwhite.team.support;

import allwhite.site.blog.BlogService;
import allwhite.support.DateFactory;
import allwhite.team.MemberProfile;
import allwhite.team.MemberProfileBuilder;
import allwhite.team.TeamLocation;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TeamControllerTests {
    @Mock
    private BlogService blogService;

    @Mock
    private TeamService teamService;

    private ExtendedModelMap model = new ExtendedModelMap();
    private TeamController teamController;
    private DateFactory dateFactory;

    @Before
    public void setUp() {
        teamController = new TeamController(teamService, blogService, dateFactory);
        List<MemberProfile> members = new ArrayList<>();

        members.add(MemberProfileBuilder.profile()
                .name("Norman")
                .geoLocation(10, 5)
                .username("normy")
                .id(123L)
                .build());

        members.add(MemberProfileBuilder.profile()
                .name("Patrick")
                .geoLocation(-5, 15)
                .username("pat")
                .id(321L)
                .build());
        BDDMockito.given(teamService.fetchActiveMembers()).willReturn(members);
    }

    @Test
    public void includeTeamLocationsInModel() {
        teamController.showTeam(model);

        List<TeamLocation> teamLocations = (List<TeamLocation>) model.get("teamLocations");

        TeamLocation norman = teamLocations.get(0);
        MatcherAssert.assertThat(norman.getName(), Matchers.equalTo("Norman"));
        MatcherAssert.assertThat(norman.getLatitude(), Matchers.equalTo(10f));
        MatcherAssert.assertThat(norman.getLongtitude(), Matchers.equalTo(5f));
        MatcherAssert.assertThat(norman.getMemberId(), Matchers.equalTo(123L));

        TeamLocation patrick = teamLocations.get(1);
        MatcherAssert.assertThat(patrick.getName(), Matchers.equalTo("Patrick"));
        MatcherAssert.assertThat(patrick.getLatitude(), Matchers.equalTo(-5f));
        MatcherAssert.assertThat(patrick.getLongtitude(), Matchers.equalTo(15f));
        MatcherAssert.assertThat(patrick.getMemberId(), Matchers.equalTo(321L));

    }
}
