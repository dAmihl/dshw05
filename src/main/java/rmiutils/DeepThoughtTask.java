package rmiutils;

public class DeepThoughtTask extends RemoteTask<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String question;
	
	
	public DeepThoughtTask(String quest){
		this.question = quest;
		this.setTaskName("Deep Thought");
	}
	
	@Override
	public Boolean executeTask() {
		super.executeTask();
		return true;
	}
	
	private String answerQuestion(String question){
		return "The answer to your question '"+question+"' is probably 42.";
	}

}
