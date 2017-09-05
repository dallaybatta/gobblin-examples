package org.dallaybatta.gobblin.impl;

import org.dallaybatta.gobblin.api.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IgniteStorage implements Storage {

	private static final Logger LOG = LoggerFactory.getLogger(IgniteStorage.class);
	
	@Override
	public void push(PartitionedData partitionedData) {
		LOG.info("Data Partioned information "+partitionedData.getStartRange()+" ---- "+partitionedData.getEndRange());
	}

}
