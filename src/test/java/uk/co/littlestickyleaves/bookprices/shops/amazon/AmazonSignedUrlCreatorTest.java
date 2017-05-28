package uk.co.littlestickyleaves.bookprices.shops.amazon;

import org.junit.Assert;
import org.junit.Test;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.*;

public class AmazonSignedUrlCreatorTest {

    // just a simple test to compare input and output for the sake of future regression
    @Test
    public void createUrl() throws Exception {
        // arrange
        AmazonSignedUrlCreator testObject = AmazonSignedUrlCreator.instance("awsAccessKeyId",
                "awsSecretKey", "associateId");
        ISBN isbn = new ISBN("9780349007441");
        testObject.setClock(Clock.fixed(Instant.ofEpochMilli(1495999679336L), ZoneId.of("UTC")));
        String expectedResult = "http://webservices.amazon.co.uk/onca/xml?AWSAccessKeyId=awsAccessKeyId&AssociateTag=associateId&IdType=ISBN&ItemId=9780349007441&Operation=ItemLookup&ResponseGroup=Large&SearchIndex=All&Service=AWSECommerceService&Timestamp=2017-05-28T19%3A27%3A59.336&Signature=pWfOBs4pc3A0TckDTcZjmcBUZCySUs2dkSfm3FYWOMM%3D";

        // act
        String result = testObject.createUrl(isbn);

        // assert
        Assert.assertEquals(expectedResult, result);
    }

}