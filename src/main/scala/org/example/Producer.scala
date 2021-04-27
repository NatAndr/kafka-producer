package org.example

import java.io.FileReader
import java.util.Properties

import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.apache.commons.csv.{CSVFormat, CSVParser, QuoteMode}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.example.model.Book

import scala.collection.JavaConverters.iterableAsScalaIterableConverter

object Producer {

  def getProducer(properties: Properties): KafkaProducer[String, String] = {
    new KafkaProducer(
      properties,
      new StringSerializer,
      new StringSerializer
    )
  }

  def getCsvRecords(reader: FileReader): CSVParser = {
    CSVFormat
      .newFormat(',')
      .withFirstRecordAsHeader
      .withQuote('"')
      .withQuoteMode(QuoteMode.MINIMAL)
      .withAutoFlush(true)
      .parse(reader)
  }

  def getReader(fileName: String): FileReader = {
    new FileReader(
      fileName
    )
  }

  def getBooks(records: CSVParser): Iterable[Book] = {
    records.asScala
      .map(r =>
        Book(
          r.get(0),
          r.get(1),
          r.get(2).toDouble,
          r.get(3).toInt,
          r.get(4).toDouble,
          r.get(5).toInt,
          r.get(6)
        )
      )
  }

  def send(
      producer: KafkaProducer[String, String],
      topic: String,
      records: Iterable[Book]
  ): Unit = {
    records.foreach { record =>
      producer.send(new ProducerRecord(topic, record.asJson.toString, record.asJson.toString))
    }
  }

  def main(args: Array[String]): Unit = {
    val properties = new Properties()
    properties.put("bootstrap.servers", "localhost:9092")

    val topic = "books"

    val producer = getProducer(properties)
    val reader   = getReader("src/main/resources/data/bestsellers_with_categories-1801-9dc31f.csv")
    val records  = getCsvRecords(reader)
    val books    = getBooks(records)

    send(producer, topic, books)

    reader.close()
    producer.close()
  }
}
