package rmiutils;

public class EchoTask extends RemoteTask<String> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String toEcho;
	
	public EchoTask(String str){
		this.toEcho = str;
	}
	
	@Override
	public String executeTask() {
		return this.toEcho;
	}

}
