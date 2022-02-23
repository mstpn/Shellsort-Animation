
import java.awt.Color;
import java.awt.Font;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.piccolo2d.nodes.PText;

/**
 * Creates an animation of Shell Sort.
 *
 * Class modified from SortingAnimation.java by Jason Heard.
 *
 * Shell Sort method modified from:
 * https://en.wikipedia.org/wiki/Shellsort#Pseudocode
 *
 * @author matt
 *
 */
public class SortingAnimation extends AnimationScreen {
    private static final long serialVersionUID = 1L;

    private static final int totalWidth = 1920;
    private static final int totalHeight = 1080;
    private static final int ARR_SIZE = 8; // CHANGE ME
    private static final int HIDDEN_ROW = -1000;
    private static final int TOP_ROW = 100;
    private static final int MID_ROW = 300;
    private static final int BOT_ROW = 500;
    private static final int BOX_ARR_SIZE = totalWidth / (ARR_SIZE + 10);
    private static final int STD_DELAY = 1000;
    private static final int HALF_DELAY = STD_DELAY / 2;

    private static PText header;
    private static TextBoxNode[] myTextBoxes = new TextBoxNode[ARR_SIZE];
    private static int[] hOffset = new int[ARR_SIZE];

    @Override
    public void addInitialNodes() {
        int i;
        for (i = 0; i < ARR_SIZE; i++) {
            hOffset[i] = (totalWidth / (ARR_SIZE + 2)) * (i + 1);
        }

        this.setBounds(0, 0, totalWidth, totalHeight);

        // add background box
        addColouredBox(0, 0, totalWidth, totalHeight, Color.LIGHT_GRAY);

        // add header text
        header = addText(900, 10, "Shell Sort");
        header.setTextPaint(Color.DARK_GRAY);
        header.setFont(new Font(null, Font.BOLD | Font.ITALIC, 30));

        // Initial node creation
        for (i = 0; i < ARR_SIZE; i++) {
            myTextBoxes[i] = addTextBox(0, 0, BOX_ARR_SIZE, BOX_ARR_SIZE, -1);
        }
        moveRow(myTextBoxes, HIDDEN_ROW, 0);
    }

    public static void main(String[] args) {
        Instant start = Instant.now();

        SortingAnimation screen = new SortingAnimation();

        // wait for initialization before animating
        screen.waitForInitialization();

        final int DATA_RNG = 50;
        Random rand = new Random();
        List<Integer> gaps = new ArrayList<Integer>();
        int temp;
        int i;
        int j;

        // create Shell (1959) gap sequence
        // https://en.wikipedia.org/wiki/Shellsort#Gap_sequences
        temp = ARR_SIZE;
        i = 1;
        while (temp > 1) {
            temp = (int) (ARR_SIZE / (Math.pow(2, i)));
            gaps.add(temp);
            i++;
        }

        // initialize nodes to random values
        for (i = 0; i < ARR_SIZE; i++) {
            temp = rand.nextInt(DATA_RNG) + 1;
            myTextBoxes[i].setValue(temp);
        }

        // bring in unsorted set to top row
        moveRow(myTextBoxes, TOP_ROW, HALF_DELAY);
        delay();

        // DEBUGGING
        System.out.println("Initial Data");
        printBoxes(myTextBoxes);

        // Shell Sort
        int[] gapMask;
        for (int gap : gaps) {
            int offset;
            TextBoxNode tempBox;
            for (offset = 0; offset < gap; offset++) {
                // move gap boxes to sort row
                gapMask = newMask();
                for (i = offset; i < ARR_SIZE; i += gap) {
                    gapMask[i] = 1;
                    myTextBoxes[i].setColor(Color.CYAN);
                }
                //add text output for which gap is being compared
                //
                moveRow(myTextBoxes, MID_ROW, HALF_DELAY, gapMask);
                delay();

                // sort gap boxes
                for (i = offset; i < ARR_SIZE; i += gap) {
                    //add text output for which indexes are being compared
                    //
                    tempBox = myTextBoxes[i];
                    int[] compareMask = newMask();
                    compareMask[i] = 1;
                    setBoxColor(myTextBoxes, Color.YELLOW, compareMask);
                    moveRow(myTextBoxes, BOT_ROW, STD_DELAY, compareMask);
                    delay();
                    halfDelay();
                    Boolean checked = false;
                    if(i-gap >= 0 && myTextBoxes[i - gap].getValue() <= tempBox.getValue()) {
                        flash(myTextBoxes[i-gap],Color.RED);
                        checked = true;
                    }
                    for (j = i; j >= gap && myTextBoxes[j - gap].getValue() > tempBox.getValue(); j -= gap) {
                        flash(myTextBoxes[j-gap],Color.GREEN);
                        myTextBoxes[j] = myTextBoxes[j - gap];
                        //ADD text output for is tempBox.value < box[j-gap].value
                        //add half delay
                        //
                    }
                    if(j-gap >= 0 && myTextBoxes[j - gap].getValue() <= tempBox.getValue() && !checked) {
                        flash(myTextBoxes[j-gap],Color.RED);
                    }
//                    else if(j-gap >= 0 && myTextBoxes[j - gap].getValue() > tempBox.getValue()){
//                        flash(myTextBoxes[j-gap],Color.GREEN);
//                    }

                    myTextBoxes[j] = tempBox;
                    tempBox.setColor(Color.ORANGE);
                    moveRow(myTextBoxes, MID_ROW, STD_DELAY, gapMask);
                    delay();
                }

                // animate movement of gap sort
//                setBoxColor(myTextBoxes, Color.ORANGE, gapMask);
//                moveRow(myTextBoxes, BOT_ROW, STD_DELAY, gapMask);
//                delay();

                // return boxes back to top row
                setBoxColor(myTextBoxes, Color.ORANGE, gapMask);
                moveRow(myTextBoxes, TOP_ROW, STD_DELAY);
                delay();
            }
            setBoxColor(myTextBoxes, Color.WHITE);
            moveRow(myTextBoxes, TOP_ROW, HALF_DELAY);
            delay();

            System.out.print("After gap = ");
            System.out.println(gap);

            printBoxes(myTextBoxes);
        }
        System.out.println("Final Sort");
        printBoxes(myTextBoxes);
        setBoxColor(myTextBoxes, Color.GREEN);
        moveRow(myTextBoxes, TOP_ROW, 3 * STD_DELAY);

        System.out.print("Runtime: ");
        System.out.println(Duration.between(start,Instant.now()));
    }

