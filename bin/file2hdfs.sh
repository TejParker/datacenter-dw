APP_HOME=`cd $(dirname "$0")/..;pwd`

CLASSPATH=`ls ${APP_HOME}/lib/core/*.jar ${APP_HOME}/lib/core/lib/*.jar  | tr "\n" ":" | sed 's/.$//'`

CMD_ARGS=$@

currentMinute=`date "+%Y%m%d%H%M"`
day=${currentMinute:0:8}
min=${currentMinute:8:4}
childDir=${day}/${min}


java -cp ${CLASSPATH} \
com.ziyun.tools.hdfs.File2HDFSToolV2 \
${CMD_ARGS} -s ${childDir}