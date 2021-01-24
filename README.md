# test1

###Usage

To use this project, you are going to need;

- Apache Maven (3.3.9 or newer)
- Java Development Kit (JDK) require JDK 11 or above to execute.
- Any Java IDE

To buid all the modules run in the project root directory, you must to add a series of libraries in the local repository, 
these libraries can be found within the project,  to be able to compile this project, for this you have to execute the following maven commands  with Maven 3:

```sh
$ mvn install:install-file -Dfile=<path-project>/libs/rest_simulacionpoliza-2.15.0.jar -DgroupId=es.sanitas.seg.SimulacionPoliza -DartifactId=SimulacionPoliza-services-api -Dversion=2.15 -Dpackaging=jar
$ mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<path-project>/ws_contratacion-1.2.28.jar
$ mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<path-path>/scontratacion-1.19.0.jar
```

To build all the modules run in the project root directory the following command with Maven 3:
```sh
$ mvn clean install
```

This application is packaged . You run it using the ```java -jar``` command.
Once successfully built, you can run the service by one of these two methods: 

```sh
$ java -jar target/test1-1.0-SNAPSHOT.jar.jar
```


### Requirements

TODO REVISAR 

The latest version of the archetype has the following requirements:
- Apache Maven (3.3.9 or newer)
- Java Development Kit (JDK) –require JDK 11 or above to execute.
