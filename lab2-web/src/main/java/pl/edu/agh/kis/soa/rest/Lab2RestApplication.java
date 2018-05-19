package pl.edu.agh.kis.soa.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import pl.edu.agh.kis.soa.resources.StudentResource;


/**
 * Klasa wystawiająca zasoby REST. Zdefiniowana w WEB-INF/web.xml.
 * @author teacher
 *
 */
@ApplicationPath("lab2-web")
public class Lab2RestApplication extends Application {

	/**
	 * Musimy tutaj dodać wszystkie klasy, które będą  wykorzystywane przez
	 * interfejs restowy.
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(StudentResource.class);
		return classes;
	}
	
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
		return singletons;
	}
}
