package com.madoka.sunb0002.springbootdemo.common.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class CommonUtils {

	private CommonUtils() {
	}

	/**
	 * 
	 * @param content
	 * @param pass
	 * @param filenameInZip
	 * @return
	 */
	public static String createZipFile(String content, String pass, String filenameInZip) {

		if (Validators.isEmpty(content) || Validators.isEmpty(filenameInZip)) {
			return null;
		}

		ByteArrayOutputStream baos = createZipInMemory(content.getBytes(StandardCharsets.UTF_8), pass, filenameInZip);
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);

	}

	private static ByteArrayOutputStream createZipInMemory(byte[] data, String pass, String filenameInZip) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(bos);

		ZipParameters parameters = new ZipParameters();

		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setSourceExternalStream(true);
		parameters.setEncryptFiles(false);
		parameters.setFileNameInZip(filenameInZip);
		if (pass != null) {
			parameters.setPassword(pass);
		}

		try {
			zipOutputStream.putNextEntry(null, parameters);
			zipOutputStream.write(data);
			zipOutputStream.closeEntry();
			zipOutputStream.finish();
			zipOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace(); // NOSONAR
		}

		return bos;
	}

}
