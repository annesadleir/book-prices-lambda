package uk.co.littlestickyleaves.bookprices;

import org.junit.Test;
import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookPricesRequestHandlerTest {

    private BookPricesRequestHandler testObject;

    @Test
    public void handleRequest() throws Exception {
        // arrange
        testObject = new BookPricesRequestHandler();
        ISBN isbn = new ISBN("9780349007441");
        BookPriceInfoCreator mockBookPriceInfoCreator = mock(BookPriceInfoCreator.class);
        testObject.setBookPriceInfoCreatorSupplier(() -> mockBookPriceInfoCreator);
        Map<Bookshop, PriceAtWebsite> expectedResult = new HashMap<>();
        when(mockBookPriceInfoCreator.start(isbn)).thenReturn(expectedResult);

        // act
        Map<Bookshop, PriceAtWebsite> result = testObject.handleRequest(isbn, null);

        // assert
        assertEquals(expectedResult, result);
    }
}