package uk.co.littlestickyleaves.bookprices.shops.waterstones;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;
import uk.co.littlestickyleaves.bookprices.shops.BookshopPriceService;

import java.io.IOException;

/**
 * Checks Waterstone's prices by looking at html
 */
public class WaterstonesPriceService extends BookshopPriceService {

    public final static String WATERSTONES_URL = "https://www.waterstones.com/books/search/term/${ISBN}";

    public WaterstonesPriceService() {
        super(Bookshop.WATERSTONES);
    }

    @Override
    public PriceAtWebsite checkPrice(ISBN isbn) {
        try {
            // make the url
            String url = WATERSTONES_URL.replace("${ISBN}", isbn.getIsbn());

            // query the url
            Document document = Jsoup.connect(url).get();

            double price = fetchPrice(document);
            String returnedUrl = fetchReturnedUrl(document);

            return new PriceAtWebsite(returnedUrl, price);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String fetchReturnedUrl(Document document) {
        return document.getElementsByAttributeValue("name", "og:url").attr("content");
    }

    private double fetchPrice(Document document) throws NumberFormatException {
        String price = document.getElementsByAttributeValue("itemprop", "price").first().text();
        String digits = price.replaceAll("[^\\d]", "");
        double pence = Double.parseDouble(digits);
        return pence / 100;
    }
}
