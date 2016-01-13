#!/bin/sh
set -e

reconnects=${ES_RECONNECTS:-10}

plugin_path="/nre-config/plugin.json"
if ! [ -f $plugin_path ]; then
  plugin_path="plugin.json"
fi

name=`cat $plugin_path | jq -r '.agents[].name'`
host=`cat $plugin_path | jq -r '.agents[].host'`
port=`cat $plugin_path | jq -r '.agents[].port'`

./npi config set license_key $NEW_RELIC_LICENSE_KEY
./npi prepare me.snov.newrelic-elasticsearch -n -q

cp $plugin_path `find . -path './plugins/me.snov.newrelic-elasticsearch/*/config/plugin.json'`

for i in `seq 1 $reconnects`
do
 	echo -n "Trying $host:$port... "
	if curl --silent "$host:$port" > /dev/null
	then
 		echo "OK"
    echo "find '$name' instance under the plugins tab at https://rpm.newrelic.com"
		break
 	fi
	echo "NOT AVAILABLE"
	sleep 5
done

./npi start me.snov.newrelic-elasticsearch --foreground
