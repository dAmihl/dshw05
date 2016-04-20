package rmiutils;

public class PurposeTask extends RemoteTask<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String question = "What is my purpose?";
	
	
	public PurposeTask(){
		
		this.setTaskName("Asking Purpose");
	}
	
	@Override
	public String executeTask() {
		super.executeTask();
		return answerQuestion(this.question);
	}
	
	private String answerQuestion(String question){
		String answer = "You asked: "+question+"\n";
		answer += "You pass the butter.";
		return answer;
	}
	
}
