package hanelsoft.vn.timeattendance.common.servicenetwork.model;

import java.io.File;

public class UploadFileDTO {
	File file;
	String filename;
	String memberTel;

	public String getMemberTel() {
		return memberTel;
	}

	public void setMemberTel(String memberTel) {
		this.memberTel = memberTel;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
