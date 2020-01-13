package allwhite.tools.support;

import allwhite.tools.EclipseDownloads;
import allwhite.tools.EclipsePlatform;
import allwhite.tools.ToolSuiteDownloads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@Controller
@RequestMapping("/tools3")
public class ToolsController {

    private ToolsService toolsService;

    @Autowired
    public ToolsController(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    @RequestMapping(method = {GET, HEAD})
    public String index(Model model) throws IOException {
        ToolSuiteDownloads stsDownloads = toolsService.getStsGaDownloads();
        model.addAttribute("stsDownloadLinks", stsDownloads.getPreferredDownloadLinks());
        model.addAttribute("stsVersion", stsDownloads.getReleaseName());
        return "tools/index";
    }

    @RequestMapping(value = "/sts", method = {GET, HEAD})
    public String stsIndex(Model model) throws IOException {
        ToolSuiteDownloads stsDownloads = toolsService.getStsGaDownloads();
        model.addAttribute("downloadLinks", stsDownloads.getPreferredDownloadLinks());
        model.addAttribute("version", stsDownloads.getReleaseName());
        return "tools/sts/index";
    }

    @RequestMapping(value = "/eclipse", method = {GET, HEAD})
    public String eclipseIndex(Model model) throws IOException {
        EclipseDownloads eclipseDownloads = toolsService.getEclipseDownloads();
        Map<String, EclipsePlatform> allPlatforms = eclipseDownloads.getPlatforms();
        List<EclipsePlatform> platforms = new ArrayList<>();
        platforms.add(allPlatforms.get("windows"));
        platforms.add(allPlatforms.get("mac"));
        platforms.add(allPlatforms.get("linux"));
        model.addAttribute("platforms", platforms);
        return "tools/eclipse/index";

    }
}
