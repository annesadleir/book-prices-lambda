package uk.co.littlestickyleaves.bookprices.shops.amazon;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;

import java.io.*;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AmazonPriceServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    private AmazonPriceService testObject;
    private AmazonSignedUrlCreator mockUrlCreator = mock(AmazonSignedUrlCreator.class);

    @Before
    public void setUp() throws Exception {
        testObject = new AmazonPriceService(mockUrlCreator);
    }

    @Test
    public void checkPrice() throws Exception {
        // arrange
        ISBN isbn = new ISBN("9780349007441");
        String mockServer = "http://localhost:8089";
        String url = "/some/dummy/url";
        when(mockUrlCreator.createUrl(isbn)).thenReturn(mockServer + url);
        String response = loadFixtureFile();
        String expectedUrl = "https://www.amazon.co.uk/Homegoing-Yaa-Gyasi/dp/024124272X?SubscriptionId=accesskey&tag=associatetag&linkCode=xm2&camp=2025&creative=165953&creativeASIN=024124272X";
        double expectedPrice = 9.09;

        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(response)));

        // act
        PriceAtWebsite result = testObject.checkPrice(isbn);

        // assert
        assertEquals(expectedUrl, result.getWebAddress());
        assertEquals(expectedPrice, result.getPrice(), 0.00001);
    }

    private String loadFixtureFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("sampleAmazonResponse.xml")))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}