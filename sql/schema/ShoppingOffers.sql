DROP TABLE ShoppingOffers;
CREATE TABLE ShoppingOffers (
  UserId BIGINT NOT NULL,
  ShoppingOfferId BIGINT PRIMARY KEY IDENTITY,
  ShoppingRequestId BIGINT REFERENCES ShoppingRequests(ShoppingRequestId) NULL,
  Text VARCHAR(200) NOT NULL DEFAULT '',
  Price BIGINT NOT NULL, -- smallest unit. penny in USD
  State VARCHAR(5) NOT NULL,
  CreatedAt DATETIME NOT NULL DEFAULT SYSDATETIME()
)
