package org.sit.cloud.marketplace.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.sit.cloud.marketplace.actors.FileBasedProvider;
import org.sit.cloud.marketplace.actors.GaussianProvider;
import org.sit.cloud.marketplace.actors.Provider;

public class GetProviderFromInputData {
	
	public static List<Provider> getProviderFromInputData(boolean gaussianWanted) throws IOException{
		FileInputStream file = new FileInputStream(new File("InputData/providersRanges.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheet("maths");
		Iterator<Row> rowIterator = sheet.iterator();
		Row row;

		List<Provider> providers = new ArrayList<Provider>();

		while(rowIterator.hasNext())
		{
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			String id = Integer.toString((int) cellIterator.next().getNumericCellValue());
			int cores = (int) cellIterator.next().getNumericCellValue(); 
			int ram = (int) cellIterator.next().getNumericCellValue();
			int disk = (int) cellIterator.next().getNumericCellValue();
			double availability = cellIterator.next().getNumericCellValue();
			double bw = cellIterator.next().getNumericCellValue();
			double cost = cellIterator.next().getNumericCellValue();
			cellIterator.next();
			cellIterator.next();
			int vmsAvail = (int) cellIterator.next().getNumericCellValue();
			int isBadProvider = (int) cellIterator.next().getNumericCellValue();
			if(gaussianWanted)
				providers.add(new GaussianProvider(id, cores, ram, disk, cost, availability, bw, vmsAvail, (isBadProvider==1)?true:false));
			else
				providers.add(new Provider(id, cores, ram, disk, cost, availability, bw, vmsAvail, (isBadProvider==1)?true:false));
		}
		return providers;
	
	}
	
	public static void main(String args[]) throws IOException{
		System.out.println(getProviderFromInputData(true).size());
	}

}
