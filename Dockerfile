FROM registry.redhat.io/openjdk/openjdk-8-rhel8
COPY target/load-1.0.0.jar /deployments/app.jar
COPY run-load.sh /deployments
CMD /deployments/run-load.sh

