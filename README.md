#=====================
#	REST API
#=====================


*REST API with two endpoints that implements the behavior stated below.*

1.POST a Message
The endpoint receives an input string and returns another string that follows the pattern: "$message from $country!" where $message is the input string and $country is the name of the country where the request comes from.

For example, if I call the server from Argentina with message "german" it shall return: "german from Argentina!", or if I call it from US it will return "german from United States!"

2.GET Messages 
The endpoint returns the list of messages the server received through endpoint #1.
The endpoint gets 2 input params where:

"numOf" is an integer that specifies the number of messages to return; there is a max num of messages M to return where M >= “numOf”;
"lang" a string specifying the language of the messages to return ("sp" or "en", for example) or "all" to return all messages irrespective of the language.

The endpoint shall return the messages in reverse reception order: newest message goes first, oldest messages goes last (the server shall honor the messages ordering whether "lang" is "all" or "lang" specifies a certain language)


### Dependencies

* Java 1.7 or higher
* Maven 3.2 or higher
* Elasticsearch 1.7
* www.db-ip.com

### Configuration

See src/main/resources/application.properties

Property | Description 
--- | --- 
es.host	| Host where Elasticsearch is running
es.port | port where Elasticsearch is running
es.cluster.name | Name of cluster of Elasticsearch
es.local.mode | If run as local mode true is useful for testing
db.ip.api.key | ApiKey of DB-IP services to get the country of request



### Build app

```
mvn clean install 
```

### Run app 

```
java -jar target/geolocalization-message-0.0.1.jar
```

### Post a message

```
curl -XPOST <host:port>/Hello pepe
```

### Get messages

```
curl -XGET <host:port>/?numOf=<[1-9]*>&country=[ar,es,...]
```


### GeoLocalization service
As mentioned in assumptions, I selected DB-IP [www.db-ip.com] GeoLocalization service, in our
case the application is using a free account that allow 2,500 queries per day to our IP
geolocation API services. In case that I need to queries for more IP, the cost can increase
around U$S 110/Month