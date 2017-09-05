package org.dallaybatta.gobblin;

import java.io.IOException;

import org.dallaybatta.gobblin.api.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gobblin.writer.DataWriter;

public class SystemDataWriter implements DataWriter<Data> {

	private static final Logger LOG = LoggerFactory.getLogger(SystemDataWriter.class);

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public long recordsWritten() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long bytesWritten() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write(Data record) throws IOException {
	  LOG.info(record.getInfo());
	  }
}
