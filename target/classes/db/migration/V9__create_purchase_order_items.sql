CREATE TABLE purchase_order_items (
    id BIGSERIAL PRIMARY KEY,

    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    quantity INTEGER NOT NULL,

    CONSTRAINT fk_poi_order
        FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders(id),

    CONSTRAINT fk_poi_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);