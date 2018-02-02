[![Build Status](https://travis-ci.org/s12v/newrelic-elasticsearch.svg?branch=master)](https://travis-ci.org/s12v/newrelic-elasticsearch)
[![codecov](https://codecov.io/gh/s12v/newrelic-elasticsearch/branch/master/graph/badge.svg)](https://codecov.io/gh/s12v/newrelic-elasticsearch)
[![Docker Pulls](https://img.shields.io/docker/pulls/s12v/newrelic-elasticsearch.svg?maxAge=2592000)]()

# Newrelic elasticsearch plugin

Elasticsearch monitoring plugin. Polls cluster/nodes stats every minute and logs metrics, including RPMs, thread pool, merges, JVM and OS stats.

http://newrelic.com/plugins/sergey-novikov/299

![screen shot 2015-02-15 at 22 41 57](https://cloud.githubusercontent.com/assets/1462574/6205166/8c7b12ee-b565-11e4-9495-4fee5de919db.png)

## Installation

### Interactive installation with New Relic NPI

See https://docs.newrelic.com/docs/plugins/plugins-new-relic/installing-plugins/installing-npi-compatible-plugin
```
./npi install me.snov.newrelic-elasticsearch
```

### Docker

```
docker run -e "ES_HOST=localhost" -e "NEW_RELIC_LICENSE_KEY=..." s12v/newrelic-elasticsearch
```

More details: https://hub.docker.com/r/s12v/newrelic-elasticsearch/

### Building from source

```
gradle dist
ls build/distributions/
```
