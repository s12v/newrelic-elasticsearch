[![Build Status](https://travis-ci.org/s12v/newrelic-elasticsearch.svg?branch=master)](https://travis-ci.org/s12v/newrelic-elasticsearch)
[![codecov](https://codecov.io/gh/s12v/newrelic-elasticsearch/branch/master/graph/badge.svg)](https://codecov.io/gh/s12v/newrelic-elasticsearch)
[![Docker Pulls](https://img.shields.io/docker/pulls/s12v/newrelic-elasticsearch.svg?maxAge=2592000)]()

# Newrelic elasticsearch plugin

Elasticsearch monitoring plugin. Polls cluster/nodes stats every minute and logs metrics, including RPMs, thread pool, merges, JVM and OS stats.

http://newrelic.com/plugins/sergey-novikov/299

![screen shot 2015-02-15 at 22 41 57](https://cloud.githubusercontent.com/assets/1462574/6205166/8c7b12ee-b565-11e4-9495-4fee5de919db.png)

## Installation

First, build the plugin jar file:

    gradle fatjar

Build the image:

    docker build -t newrelic-elasticsearch .

or:

    make build

Make a proper envfile. For an example, see env.sample.

Run the container:

    docker run --env-file=envfile newrelic-elasticsearch
