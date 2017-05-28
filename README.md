## book-prices-lambda
An AWS lambda for getting book prices from Amazon and Waterstone's, using an ISBN

### Requires environment variables for Amazon product API use
MY_ASSOCIATE_ID -- should contain the associate ID of the account which will be used to query the Amazon product API

MY_ACCESS_KEY_ID -- access key id from the same account

MY_SECRET_KEY -- secret key from the same account

### Takes input in json of the following format
{

  "isbn": "9780349007441"
  
}
