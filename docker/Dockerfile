FROM java:8-jre
MAINTAINER Sergey Novikov <snov@snov.me>

RUN apt-get update && apt-get install jq
RUN UNATTENDED=true bash -c "$(curl -sSL https://download.newrelic.com/npi/release/install-npi-linux-debian-x64.sh)"

WORKDIR /root/newrelic-npi
RUN ./npi fetch me.snov.newrelic-elasticsearch -y

COPY plugin.json ./plugin.json
COPY start.sh ./start.sh

CMD bash start.sh

VOLUME "/nre-config"
