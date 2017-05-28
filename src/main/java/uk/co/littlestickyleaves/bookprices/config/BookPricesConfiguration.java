package uk.co.littlestickyleaves.bookprices.config;

import com.google.common.collect.Lists;
import uk.co.littlestickyleaves.bookprices.BookPriceInfoCreator;
import uk.co.littlestickyleaves.bookprices.shops.BookshopPriceService;
import uk.co.littlestickyleaves.bookprices.shops.amazon.AmazonPriceService;
import uk.co.littlestickyleaves.bookprices.shops.amazon.AmazonSignedUrlCreator;
import uk.co.littlestickyleaves.bookprices.shops.waterstones.WaterstonesPriceService;

import java.util.List;
import java.util.function.Supplier;

/**
 * Sets up the objects
 */
public class BookPricesConfiguration implements Supplier<BookPriceInfoCreator> {

    public BookPriceInfoCreator get() {

        String awsAccessKeyId = System.getenv("MY_ACCESS_KEY_ID");
        String awsSecretKey = System.getenv("MY_SECRET_KEY");
        String associateId = System.getenv("MY_ASSOCIATE_ID");
        AmazonSignedUrlCreator amazonSignedUrlCreator = new AmazonSignedUrlCreator(awsAccessKeyId, awsSecretKey, associateId);

        List<BookshopPriceService> bookshopPriceServices = Lists.newArrayList(
                new WaterstonesPriceService(),
                new AmazonPriceService(amazonSignedUrlCreator)
        );

        return new BookPriceInfoCreator(bookshopPriceServices);
    }
}
