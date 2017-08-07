# 注意: 导出的sql中含有drop table, 属于全量备份  --where 可以添加条件
mysqldump -u root --password=mysql mysql ST_CONFIG > ../backup/database/tables/170722/ST_CONFIG.sql;
#mysqldump -u root --password=mysql mysql ST_HISTORY > ../backup/database/tables/170722/ST_HISTORY.sql;
mysqldump -u root --password=mysql mysql ST_INDEX > ../backup/database/tables/170722/ST_INDEX.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_HOT > ../backup/database/tables/170722/ST_INDUSTRY_HOT.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_HOT_STOCKS > ../backup/database/tables/170722/ST_INDUSTRY_HOT_STOCKS.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_INFO > ../backup/database/tables/170722/ST_INDUSTRY_INFO.sql;
mysqldump -u root --password=mysql mysql ST_INDUSTRY_STOCK > ../backup/database/tables/170722/ST_INDUSTRY_STOCK.sql;
mysqldump -u root --password=mysql mysql ST_INFO > ../backup/database/tables/170722/ST_INFO.sql;
mysqldump -u root --password=mysql mysql ST_JOB > ../backup/database/tables/170722/ST_JOB.sql;
#mysqldump -u root --password=mysql mysql ST_MA > ../backup/database/tables/170722/ST_MA.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_HOT > ../backup/database/tables/170722/ST_NOTION_HOT.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_HOT_STOCKS > ../backup/database/tables/170722/ST_NOTION_HOT_STOCKS.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_INFO > ../backup/database/tables/170722/ST_NOTION_INFO.sql;
mysqldump -u root --password=mysql mysql ST_NOTION_STOCK > ../backup/database/tables/170722/ST_NOTION_STOCK.sql;
mysqldump -u root --password=mysql mysql ST_STOCK_CAP_FLOW > ../backup/database/tables/170722/ST_STOCK_CAP_FLOW.sql;
mysqldump -u root --password=mysql mysql ST_STOCK_FIVE_CHANGE > ../backup/database/tables/170722/ST_STOCK_FIVE_CHANGE.sql;
