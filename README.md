# Jarvis [![Build Status](https://travis-ci.org/pivotal/jarvis.svg)](https://travis-ci.org/pivotal/jarvis)

![Wake up](wakeup.gif)

![Angry](angry.gif)

# Setup

1. Copy and update `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
2. Copy and update `src/main/resources/pivots.json.example` to `src/main/resources/pivots.json`
3. `mvn package exec:java`

# Notes

Here's a curl command to get all pivots:

~~~
curl -X GET "https://pivots.pivotallabs.com/api/users.json" \
     -H "Cookie: _pivots-two_session=LITERALLY_ANY_COOKIE_YOU_FIND_LYING_AROUND" \
     -m 30 \
     -v \
~~~

We can use [Skycons](https://github.com/darkskyapp/skycons) if we want darksky's animated weather icons.

To get the divvy bikes, use their [semi-official but not officially supported](http://www.divvybikes.com/stations/json) api.