package com.jobtest.orders.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * <p>Converter for JSON serialization/deserialization and DB storage/retrieval</p>
 *
 * Annotate fields of type ZonedDateTime with Jackson annotations to enable defined JSON conversion
 * {@link JsonSerialize}(converter = <tt>ZonedDateTimeConverter.Serializer.class</tt>)
 * {@link JsonDeserialize}(converter = <tt>ZonedDateTimeConverter.Deserializer.class</tt>)
 */
public class ZonedDateTimeConverter {
	
	private ZonedDateTimeConverter() {}

	/**
	 * Instructs Jackson how to convert/serialize ZonedDateTime (JSON conversion)
	 */
	public static class Serializer extends StdConverter<ZonedDateTime, String> {
		@Override
		public String convert(ZonedDateTime value) {
			try {
				return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(value);
			} catch (NullPointerException ex) {
				return null;
			}
		}
	}

	/**
	 * Instructs Jackson how to convert/deserialize ZonedDateTime (JSON conversion)
	 */
	public static class Deserializer extends StdConverter<String, ZonedDateTime> {
		@Override
		public ZonedDateTime convert(String value) {
			try {
				return ZonedDateTime.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
			} catch (NullPointerException ex) {
				return null;
			}
		}
	}

}

