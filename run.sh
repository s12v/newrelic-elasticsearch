#! /bin/bash
./npi config set license_key $NEW_RELIC_LICENSE_KEY
./npi prepare me.snov.newrelic-elasticsearch --noedit --override --quiet
PLUGIN_TEMPLATE_FILE=plugins/me.snov.newrelic-elasticsearch/newrelic-elasticsearch-plugin/config/plugin.template.json
PLUGIN_FILE=plugins/me.snov.newrelic-elasticsearch/newrelic-elasticsearch-plugin/config/plugin.json
cat $PLUGIN_TEMPLATE_FILE | envsubst > $PLUGIN_FILE
if [ "$1" == "bash" ]; then
    exec bash
else
    exec ./npi start me.snov.newrelic-elasticsearch --foreground
fi
