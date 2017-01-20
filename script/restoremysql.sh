#! /bin/bash
dir=$1
echo dir: "$dir"
for sql in `find $dir -name *.sql`; do
   echo "processing $sql"
   mysql -u root --password=mysql --database mysql < $sql
done