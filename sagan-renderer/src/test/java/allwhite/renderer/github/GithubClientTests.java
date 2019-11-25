package allwhite.renderer.github;

import allwhite.renderer.RendererProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest({GithubClient.class, RendererProperties.class})
@TestPropertySource(properties = "allwhite.renderer.github.token=testtoken")
public class GithubClientTests {

    private static final MediaType GITHUB_PREVIEW = MediaType.parseMediaType("application/vnd.github.mercy-preview+json");

    private static final MediaType APPLICATION_ZIP = MediaType.parseMediaType("application/zip");

    @Autowired
    private GithubClient client;
    @Autowired
    private MockRestServiceServer server;

    @Test
    public void downloadRepositoryInfo() {
        String org = "spring-guides";
        String repo = "gs-rest-services";
        String expectedUrl = String.format("/repos/%s/%s", org, repo);
        String authorization = getAuthorizationHeader();
        this.server.expect(requestTo(expectedUrl))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
                .andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
                .andRespond(withSuccess(getClassPathResource("gs-rest-service.json"), GITHUB_PREVIEW));
        Repository repository = this.client.fetchOrgRepository(org, repo);
        assertThat(repository).extracting("name").containsOnly("gs-rest-service");
    }

    @Test
    public void downloadRepositoryAsZipBall() throws Exception {
        String org = "spring-guides";
        String repo = "gs-rest-service";
        String expectedUrl = String.format("/repos/%s/%s/zipball", org, repo);
        String authorization = getAuthorizationHeader();
        this.server.expect(requestTo(expectedUrl))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authorization))
                .andExpect(header(HttpHeaders.ACCEPT, GITHUB_PREVIEW.toString()))
                .andRespond(withSuccess(getClassPathResource("gs-rest-service.zip"), APPLICATION_ZIP));
        byte[] result = this.client.downloadRepositoryAsZipball(org, repo);
        ClassPathResource resource = getClassPathResource("gs-rest-service.zip");
        assertThat(result).isEqualTo(StreamUtils.copyToByteArray(resource.getInputStream()));
    }

    private String getAuthorizationHeader() {
        return "Token testtoken";
    }

    private ClassPathResource getClassPathResource(String path) {
        return new ClassPathResource(path, getClass());
    }

}
