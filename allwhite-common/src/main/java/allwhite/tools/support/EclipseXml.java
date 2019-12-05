package allwhite.tools.support;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class EclipseXml {
    @JacksonXmlProperty(localName = "product")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<EclipseXmlProduct> eclipseXmlProductList;

    public List<EclipseXmlProduct> getEclipseXmlProducts() {
        return eclipseXmlProductList;
    }

    public void setEclipseXmlProducts(List<EclipseXmlProduct> eclipseXmlProductList) {
        this.eclipseXmlProductList = eclipseXmlProductList;
    }
}
