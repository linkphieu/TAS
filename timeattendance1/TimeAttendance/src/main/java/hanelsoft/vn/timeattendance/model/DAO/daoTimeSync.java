package hanelsoft.vn.timeattendance.model.DAO;

public class daoTimeSync {
	String TimeSync;

	public daoTimeSync(String timeStamp) {
		this.TimeSync = timeStamp;
	}

	public daoTimeSync() {
	}

	public String getTimeSync() {
		return TimeSync;
	}

	public void setTimeSync(String timeStamp) {
		this.TimeSync = timeStamp;
	}
}
