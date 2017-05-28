package uk.co.littlestickyleaves.bookprices;

import com.google.common.collect.Lists;
import org.junit.Test;
import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;
import uk.co.littlestickyleaves.bookprices.shops.BookshopPriceService;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BookPriceInfoCreatorTest {

    private BookPriceInfoCreator testObject;

    @Test
    public void testStart() throws Exception {
        // arrange
        ISBN isbn = new ISBN("9780349007441");
        BookshopPriceService mockedBookshopPriceService = mock(BookshopPriceService.class);
        testObject = new BookPriceInfoCreator(Lists.newArrayList(mockedBookshopPriceService, mockedBookshopPriceService));
        when(mockedBookshopPriceService.getBookshop()).thenReturn(Bookshop.AMAZON, Bookshop.WATERSTONES);
        PriceAtWebsite firstPriceAtWebsite = new PriceAtWebsite("first_url", 1.00);
        PriceAtWebsite secondPriceAtWebsite = new PriceAtWebsite("second_url", 2.00);
        when(mockedBookshopPriceService.checkPrice(isbn)).thenReturn(firstPriceAtWebsite, secondPriceAtWebsite);

        // act
        Map<Bookshop, PriceAtWebsite> result = testObject.start(isbn);

        // assert
        assertEquals(2, result.size());
        PriceAtWebsite amazon = result.get(Bookshop.AMAZON);
        PriceAtWebsite waterstones = result.get(Bookshop.WATERSTONES);
        assertNotNull(amazon);
        assertNotNull(waterstones);
        assertEquals(firstPriceAtWebsite, amazon);
        assertEquals(secondPriceAtWebsite, waterstones);
    }


}