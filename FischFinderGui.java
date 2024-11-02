import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class FischFinderGui extends JFrame {
    private JLabel textLabel;
    private JComboBox<String> weatherSelect, timeSelect, seasonSelect;
    private ArrayList<Fish> fish; // An array of fish and their properties.
    final String htmlTextFile = System.getProperty("user.dir") + "\\site.txt";

    public FischFinderGui(){
        initComponents();
    }
    public void initComponents(){
        textLabel = new JLabel();
        final JButton mythicButton = new JButton();
        final JButton legendButton = new JButton();
        final JButton rareButton = new JButton();

        final String[] weather = {"Clear", "Windy", "Rainy", "Foggy"};
        final String[] time = {"Day", "Night"};
        final String[] season = {"Summer", "Autumn", "Winter", "Spring"};

        getHtml(); // Update HTML text file
        getFish(); // Create the fish ArrayList

        weatherSelect = new JComboBox<>(weather);
        timeSelect = new JComboBox<>(time);
        seasonSelect = new JComboBox<>(season);

        Dimension d = new Dimension();
        d.setSize(100, 30);

        weatherSelect.setMaximumSize(d);
        timeSelect.setMaximumSize(d);
        seasonSelect.setMaximumSize(d);

        weatherSelect.setMinimumSize(d);
        timeSelect.setMinimumSize(d);
        seasonSelect.setMinimumSize(d);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Fisch Finder");

        textLabel.setText("Please Press Any Button");
        textLabel.setFont(new Font("Dialog", Font.PLAIN, 14));

        mythicButton.setText("Mythical");
        mythicButton.setMaximumSize(d);
        mythicButton.setMinimumSize(d);
        mythicButton.addActionListener(_ -> rarityButtonClicked(5));

        legendButton.setText("Legendary");
        legendButton.setMaximumSize(d);
        legendButton.setMinimumSize(d);
        legendButton.addActionListener(_ -> rarityButtonClicked(4));

        rareButton.setText("Rare");
        rareButton.setMaximumSize(d);
        rareButton.setMinimumSize(d);
        rareButton.addActionListener(_ -> rarityButtonClicked(3));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(weatherSelect)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(mythicButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(timeSelect)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(legendButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(seasonSelect)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rareButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(textLabel)
                                        )
                                )
                                .addContainerGap(27, Short.MAX_VALUE)
                        ));

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(weatherSelect)
                                        .addComponent(mythicButton)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(timeSelect)
                                        .addComponent(legendButton)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(seasonSelect)
                                        .addComponent(rareButton)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(textLabel)
                                )
                                .addContainerGap(27, Short.MAX_VALUE)
                        ));
        pack();

        Dimension window = new Dimension();
        window.setSize(450,500);
        setSize(window);
    }

    public void getHtml(){
        try {
            URL u = new URI("https://fisch.miraheze.org/wiki/fish").toURL();
            URLConnection urlConnect = u.openConnection();
            InputStream stream = urlConnect.getInputStream();
            int i;
            try{
                FileWriter writer = new FileWriter(htmlTextFile);
                while ((i = stream.read()) != -1) {
                    writer.write((char)i);
                }
                writer.close();
            }
            catch(IOException e){
                System.out.println("Failed to write to file.");
            }
        }
        catch (Exception e) {
            System.out.println("Failed to connect to URL.");
        }
    }

    public void getFish(){
        ArrayList<Fish> array = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(htmlTextFile));
            String line = br.readLine();
            String loc = "Regionless";
            while (line != null) {
                Fish fish = new Fish();
                if (line.length() > 6 && line.substring(0, 6).matches("<td><a")) {
                    int start = line.indexOf("\">") + 2;
                    int end = line.substring(start).indexOf("<") + start;
                    fish.location = loc;
                    fish.name = line.substring(start, end);
                    line = br.readLine();
                    fish.rarity = line.substring(4).replace("</td>", "");
                    line = br.readLine();
                    fish.weather = line.substring(4).replace("</td>", "").split(", ");
                    line = br.readLine();
                    fish.time = line.substring(4).replace("</td>", "");
                    line = br.readLine();
                    fish.season = line.substring(4).replace("</td>", "").split(", ");
                    line = br.readLine();
                    fish.bait = line.substring(4).replace("</td>", "");
                    array.add(fish);
                } else if (line.length() > 9 && line.substring(0, 9).matches("<h3><span")) {
                    int start = line.indexOf("\">") + 2;
                    int end = line.substring(start).indexOf("<") + start;
                    loc = line.substring(start, end);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to read text file.");
        }
        fish = array;
    }

    public void rarityButtonClicked(int type){
        String weather = weatherSelect.getSelectedItem() + "";
        String time = timeSelect.getSelectedItem() + "";
        String season = seasonSelect.getSelectedItem() + "";

        BoostedFish boosted = new BoostedFish(fish, weather, time, season);
        StringBuilder sb = new StringBuilder();

        for (String each : boosted.rarity[type]) {
            sb.append(each).append("<br/>");
        }
        textLabel.setText("<html>" + sb + "</html>");
    }

    public static void main (String[] args){
        EventQueue.invokeLater(() -> new FischFinderGui().setVisible(true));
    }
}

class Fish {
    String name;
    String location;
    String rarity;
    String[] weather;
    String time;
    String[] season;
    String bait;
    int points;
    Fish(){}
}