    private static void flash(TextBoxNode box, Color color) {
        box.animateToColor(color, HALF_DELAY/2);
        halfDelay();
        box.animateToColor(box.getColor(), HALF_DELAY/2);
        halfDelay();
    }

    private static int[] newMask() {
        return new int[ARR_SIZE];
    }

    /**
     * Sets the color of every box in the array.
     */
    private static void setBoxColor(TextBoxNode[] boxes, Color color) {
        for (int i = 0; i < ARR_SIZE; i++) {
            boxes[i].setColor(color);
        }
    }

    /**
     * Sets the color of every box matching the masked array.
     */
    private static void setBoxColor(TextBoxNode[] boxes, Color color, int[] mask) {
        for (int i = 0; i < ARR_SIZE; i++) {
            if (mask[i] == 1) {
                boxes[i].setColor(color);
            }
        }
    }

    /**
     * Move boxes to their horizontal offset and specified row.
     *
     * @param boxes:   array of nodes
     * @param row:     the row to move the boxes to
     * @param duration duration of animation
     */
    private static void moveRow(TextBoxNode[] boxes, int row, int duration) {
        for (int i = 0; i < ARR_SIZE; i++) {
            boxes[i].animateToPositionScaleRotation(hOffset[i], row, 1, 0, duration);
            boxes[i].animateToColor(boxes[i].getColor(), duration);
        }
    }

    /**
     * Move boxes in the mask to their horizontal offset and specified row.
     *
     * @param boxes    array of nodes
     * @param row      the row to move the boxes to
     * @param duration duration of animation
     * @param mask     choose which boxes to move (move if mask[i] == 1)
     */
    private static void moveRow(TextBoxNode[] boxes, int row, int duration, int[] mask) {
        for (int i = 0; i < ARR_SIZE; i++) {
            if (mask[i] == 1) {
                boxes[i].animateToPositionScaleRotation(hOffset[i], row, 1, 0, duration);
                boxes[i].animateToColor(boxes[i].getColor(), duration);
            }
        }
    }

    /**
     * Pause execution for preset delay seconds.
     */
    private static void delay() {
        try {
            Thread.sleep(STD_DELAY);
        } catch (InterruptedException e) {
            // Yes mom I'm awake! Give me a second...
        }
    }

    /**
     * Pause execution for preset delay seconds.
     */
    private static void halfDelay() {
        try {
            Thread.sleep(HALF_DELAY);
        } catch (InterruptedException e) {

            /**
             * "I don't know half of you half as well as I should like; and I like less than
             * half of you half as well as you deserve." -J.R.R. Tolkien, The Fellowship of
             * the Ring
             */
        }
    }

    /**
     * Print off the order of the boxes, comma separated.
     */
    private static void printBoxes(TextBoxNode[] boxes) {
        for (int i = 0; i < ARR_SIZE; i++) {
            System.out.print(boxes[i].getText());
            System.out.print(", ");
        }
        System.out.println();
    }

}
