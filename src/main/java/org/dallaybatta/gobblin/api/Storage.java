package org.dallaybatta.gobblin.api;

import org.dallaybatta.gobblin.impl.PartitionedData;

public interface Storage {
	
	public void push(PartitionedData partitionedData) ;

}
