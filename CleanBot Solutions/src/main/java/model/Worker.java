package model;

public class Worker {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int role_id;
    private int[] categoryIds;

    public Worker() {}
    public Worker(String name, String email, String phone, String password, int role_id, int[] categoryIds) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role_id = role_id;
        this.categoryIds = categoryIds;
    }


    public Worker(int id, String name, String email, String phone, String password, int role_id, int[] categoryIds) {
        this.id = id;  // ✅ Store worker ID properly
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role_id = role_id;
        this.categoryIds = categoryIds;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public int getRoleId() {
		return role_id;
	}

	public void setRoleId(int role_id) {
		this.role_id = role_id;
	}

	public int[] getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(int[] categoryIds) {
		this.categoryIds = categoryIds;
	}

}
