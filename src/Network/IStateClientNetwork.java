package Network;

import java.io.IOException;

public interface IStateClientNetwork {
	
	/**
	 * Execute the current state.
	 * 
	 * @throws IOException
	 */
	void execute() throws IOException;
	
	/**
	 * Prepare internal buffers.
	 */
	void prepareBuffer();
	
	/**
	 * Process message.
	 */
	void processMessage();
}
