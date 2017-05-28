package uk.co.littlestickyleaves.bookprices.config;

import uk.co.littlestickyleaves.bookprices.BookPriceInfoCreator;
import uk.co.littlestickyleaves.bookprices.shops.BookshopPriceService;
import uk.co.littlestickyleaves.bookprices.shops.amazon.AmazonPriceService;
import uk.co.littlestickyleaves.bookprices.shops.amazon.AmazonSignedUrlCreator;
import uk.co.littlestickyleaves.bookprices.shops.waterstones.WaterstonesPriceService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Sets up the objects
 */
public class BookPricesConfiguration implements Supplier<BookPriceInfoCreator> {

    public static final Charset UTF8 = StandardCharsets.UTF_8;

    public BookPriceInfoCreator get() {
        AmazonPriceService amazonPriceService = createAmazonPriceService();

        List<BookshopPriceService> bookshopPriceServices = new ArrayList<>();
        bookshopPriceServices.add(new WaterstonesPriceService());
        bookshopPriceServices.add(amazonPriceService);

        return new BookPriceInfoCreator(bookshopPriceServices);
    }

    private AmazonPriceService createAmazonPriceService() {
        String awsAccessKeyId = System.getenv("MY_ACCESS_KEY_ID");
        String awsSecretKey = System.getenv("MY_SECRET_KEY");
        String associateId = System.getenv("MY_ASSOCIATE_ID");
        AmazonSignedUrlCreator amazonSignedUrlCreator = AmazonSignedUrlCreator.instance(awsAccessKeyId, awsSecretKey, associateId);

        return new AmazonPriceService(amazonSignedUrlCreator);
    }


}
