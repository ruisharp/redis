package com.test.util;

import com.alibaba.fastjson.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.core.ConfigurableObjectInputStream;

public class SerializingUtil  {
	private static final Logger logger = Logger.getLogger(SerializingUtil.class);
	private static final String KEY_SPLIT = ":";

	public static byte[] serialize(Object source) {
		Optional.ofNullable(source);
		if (source instanceof byte[]) {
			return (byte[]) ((byte[]) source);
		} else {
			ByteArrayOutputStream byteOut = null;
			ObjectOutputStream ObjOut = null;

			try {
				byteOut = new ByteArrayOutputStream();
				ObjOut = new ObjectOutputStream(byteOut);
				ObjOut.writeObject(source);
				ObjOut.flush();
			} catch (IOException arg6) {
				logger.error(source.getClass().getName() + " serialized error !", arg6);
			} finally {
				IOUtils.close(ObjOut);
			}

			return byteOut.toByteArray();
		}
	}

	public static Object deserialize(byte[] source) {
		Object retVal = null;
		if (source != null) {
			Object ObjIn = null;

			try {
				ConfigurableObjectInputStream e = new ConfigurableObjectInputStream(new ByteArrayInputStream(source),
						Thread.currentThread().getContextClassLoader());
				retVal = e.readObject();
			} catch (Exception arg6) {
				logger.error("deserialized error  !", arg6);
			} finally {
				IOUtils.close((Closeable) ObjIn);
			}
		}

		return retVal;
	}

	public static byte[] generateKey(String prefix, String key, Object[] k) {
		return k != null && k.length != 0
				? serialize(MessageFormat.format(prefix + ":" + key, k))
				: serialize(prefix + ":" + key);
	}
}