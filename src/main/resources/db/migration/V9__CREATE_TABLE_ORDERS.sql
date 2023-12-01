DROP TABLE IF EXISTS orders;
CREATE TABLE orders
(
    id             VARCHAR(50) PRIMARY KEY,
    userid         VARCHAR(20)    NOT NULL,
    address        VARCHAR(200)   NOT NULL,
    state          VARCHAR(50)    NOT NULL,
    country        VARCHAR(20)    NOT NULL,
    pincode        VARCHAR(10)    NOT NULL,
    mobilenum      VARCHAR(15)    NOT NULL,
    alt_mobile_num VARCHAR(15)    NOT NULL,
    total_amt      NUMERIC(12, 2) NOT NULL,
    payment_mode   VARCHAR(5)     NOT NULL,
    payment_id     VARCHAR(15),
    status         VARCHAR(10)    NOT NULL,
    created_date   TIMESTAMP      NOT NULL,
    updated_date   TIMESTAMP      NOT NULL
);