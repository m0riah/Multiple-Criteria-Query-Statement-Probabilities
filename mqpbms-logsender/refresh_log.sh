SCRIPT=refresh_general_log_table.sql
MYSQL_CONN="-udlp -ptester"
echo "Truncate table mysql.general_log;" > ${SCRIPT}
mysql ${MYSQL_CONN} < ${SCRIPT}



