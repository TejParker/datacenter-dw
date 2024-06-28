BIN_HOME=`cd $(dirname "$0");pwd`
APP_HOME=`cd ${BIN_HOME}/..;pwd`
COMMON_LIB="${APP_HOME}/lib/common"

mainJar="${APP_HOME}/lib/datacenter-dw-1.0-SNAPSHOT.jar"

libJars=`ls ${APP_HOME}/lib/common/*.jar | awk '{print $1}' | tr "\n" "," | sed 's/.$//'`

spark-submit \
--master local[*] \
--deploy-mode client \
--class com.ziyun.datacenter.mysql2hdfs.Mysql2Hdfs \
--jars ${libJars} \
${mainJar}