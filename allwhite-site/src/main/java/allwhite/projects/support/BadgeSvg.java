package allwhite.projects.support;

import org.xmlbeam.annotation.XBRead;
import org.xmlbeam.annotation.XBWrite;

import java.util.List;

public interface BadgeSvg {

    @XBRead("/svg/g/text")
    List<GraphicElement> getGraphicElements();

    @XBRead("/svg/g/path")
    List<Path> getPaths();

    @XBRead("/svg/mask/rect")
    Mask getMask();

    @XBWrite("/svg/@width")
    void setWidth(int width);

    interface GraphicElement {
        @XBRead("@x")
        Integer getXPosition();

        @XBRead("@y")
        Integer getYPosition();

        @XBWrite("@x")
        void setXPosition(int x);

        @XBWrite("@y")
        void setYPosition(int y);

        @XBRead(".")
        String getText();

        @XBWrite(".")
        void setText(String text);
    }

    interface Path {
        @XBWrite("@d")
        void setDraw(String d);
    }

    interface Mask {
        @XBRead("@width")
        Integer getWidth();

        @XBWrite("@width")
        void setWidth(int width);
    }
}
