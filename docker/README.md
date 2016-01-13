
# How to use this image

You can run elasticsearch command simply:
```
$ docker run --name elasticsearch -d elasticsearch
```
than you need to attach the monitor daemon:
```
$ docker run -d --link elasticsearch -e NEW_RELIC_LICENSE_KEY=<YOUR_KEY> s12v/newrelic-elasticsearch 
```
This image comes with the default ```plugin.json``` configuration file:
```
{
  "agents": [
    {
      "host" : "elasticsearch",
      "port" : 9200,
      "_name": "Optional. By default loaded from elasticsearch. Rename to 'name' if you need to customize it"
    }
  ]
}
```

if you want to provide your own configuration file, you can do so via a volume mounted at /nre-config:

```
$ docker run -d --link elasticsearch -e NEW_RELIC_LICENSE_KEY=<YOUR_KEY> -v /dir/with/config:/nre-config s12v/newrelic-elasticsearch 
```

if you want to access elasticsearch instances that are not linked, simply use the hosts network:
```
$ docker run -d -e NEW_RELIC_LICENSE_KEY=<YOUR_KEY> -v /dir/with/config:/nre-config --net=host s12v/newrelic-elasticsearch
```

you can also change the default reconnect attempts from 10 to whatever you like,
by changing the ```ES_RECONNECTS``` environment variable.

# usage with compose

Example ```docker-compose.yml``` defines a cluster with 3 elasticsearch nodes and 1 New Relic monitoring container:
```
es1:
  image: elasticsearch
  ports:
  - "9200:9200"
  links:
  - es2
  - es3
es2:
  image: elasticsearch
es3:
  image: elasticsearch
newrelic:
  image: s12v/newrelic-elasticsearch
  links:
    - es1:elasticsearch
  environment:
    - NEW_RELIC_LICENSE_KEY=itsasecret
```

Change your license key and run ```docker-compose up -d```.
