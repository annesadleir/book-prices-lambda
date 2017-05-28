package uk.co.littlestickyleaves.bookprices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import uk.co.littlestickyleaves.bookprices.config.BookPricesConfiguration;
import uk.co.littlestickyleaves.bookprices.domain.Bookshop;
import uk.co.littlestickyleaves.bookprices.domain.ISBN;
import uk.co.littlestickyleaves.bookprices.domain.PriceAtWebsite;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Handles the Lambda request
 */
public class BookPricesRequestHandler implements RequestHandler<ISBN, Map<Bookshop, PriceAtWebsite>> {

    private Supplier<BookPriceInfoCreator> bookPriceInfoCreatorSupplier;

    public BookPricesRequestHandler() {
        bookPriceInfoCreatorSupplier = new BookPricesConfiguration();
    }

    public Map<Bookshop, PriceAtWebsite> handleRequest(ISBN isbn, Context context) {
        BookPriceInfoCreator bookPriceInfoCreator = bookPriceInfoCreatorSupplier.get();
        return bookPriceInfoCreator.start(isbn);
    }
}
