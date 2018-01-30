#!/bin/bash
# 将本地数据库内容更新到ecs

# 注意: 导出的sql中含有drop table, 属于全量备份  --where 可以添加条件
/bin/rm -rf /home/leslie/MyProject/StockAnalyse/backup/database/tables/180127
mkdir /home/leslie/MyProject/StockAnalyse/backup/database/tables/180127
mysqldump -u root --password=mysql mysql ST_CONFIG > ../backup/database/tables/180127/ST_CONFIG.sql;
mysqldump -u root --password=mysql mysql ST_INDEX > ../backup/database/tables/180127/ST_INDEX.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_HOT > ../backup/database/tables/180127/ST_INDUSTRY_HOT.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_HOT_STOCKS > ../backup/database/tables/180127/ST_INDUSTRY_HOT_STOCKS.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_INFO > ../backup/database/tables/180127/ST_INDUSTRY_INFO.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_STOCK > ../backup/database/tables/180127/ST_INDUSTRY_STOCK.sql;
mysqldump -u root --password=mysql mysql ST_INFO > ../backup/database/tables/180127/ST_INFO.sql;
mysqldump -u root --password=mysql mysql ST_JOB > ../backup/database/tables/180127/ST_JOB.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_HOT > ../backup/database/tables/180127/ST_NOTION_HOT.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_HOT_STOCKS > ../backup/database/tables/180127/ST_NOTION_HOT_STOCKS.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_INFO > ../backup/database/tables/180127/ST_NOTION_INFO.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_STOCK > ../backup/database/tables/180127/ST_NOTION_STOCK.sql;
mysqldump -u root --password=mysql mysql ST_STOCK_CAP_FLOW > ../backup/database/tables/180127/ST_STOCK_CAP_FLOW.sql;
mysqldump -u root --password=mysql mysql ST_STOCK_FIVE_CHANGE > ../backup/database/tables/180127/ST_STOCK_FIVE_CHANGE.sql;
mysqldump -u root --password=mysql mysql ST_LHB_DETAIL > ../backup/database/tables/180127/ST_LHB_DETAIL.sql;


cd /home/leslie/MyProject/StockAnalyse/backup/database/tables;
tar -zcv -f 180127.tar.gz 180127
scp 180127.tar.gz  leslie@59.110.235.115:~/MyProject/data/mysql

ssh leslie@59.110.235.115 "cd ~/MyProject/data/mysql/; tar -zxv -f 180127.tar.gz; ~/Shortcut/restoremysql.sh 180127;"

