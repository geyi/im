PRG_PATH=$(dirname $0)
PRG_HOME=$(readlink -f $PRG_PATH/..)
CONFIG_PATH=$PRG_HOME/config
LIB_PATH=$PRG_HOME/lib
CLASS_PATH=$PRG_HOME:$LIB_PATH:$LIB_PATH/*:$CONFIG_PATH

BOOT_CLASS=com.airing.im.IMApplication

echo $CLASS_PATH

JAVA_OPTS="-Xms256M -Xmx256M -Dfile.encoding=UTF-8 -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -Xloggc:$PRG_HOME/logs/gc.log"

java -cp $CLASS_PATH $JAVA_OPTS $BOOT_CLASS
