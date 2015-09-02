package hanelsoft.vn.timeattendance.model.DAO;

public class DaoProjectLocation {
	String projectID, LocationX, LocationY, LocationID, Address, AllowSkip;

	public DaoProjectLocation() {
	}

	public DaoProjectLocation(String projectID, String LocationX,
			String LocationY, String LocationID, String Address,
			String AllowSkip) {
		this.projectID = projectID;
		this.LocationX = LocationX;
		this.LocationY = LocationY;
		this.LocationID = LocationID;
		this.Address = Address;
		this.AllowSkip = AllowSkip;
	}

	public String getAllowSkip() {
		return AllowSkip;
	}

	public void setAllowSkip(String allowSkip) {
		AllowSkip = allowSkip;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
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

	public String getLocationID() {
		return LocationID;
	}

	public void setLocationID(String locationID) {
		LocationID = locationID;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

}
