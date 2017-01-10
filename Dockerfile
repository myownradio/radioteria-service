FROM openjdk:8-jdk

ARG USER_ID=1000
ARG GROUP_ID=1000

ARG MAVEN_VERSION=3.3.9
ARG USER_HOME_DIR="/home/app"

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

RUN groupadd --gid $GROUP_ID app && \
    useradd  --uid $USER_ID \
             --create-home \
             --home $USER_HOME_DIR \
             --gid app \
             --shell /bin/bash app

USER app

VOLUME [$USER_HOME_DIR]

EXPOSE 8080

