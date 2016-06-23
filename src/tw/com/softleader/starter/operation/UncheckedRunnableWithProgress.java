package tw.com.softleader.starter.operation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public abstract class UncheckedRunnableWithProgress implements IRunnableWithProgress {

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			runUnchecked(monitor);
		} catch (Exception e) {
			monitor.done();
			e.printStackTrace();
			throw new Error(e);
		}
	}

	public abstract void runUnchecked(IProgressMonitor monitor) throws Exception;

}
