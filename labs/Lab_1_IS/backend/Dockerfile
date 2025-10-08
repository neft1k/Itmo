FROM gradle:8.8-jdk17 AS build
WORKDIR /workspace
# Кладём только нужное для сборки (ускоряет кеширование)
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean build -x test




FROM quay.io/wildfly/wildfly:31.0.1.Final-jdk17

ENV JBOSS_HOME=/opt/jboss/wildfly \
    WF_BIN=/opt/jboss/wildfly/bin \
    PG_MODULE_DIR=/opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main

USER root
RUN mkdir -p ${PG_MODULE_DIR} \
 && curl -fsSL -o ${PG_MODULE_DIR}/postgresql-42.7.3.jar \
      https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.3/postgresql-42.7.3.jar
COPY module.xml ${PG_MODULE_DIR}/module.xml
RUN chown -R jboss:jboss /opt/jboss/wildfly/modules
USER jboss

COPY add-ds.cli ${WF_BIN}/add-ds.cli
RUN ${WF_BIN}/jboss-cli.sh --file=${WF_BIN}/add-ds.cli \
 && rm -rf ${JBOSS_HOME}/standalone/configuration/standalone_xml_history


COPY --from=build /workspace/build/libs/app.war ${JBOSS_HOME}/standalone/deployments/app.war

EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh","-b","0.0.0.0","-bmanagement","0.0.0.0"]
