package com.nereus.AvroUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;



import net.agkn.hll.HLL;

public class PopulateSampledData {

	public static void main(String[] args) throws JSONException, IOException {

		// String[] dataFileList = { "STATPEJ.POC_CLAIM", "STATPEJ.POC_CLAIMPAYMENT",
		// "STATPEJ.POC_CUSTOMER",
		// "STATPEJ.POC_POLICY" };

		// String sourceTopicList =
		// "STATPEJ.POC_CLAIM,STATPEJ.POC_CLAIMPAYMENT,STATPEJ.POC_CUSTOMER,STATPEJ.POC_POLICY";
		String sourceTopicList = "STATPEJ.POC_ADDRESS3,STATPEJ.POC_CLAIM3,STATPEJ.POC_CLAIMPAYMENT3,STATPEJ.POC_CUSTOMER3,STATPEJ.POC_POLICY3";
		// String sourceTopicList = "STATPEJ.POC_CUSTOMER3";

		int consumerGroupId = 2057;
		long targetTotalCount = 50000;

		// HashMap<String, Object> hllCollection = populateHLLObjects(sourceTopicList,
		// consumerGroupId, targetTotalCount);
		// System.out.println(hllCollection);
		// System.out.println(regenerateHLLObjects("CLAIMNUMBER", "_", sourceTopicList,
		// "STATPEJ.POC_CLAIM3,STATPEJ.POC_CLAIMPAYMENT3", consumerGroupId,
		// targetTotalCount, "POLICY"));

		HashMap<String, Object> data = regenerateHLLObjects("CLAIMNUMBER", "_", sourceTopicList,
				"STATPEJ.POC_CLAIM3", consumerGroupId, targetTotalCount, "POLICY");

		ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> data1 = (ArrayList<HashMap<String, HashMap<Set<String>, HLL>>>) data
				.get("hllCollection");

		for (int i = 0; i < data1.size(); i++) {
			HashMap<String, HashMap<Set<String>, HLL>> data2 = data1.get(i);
			Iterator<Entry<String, HashMap<Set<String>, HLL>>> dataItr=data2.entrySet().iterator();
			while (dataItr.hasNext()) {
				Entry<String, HashMap<Set<String>, HLL>> datNxt=dataItr.next();
				String name=datNxt.getKey();
				System.out.println("Name----"+name);
				if("STATPEJ.POC_CLAIM3".equals(name)) {
					HashMap<Set<String>, HLL> value=datNxt.getValue();
					Iterator<Entry<Set<String>, HLL>> dataNxtItr=value.entrySet().iterator();
					while (dataNxtItr.hasNext()) {
						Entry<Set<String>, HLL> dataInsNxt=dataNxtItr.next();
						System.out.println(dataInsNxt.getKey());
						System.out.println(dataInsNxt.getValue().cardinality());
						
					}
				}
			}

		}

		// GenerateRelationsTest.generateRelationships(hllCollection);
	}

