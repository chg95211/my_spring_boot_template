package com.zsk.template.config.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-15 23:42
 **/
public class CustomDateDeserializer extends StdDeserializer<LocalDateTime>
{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CustomDateDeserializer()
    {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc)
    {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException
    {

        String date = jsonparser.getText();
        return LocalDateTime.parse(date, formatter);
    }
}

