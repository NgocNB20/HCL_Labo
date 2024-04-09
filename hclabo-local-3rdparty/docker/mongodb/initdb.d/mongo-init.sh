#!/bin/bash

database_name=$MONGO_INITDB_DATABASE
user=$MONGO_INITDB_USER
password=$MONGO_INITDB_PWD

echo "<--------Started Adding the Users for write Log.----------->"
mongo --eval "db = db.getSiblingDB('$database_name');
             db.createUser({
                 user: '$user',
                 pwd: '$password',
                 roles: [{ role: 'readWrite', db: '$database_name' }]
             });
             db.createCollection('orderSearchCsvOptionQueryModel');"
echo "<--------End Adding the User.--------->"

echo "<--------Started Insert DML----------->"
dump_data_dir="/tmp/dump_data"
for file in "$dump_data_dir"/*.json; do
    json=$(cat "$file")
    mongo --eval "db = db.getSiblingDB('$database_name');
                db.orderSearchCsvOptionQueryModel.insert($json);"
done
echo "<--------End Insert DML----------->"
