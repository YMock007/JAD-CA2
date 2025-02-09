package Inquiry;

import java.time.LocalDateTime;

public class Inquiry {
	private int id;
	private String name;
	private String email;
	private String title;
	private String description;
	private String createdAt;
	
	
	public Inquiry(String title, String description, String createdAt) {
		super();
		this.title = title;
		this.description = description;
		this.setCreatedAt(createdAt);
	}

	public Inquiry(String name, String email, String title, String description) {
		super();
		this.name = name;
		this.email = email;
		this.title = title;
		this.description = description;
	}
	
	public Inquiry(int id, String name, String email, String title, String description) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.title = title;
		this.description = description;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt2) {
		this.createdAt = createdAt2;
	}
	
	

}
