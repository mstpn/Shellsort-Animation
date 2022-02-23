import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import org.piccolo2d.PNode;
import org.piccolo2d.extras.PFrame;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

public abstract class AnimationScreen extends PFrame {
	private static final long serialVersionUID = 1L;

	private boolean isInitialized;

	public AnimationScreen () {
		super ();

		isInitialized = false;
	}

	@Override
    public final void initialize () {
		// Remove the default pan and zoom event handlers
		getCanvas ().setPanEventHandler (null);
		getCanvas ().setZoomEventHandler (null);

		addInitialNodes ();

		isInitialized = true;
	}

	public abstract void addInitialNodes ();

	public void waitForInitialization () {
		while (!isInitialized) {
			try {
				Thread.sleep (100);
			} catch (InterruptedException e) {
				// Whatever, I do what I want.
			}
		}
	}

	private void addAt (PNode node, double x, double y) {
		// position item at location
		node.translate (x, y);

		// add text box to canvas (screen)
		getCanvas ().getLayer ().addChild (node);
	}

	public TextBoxNode addTextBox (double x, double y, double width, double height, int value) {
		TextBoxNode newNode = new TextBoxNode (new Rectangle2D.Double(x, y, width, height), value);

		addAt (newNode, x, y);

		return newNode;
	}

	public PText addText (double x, double y, String text) {
		PText newNode = new PText (text);

		addAt (newNode, x, y);

		return newNode;
	}

	public PNode addColouredBox (double x, double y, double width, double height, Paint colour) {
		PNode newNode = new PNode ();
		// A node will not be visible until its bounds and paint are set.
		newNode.setBounds (0, 0, width, height);
		newNode.setPaint (colour);

		addAt (newNode, x, y);

		return newNode;
	}

	public PPath addElipse (double x, double y, double width, double height) {
		PPath newNode = PPath.createEllipse (0, 0, width, height);

		addAt (newNode, x, y);

		return newNode;
	}

	public PPath addRectangle (double x, double y, double width, double height) {
		PPath newNode = PPath.createRectangle (0, 0, width, height);

		addAt (newNode, x, y);

		return newNode;
	}

	// lines are difficult to animate...
	public PPath addLine (double x1, double y1, double x2, double y2) {
		PPath newNode = PPath.createLine (x1, y1, x2, y2);

		// add line to canvas (screen)
		getCanvas ().getLayer ().addChild (newNode);

		return newNode;
	}
}