	/**
	 * The function to regenerate the HLL object by adding necessary extra columns
	 * that you need.
	 * 
	 * @param coumnNameToSplit
	 *            Specify the Column Name that you want to split
	 * @param columnSplitter
	 *            Specify the Splitter that you want to use to split the column
	 * @param sourceTopicList
	 *            Specify the kafka topics names you want to consume from
	 * @param topicNamesToSplit
	 *            Specify the kafka topics names you want to split the column
	 * @param consumerGroupId
	 *            Specify the kafka consumer group id
	 * @param targetTotalCount
	 *            Specify the total number of message you want to consume from all
	 *            the topics
	 * @param newColumnName
	 *            Specify the Column name that you want to provide after splitting
	 * @return HashMap<String, Object> contains the HLL for all tables data. Please
	 *         note it will return null if consumer group doesn't have anything more
	 *         to read from kafka topic
	 */
	public static HashMap<String, Object> regenerateHLLObjects(String coumnNameToSplit, String columnSplitter,
			String sourceTopicList, String topicNamesToSplit, int consumerGroupId, long targetTotalCount,
			String newColumnName) {

		String[] sourceTopicArray = sourceTopicList.split(",");
		String[] splitTopicArray = topicNamesToSplit.split(",");

		Set<String> splitSet = new HashSet<String>(Arrays.asList(splitTopicArray));

		HashMap<String, Object> finalMap = new HashMap<String, Object>();
		ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> hllCollection = new ArrayList<>();
		HashMap<String, String> schemaMap = new HashMap<String, String>();
		HashMap<String, Long> dataCountMap = new HashMap<String, Long>();

		for (int i = 0; i < sourceTopicArray.length; i++) {

			String sourceTopics = sourceTopicArray[i];
			String topicName = sourceTopics.replaceAll("\\.", "");
			HashMap<String, ArrayList<String>> dataMapFromKafka = new HashMap<String, ArrayList<String>>();

			if (splitSet.contains(sourceTopics)) {
				dataMapFromKafka = KafkaDataUtils.fetchRegeneratedDataFromKafkaTopics(consumerGroupId, sourceTopics,
						targetTotalCount, coumnNameToSplit, columnSplitter, newColumnName);
			} else {
				dataMapFromKafka = KafkaDataUtils.fetchAllDataFromKafkaTopics(consumerGroupId, sourceTopics,
						targetTotalCount);
			}
			System.out.println(sourceTopics);
			// System.out.println(dataMapFromKafka);

			ArrayList<String> kafkaDataList = new ArrayList<String>();

			if (dataMapFromKafka.containsKey("DataList")) {
				kafkaDataList = dataMapFromKafka.get("DataList");
			}

			if (dataMapFromKafka.containsKey("SchemaList")) {
				schemaMap.put(sourceTopics, dataMapFromKafka.get("SchemaList").get(0));
			}

			dataCountMap.put(sourceTopics, Long.valueOf(kafkaDataList.size()));

			if (kafkaDataList.size() > 0) {
				HashMap<String, HashMap<Set<String>, HLL>> sourceHllCollectionObj = PopulateSampledDataUtils
						.generateHLLObjects(sourceTopics, hllCollection, kafkaDataList);
				hllCollection.add(sourceHllCollectionObj);
			} else {
				System.out.println("----No Record Found In The Kafka Topics. Please check the topic.----");
				System.out.println("----If Data Is available in the topic, try changing the Consumer Group ID.----");
				System.out.println("----Exiting The Application-----");
				return null;
			}

		}
		finalMap.put("hllCollection", hllCollection);
		finalMap.put("schemaCollection", schemaMap);
		finalMap.put("dataCountCollection", dataCountMap);
		return finalMap;
	}

	/**
	 * This function is to generate the HLL objects of tables and its column
	 * combinations.
	 * 
	 * @param sourceTopicList
	 *            Specify the Kafka topics names you want to consume from
	 * @param consumerGroupId
	 *            Specify the kafka consumer group id
	 * @param targetTotalCount
	 *            Specify the total number of message you want to consume from all
	 *            the topics
	 * @return HashMap<String, Object> contains the HLL for all tables data. Please
	 *         note it will return null if consumer group doesn't have anything more
	 *         to read from kafka topic
	 */
	public static HashMap<String, Object> populateHLLObjects(String sourceTopicList, int consumerGroupId,
			long targetTotalCount) {
		String[] sourceTopicArray = sourceTopicList.split(",");

		HashMap<String, Object> finalMap = new HashMap<String, Object>();

		ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> hllCollection = new ArrayList<>();
		HashMap<String, String> schemaMap = new HashMap<String, String>();
		HashMap<String, Long> dataCountMap = new HashMap<String, Long>();

		for (int i = 0; i < sourceTopicArray.length; i++) {

			String sourceTopics = sourceTopicArray[i];

			String topicName = sourceTopics.replaceAll("\\.", "");

			HashMap<String, ArrayList<String>> dataMapFromKafka = KafkaDataUtils
					.fetchAllDataFromKafkaTopics(consumerGroupId, sourceTopics, targetTotalCount);

			ArrayList<String> kafkaDataList = new ArrayList<String>();

			if (dataMapFromKafka.containsKey("DataList")) {
				kafkaDataList = dataMapFromKafka.get("DataList");
			}

			if (dataMapFromKafka.containsKey("SchemaList")) {
				schemaMap.put(sourceTopics, dataMapFromKafka.get("SchemaList").get(0));
			}

			dataCountMap.put(sourceTopics, Long.valueOf(kafkaDataList.size()));

			if (kafkaDataList.size() > 0) {
				HashMap<String, HashMap<Set<String>, HLL>> sourceHllCollectionObj = PopulateSampledDataUtils
						.generateHLLObjects(sourceTopics, hllCollection, kafkaDataList);
				hllCollection.add(sourceHllCollectionObj);
			} else {
				System.out.println("----No Record Found In The Kafka Topics. Please check the topic.----");
				System.out.println("----If Data Is available in the topic, try changing the Consumer Group ID.----");
				System.out.println("----Exiting The Application-----");
				return null;
			}
		}
		finalMap.put("hllCollection", hllCollection);
		finalMap.put("schemaCollection", schemaMap);
		finalMap.put("dataCountCollection", dataCountMap);
		return finalMap;
	}

}
