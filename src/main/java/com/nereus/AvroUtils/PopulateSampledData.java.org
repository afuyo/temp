package com.nereus.AvroUtils;

import net.agkn.hll.HLL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PopulateSampledData {

//	public static void main(String[] args) throws JSONException, IOException {
//
//		// String[] dataFileList = { "STATPEJ.POC_CLAIM", "STATPEJ.POC_CLAIMPAYMENT",
//		// "STATPEJ.POC_CUSTOMER",
//		// "STATPEJ.POC_POLICY" };
//
//		String sourceTopicList = "STATPEJ.POC_CLAIM,STATPEJ.POC_CLAIMPAYMENT,STATPEJ.POC_CUSTOMER,STATPEJ.POC_POLICY";
//		int consumerGroupId = 127;
//		long targetTotalCount = 50000;
//
//		populateHLLObjects(sourceTopicList, consumerGroupId, targetTotalCount);
//		// GenerateRelationsTest.generateRelationships(hllCollection);
//	}

	public static HashMap<String, Object> populateHLLObjects(String sourceTopicList, int consumerGroupId,
			long targetTotalCount) {
		String[] sourceTopicArray = sourceTopicList.split(",");

		HashMap<String, Object> finalMap = new HashMap<String, Object>();

		ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> hllCollection = new ArrayList<>();
		HashMap<String, String> schemaMap = new HashMap<String, String>();
		HashMap<String, Long> dataCountMap = new HashMap<String, Long>();

		for (int i = 0; i < sourceTopicArray.length; i++) {

			String sourceTopics = sourceTopicArray[i];


			HashMap<String, ArrayList<String>> dataMapFromKafka = KafkaDataUtils
					.fetchAllDataFromKafkaTopics(consumerGroupId, sourceTopics, targetTotalCount);
			ArrayList<String> kafkaDataList = new ArrayList<String>();


			if (dataMapFromKafka.containsKey("DataList")) {
				kafkaDataList = dataMapFromKafka.get("DataList");
			}
			dataCountMap.put(sourceTopics, Long.valueOf(kafkaDataList.size()));

			if (dataMapFromKafka.containsKey("SchemaList")) {
				schemaMap.put(sourceTopics, dataMapFromKafka.get("SchemaList").get(0));
			}

			if (kafkaDataList.size() > 0) {
				HashMap<String, HashMap<Set<String>, HLL>> sourceHllCollectionObj = PopulateSampledDataUtils
						.generateHLLObjects(sourceTopics, hllCollection, kafkaDataList);
				hllCollection.add(sourceHllCollectionObj);
			} else {
				System.out.println("----No Record Found In The Kafka Topics. Please check the topic.----");
				System.out.println("----If Data Is available in the topic, try changing the Consumer Group ID.----");
				System.out.println("----Exiting The Application-----");
				System.exit(0);
			}

		}
		finalMap.put("hllCollection", hllCollection);
		finalMap.put("schemaCollection", schemaMap);
		finalMap.put("dataCountCollection", dataCountMap);
		return finalMap;
	}

}
