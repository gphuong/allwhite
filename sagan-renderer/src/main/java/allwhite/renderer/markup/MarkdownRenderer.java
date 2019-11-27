package allwhite.renderer.markup;

import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.RootNode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MarkdownRenderer implements MarkupRenderer {
    private final PegDownProcessor pegdown;

    public MarkdownRenderer() {
        this.pegdown = new PegDownProcessor(Extensions.ALL ^ Extensions.ANCHORLINKS);
    }

    @Override
    public String renderToHtml(String markup) {
        synchronized (this.pegdown) {
            RootNode astRoot = this.pegdown.parseMarkdown(markup.toCharArray());
            MarkdownToHtmlSerializer serializer = new MarkdownToHtmlSerializer(
                    new LinkRenderer(),
                    Collections.singletonMap(VerbatimSerializer.DEFAULT,
                            PrettifyVerbatimSerializer.INSTANCE));
            return serializer.toHtml(astRoot);
        }
    }

    @Override
    public boolean canRender(MediaType mediaType) {
        return MediaType.TEXT_MARKDOWN.isCompatibleWith(mediaType);
    }
}
