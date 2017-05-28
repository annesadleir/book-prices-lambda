package uk.co.littlestickyleaves.bookprices.shops.amazon;

import uk.co.littlestickyleaves.bookprices.domain.ISBN;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static uk.co.littlestickyleaves.bookprices.config.BookPricesConfiguration.UTF8;

/**
 * originally from http://docs.aws.amazon.com/AWSECommerceService/latest/DG/AuthJavaSampleSig2.html
 * but with much adaptation
 */

public class AmazonSignedUrlCreator {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String REQUEST_URI = "/onca/xml";
    private static final String REQUEST_METHOD = "GET";
    private static final String ENDPOINT = "webservices.amazon.co.uk"; // must be lowercase

    private final String awsAccessKeyId;
    private final String associateId;
    private final Mac mac;
    private Clock clock = Clock.systemUTC();

    protected AmazonSignedUrlCreator(String awsAccessKeyId, String associateId, Mac mac) {
        this.associateId = associateId;
        this.awsAccessKeyId = awsAccessKeyId;
        this.mac = mac;
    }

    public static AmazonSignedUrlCreator instance(String awsAccessKeyId, String awsSecretKey, String associateId) {
        byte[] secretKeyBytes = awsSecretKey.getBytes(UTF8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA256);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            return new AmazonSignedUrlCreator(awsAccessKeyId, associateId, mac);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to setup Message-Authentication Code algorithm for Amazon API signing", e);
        }
    }

    public String createUrl(ISBN isbn) {
        Map<String, String> parameters = createParameterMap(isbn);
        String parametersAsOrderedString = convertToOrderedString(parameters);
        String toSign = REQUEST_METHOD + "\n"
                + ENDPOINT + "\n"
                + REQUEST_URI + "\n"
                + parametersAsOrderedString;

        String signature = percentEncodeRfc3986(hmac(toSign));

        return "http://" + ENDPOINT + REQUEST_URI + "?" +
                parametersAsOrderedString + "&Signature=" + signature;
    }

    private String convertToOrderedString(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(this::entryToString)
                .sorted()
                .collect(Collectors.joining("&"));
    }

    private String entryToString(Map.Entry<String, String> entry) {
        return percentEncodeRfc3986(entry.getKey()) + "=" +
                percentEncodeRfc3986(entry.getValue());
    }

    private Map<String, String> createParameterMap(ISBN isbn) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Service", "AWSECommerceService");
        parameters.put("Operation", "ItemLookup");
        parameters.put("ResponseGroup", "Large");
        parameters.put("SearchIndex", "All");
        parameters.put("IdType", "ISBN");
        parameters.put("ItemId", isbn.getIsbn());
        parameters.put("AssociateTag", associateId);
        parameters.put("AWSAccessKeyId", awsAccessKeyId);
        parameters.put("Timestamp", timestamp());
        return parameters;
    }

    private String hmac(String stringToSign) {
        byte[] rawHmac = mac.doFinal(stringToSign.getBytes(UTF8));
        byte[] encoded = Base64.getEncoder().encode(rawHmac);
        return new String(encoded, UTF8);
    }

    private String timestamp() {
        ZonedDateTime now = ZonedDateTime.now(clock);
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("UTC"))
                .format(now);
    }

    private String percentEncodeRfc3986(String s) {
        try {
            return URLEncoder.encode(s, UTF8.displayName())
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(UTF8.displayName() + " is not a supported encoding");
        }
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}