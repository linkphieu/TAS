package hanelsoft.vn.timeattendance.model.DAO;

public class daoClock extends BaseObject {
	private static final long serialVersionUID = 1L;
	int id, employeeID, status;

	String action, datetime, image, projectID, LocationID;
	double lat, lng;

	public daoClock() {
	}

	public daoClock(int employID, double lat, double lng, String image,
			String datetime, String action, int status, String projectID,
			String LocationID) {
		this.employeeID = employID;
		this.status = status;
		this.action = action;
		this.datetime = datetime;
		this.image = image;
		this.lat = lat;
		this.lng = lng;
		this.projectID = projectID;
		this.LocationID = LocationID;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLocationID() {
		return LocationID;
	}

	public void setLocationID(String locationID) {
		LocationID = locationID;
	}

}
