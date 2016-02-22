[![Build Status](https://travis-ci.org/s12v/newrelic-elasticsearch.svg?branch=master)](https://travis-ci.org/s12v/newrelic-elasticsearch)
[![codecov.io](https://codecov.io/github/s12v/newrelic-elasticsearch/coverage.svg?branch=master)](https://codecov.io/github/s12v/newrelic-elasticsearch?branch=master)

# Newrelic elasticsearch plugin

Elasticsearch monitoring plugin. Polls cluster/nodes stats every minute and logs metrics, including RPMs, thread pool, merges, JVM and OS stats.

http://newrelic.com/plugins/sergey-novikov/299

![screen shot 2015-02-15 at 22 41 57](https://cloud.githubusercontent.com/assets/1462574/6205166/8c7b12ee-b565-11e4-9495-4fee5de919db.png)

## Installation

### Simple interactive installation with NPI

See https://docs.newrelic.com/docs/plugins/plugins-new-relic/installing-plugins/installing-npi-compatible-plugin
```
./npi install me.snov.newrelic-elasticsearch
```

### Docker

See https://hub.docker.com/r/s12v/newrelic-elasticsearch/
