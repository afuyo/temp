package com.nereus.AvroUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.attunity.avro.decoder.AttunityDataMessage;
import com.attunity.avro.decoder.AttunityDecoderException;
import com.attunity.avro.decoder.AttunityMessageDecoder;


public class KafkaDataUtils {
	static AttunityMessageDecoder messageDecoder;
	static ConfluentBasedSchemaRegistry schemaLocator;

	static {
		schemaLocator = new ConfluentBasedSchemaRegistry(new AttunityMessageDecoder());
		messageDecoder = new AttunityMessageDecoder(schemaLocator);
	}

	/**
	 * This function used for fetching data from kafka topics based on the
	 * targetTotalCount
	 * 
	 * @param consumerGroupId
	 *            Specify the consumer group id for Kafka.
	 * @param sourceTopics
	 *            Specify the list of topics to consume from.
	 * @param targetTotalCount
	 *            Specify the total count of message you want to consume from the
	 *            topics.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> fetchAllDataFromKafkaTopics(int consumerGroupId,
			String sourceTopics, long targetTotalCount) {
		Properties consumerProperties = KafkaUtilities.getKafkaConsumerPropertiesByte(consumerGroupId);
		KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(consumerProperties);
		String[] topicarray = sourceTopics.split(",");
		consumer.subscribe(Arrays.asList(topicarray));

		long recordCount = 0;
		int emptyChk = 0;
		HashMap<String, ArrayList<String>> finalMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> dataList = new ArrayList<>();
		ArrayList<String> schemaList = new ArrayList<>();
		while (true) {
			ConsumerRecords<String, byte[]> records = consumer.poll(100);
			int currentRecordCount = records.count();
			recordCount = recordCount + currentRecordCount;
			for (ConsumerRecord<String, byte[]> record : records) {
				long processingTime = record.timestamp();
				AttunityDataMessage message = null;
				try {
					message = (AttunityDataMessage) messageDecoder.decode(record.value());
				} catch (AttunityDecoderException e) {
					System.out.println("getErrorCode------" + e.getErrorCode());
					e.printStackTrace();
				}

				String finalObj = generateModifiedDataMessage(message, processingTime, true, null, null, null);
				String schemaJSon = generateModifiedDataSchema(message, true, null);

				if (finalMap.containsKey("DataList")) {
					ArrayList<String> existingDataList = finalMap.get("DataList");
					existingDataList.add(finalObj);
					finalMap.put("DataList", existingDataList);
				} else {
					dataList.add(finalObj);
					finalMap.put("DataList", dataList);
				}

				if (!finalMap.containsKey("SchemaList")) {
					schemaList.add(schemaJSon);
					finalMap.put("SchemaList", schemaList);
				}

			}

			if (recordCount >= targetTotalCount) {
				break;
			}

			if (currentRecordCount == 0) {
				emptyChk++;
				if (emptyChk == 3) {
					break;
				}
			}
		}
		consumer.close();
		return finalMap;
	}

	/**
	 * This function is to generate the data from Kafka topics and prepare the data
	 * based on the columns.
	 * 
	 * @param consumerGroupId
	 *            Specify the consumer group id for Kafka.
	 * @param sourceTopics
	 *            Specify the list of topics to consume from.
	 * @param targetTotalCount
	 *            Specify the total count of message you want to consume from the
	 *            topics.
	 * @param coumnNameToSplit
	 *            Specify the Column Name that you want to split
	 * @param columnSplitter
	 *            Specify the Splitter that you want to use to split the column
	 * @param newColumnName
	 *            Specify the Column name that you want to provide after splitting
	 * @return HashMap
	 */
	public static HashMap<String, ArrayList<String>> fetchRegeneratedDataFromKafkaTopics(int consumerGroupId,
			String sourceTopics, long targetTotalCount, String coumnNameToSplit, String columnSplitter,
			String newColumnName) {
		Properties consumerProperties = KafkaUtilities.getKafkaConsumerPropertiesByte(consumerGroupId);
		KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(consumerProperties);
		String[] topicarray = sourceTopics.split(",");
		consumer.subscribe(Arrays.asList(topicarray));
		long recordCount = 0;
		int emptyChk = 0;
		HashMap<String, ArrayList<String>> finalMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> dataList = new ArrayList<>();
		ArrayList<String> schemaList = new ArrayList<>();
		while (true) {
			ConsumerRecords<String, byte[]> records = consumer.poll(100);
			recordCount = recordCount + records.count();
			for (ConsumerRecord<String, byte[]> record : records) {
				long processingTime = record.timestamp();
				AttunityDataMessage message = null;
				try {
					message = (AttunityDataMessage) messageDecoder.decode(record.value());
				} catch (AttunityDecoderException e) {
					System.out.println("getErrorCode------" + e.getErrorCode());
					e.printStackTrace();
				}

				String finalObj = generateModifiedDataMessage(message, processingTime, false, coumnNameToSplit,
						columnSplitter, newColumnName);

				String schemaJSon = generateModifiedDataSchema(message, false, newColumnName);

				if (finalMap.containsKey("DataList")) {
					ArrayList<String> existingDataList = finalMap.get("DataList");
					existingDataList.add(finalObj);
					finalMap.put("DataList", existingDataList);
				} else {
					dataList.add(finalObj);
					finalMap.put("DataList", dataList);
				}

				if (!finalMap.containsKey("SchemaList")) {
					schemaList.add(schemaJSon);
					finalMap.put("SchemaList", schemaList);
				}

			}

			if (recordCount >= targetTotalCount) {
				break;
			}

			if (records.count() == 0) {
				emptyChk++;
				if (emptyChk == 3) {
					break;
				}
			}
		}
		consumer.close();
		return finalMap;
	}

