CREATE TYPE transaction (
    bank_id TEXT,
    id INT,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    time LONG
);

CREATE TYPE sign {
    bank_id TEXT,
    digsig TEXT
}

CREATE TABLE transactions (
    bank_id TEXT,
    id INT,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    time LONG

    PRIMARY KEY(bank_id, id)
);

CREATE TABLE blocks {
    bank_id TEXT,
    id INT,
    transactions SET<frozen <transaction>>,
    signs SET<frozen <sign>>,
    time LONG,
    PRIMARY KEY(bank_id, id)
}
