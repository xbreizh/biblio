FROM tomcat:9.0.14-jre8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./troparo-web/target/troparo_app.war /usr/local/tomcat/webapps/troparo_app.war
#COPY ./troparo-technical/src/main/resources/* /usr/app/resources/
CMD ["catalina.sh","run"]