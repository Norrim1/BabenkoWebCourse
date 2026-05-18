CREATE TABLE purchase_orders (
    id BIGSERIAL PRIMARY KEY,

    supplier_id BIGINT NOT NULL,

    status VARCHAR(100),

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_po_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id)
);