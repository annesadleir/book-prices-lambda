package uk.co.littlestickyleaves.bookprices.shops.amazon;

import org.junit.Ignore;
import org.junit.Test;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;

/**
 * Created by Rebecca on 27/05/2017.
 */
@Ignore
public class AmazonPriceServiceTest {

    

    @Ignore
    @Test
    public void tryItOut() throws Exception {
        // arrange
        String awsAccessKeyId = System.getenv("MY_ACCESS_KEY_ID");
        String awsSecretKey = System.getenv("MY_SECRET_KEY");
        String associateId = System.getenv("MY_ASSOCIATE_ID");
        AmazonSignedUrlCreator amazonSignedUrlCreator = new AmazonSignedUrlCreator(awsAccessKeyId, awsSecretKey, associateId);

        AmazonPriceService amazonPriceService = new AmazonPriceService(amazonSignedUrlCreator);

        // act
        PriceAtWebsite priceAtWebsite = amazonPriceService.checkPrice(new ISBN("9780241242728"));

        // assert
        System.out.println(priceAtWebsite);
    }
}