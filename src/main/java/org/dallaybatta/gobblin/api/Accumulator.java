package org.dallaybatta.gobblin.api;

import org.dallaybatta.gobblin.impl.PartitionedData;

public interface Accumulator {

	public void accumulate(Data dataTuple);

	public PartitionedData getPartitionedData();

	public void clear();

	public void setStartId(String startId);
	public void setEndId(String endId);
	public String getStartId();
	public String getEndId();
}
