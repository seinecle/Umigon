/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


/**
 *
 * @author C. Levallois
 */
public class Clock {

    private long start;
    private String action;
    private StringBuilder logText;
    private StringBuilder intermediateText;
    private final String newLine = "\n";
    private final String interval = "-------------------------------\n\n";

    public Clock(String action) {

        this.action = action;
        intermediateText = new StringBuilder();
        logText = new StringBuilder();
        startClock();
    }

    private void startClock() {

        start = System.currentTimeMillis();
        logText.append(action).append("...").append(newLine);
//        Screen1.logArea.setText(Screen1.logArea.getText().concat(logText.toString()));
//        Screen1.logArea.setCaretPosition(Screen1.logArea.getText().length());

        //GUI_Screen_1.logArea.repaint();

        System.out.print(logText.toString());
    }

    public void addText(String it) {
        intermediateText.append(it).append(newLine);
    }

    public void printText() {
        System.out.println(logText.toString());
        logText = new StringBuilder();
        intermediateText = new StringBuilder();

//        Screen1.logArea.setText(Screen1.logArea.getText().concat(logText.toString()));
//        Screen1.logArea.setCaretPosition(Screen1.logArea.getText().length());
    }

    public void printElapsedTime() {

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - start;

        if (elapsedTime
                < 1000) {
            System.out.println("still " + action.toLowerCase() + ", " + elapsedTime + " milliseconds]");

        } else {
            System.out.println("still " + action.toLowerCase() + ", " + elapsedTime / 1000 + " seconds]");
        }

    }

    public void closeAndPrintClock() {

        long currentTime = System.currentTimeMillis();
        long totalTime = currentTime - start;
        logText = new StringBuilder();
        logText.append(intermediateText.toString());
        logText.append("finished [took: ");


        if (totalTime < 10000) {
            logText.append(Math.round(totalTime / 1000))
                    .append(" seconds, ")
                    .append(Math.round(totalTime % 1000))
                    .append(" milliseconds]")
                    .append(newLine + interval);
        } else if (totalTime < 60000) {
            logText.append(totalTime / 1000).append(" seconds]").append(newLine + interval);
        } else {
            logText.append(totalTime / 60000).append(" minutes ").append(Math.round((totalTime % 60000) / 1000)).append(" seconds").append(newLine + interval);
        }

        System.out.println(logText.toString());
//        Screen1.logArea.setText(Screen1.logArea.getText().concat(logText.toString()));
//        Screen1.logArea.setCaretPosition(Screen1.logArea.getText().length());

    }
}
