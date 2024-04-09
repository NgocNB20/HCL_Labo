#!/bin/bash

set -e
set -u

function create_user_and_database() {
	local database=$1
	echo "  Creating user and database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE USER $database with PASSWORD 'password';
	    CREATE DATABASE $database;
	    GRANT ALL PRIVILEGES ON DATABASE $database TO $database;
		ALTER DATABASE $database OWNER TO $database;
EOSQL
}

function init_database() {
    local database=$1

    echo "Run script init DDL "$database
    for file in /tmp/dump_data/"$database"/common/30_DB/10_DDL/10_DEV/4.0.0/*.sql; do
        psql -U $database -d $database <<-EOSQL
            \i $file;
EOSQL
    done

    echo "Run script init DML "$database
    for file in /tmp/dump_data/"$database"/common/30_DB/20_DML/*.sql; do
        psql -U $database -d $database <<-EOSQL
            \i $file;
EOSQL
    done
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
	echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
	for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
		create_user_and_database $db
		init_database $db
	done
	echo "Multiple databases created"
fi
