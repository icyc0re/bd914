EXAMPLE OF SPARK USAGE:
* reads a file
* does basic parallel processing
* triggers the url after the jobs are done
* persists some data to database

Setup Spark using by following documentation at https://github.com/apache/incubator-spark.
Verify that it is running by executing ./bin/spark-shell!


-- Change local paths in src/main/scala/MainApp.scala

Tested in the following environment:
* scala 2.10.3
* sbt 0.13.0
* java 1.6.0_65
* spark-0.9.0-incubating



How to run:
1. cd bd914/cluster
2. sbt package
2.1 in case there is OutOfMemory exception, execute: cat `which sbt`
2.2 append -XX:MaxPermSize=768m option to the command running the sbt jar
3. sbt run