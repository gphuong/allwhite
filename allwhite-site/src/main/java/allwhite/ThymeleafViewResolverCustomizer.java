package allwhite;

import org.thymeleaf.spring4.view.ThymeleafViewResolver;

public class ThymeleafViewResolverCustomizer {
    public ThymeleafViewResolverCustomizer(ThymeleafViewResolver viewResolver, String applicationVersion) {
        viewResolver.addStaticVariable("allwhiteAppVersion", applicationVersion);
    }
}
