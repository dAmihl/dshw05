package rmiutils;

import java.io.Serializable;

public class RemoteTask<T> implements RMITask<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String TASK_NAME = "GenericTask(Unnamed)";
	
	@Override
	public T executeTask() {
		printExecutionInfo();
		return null;
	}
	
	private void printExecutionInfo(){
		System.out.println("Executing task "+this.TASK_NAME+"...");
	}
	
	public void setTaskName(String taskName){
		this.TASK_NAME = taskName;
	}


}
