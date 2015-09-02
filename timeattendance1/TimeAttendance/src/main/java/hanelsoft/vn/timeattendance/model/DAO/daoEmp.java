package hanelsoft.vn.timeattendance.model.DAO;

public class daoEmp {
	String ID, NameEmp;

	public daoEmp(String id, String nameEmp) {
		this.ID = id;
		this.NameEmp = nameEmp;
	}

	public daoEmp() {
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getNameEmp() {
		return NameEmp;
	}

	public void setNameEmp(String nameEmp) {
		NameEmp = nameEmp;
	}

}
