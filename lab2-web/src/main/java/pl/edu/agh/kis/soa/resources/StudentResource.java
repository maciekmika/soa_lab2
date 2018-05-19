package pl.edu.agh.kis.soa.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.exception.ResteasyClientException;
import pl.edu.agh.kis.soa.resources.model.Student;
import pl.edu.agh.kis.soa.resources.model.StudentBuilder;

/**
 * Klasa wystawiająca interfejs REST.
 * @author teacher
 *
 */

@Path("rest")
@Stateless
public class StudentResource {

	private static final Logger logger = Logger.getLogger("StudentResource");
	private List<Student> students = new ArrayList<>();



	public StudentResource(){
		List<String> subjects = new ArrayList<>();
		subjects.add("SOA");
		subjects.add("Kompilatory");
		subjects.add("Systemy Wbudowane");


		Student student = StudentBuilder.aStudent()
				.withStudentId("1")
				.withAvatar("C:\\Users\\Admin\\java_workspace\\soa\\abc\\avatar.png")
				.withFirstName("Baltazar")
				.withLastName("Gąbka")
				.withSubjects(subjects)
				.build();

		Student student2 = StudentBuilder.aStudent()
				.withStudentId("2")
				.withAvatar("C:\\Users\\Admin\\java_workspace\\soa\\abc\\avatar.png")
				.withFirstName("Tajemniczy Don Pedro")
				.withLastName("Z krainy deszczowców")
				.withSubjects(subjects)
				.build();

		students.add(student);
		students.add(student2);
	}

	@RolesAllowed("other")
	@GET
	@Path("getStudent")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudent(@QueryParam("id") String studentId) {
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(studentId))
				return students.get(i);
		}
		return null;
	}

	@RolesAllowed("other")
	@GET
	@Path("getStudents")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> getStudents() {
		return students;
	}

	@RolesAllowed("other")
	@GET
	@Path("getAvatar")
	@Produces(MediaType.APPLICATION_JSON)
	public byte[] getAvatarById(@QueryParam("id") String id) {
		List<Student> studentsWithId = students.stream().filter(t -> t.getStudentId().equals(id)).collect(Collectors.toList());
		if (studentsWithId.isEmpty())
			throw new IllegalArgumentException("Student with provided id does not exist");
		if (studentsWithId.get(0).getAvatar() == null)
			throw new ResteasyClientException("Student doesn't have any avatar!");
		return studentsWithId.get(0).getAvatar();
	}
}
