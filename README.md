# Prepare demo DB

* Install MySQL
* Create a schema called "demo"
* Create an account called "test"
* Create a table by "extras/mysql/users.sql"

# Copy JNDI setting to Tomcat

* Copy "Resource" in "extras/jndi/context.xml" to Eclipse or Tomcat "context.xml".
  * For Tomcat: "apache-tomcat-8.0.28\conf\context.xml"
  * For Eclipse: "<eclipse workspace>\Servers\Tomcat v8.0 Server at localhost-config\context.xml"
  * See <https://tomcat.apache.org/tomcat-8.0-doc/config/context.html> for other places in Tomcat.