package imann.graphvisualization.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NumberControls implements KeyListener {
	private RunVisualGraph controller;
	public NumberControls(RunVisualGraph controller) {
		this.controller = controller;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	//allows use of number keys to toggle the current mode
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_1) {
			controller.getButtons()[0].doClick(0);
		}else if (e.getKeyCode() == KeyEvent.VK_2) {
			controller.getButtons()[1].doClick(0);
		}else if (e.getKeyCode() == KeyEvent.VK_3) {
			controller.getButtons()[2].doClick(0);
		}else if (e.getKeyCode() == KeyEvent.VK_4) {
			controller.getButtons()[3].doClick(0);
		}	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
