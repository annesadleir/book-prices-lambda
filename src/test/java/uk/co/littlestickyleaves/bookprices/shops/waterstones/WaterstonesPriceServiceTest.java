package uk.co.littlestickyleaves.bookprices.shops.waterstones;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;

import javax.xml.parsers.DocumentBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class) // The runner of PowerMock
@PrepareForTest(Jsoup.class) // the class to prepare
public class WaterstonesPriceServiceTest {

    private WaterstonesPriceService testObject;

    @Test
    @PrepareForTest({Jsoup.class, WaterstonesPriceService.class})
    public void checkPrice() throws Exception {
        // arrange
        testObject = new WaterstonesPriceService();
        ISBN isbn = new ISBN("9780349007441");
        String url = "https://www.waterstones.com/books/search/term/9780349007441";
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);
        PowerMockito.mockStatic(Jsoup.class);
        Connection mockConnection = mock(Connection.class);
        String sampleUrl = "sampleUrl";
        String text = "9.99";

        BDDMockito.given(Jsoup.connect(url)).willReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);
        when(mockDocument.getElementsByAttributeValue("name", "og:url")).thenReturn(mockElements);
        when(mockDocument.getElementsByAttributeValue("itemprop", "price")).thenReturn(mockElements);
        when(mockElements.attr("content")).thenReturn(sampleUrl);
        when(mockElements.first()).thenReturn(mockElement);
        when(mockElement.text()).thenReturn(text);

        // act
        PriceAtWebsite result = testObject.checkPrice(isbn);

        // assert
    }

}