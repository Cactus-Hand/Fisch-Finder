import java.util.ArrayList;

public class BoostedFish {
    String[][] rarity = new String[7][];

    BoostedFish(ArrayList<Fish> fish, String weather, String time, String season){
        // Assign points for each fish
        for (Fish each : fish){
            if (each.time.matches(time)){
                each.points++;
            }
            for (String now : each.weather){
                if (now.matches(weather)){
                    each.points++;
                    break;
                }
            }
            for (String now : each.season){
                if (now.matches(season)){
                    each.points++;
                    break;
                }
            }
        }

        // Initialize a 2D ArrayList
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for (int i=0; i<7; i++){
            list.add(new ArrayList<>());
        }

        final String[] REGEX = {
                "Common",
                "Uncommon",
                "Unusual",
                "Rare",
                "Legendary",
                "Mythic",
                "Event"
        };

        // Add each fish to their designated rarity in the 2D ArrayList, in order of the highest points.
        for (int score=3; score>0; score--){
            for (Fish each : fish){
                StringBuilder s = new StringBuilder();
                int point = each.points;
                int total = 3;

                // Fish points / Total possible points
                if (each.weather[0].equals("N/a")){
                    total--;
                }
                if (each.time.equals("N/a")){
                    total--;
                }
                if (each.season[0].equals("N/a")){
                    total--;
                }
                if (point == score) {
                    // If a fish's points are maximum, add a star.
                    if (point == total){
                        s.append("★");
                    }
                    s.append(score).append("/").append(total).append(": ").append(each.name).append(" (").append(each.location).append(")");

                    for (int k=0; k<7; k++) {
                        if (each.rarity.matches(REGEX[k])) {
                            list.get(k).add(s.toString());
                        }
                    }
                }
            }
        }
        // Reset fish points
        for (Fish each : fish){
            each.points = 0;
        }

        // Convert the 2D ArrayList to a 2D Array
        for (int i=0; i<7; i++){
            this.rarity[i] = new String[list.get(i).size()];
            this.rarity[i] = list.get(i).toArray(this.rarity[i]);
        }
    }
}