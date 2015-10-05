
package library.interfaces.hardware;

import library.interfaces.hardware.IScannerListener;

public interface IScanner {
	
	public void addListener(IScannerListener listener);
	
	public void setEnabled(boolean enabled);

}
