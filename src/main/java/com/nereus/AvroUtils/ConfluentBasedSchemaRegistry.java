package com.nereus.AvroUtils;

import com.attunity.avro.decoder.AttunityMessageDecoder;
import com.attunity.avro.decoder.AttunityMetadataMessage;
import com.attunity.avro.decoder.ISchemaLocator;
import com.tryg.common.bean.ConfluentSchemaRegBean;
import com.tryg.common.logging.LogManager;
import com.tryg.common.logging.Logger;
import com.tryg.metadata.confluent.impl.ConfluentSchemaRegistryHandlerImpl;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;

public class ConfluentBasedSchemaRegistry implements ISchemaLocator {
	static Logger LOGGER = LogManager.getLogger(ConfluentBasedSchemaRegistry.class);
	private String metadataRootFolder;
	private Map<String, String> dataSchemaCache = new LinkedHashMap<String, String>();
	private Map<String, AttunityMetadataMessage> metadataCache = new LinkedHashMap<String, AttunityMetadataMessage>();
	private AttunityMessageDecoder decoder;
	ConfluentSchemaRegBean schemaRegistryBean;
	ConfluentSchemaRegistryHandlerImpl schemaRegImpl;

	public ConfluentBasedSchemaRegistry(AttunityMessageDecoder messageDecoder) {
		this.decoder = messageDecoder;
		this.schemaRegistryBean = new ConfluentSchemaRegBean();
		this.schemaRegImpl = new ConfluentSchemaRegistryHandlerImpl();
		this.schemaRegistryBean.setRepoURL("http://10.84.0.4:8081");
	}

