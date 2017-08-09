package util

import java.io.{PrintWriter, StringWriter}

import org.slf4j.LoggerFactory

trait SenzLogger {

  val logger = LoggerFactory.getLogger(this.getClass)

  def logError(throwable: Throwable) = {
    val writer = new StringWriter
    throwable.printStackTrace(new PrintWriter(writer))
    logger.error(writer.toString)
  }
}
