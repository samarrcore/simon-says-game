import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class SimonSaysGame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel startPage, modePage  , gamePage;

    private JButton redButton, blueButton, greenButton, yellowButton;
    private ArrayList<JButton> sequence ;
    private ArrayList<JButton> playerSequence;
    private Timer timer;
    private int  currentStep;
    private int flashDelay;



    public SimonSaysGame(){
        setTitle("Simon Says Game");
        setSize(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //adding new pages for the game

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        //pages created
        createStartPage();
        createModePage();
        createGamePage();

        //adsing the creeated pages to main game
        mainPanel.add(startPage,"StartPage");
        mainPanel.add(modePage,"ModePage");
        mainPanel.add(gamePage,"GamePage");

        cardLayout.show(mainPanel,"startPage");

        //setLayout(new GridLayout(2,2)); (part of original code)
    }

    //start page
    private void createStartPage(){
        startPage = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Welcome to Simon Says",JLabel.CENTER);
        titleLabel.setFont(new Font("TImesNEwRoman",Font.BOLD,24));
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> cardLayout.show(mainPanel,"ModePage"));

        startPage.add(titleLabel, BorderLayout.CENTER);
        startPage.add(startButton, BorderLayout.SOUTH);
    }

//mode page
private void createModePage(){
    modePage = new JPanel(new GridLayout(3,1));
    JLabel modeLabel = new JLabel("Select Difficulty",JLabel.CENTER);
    modeLabel.setFont(new Font("Arial",Font.BOLD,24) );

    JButton easyButton = new JButton("Easy");
    easyButton.addActionListener(e -> startGame(800));

    JButton medButton = new JButton("Medium");
    medButton.addActionListener( e -> startGame(400));

    JButton hardButton = new JButton("Hard");
    hardButton.addActionListener(e -> startGame(250));

    modePage.add(modeLabel);
    modePage.add(easyButton);
    modePage.add(medButton);
    modePage.add(hardButton);
}

private void createGamePage(){
    gamePage = new JPanel(new GridLayout(2,2));

    //initializing buttons
    redButton =createButton(Color.RED);
    blueButton= createButton(Color.BLUE);
    greenButton = createButton(Color.GREEN);
    yellowButton = createButton(Color.YELLOW);

    //adding buttons to the frame
    gamePage.add(redButton);
    gamePage.add(blueButton);
    gamePage.add(greenButton);
    gamePage.add(yellowButton);

    sequence = new ArrayList<>();
    playerSequence = new ArrayList<>();
    currentStep = 0;
}
private void startGame(int delay){
    flashDelay =delay;
    sequence.clear();
    playerSequence.clear();
    currentStep =0;
    nextRound();
    cardLayout.show(mainPanel,"GamePage");
}

    private JButton createButton(Color color){
        JButton button = new JButton();
        button.setBackground(color);
        button.addActionListener(e -> handleButtonPress((JButton) e.getSource()));
        return button;
    }

    private void nextRound(){

        JButton nextButton = getRandomButton();
        sequence.add(nextButton);
        playerSequence.clear();
        currentStep = 0;
        flashSequence();
    }
    private void flashSequence() {
        timer = new Timer(flashDelay, new ActionListener() {
            int index = 0;

            public void actionPerformed(ActionEvent e) {
                if (index >= sequence.size()) {
                    timer.stop();
                    return;
                }
                JButton button = sequence.get(index);

                // Set to white for flashing
                button.setBackground(Color.WHITE);

                // Reset to original color after 250 ms
                Timer resetColorTimer = new Timer(flashDelay / 2, ae -> button.setBackground(getOriginalColor(button)));
                resetColorTimer.setRepeats(false);
                resetColorTimer.start();
                index++;
            }
        });
        timer.start();
    }

private void handleButtonPress(JButton button){
        playerSequence.add(button);
        if(playerSequence.get(currentStep) != sequence.get(currentStep)){
            JOptionPane.showMessageDialog(this,"GAME OVER YOU REACHED LEVEL:" + sequence.size());
            cardLayout.show(mainPanel,"StartPage");
            return;
        }
        currentStep++;
        if (currentStep == sequence.size()){
            JOptionPane.showMessageDialog(this,"NICE! NEXT ROUND");
            nextRound();
        }
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
