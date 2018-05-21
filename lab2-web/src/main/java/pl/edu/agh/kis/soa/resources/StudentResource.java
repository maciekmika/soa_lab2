package pl.edu.agh.kis.soa.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.persistence.PostUpdate;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.exception.ResteasyClientException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.Base64;
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

	private final String UPLOADED_FILE_PATH = "C:\\Users\\Admin\\java_workspace\\soa\\lab2\\";

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
	@Path("getStudent/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudent(@PathParam("id") String studentId) {
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(studentId))
				return students.get(i);
		}
		throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Student not found!").build());
	}

	@RolesAllowed("other")
	@GET
	@Path("getStudents")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> getStudents() {
		if(students.isEmpty())
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Students not found!").build());
		else
			return students;
	}

	@RolesAllowed("other")
	@GET
	@Path("getAvatar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public byte[] getAvatarById(@PathParam("id") String id) {
		List<Student> studentsWithId = students.stream().filter(t -> t.getStudentId().equals(id)).collect(Collectors.toList());
		if (studentsWithId.isEmpty())
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Student not found!").build());//return null;//throw new IllegalArgumentException("Student with provided id does not exist");
		if (studentsWithId.get(0).getAvatar() == null)
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Avatar not found!").build());//return null;//throw new ResteasyClientException("Student doesn't have any avatar!");
		return studentsWithId.get(0).getAvatar();
	}

	@RolesAllowed("other")
	@PUT
	@Path("addStudent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addStudent(Student student){
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(student.getStudentId()))
				students.remove(i);
		}
		students.add(student);
		return Response.status(Response.Status.CREATED).entity("Student added").build();
	}

	@RolesAllowed("other")
	@DELETE
	@Path("deleteStudent/{id}")
	public Response deleteStudent(@PathParam("id") String id){
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(id)) {
				students.remove(i);
				return Response.status(Response.Status.OK).entity("Student removed").build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).entity("Student doesn't exsist!").build();

	}

	@RolesAllowed("other")
	@PUT
	@Path("updateStudent/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStudent(@PathParam("id") String id, Student student){
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(id)) {
				students.get(i).setAvatar(student.getAvatar());
				students.get(i).setFirstName(student.getFirstName());
				students.get(i).setLastName(student.getLastName());
				students.get(i).setSubjects(student.getSubjects());
				return Response.status(Response.Status.OK).entity("Student updated").build();
			}
		}

		return Response.status(Response.Status.NOT_FOUND).entity("Student doesn't exsist!").build();
	}

	@RolesAllowed("other")
	@POST
	@Path("addAvatarForStudent/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadAvatar(@PathParam("id") String studentId, MultipartFormDataInput input){
		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				//convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);

				for(int i=0;i<students.size();i++){
					if(students.get(i).getStudentId().equals(studentId))
						students.get(i).setAvatar(bytes);
				}
				//constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;

				writeFile(bytes,fileName);

				System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return Response.status(200)
				.entity("uploadFile is called, Uploaded file name : " + fileName).build();
	}

	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
