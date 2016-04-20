package rmiutils;

import java.rmi.RemoteException;

public class DeepThoughtTask extends RemoteTask<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String question;
	private IRemoteClientCallback callback;
	
	
	public DeepThoughtTask(String quest, IRemoteClientCallback callback){
		this.question = quest;
		this.callback = callback;
		this.setTaskName("Deep Thought");
	}
	
	@Override
	public Boolean executeTask() {
		super.executeTask();
		startCalculation();
		return true;
	}
	
	private String answerQuestion(String question){
		return "The answer to your question '"+question+"' is probably 42.";
	}
	
	private void startCalculation(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					System.out.println("Calculation of question "+question+" finished.");
					callback.callback(answerQuestion(question));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
			}
		});
		t.start();
	}

}
