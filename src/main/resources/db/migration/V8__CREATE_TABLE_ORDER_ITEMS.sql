DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items
(
    bookid     VARCHAR(50)    NOT NULL,
    orderid    VARCHAR(50)    NOT NULL,
    quantity   NUMERIC        NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL
);

ALTER TABLE order_items
    ADD CONSTRAINT PK_order_items PRIMARY KEY (bookid, orderid);