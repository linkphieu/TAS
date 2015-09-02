package hanelsoft.vn.timeattendance.model.DAO;

public class daoProject extends BaseObject {
	private static final long serialVersionUID = 1L;
	private String ID, nameProject, LocationX, LocationY, Address,
			ProjectLocationID, AllowSkip;

	public daoProject(String id, String nameProject, String LocationX,
			String LocationY, String Address, String ProjectLocationID,
			String AllowSkip) {
		this.ID = id;
		this.nameProject = nameProject;
		this.LocationX = LocationX;
		this.LocationY = LocationY;
		this.Address = Address;
		this.ProjectLocationID = ProjectLocationID;
		this.AllowSkip = AllowSkip;

	}

	public String getAllowSkip() {
		return AllowSkip;
	}

	public void setAllowSkip(String allowSkip) {
		AllowSkip = allowSkip;
	}

	public daoProject(String id, String nameProject, String AllowSkip) {
		this.ID = id;
		this.nameProject = nameProject;
		this.AllowSkip = AllowSkip;
	}

	public daoProject() {
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getNameProject() {
		return nameProject;
	}

	public void setNameProject(String nameProject) {
		this.nameProject = nameProject;
	}

	public String getLocationX() {
		return LocationX;
	}

	public void setLocationX(String locationX) {
		LocationX = locationX;
	}

	public String getLocationY() {
		return LocationY;
	}

	public void setLocationY(String locationY) {
		LocationY = locationY;
	}

	public String getProjectLocationID() {
		return ProjectLocationID;
	}

	public void setProjectLocationID(String projectLocationID) {
		ProjectLocationID = projectLocationID;
	}

	@Override
	public String toString() {
		return "daoProject [ID=" + ID + ", nameProject=" + nameProject
				+ ",LocationX=" + LocationX + ",LocationY = " + LocationY
				+ ",Address = " + Address + "]";
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
}
