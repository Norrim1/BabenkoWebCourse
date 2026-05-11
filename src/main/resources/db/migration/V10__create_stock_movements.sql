CREATE TABLE stock_movements (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    from_warehouse_id BIGINT,
    to_warehouse_id BIGINT,

    quantity INTEGER NOT NULL,

    type VARCHAR(100) NOT NULL,

    reason VARCHAR(255),

    created_by_user_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_movement_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_movement_from_warehouse
        FOREIGN KEY (from_warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_movement_to_warehouse
        FOREIGN KEY (to_warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_movement_user
        FOREIGN KEY (created_by_user_id)
        REFERENCES users(id)
);