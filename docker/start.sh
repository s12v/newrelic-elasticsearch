#!/bin/sh

name=${ES_NAME:-docker}
host=${ES_HOST:-elasticsearch}
port=${ES_PORT:-9200}
reconnects=${ES_RECONNECTS:-10}

plugin_path="plugin.json"

if [ -f "/nre-config/plugin.json" ]; then
   plugin_path="/nre-config/plugin.json"
fi


./npi config set license_key $NEW_RELIC_LICENSE_KEY
./npi prepare me.snov.newrelic-elasticsearch -n -q

sed -i "s/%HOST%/$host/g" plugin.json
sed -i "s/%PORT%/$port/g" plugin.json
sed -i "s/%NAME%/$name/g" plugin.json
mv $plugin_path `find . -path './plugins/me.snov.newrelic-elasticsearch/*/config/plugin.json'`

for i in `seq 1 $reconnects`
do
 	echo -n "Trying $host:$port... "
	if curl --silent "$host:$port" > /dev/null
	then
 		echo "OK"
		break
 	fi
	echo "NOT AVAILABLE"
	sleep 5
done

./npi start me.snov.newrelic-elasticsearch --foreground
