CREATE TYPE transaction (
    bank_id TEXT,
    id INT,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    timestamp BIGINT
)

CREATE TYPE signature (
    bank_id TEXT,
    digsig TEXT
)

CREATE TABLE transactions (
    bank_id TEXT,
    id INT,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE blocks (
    bank_id TEXT,
    id INT,
    transactions SET<frozen <transaction>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)
