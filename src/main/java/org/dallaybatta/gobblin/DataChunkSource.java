package org.dallaybatta.gobblin;

import gobblin.configuration.ConfigurationKeys;
import gobblin.configuration.SourceState;
import gobblin.configuration.WorkUnitState;
import gobblin.source.extractor.Extractor;
import gobblin.source.extractor.extract.AbstractSource;
import gobblin.source.workunit.Extract;
import gobblin.source.workunit.WorkUnit;
import gobblin.source.workunit.Extract.TableType;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.dallaybatta.Utility;
import org.dallaybatta.gobblin.api.Accumulator;
import org.dallaybatta.gobblin.api.Data;
import org.dallaybatta.gobblin.api.DataPartitionInfo;
import org.dallaybatta.gobblin.api.DataPartitionMetaData;
import org.dallaybatta.gobblin.api.DataReader;
import org.dallaybatta.gobblin.api.Storage;
import org.dallaybatta.gobblin.impl.PartitionedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataChunkSource extends AbstractSource<String,Data> {

	private static final Logger LOG = LoggerFactory.getLogger(DataChunkSource.class);
	
	@Override
	public List<WorkUnit> getWorkunits(SourceState state) {
		List<WorkUnit> workUnits = new ArrayList<WorkUnit>();
		int workUnitNumber = 1;
		String dpMetaDataClassName = state.getProp(Utility.DataPartitionMetaData_CLASS);
		dpMetaDataClassName = (dpMetaDataClassName!=null)?dpMetaDataClassName:Utility.DataPartitionMetaData_CLASS_IMPL;
		// Pluggable DataPartitionMetaData.
		DataPartitionMetaData dpMetaData = Utility.instantiate(dpMetaDataClassName);
		
		String readerClassName = state.getProp(Utility.DataReader_CLASS);
		readerClassName = (readerClassName!=null)?readerClassName:Utility.DataReader_CLASS_IMPL;
		// Pluggable DataReader.
		DataReader reader = Utility.instantiate(readerClassName);
		reader.setResourceUrl(state.getProp(Utility.SOURCE_REST_ENDPOINT));
		dpMetaData.setChunkReader( reader);
		
		if(dpMetaData.exists()) {
			List<DataPartitionInfo>dpMetaDataList = dpMetaData.getDataPartionInfo();
			// Create WorkUnits and pass the information from the DataPartitionInfo.
			// TBD ???
		}
		else {
			// Get Data partition Count as defined. 
			int WORK_UNIT_COUNT = state.getPropAsInt(Utility.WORK_UNIT_COUNT,Utility.DEFAULT_WORK_UNIT_COUNT);
			int workUnitCount = 0;
			
			//	Define DataReader and read.
			DataReader chunkDataReader = dpMetaData.getChunkReader();
			Iterator<Data> data = chunkDataReader.read(dpMetaData.getDataChunkUrl());
			
			String accumulatorClassName = state.getProp(Utility.Accumulator_CLASS);
			accumulatorClassName = (accumulatorClassName!=null)?accumulatorClassName:Utility.Accumulator_CLASS_IMPL;
			// Pluggable Accumulator
			Accumulator accumulator = Utility.instantiate(accumulatorClassName); 
			
			// Start Reading the data.
			while(data.hasNext()) {
				Data dataTuple = data.next();
				LOG.info("Reading data from Source using Rest Endpoint "+dataTuple.getInfo());
				// keep Accumulating Data.
				accumulator.accumulate(dataTuple);
				workUnitCount++;
				// Data bucket created.
				if(workUnitCount == WORK_UNIT_COUNT) {
					LOG.info("Going to build the WorkUnits using source "+state.getProp("source.class"));
					PartitionedData partitionedData = accumulator.getPartitionedData();
					
					String storageClassName = state.getProp(Utility.Storage_CLASS);
					storageClassName = (storageClassName!=null)?storageClassName:Utility.Storage_CLASS_IMPL;
					// Store Accumulating Data to Distributed Inmemory.
					// Pluggable Storage
					Storage storage = Utility.instantiate(storageClassName);
					storage.push(partitionedData);
					LOG.info("Creating workunit number : "+workUnitNumber);
					workUnitNumber++;
					// prepare WorkUnit and pass the reference of the Distributed Storage to Work Unit.
					Extract extract = createExtract(TableType.SNAPSHOT_ONLY,
					        state.getProp(ConfigurationKeys.EXTRACT_NAMESPACE_NAME_KEY), "SampleExtractOutput");				
					WorkUnit workUnit = WorkUnit.create(extract);
					workUnit.setProp("work.unit.number", workUnitNumber);
					workUnit.setProp("work.unit.number.key", partitionedData.getStartRange()+"-"+partitionedData.getEndRange());
					workUnits.add(workUnit);
					// Clear Accumulator.
					accumulator.clear();
					workUnitCount=0;
				}
			}
		}
		return workUnits;
	}

	@Override
	public Extractor<String, Data> getExtractor(WorkUnitState state) throws IOException {
		return new DataChunkExtractor(state);
	}

	@Override
	public void shutdown(SourceState state) {
		// TODO Auto-generated method stub
	}
  }
