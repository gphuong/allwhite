package allwhite.tools.support;

import allwhite.tools.SpringToolsDownload;
import allwhite.tools.SpringToolsPlatform;
import allwhite.tools.SpringToolsPlatformRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/tools")
public class SpringToolsAdminController {
    private final SpringToolsPlatformRepository repository;

    public SpringToolsAdminController(SpringToolsPlatformRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("platforms", this.repository.findAll());
        return "admin/tools/index";
    }

    @GetMapping("{id}")
    public String edit(@PathVariable String id, Model model) {
        SpringToolsPlatform platform = this.repository.findOne(id);
        return edit(platform, model);
    }

    @GetMapping("new")
    public String newPlatform(Model model) {
        SpringToolsPlatform platform = new SpringToolsPlatform("new-platform");
        return edit(platform, model);
    }

    private String edit(SpringToolsPlatform platform, Model model) {
        if (platform == null) {
            return "error/404";
        }
        model.addAttribute("platform", platform);
        return "admin/tools/edit";
    }

    @PostMapping("{id}")
    public String save(SpringToolsPlatform platform,
                       @RequestParam(defaultValue = "") List<String> downloadsToDelete) {
        List<SpringToolsDownload> toDelete = platform.getDownloads().stream()
                .filter(p -> downloadsToDelete.contains(p.getVariant()) || StringUtils.isEmpty(p.getVariant())
                ).collect(Collectors.toList());
        platform.getDownloads().removeAll(toDelete);
        this.repository.save(platform);
        return "redirect:" + platform.getId();
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable String id) {
        this.repository.delete(id);
        return "redirect:./";
    }
}
