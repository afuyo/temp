package com.nereus.AvroUtils;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.FieldAssembler;
import org.json.JSONArray;
import org.json.JSONObject;

public class SchemaGeneratorUtils {
	static String groupBySchema = Constants.GROUPBY_SCHEMA_STRUCTURE;
	static String groupBySchemaSuffix = Constants.GROUPBY_SCHEMA_SUFFIX;
	static String joinSchemaSuffix = Constants.JOIN_SCHEMA_SUFFIX;

	/**
	 * This function will generate the schema for two joined dataset. Need to
	 * provide the Schema of Two sources to be joined and the corresponding source
	 * names as the second and fourth parameters.
	 * 
	 * @param schemaStrLeft
	 * @param sourceNameLeft
	 * @param schemaStrRight
	 * @param sourceNameRight
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Schema generateJoinedSchema(String schemaStrLeft, String sourceNameLeft, String schemaStrRight,
			String sourceNameRight) {
		if (sourceNameLeft.contains(".")) {
			sourceNameLeft = sourceNameLeft.replaceAll("\\.","");
		}
		if (sourceNameRight.contains(".")) {
			sourceNameRight = sourceNameRight.replaceAll("\\.","");
		}
		String sourceName = sourceNameLeft + "And" + sourceNameRight + joinSchemaSuffix;

		JSONObject obj = new JSONObject(schemaStrLeft);
		FieldAssembler<Schema> builder = SchemaBuilder.builder().record(sourceName).fields();

		if ("Data".equals(obj.getString("name"))) {
			obj.put("name", sourceNameLeft);
			Schema dataSchema = Schema.parse(obj.toString());
			// builder.name(sourceNameLeft).type().nullable().array().items(dataSchema).noDefault();
			builder.name(sourceNameLeft + "List").type().nullable().array().items(dataSchema).noDefault();
		} else {
			JSONArray firstArray = obj.getJSONArray("fields");
			for (int i = 0; i < firstArray.length(); i++) {
				JSONObject dataObj = firstArray.getJSONObject(i);
				JSONArray dataObjArray = dataObj.getJSONArray("type");
				for (int j = 0; j < dataObjArray.length(); j++) {
					if (!dataObjArray.get(j).equals("null")) {
						JSONObject finalObject = dataObjArray.getJSONObject(j).getJSONObject("items");
						Schema dataSchema = Schema.parse(finalObject.toString());
						builder.name(finalObject.getString("name") + "List").type().nullable().array().items(dataSchema)
								.noDefault();
					}
				}
			}
		}

		JSONObject objRight = new JSONObject(schemaStrRight);
		if ("Data".equals(objRight.getString("name"))) {
			objRight.put("name", sourceNameRight);
			Schema dataSchema = Schema.parse(objRight.toString());
			// builder.name(sourceNameRight).type().nullable().array().items(dataSchema).noDefault();
			builder.name(sourceNameRight + "List").type().nullable().array().items(dataSchema).noDefault();
		} else {

			JSONArray firstArray = objRight.getJSONArray("fields");
			for (int i = 0; i < firstArray.length(); i++) {
				JSONObject dataObj = firstArray.getJSONObject(i);
				JSONArray dataObjArray = dataObj.getJSONArray("type");
				for (int j = 0; j < dataObjArray.length(); j++) {
					if (!dataObjArray.get(j).equals("null")) {
						JSONObject finalObject = dataObjArray.getJSONObject(j).getJSONObject("items");
						Schema dataSchema = Schema.parse(finalObject.toString());
						builder.name(finalObject.getString("name") + "List").type().nullable().array().items(dataSchema)
								.noDefault();
					}
				}
			}
		}

		Schema schema = builder.endRecord();
		return schema;
	}

	/**
	 * This function is to generate the schema for a source which is going to be
	 * grouped.
	 * 
	 * @param schemaStr
	 * @param sourceName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Schema generateGroupBySchema(String schemaStr, String sourceName) {
		if (sourceName.contains(".")) {
			sourceName = sourceName.replaceAll("\\.","");
		}
		String schemaName = sourceName + groupBySchemaSuffix;
		JSONObject obj = new JSONObject(schemaStr);
		FieldAssembler<Schema> builder = SchemaBuilder.record(schemaName).fields();
		if ("Data".equals(obj.getString("name"))) {
			obj.put("name", sourceName);
			Schema dataSchema = Schema.parse(obj.toString());
			// builder.name(schemaName).type().nullable().array().items(dataSchema).noDefault();
			builder.name(sourceName + "List").type().nullable().array().items(dataSchema).noDefault();
		} else {
			JSONArray firstArray = obj.getJSONArray("fields");
			for (int i = 0; i < firstArray.length(); i++) {
				JSONObject dataObj = firstArray.getJSONObject(i);
				JSONArray dataObjArray = dataObj.getJSONArray("type");
				for (int j = 0; j < dataObjArray.length(); j++) {
					if (!dataObjArray.get(j).equals("null")) {
						JSONObject finalObject = dataObjArray.getJSONObject(j).getJSONObject("items");
						Schema dataSchema = Schema.parse(finalObject.toString());
						builder.name(finalObject.getString("name")).type().nullable().array().items(dataSchema)
								.noDefault();
					}
				}
			}
		}

		return builder.endRecord();

	}

}
