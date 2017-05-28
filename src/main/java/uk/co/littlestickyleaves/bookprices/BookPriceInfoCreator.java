package uk.co.littlestickyleaves.bookprices;

import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;
import uk.co.littlestickyleaves.bookprices.shops.BookshopPriceService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds the price information
 */
public class BookPriceInfoCreator {

    private List<BookshopPriceService> bookshopPriceServices;

    public BookPriceInfoCreator(List<BookshopPriceService> bookshopPriceServices) {
        this.bookshopPriceServices = bookshopPriceServices;
    }

    public Map<Bookshop, PriceAtWebsite> start(ISBN isbn) {
        return bookshopPriceServices.stream()
                .collect(Collectors.toMap(
                        BookshopPriceService::getBookshop,
                        bps -> bps.checkPrice(isbn)
                ));
    }
}
