package uk.co.littlestickyleaves.bookprices.shops.amazon;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;
import uk.co.littlestickyleaves.bookprices.shops.BookshopPriceService;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

/**
 * Checks Amazon prices via the API using XPath
 */
public class AmazonPriceService extends BookshopPriceService {

    private static final String XPATH_NOT_KINDLE_ITEM = "/ItemLookupResponse/Items/Item[ItemAttributes/Binding != 'Kindle']";
    private static final String XPATH_ITEM_WEBPAGE = "DetailPageURL";
    private static final String XPATH_FIRST_OFFER_PRICE = "Offers/Offer[1]/OfferListing/Price/Amount";

    private final AmazonSignedUrlCreator signatureHelper;
    private final XPath xPath;

    public AmazonPriceService(AmazonSignedUrlCreator signatureHelper) {
        super(Bookshop.AMAZON);
        this.signatureHelper = signatureHelper;
        xPath = XPathFactory.newInstance().newXPath();
    }

    @Override
    public PriceAtWebsite checkPrice(ISBN isbn) {
        try {
            String responseBody = queryAmazonApi(isbn);
            return parseResponseForPrice(responseBody);
        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private String queryAmazonApi(ISBN isbn) {
        try {
            String totalURL = signatureHelper.createUrl(isbn);
            return Unirest.get(totalURL).asString().getBody();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private PriceAtWebsite parseResponseForPrice(String responseBody)
            throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new InputSource(new StringReader(responseBody)));

        return identifyNode(document)
                .map(this::fromNode)
                .orElse(new PriceAtWebsite("No non-Kindle edition found", 0.0));
    }

    private Optional<Node> identifyNode(Document document) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.evaluate(XPATH_NOT_KINDLE_ITEM, document, XPathConstants.NODESET);
        return nodeList.getLength() == 0 ? Optional.empty() :
                Optional.of(nodeList.item(1));
    }

    private PriceAtWebsite fromNode(Node node) {
        try {
            String webpage = (String) xPath.evaluate(XPATH_ITEM_WEBPAGE, node, XPathConstants.STRING);
            Double pence = (Double) xPath.evaluate(XPATH_FIRST_OFFER_PRICE, node, XPathConstants.NUMBER);
            return new PriceAtWebsite(webpage, pence / 100);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
