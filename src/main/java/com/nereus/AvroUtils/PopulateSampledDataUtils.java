package com.nereus.AvroUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

import com.google.common.collect.Sets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import net.agkn.hll.HLL;

public class PopulateSampledDataUtils {
	private static int seed = 123456;
	/** use murmur3 hash function from `com.google.common.hash` */
	public static HashFunction hash = Hashing.murmur3_128(seed);

	/**
	 * @param dataFields
	 * @return
	 */
	// public static Set<Set<String>> createTags(Set<String> dataFields) {
	// Set<Set<String>> sets = Sets.powerSet(dataFields);
	// return sets;
	// }

	/**
	 * @param str
	 * @return
	 */
	public static long toHash(String str) {
		Hasher hasher = hash.newHasher();
		try {
			hasher.putBytes(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hasher.hash().asLong();

	}

	/**
	 * @param kafkaDataList
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> populateDataFromDataList(ArrayList<String> kafkaDataList) {

		ArrayList<HashMap<String, String>> DDup = new ArrayList<HashMap<String, String>>();

		Set<String> dataFields = new HashSet<String>();

		for (int i = 0; i < kafkaDataList.size(); i++) {
			HashMap<String, String> DDupIns = new HashMap<String, String>();
			JSONObject jsonObj = new JSONObject(kafkaDataList.get(i));
			dataFields = jsonObj.keySet();
			dataFields.forEach(jsonKey -> {
				DDupIns.put(jsonKey, String.valueOf(jsonObj.get(jsonKey)));
			});
			DDup.add(DDupIns);
		}
		return DDup;

	}

	/**
	 * @param kafkaDataList
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Set<Set<String>> createTags(ArrayList<String> kafkaDataList) {
		Set<String> dataFields = new HashSet<String>();
		JSONObject jsonObj = new JSONObject(kafkaDataList.get(0));
		dataFields = jsonObj.keySet();
		Set<Set<String>> sets = Sets.powerSet(dataFields);
		return sets;
	}

	/**
	 * @param sourceTopics
	 * @param hllCollection
	 * @param kafkaDataList
	 * @return
	 */
	public static HashMap<String, HashMap<Set<String>, HLL>> generateHLLObjects(String sourceTopics,
			ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> hllCollection, ArrayList<String> kafkaDataList) {

		Set<Set<String>> tagSets = createTags(kafkaDataList);
		ArrayList<HashMap<String, String>> dataMapList = populateDataFromDataList(kafkaDataList);
		
		HashMap<String, HashMap<Set<String>, HLL>> cardinalityMap = new HashMap<>();
		for (Set<String> sets : tagSets) {
			HLL hllD = new HLL(14, 5);
			dataMapList.forEach((d) -> {
				int hashKey = 0;
				Iterator<String> itr = sets.iterator();
				while (itr.hasNext()) {
					String field = itr.next();
					int currentHashKey = d.get(field).hashCode();
					hashKey = hashKey + currentHashKey;
				}
				long hashedValue = PopulateSampledDataUtils.hash.newHasher().putInt(hashKey).hash().asLong();

				hllD.addRaw(hashedValue);

				if (cardinalityMap.containsKey(sourceTopics)) {
					cardinalityMap.get(sourceTopics).put(sets, hllD);
				} else {
					HashMap<Set<String>, HLL> newMap = new HashMap<Set<String>, HLL>();
					newMap.put(sets, hllD);
					cardinalityMap.put(sourceTopics, newMap);
				}
			});
		}
		return cardinalityMap;
	}

}
