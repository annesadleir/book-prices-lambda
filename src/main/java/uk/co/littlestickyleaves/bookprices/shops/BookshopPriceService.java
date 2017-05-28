package uk.co.littlestickyleaves.bookprices.shops;

import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;

/**
 * A service for checking prices at a bookshop
 */
public abstract class BookshopPriceService {

    private final Bookshop bookshop;

    public BookshopPriceService(Bookshop bookshop) {
        this.bookshop = bookshop;
    }

    public abstract PriceAtWebsite checkPrice(ISBN isbn);

    public Bookshop getBookshop() {
        return bookshop;
    }
}
