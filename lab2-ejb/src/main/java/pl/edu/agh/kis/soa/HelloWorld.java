package pl.edu.agh.kis.soa;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import pl.edu.agh.kis.soa.model.Student;

@Stateless
@WebService(	name="HelloWorldPortType",
				portName="HelloWorldPort",
				targetNamespace="http://soa.kis.agh.edu.pl/lab2")
public class HelloWorld {
	private static Logger logger = Logger.getLogger("HelloWorld");

	public HelloWorld() {
		logger.info("Instance created.");
	}
	
	@WebMethod
	@WebResult(name="HelloResponse", targetNamespace="http://soa.kis.agh.edu.pl/lab2")
	public String hello(@WebParam(name="name") String who) {
		logger.info(String.format("hello - invoked, who=%s", who));
		return "Hello " + who;
	}

	@WebMethod
	@WebResult(name="GetStudentResponse", targetNamespace="http://soa.kis.agh.edu.pl/lab2")
	public Student getStudent(@WebParam(name="albumNo") String id) {
		logger.info(String.format("getStudent - invoked, id=%s", id));
		List<String> subjects = new ArrayList<String>();
		subjects.add("Bazy danych");
		subjects.add("SOA - wprowadzenie");		
		Student s = new Student("Jan", "Nowak", id, subjects);
		return s;
	}

	/**
	 * Binarne wczytanie pliku.
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public byte[] readFile(String path) throws Exception {
		File file = new File(path);
		BufferedInputStream bufin = new BufferedInputStream(new FileInputStream(file));
		byte [] content = new byte[bufin.available()];
		bufin.read(content);
		bufin.close();
		return content;
	}
/*
 * Base64 kodowanie i dekodowanie:  
		byte[] Base64.encodeBase64(byte [])
		byte[] Base64.decodeBase64(byte []) 
 */

}
