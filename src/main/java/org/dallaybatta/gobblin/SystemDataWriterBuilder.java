package org.dallaybatta.gobblin;

import java.io.IOException;

import org.dallaybatta.gobblin.api.Data;

import gobblin.writer.DataWriter;
import gobblin.writer.DataWriterBuilder;

public class SystemDataWriterBuilder  extends DataWriterBuilder<String,Data>{

	@Override
	public DataWriter build() throws IOException {
		return new SystemDataWriter();
	}
}


