import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;

import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;
import org.piccolo2d.util.PBounds;


public class TextBoxNode extends PPath.Float {
	private static final long serialVersionUID = 1L;

	private PText nodeLabel;
	private int value;
	private Color color;

	public TextBoxNode (Shape shape, int value) {
		super(shape);

		nodeLabel = new PText (String.valueOf(value));
		nodeLabel.setPickable (false);
		nodeLabel.setFont(new Font(null, Font.BOLD, 20));
		this.color = Color.WHITE;
		this.value = value;

		this.addChild (nodeLabel);

		centerText();
	}

	public int getValue() {
	    return this.value;
	}

	public void setValue(int value) {
	    this.value = value;
	    this.setText(String.valueOf(value));
	}

	public Color getColor() {
	    return this.color;
	}

	public void setColor(Color color) {
	    this.color = color;
	}

	public Paint getTextPaint () {
		return nodeLabel.getTextPaint ();
	}

	public void setTextPaint (Paint textPaint) {
		nodeLabel.setTextPaint (textPaint);
	}

	public String getText () {
		return nodeLabel.getText ();
	}

	private void setText (String newText) {
		nodeLabel.setText (newText);
		centerText ();
	}

	private void centerText () {
		PBounds ourBounds = this.getBounds ();
		nodeLabel.centerBoundsOnPoint (ourBounds.getCenterX (), ourBounds.getCenterY ());
	}

}
