package org.dallaybatta.gobblin.impl;

import java.util.HashMap;
import java.util.List;

import org.dallaybatta.gobblin.api.Data;
import org.dallaybatta.gobblin.api.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryStorage implements Storage {

	private static final Logger LOG = LoggerFactory.getLogger(InMemoryStorage.class);
	
	public static HashMap<String,List<Data>> workUnitMap =  new HashMap<String,List<Data>>();
	
	@Override
	public void push(PartitionedData partitionedData) {
		List<Data> data = partitionedData.getPartionedData();
		LOG.info("Data Partioned information "+partitionedData.getStartRange()+" ---- "+partitionedData.getEndRange());
		String key = partitionedData.getStartRange()+"-"+partitionedData.getEndRange();
		workUnitMap.put(key, data);
	}
}
