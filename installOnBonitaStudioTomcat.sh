#!/bin/sh
#set -xv

BONITA_BASE="/home/bonita/Downloads/toDelete/FormV6_to_V7_Migration/BonitaStudioSubscription-7.7.5/workspace/tomcat"
BONITA_WAR="$BONITA_BASE/server/webapps/bonita.war"

# Patch bonita.war app directly in the webapps folder of Tomcat
mkdir -p tmpLibFolder/WEB-INF/lib

cp url-rewriter/build/libs/url-rewriter*.jar tmpLibFolder/WEB-INF/lib/.
cp url-rewriter/build/resources/main/web.xml tmpLibFolder/WEB-INF/.

cd tmpLibFolder
zip $BONITA_WAR WEB-INF/* WEB-INF/lib/*

cd -
rm -rf tmpLibFolder
