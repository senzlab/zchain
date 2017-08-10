# zchain

## about zchain

blockchain implementation based on apache cassandra distributed database. We implemented all consensus 
based on cassandra cluster with scala langauge

## run cassandra 

run dockerize cassandra

```
docker run -d -p 9160:9160 -p 9042:9042 erangaeb/cassandra:0.2
```

## create keyspace and tables

first connect to cassandra via cqlsh 

```
# command
cqlsh <docker host> 9042 --cqlversion="3.4.4"

# example
cqlsh localhost 9042 --cqlversion="3.4.4"
```

then create keyspace

```
CREATE KEYSPACE zchain WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1}
```

finally create tables and user define types(UDT's)

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
```

## run zchain

Simply run main method. We will dockerize this project later