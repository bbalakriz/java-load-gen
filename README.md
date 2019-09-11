# java-load-gen
This application can be used to trigger load tests (specifically GET) to a configured target endpoint URL. 

do a 
  mvn clean install
 
and then do
  docker build --tag load-gen-jdk8:latest .
  
If you need to a ready made docker image, you can get one from here
  docker pull quay.io/balki404/load-gen-jdk8  

When using the docker image you can run it with the following env variables.

  docker run -it --rm -e ENDPOINT_URL='https://<end point>' -e THREAD_COUNT=1000 -e CONCURRENT_USERS=1000 -e TIMEOUT=1000 --net="host" load-gen-jdk8
