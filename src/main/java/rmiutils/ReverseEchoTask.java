package rmiutils;

public class ReverseEchoTask extends RemoteTask<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String toReverse;
	
	public ReverseEchoTask(String str){
		this.toReverse = str;
		this.setTaskName("Reverse Echo Task");
	}
	
	@Override
	public String executeTask() {
		super.executeTask();
		return new StringBuilder(this.toReverse).reverse().toString();
	}

}