	/**
	 * Generate the data based on the column splitter.
	 * 
	 * @param message
	 * @param processingTime
	 * @param check
	 * @param coumnNameToSplit
	 * @param columnSplitter
	 * @param newColumnName
	 * @return
	 */
	private static String generateModifiedDataMessage(AttunityDataMessage message, long processingTime, boolean check,
			String coumnNameToSplit, String columnSplitter, String newColumnName) {

		long eventTime = message.getDataHeaders().getTimestamp().getTime();
		JSONObject dataMessage = new JSONObject(message.getRawMessage().toString()).getJSONObject("data");

		if (check == true) {
			dataMessage.put("PROCESSING_TIME", processingTime);
			dataMessage.put("EVENT_TIME", eventTime);
		} else {
			String newColumnValue = "";
			if (dataMessage.has(coumnNameToSplit)) {
				String toSplitMessage = dataMessage.getString(coumnNameToSplit);
				newColumnValue = toSplitMessage.split(columnSplitter)[0];
			}
			dataMessage.put(newColumnName, newColumnValue);
			dataMessage.put("PROCESSING_TIME", processingTime);
			dataMessage.put("EVENT_TIME", eventTime);
		}

		String finalObj = dataMessage.toString();
		return finalObj;
	}

	/**
	 * @param message
	 * @param check
	 * @param newColumnName
	 * @return
	 */
	private static String generateModifiedDataSchema(AttunityDataMessage message, boolean check, String newColumnName) {
		String schemaStr = message.getRawMessage().getSchema().toString();

		JSONObject schemaJSonObj = new JSONObject(schemaStr).getJSONArray("fields").getJSONObject(0)
				.getJSONObject("type");

		JSONArray filedsArray = schemaJSonObj.getJSONArray("fields");

		if (check == false) {
			JSONObject newFieldObj = new JSONObject();
			newFieldObj.put("name", newColumnName);
			newFieldObj.put("type", "string");
			filedsArray.put(newFieldObj);
		}

		JSONObject newFieldProcessingObj = new JSONObject();
		newFieldProcessingObj.put("name", "PROCESSING_TIME");
		newFieldProcessingObj.put("type", "long");
		filedsArray.put(newFieldProcessingObj);

		JSONObject newFieldEventObj = new JSONObject();
		newFieldEventObj.put("name", "EVENT_TIME");
		newFieldEventObj.put("type", "long");
		filedsArray.put(newFieldEventObj);

		schemaJSonObj.put("fields", filedsArray);

		String schemaJSon = schemaJSonObj.toString();
		return schemaJSon;

	}
	// public static void main(String[] args) {
	// System.out.println(fetchAllDataFromKafkaTopics(117, "STATPEJ.POC_CLAIM",
	// 5000));
	// }
}
