# zchain

Blockchain implementation based on apache cassandra distributed database. We implemented all consensus 
based on cassandra cluster with scala language

# Set up and run

## 1. Run cassandra 

```
docker run -d -p 9160:9160 -p 9042:9042 erangaeb/cassandra:0.2
```

## 2. Connects to cassandra(cqlsh) 

```
# command
cqlsh <docker host> 9042 --cqlversion="3.4.4"

# example
cqlsh localhost 9042 --cqlversion="3.4.4"
```

## 3. Create keyspace

```
CREATE KEYSPACE zchain WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1}
```

## 4. Create tables

```
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

CREATE TYPE balance (
    bank_id TEXT,
    t_in INT,
    t_out INT
)

CREATE TABLE zchain.transactions (
    bank_id TEXT,
    id UUID,
    from_acc TEXT,
    to_acc TEXT,
    amount INT,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE zchain.blocks (
    bank_id TEXT,
    id UUID,
    hash TEXT,
    transactions SET<frozen <transaction>>,
    balances SET<frozen <balance>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)

CREATE TABLE zchain.keys (
    bank_id TEXT,
    id UUID,
    transactions SET<frozen <transaction>>,
    signatures SET<frozen <signature>>,
    timestamp BIGINT,

    PRIMARY KEY(bank_id, id)
)
```

## Create lucene index and query for test

```
CREATE CUSTOM INDEX transactions_index ON transactions ()
USING 'com.stratio.cassandra.lucene.Index'
WITH OPTIONS = {
   'refresh_seconds': '1',
   'schema': '{
      fields: {
         "bank_id": {type: "string"},
         "from_acc": {type: "string"},
         "to_acc": {type: "string"}
      }
   }'
}

SELECT * FROM transactions WHERE expr(transactions_index, '{
    filter: [
        {type: "wildcard", field:"bank_id", value:"sampath"},
        {type: "wildcard", field:"from_acc", value:"*"},
        {type: "wildcard", field:"to_acc", value:"*"}
    ]
}')

```

## 5. Run zchain

Run via docker compose

