package hanelsoft.vn.timeattendance.model.DAO;

public class daoEmpClock {
	String id, statusClock, dateClock;
	String projectId, projectName, projectLat, projectLon;

	public daoEmpClock(String id, String _statusClock, String _dateClock,
			String _projectId, String _projectName, String _projectLat,
			String _projectLon) {
		this.id = id;
		this.statusClock = _statusClock;
		this.dateClock = _dateClock;
		this.projectId = _projectId;
		this.projectName = _projectName;
		this.projectLat = _projectLat;
		this.projectLon = _projectLon;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectLat() {
		return projectLat;
	}

	public void setProjectLat(String projectLat) {
		this.projectLat = projectLat;
	}

	public String getProjectLon() {
		return projectLon;
	}

	public void setProjectLon(String projectLon) {
		this.projectLon = projectLon;
	}

	public daoEmpClock() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusClock() {
		return statusClock;
	}

	public void setStatusClock(String statusClock) {
		this.statusClock = statusClock;
	}

	public String getDateClock() {
		return dateClock;
	}

	public void setDateClock(String dateClock) {
		this.dateClock = dateClock;
	}

}
