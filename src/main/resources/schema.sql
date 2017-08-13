CREATE KEYSPACE IF NOT EXISTS zchain
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor': 1
    }

CREATE TYPE IF NOT EXISTS transaction (
    bank_id TEXT,
    id UUID,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    timestamp BIGINT
)

CREATE TYPE IF NOT EXISTS signature (
    bank_id TEXT,
    digsig TEXT
)

CREATE TABLE IF NOT EXISTS transactions (
    bank_id TEXT,
    id UUID,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE IF NOT EXISTS blocks (
    bank_id TEXT,
    id UUID,
    transactions SET<frozen <transaction>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE IF NOT EXISTS key_chain (
    bank_id TEXT,
    id UUID,
    transactions SET<frozen <transaction>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)