	public String locateSchema(String schemaId) {
		for (int retries = 0; retries < 10; ++retries) {
			String schemaFromCache = (String) this.dataSchemaCache.get(schemaId);
			if (schemaFromCache != null) {
				return schemaFromCache;
			}

			String op = "";
			byte[] encoded = new byte[0];

			try {
				this.schemaRegistryBean.setSubject(schemaId);
				op = this.schemaRegImpl.getLatestSchema(this.schemaRegistryBean);
				encoded = op.getBytes();
			} catch (Exception arg9) {
				LOGGER.error("Eror in locating Schema" + arg9.getMessage());

				try {
					Thread.sleep(3000L);
				} catch (InterruptedException arg8) {
					LOGGER.error("Interupt Error in locating Schema" + arg8.getMessage());
				}
				continue;
			}

			if (!op.equals("")) {
				String e = new String(encoded, StandardCharsets.UTF_8);
				this.dataSchemaCache.put(schemaId, e);
				return e;
			}

			try {
				Thread.sleep(3000L);
			} catch (InterruptedException arg7) {
				LOGGER.error("Interupt Error in locating Schema" + arg7.getMessage());
			}
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	AttunityMetadataMessage getMetadataMessage(String schemaId) {
		AttunityMetadataMessage metadataMessage = (AttunityMetadataMessage) this.metadataCache.get(schemaId);
		if (metadataMessage != null) {
			return metadataMessage;
		} else {
			this.schemaRegistryBean.setSubject(schemaId + ".Schema");
			String op = this.schemaRegImpl.getLatestSchema(this.schemaRegistryBean);
			if (!op.equals("")) {
				byte[] encoded = new byte[0];

				try {
					encoded = op.getBytes();
					String e = new String(encoded, StandardCharsets.UTF_8);
					Schema metadataSchema = (new Parser()).parse(e);
					Path metadataFilePath = Paths.get(this.metadataRootFolder, new String[] { schemaId + ".dat" });
					File file = new File(metadataFilePath.toString());
					GenericDatumReader datumReader = new GenericDatumReader(metadataSchema);
					DataFileReader dataFileReader = new DataFileReader(file, datumReader);
					GenericRecord metadataRecord = null;
					metadataRecord = (GenericRecord) dataFileReader.next(metadataRecord);
					metadataMessage = this.decoder.createMetadataMessage(metadataRecord);
					this.metadataCache.put(schemaId, metadataMessage);
					return metadataMessage;
				} catch (IOException arg11) {
					LOGGER.error("IOException " + arg11.getMessage());
					return null;
				}
			} else {
				return null;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveSchemaToRegistry(AttunityMetadataMessage metadataMessage) throws IOException {
		this.dataSchemaCache.put(metadataMessage.getSchemaId(), metadataMessage.getDataSchema());
		this.schemaRegistryBean.setSubject(metadataMessage.getSchemaId());
		this.schemaRegistryBean.setSchema(inputToSchemaRegistryFormat(metadataMessage.getDataSchema()));
		this.schemaRegImpl.writeSchema(this.schemaRegistryBean);
		this.metadataCache.put(metadataMessage.getSchemaId(), metadataMessage);
		Path metadataFilePath = Paths.get(this.metadataRootFolder,
				new String[] { metadataMessage.getSchemaId() + ".dat" });
		File file = new File(metadataFilePath.toString());
		GenericDatumWriter datumWriter = new GenericDatumWriter(metadataMessage.getRawMessage().getSchema());
		DataFileWriter dataFileWriter = new DataFileWriter(datumWriter);
		dataFileWriter.create(metadataMessage.getRawMessage().getSchema(), file);
		dataFileWriter.append(metadataMessage.getRawMessage());
		dataFileWriter.close();
		this.schemaRegistryBean.setSubject(metadataMessage.getSchemaId() + ".Schema");
		this.schemaRegistryBean
				.setSchema(inputToSchemaRegistryFormat(metadataMessage.getRawMessage().getSchema().toString()));
		this.schemaRegImpl.writeSchema(this.schemaRegistryBean);
	}

	public static String insertSchemaHandler(String schema) {
		String schemaContent = "";
		String formattedSchema;
		if (schema.contains("schema")) {
			if (schema.matches("\\{\"schema\":\"\\{.*}\"}")) {
				return schema;
			} else {
				int index = schema.indexOf("{", schema.indexOf("{") + 1);
				int index2 = schema.lastIndexOf("}", schema.indexOf("}"));
				schemaContent = schema.substring(index, index2 + 1);
				schemaContent = schemaContentHandler(schemaContent);
				formattedSchema = "{\"schema\":\"" + schemaContent + "\"}";
				return formattedSchema;
			}
		} else {
			schemaContent = schemaContentHandler(schema);
			formattedSchema = "{\"schema\":\"" + schemaContent + "\"}";
			return formattedSchema;
		}
	}

	public static String schemaContentHandler(String schemaContent) {
		StringBuilder schemaContentBuilder = new StringBuilder(schemaContent);

		for (int i = 1; i < schemaContentBuilder.length(); ++i) {
			if (schemaContentBuilder.charAt(i) == 34 & schemaContentBuilder.charAt(i - 1) != 92) {
				schemaContentBuilder.replace(i, i + 1, "\\\"");
			}
		}

		return schemaContentBuilder.toString().replaceAll("[\n]", "");
	}

	public static String retrieveSchemaHandler(String schema) {
		String schemanew = "";
		Pattern pattern = Pattern.compile("\"schema\":\"\\{(.*?)\\}\"");
		Matcher matcher = pattern.matcher(schema);
		if (matcher.find()) {
			schemanew = "{" + matcher.group(1) + "}";
		}

		StringBuilder schemaContentBuilder = new StringBuilder(schemanew);

		for (int i = 1; i < schemaContentBuilder.length(); ++i) {
			if (schemaContentBuilder.charAt(i) == 34 & schemaContentBuilder.charAt(i - 1) == 92) {
				schemaContentBuilder.replace(i - 1, i, "");
			}
		}

		return schemaContentBuilder.toString();
	}

	public static String inputToSchemaRegistryFormat(String schema) {
		String formattedSchema = "";

		try {
			schema = schema.replaceAll("\"", "").replaceAll("\\\\", "").replaceAll("\\b\\w+\\b", "\"$0\"");
			// System.out.println("schema in schemahandler " + schema);
			if (schema.contains("\"schema\":")) {
				int e = schema.indexOf("{", schema.indexOf("{") + 1);
				int index2 = schema.lastIndexOf("}", schema.lastIndexOf("}"));
				formattedSchema = "{\"schema\":\"" + schemaContentHandler(schema.substring(e, index2 + 1)) + "\"}";
			} else {
				formattedSchema = "{\"schema\":\"" + schemaContentHandler(schema) + "\"}";
			}
		} catch (Exception arg3) {
			LOGGER.error(arg3);
		}

		// System.out.println("final insert scheam is " + formattedSchema);
		return formattedSchema;
	}
}