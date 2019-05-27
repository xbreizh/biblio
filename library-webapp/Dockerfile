FROM tomcat:9.0.14-jre8
RUN rm -rf /usr/local/tomcat/webapps/*

COPY ./library-web/target/library-webapp.war /usr/local/tomcat/webapps/library-webapp.war
#COPY ./library-technical/src/main/resources/* /usr/app/resources/
CMD ["catalina.sh","run"]