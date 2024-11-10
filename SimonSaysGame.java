import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class SimonSaysGame extends JFrame implements ActionListener{
    private JButton redButton, blueButton, greenButton, yellowButton;
    private ArrayList<JButton> sequence;
    private ArrayList<JButton> playerSequence;
    private Timer timer;
    private int currentStep;


    public SimonSaysGame(){
        setTitle("Simon Says Game");
        setSize(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,2));

        //initializing buttons
        redButton =createButton(Color.RED);
        blueButton= createButton(Color.BLUE);
        greenButton = createButton(Color.GREEN);
        yellowButton = createButton(Color.YELLOW);

        //adding buttons to the frame
        add(redButton);
        add(blueButton);
        add(greenButton);
        add(yellowButton);

        sequence = new ArrayList<>();
        playerSequence = new ArrayList<>();
        currentStep = 0;

        nextRound();
    }

    private JButton createButton(Color color){
        JButton button = new JButton();
        button.setBackground(color);
        button.addActionListener(this);
        return button;
    }

    private void nextRound(){

        JButton nextButton = getRandomButton();
        sequence.add(nextButton);
        playerSequence.clear();
        currentStep = 0;


        flashSequence();
    }
    private void flashSequence(){
        timer = new Timer(500, new ActionListener(){
            int index =0;
            public void actionPerformed(ActionEvent e){
                if(index >= sequence.size()){
                    timer.stop();
                    return;
                }
                JButton button = sequence.get(index);
                button.setBackground(Color.WHITE); //flashing color
                new Timer(250,ae -> button.setBackground(getOriginalColor(button))).setRepeats(false);
                index++;
            }
        });
        timer.start();
    }


    private Color getOriginalColor(JButton button){
        if (button == redButton) return Color.RED;
        if (button == blueButton) return Color.BLUE;
        if (button == greenButton) return Color.GREEN;
        return Color.YELLOW;
    }

    private JButton getRandomButton(){
        Random random = new Random();
        int choice = random.nextInt(4);
        return switch(choice){
            case 0 -> redButton;
            case 1 -> blueButton;
            case 2 -> greenButton;
            case 3 -> yellowButton;
            default -> redButton;
        };
    }

    @Override
    public void actionPerformed(ActionEvent e){
        JButton button = (JButton) e.getSource();
        playerSequence.add(button);

        //to check if player following the sequence
        if(playerSequence.get(currentStep) != sequence.get(currentStep)){
            JOptionPane.showMessageDialog(this,"game over! you reached level"+ sequence.size());
            sequence.clear();
            nextRound();
            return;
        }

        currentStep++;

        //if completed the sequence
        if(currentStep == sequence.size()){
            JOptionPane.showMessageDialog(this,"Nice!! Next Round");
            nextRound();
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            SimonSaysGame game = new SimonSaysGame();
            game.setVisible(true);
        });
    }
}