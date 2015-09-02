package hanelsoft.vn.timeattendance.common.servicenetwork.model;

import java.io.File;

public class UploadDoc {
	String title;
	File file;
	long size;
	String language;
	String targetFormat;
	String filename;
	String memberTel;

	public String getMemberTel() {
		return memberTel;
	}

	public void setMemberTel(String memberTel) {
		this.memberTel = memberTel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTargetFormat() {
		return targetFormat;
	}

	public void setTargetFormat(String targetFormat) {
		this.targetFormat = targetFormat;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
