/*
 * Copyright 2015 PRImA Research Lab, University of Salford, United Kingdom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primaresearch.page.viewer.extra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Abstract class for tasks that can be run asynchronously
 * 
 * @author Christian Clausner
 *
 */
public abstract class Task {

	private boolean success = false;
	private volatile boolean running = false;
	private TaskThread thread = null;
	private Set<TaskListener> listeners = new HashSet<TaskListener>();
	
	/**
	 * Method containing the task content (to be overridden)
	 * @return <code>true</code> if the task was successful; <code>false</code> otherwise
	 */
	protected abstract boolean doRun();
	
	/**
	 * Runs the task synchronously (in the current thread)
	 */
	public synchronized void run() {
		if (running)
			return;
		running = true;
		
		success = doRun();
		
		thread = null;
		running = false;
		notifyFinished();
	}
	
	/**
	 * Runs the task asynchronously (in its own thread)
	 */
	public synchronized void runAsync() {
		if (thread != null)
			return;
		thread = new TaskThread(this);
		thread.start();
	}
	
	/**
	 * Returns <code>true</code> if the task was successful; <code>false</code> otherwise
	 */
	public boolean isSuccessfull() {
		return success;
	}
	
	/**
	 * Adds a listener for task related events
	 * @param listener Listener object
	 */
	public void addListener(TaskListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Notifies all listeners that this task has finished
	 */
	private void notifyFinished() {
		for (Iterator<TaskListener> it = listeners.iterator(); it.hasNext(); ) {
			TaskListener listener = it.next();
			listener.taskFinished(this);
		}
	}
	
	/**
	 * Thread implementation for asynchronous tasks
	 *  
	 * @author Christian Clausner
	 *
	 */
	static class TaskThread extends Thread {
		private Task task;
		
		public TaskThread(Task task) {
			this.task = task;
		}
		
		public void run() {
	        task.run();
	    }
	}

}
