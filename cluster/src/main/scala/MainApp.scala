import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import scalaj.http.Http

object MainApp{

	val JOB_DONE_URL:String = "https://google.com"
	val INPUT_FILES:List[String] = List("", "")

	val SPARK_HOME:String = "/Users/ivan/Dev/spark/spark-0.9.0-incubating"
	val SPARK_MASTER = "local"
	val SPARK_JOB = "SimpleApp"
	val SPARK_JARS:Seq[String] = List("target/scala-2.10/simple-project_2.10-1.0.jar")
 
	def main(args:Array[String]){
		// access the read file
		val logFile = "/Users/ivan/Dev/spark/spark-0.9.0-incubating/README.md"

		// create spark context
		val sc = new SparkContext(SPARK_MASTER, SPARK_JOB, SPARK_HOME, SPARK_JARS)
		
		// cache the text file into the memory
		val logData = sc.textFile(logFile, 2).cache()


		val numAs = logData.filter(line => line.contains("a")).count
		val numBs = logData.filter(line => line.contains("b")).count
		println("Lines with a: %s, and lines with b %s".format(numAs, numBs))

		notifyJobDone()

		// example how to save output to DB
		FoursquareResults.saveToDB()
	}

	def notifyJobDone() = Http(JOB_DONE_URL).asString
}