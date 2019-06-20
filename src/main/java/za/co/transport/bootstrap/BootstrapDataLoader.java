package za.co.transport.bootstrap;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import za.co.transport.entity.Planet;
import za.co.transport.entity.Route;
import za.co.transport.service.PlanetService;
import za.co.transport.service.RouteService;
import za.co.transport.service.TransportUtility;

/**
 * load input data from the class path file during bootstrapping
 */

@Component
public class BootstrapDataLoader {
	
	
	
	public BootstrapDataLoader() {
		
	}
	
	@Autowired
	PlanetService planetService;
	
	@Autowired
	RouteService routeService;
	
	@Autowired
	ResourceLoader loader;
	
	@Value(value = "${inputdata.file.location}")
	private String INPUT_XLS_FILE;
	
	private List<Planet> planetList = null;
	private List<Route> routeList = null;
	
	/**
	 * The Springboot application invoke init() method on bootstrapping
	 * and invoke loadXlsDataFile();
	 */
	
	@PostConstruct
	public void init() {
		loadXlsDataFile();
	}

	/*
	 * This method loads the inputData.xlsx file. First load Planet sheet and then Routes sheets
	 */
	private void loadXlsDataFile(){
		try {			
			Resource resource = loader.getResource(INPUT_XLS_FILE);
			InputStream inputXlsx = resource.getInputStream();
			
			Workbook workbook = new XSSFWorkbook(inputXlsx);
			
			processPlanetData(workbook);
			processRoutesData(workbook);
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("There is an error loading inputData file from class path");
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("There is an error processing the inputData file from class path");
		}
		
	}

	private void processRoutesData(Workbook workbook){
		try {
			boolean firstRow = true;
			Sheet routeSheet = getSheet(workbook, 1);
			routeList = new ArrayList<Route>();
			
			//Iterate through each rows one by one
	        Iterator<Row> rowIterator = routeSheet.iterator();
			while(rowIterator.hasNext()) {
				Route route = new Route();			
				
				Row row = rowIterator.next();
				if(firstRow) {
					firstRow=false;
					continue;
				}
				
				Cell routeIdCell = row.getCell(0);
				Cell planetOriginCell = row.getCell(1);
				Cell planetDestinationCell = row.getCell(2);
				Cell distanceCell = row.getCell(3);
				
				long routeId = 0;
				String planetOrigin = "";
				String planetDestination = "";
				double distance = 0.0;
				
				if (routeIdCell.getCellType() == CellType.NUMERIC) {
					routeId = (long) routeIdCell.getNumericCellValue();
				}
				
				if(planetOriginCell.getCellType() == CellType.STRING) {
					planetOrigin = planetOriginCell.getStringCellValue();
				}
				
				if(planetDestinationCell.getCellType() == CellType.STRING) {
					planetDestination = planetDestinationCell.getStringCellValue();
				}
				
				if(distanceCell.getCellType() == CellType.NUMERIC) {
					distance = distanceCell.getNumericCellValue();
				}
				Planet origin = planetService.getPlanetById(planetOrigin, TransportUtility.getRequestType(1));
				Planet destination = planetService.getPlanetById(planetDestination, TransportUtility.getRequestType(1));
				
				if(StringUtils.isEmpty(origin) || StringUtils.isEmpty(destination)) {
					continue;
				}
				
				route.setRouteId(routeId);
				route.setOrigin(origin);	
				route.setDestination(destination);
				route.setDistance(distance);
				
				routeList.add(route);
				
			}
			
			routeService.saveRoute(routeList);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("There is an error reading the excel sheet2 ");
		}finally {
			routeList = null;
		}
		
	}

	/*
	 * Process Planets object from first sheet of inputData file and save objects one by one
	 * @param workbook that contains planets sheet
	 * */
	private void processPlanetData(Workbook workbook)  {
		try {
			boolean firstRow = true;
			Sheet planetSheet = getSheet(workbook, 0);
			planetList = new ArrayList<Planet>();
			
			//Iterate through each rows one by one
	        Iterator<Row> rowIterator = planetSheet.iterator();
			while(rowIterator.hasNext()) {
				Planet planet = new Planet();			
				
				Row row = rowIterator.next();
				if(firstRow) {
					firstRow=false;
					continue;
				}
				
				Cell planetId = row.getCell(0);
				Cell planetName = row.getCell(1);
				
				if(planetId.getCellType() == CellType.STRING) {
					planet.setPlanetId(planetId.getStringCellValue());
				}
				
				if(planetName.getCellType() == CellType.STRING) {
					planet.setPlanetName(planetName.getStringCellValue());
				}
				
				planetList.add(planet);
				
			}
			
			planetService.savePlanet(planetList);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("There is an error reading the excel sheet1 ");
		}finally {
			planetList = null;
		}		
		
	}

	
	/*
	 * This method return Sheet object from inputData excel file based on index
	 * @param workbook Object of excel file
	 * @param index index of sheet inexcel file
	 * */
	private Sheet getSheet(Workbook workbook, int index) throws FileNotFoundException, Exception{		
		return workbook.getSheetAt(index);
	}

}
