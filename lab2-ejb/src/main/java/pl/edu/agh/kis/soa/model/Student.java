package pl.edu.agh.kis.soa.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (
		namespace="http://soa.kis.agh.edu.pl/lab2/model",
		name="Student"
)
public class Student {

	private String firstName;
	private String lastName;
	private String albumNo;
	private List<String> subjects;

	public Student() {
		super();
	}
	public Student(String firstName, String lastName, String albumNo,
			List<String> subjects) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.albumNo = albumNo;
		this.subjects = subjects;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAlbumNo() {
		return albumNo;
	}
	public void setAlbumNo(String albumNo) {
		this.albumNo = albumNo;
	}
	public List<String> getSubjects() {
		return subjects;
	}
	
	@XmlElementWrapper(name = "subjectsAttended")
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

}
