CREATE KEYSPACE zchain WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1}

CREATE TYPE transaction (
    bank_id TEXT,
    id UUID,
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
    id UUID,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE blocks (
    bank_id TEXT,
    id UUID,
    transactions SET<frozen <transaction>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE key_chain (
    bank_id TEXT,
    id UUID,
    transactions SET<frozen <transaction>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)
