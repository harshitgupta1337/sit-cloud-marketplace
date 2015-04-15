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
import org.sit.cloud.marketplace.entities.UserRequest;

public class GetUserRequestsFromInputData {
	
	public static List<UserRequest> getUserRequestFromInputData() throws IOException{
		FileInputStream file = new FileInputStream(new File("InputData/custrreqRanges.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheet("reducedReq");
		Iterator<Row> rowIterator = sheet.iterator();
		Row row;

		List<UserRequest> userRequests = new ArrayList<UserRequest>();

		while(rowIterator.hasNext())
		{
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			String id = Integer.toString((int) cellIterator.next().getNumericCellValue());
			if(id.equals("0"))
				break;
			int cores = (int) cellIterator.next().getNumericCellValue(); 
			int ram = (int) cellIterator.next().getNumericCellValue();
			int disk = (int) cellIterator.next().getNumericCellValue();
			double availability = cellIterator.next().getNumericCellValue();
			double bw = cellIterator.next().getNumericCellValue();
			double cost = cellIterator.next().getNumericCellValue();
			int numOfVms = (int) cellIterator.next().getNumericCellValue();
			
			userRequests.add(new UserRequest(id, numOfVms, availability, bw, cores, ram, disk, cost, (Math.random()<0.4)?true:false));
		}
		return userRequests;
	
	}
	
	public static void main(String args[]) throws IOException{
		System.out.println(getUserRequestFromInputData().size());
	}

}
