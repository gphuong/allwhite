package allwhite.renderer.markup;

import org.asciidoctor.Asciidoctor;
import org.junit.Before;
import org.junit.Test;

public class AsciidoctorRendererTests {
    private AsciidoctorRenderer converter;

    @Before
    public void setup() {
        converter = new AsciidoctorRenderer(Asciidoctor.Factory.create());
    }

    @Test
    public void renderLink()
}
