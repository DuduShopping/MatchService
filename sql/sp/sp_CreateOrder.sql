CREATE PROCEDURE sp_CreateOrder(
  @UserId BIGINT,
  @ShoppingRequestId BIGINT,
  @ShoppingOfferId BIGINT
) AS

DECLARE @Error INT = 0
DECLARE @RequestText VARCHAR(200)
DECLARE @RequestCreatedAt DATETIME
DECLARE @OfferText VARCHAR(300)
DECLARE @OfferPrice BIGINT
DECLARE @OfferCreatedAt DATETIME
DECLARE @OrderState VARCHAR(5) = 'RD1'
DECLARE @OrderId BIGINT

SELECT @RequestText = Text, @RequestCreatedAt = CreatedAt FROM ShoppingRequests WHERE ShoppingRequestId = @ShoppingRequestId AND ShoppingOfferAccepted = @ShoppingOfferId AND State = 'SR15' AND UserId = @UserId
IF @@ERROR <> 0 OR @@ROWCOUNT <> 1 BEGIN
  SET @Error = 5
  GOTO ExitProc
END

SELECT @OfferText = Text, @OfferPrice = Price, @OfferCreatedAt = CreatedAt FROM ShoppingOffers WHERE ShoppingOfferId = @ShoppingOfferId AND State = 'SF5' AND UserId = @UserId
IF @@ERROR <> 0 OR @@ROWCOUNT <> 1 BEGIN
  SET @Error = 10
  GOTO ExitProc
END

SET @RequestText = ISNULL(@RequestText, '')
SET @OfferText = ISNULL(@OfferText, '')

BEGIN TRANSACTION

UPDATE ShoppingRequests SET State = 'SR30' WHERE ShoppingRequestId = @ShoppingRequestId
IF @@ERROR <> 0 OR @@ROWCOUNT <> 1 BEGIN
  SET @Error = 15
  GOTO ExitProc
END

UPDATE ShoppingOffers SET State = 'SF35' WHERE ShoppingOfferId = @ShoppingOfferId
IF @@ERROR <> 0 OR @@ROWCOUNT <> 1 BEGIN
  SET @Error = 20
  GOTO ExitProc
END

INSERT INTO ShoppingOrders(UserId, RequestId, RequestText, RequestCreatedAt, OfferId, OfferText, OfferPrice, OfferCreatedAt, OrderState) VALUES (@UserId, @ShoppingRequestId, @RequestText, @RequestCreatedAt, @ShoppingOfferId, @OfferText, @OfferPrice, @OfferCreatedAt, @OrderState)
IF @@ERROR <> 0 OR @@ROWCOUNT <> 1 BEGIN
  SET @Error = 25
  GOTO ExitProc
END

SET @OrderId = @@IDENTITY


ExitProc:
  IF @@TRANCOUNT <> 0 BEGIN
    IF @Error <> 0
      ROLLBACK
    ELSE
      COMMIT TRANSACTION
  END

  SELECT @Error AS Error, @OrderId AS OrderId

