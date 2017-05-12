FROM java:8-jre
MAINTAINER infrastructure-team@points.com
ENV NEW_RELIC_LICENSE_KEY deadbeefcafebabe
RUN apt-get update && apt-get install -y gettext-base
RUN UNATTENDED=true bash -c "$(curl -sSL https://download.newrelic.com/npi/release/install-npi-linux-debian-x64.sh)"
WORKDIR /root/newrelic-npi
RUN mkdir -p plugins/me.snov.newrelic-elasticsearch/newrelic-elasticsearch-plugin
COPY .manifest plugins/me.snov.newrelic-elasticsearch/
COPY build/libs/plugin-2.3.0.jar plugins/me.snov.newrelic-elasticsearch/newrelic-elasticsearch-plugin/plugin.jar
COPY config plugins/me.snov.newrelic-elasticsearch/newrelic-elasticsearch-plugin/config
COPY run.sh .
ENTRYPOINT ./run.sh
