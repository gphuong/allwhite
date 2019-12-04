package allwhite.team.support;

import allwhite.search.SearchEntryMapper;
import allwhite.search.types.SitePage;
import allwhite.team.MemberProfile;
import org.springframework.stereotype.Component;

@Component
public class MemberProfileSearchEntryMapper implements SearchEntryMapper<MemberProfile> {
    @Override
    public SitePage map(MemberProfile profile) {
        SitePage entry = new SitePage();
        entry.setTitle(profile.getFullName());
        entry.setSummary(profile.getBio());
        entry.setRawContent(profile.getBio());
        entry.setPath("/team/" + profile.getUsername());
        return entry;
    }
}
