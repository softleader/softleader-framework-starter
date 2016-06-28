package tw.com.softleader.starter.pojo;

public class Version {

	private String sl;
	private String io;
	private boolean dft;

	public Version() {
		super();
	}

	public Version(String sl, String io, boolean dft) {
		super();
		this.sl = sl;
		this.io = io;
		this.dft = dft;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getIo() {
		return io;
	}

	public void setIo(String io) {
		this.io = io;
	}

	public boolean isDft() {
		return dft;
	}

	public void setDft(boolean dft) {
		this.dft = dft;
	}

}
