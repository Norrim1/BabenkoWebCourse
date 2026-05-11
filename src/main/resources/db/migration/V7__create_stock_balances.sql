CREATE TABLE stock_balances (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,

    quantity INTEGER NOT NULL,

    version BIGINT,

    CONSTRAINT fk_stock_balance_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_stock_balance_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT uq_product_warehouse
        UNIQUE(product_id, warehouse_id)
);