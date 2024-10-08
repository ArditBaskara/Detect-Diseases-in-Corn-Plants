import java.io.*;
import java.util.*;

public class UI {
    private ArrayList<String> questions;
    private int[] answers;
    private Map<String, Set<String>> knowledgeBase = new HashMap<>();

    public UI() {
        this.answers = new int[8]; 
        this.questions = new ArrayList<>();
        this.setQuestions();
        this.loadKnowledgeBase();  
    }           
                                                                                    
    private void setQuestions() {
        questions.add("Apakah daun jagung mengalami warna kuning (chlorotic)?");
        questions.add("Apakah tanaman jagung mengalami pertumbuhan yang terhambat?");
        questions.add("Apakah daun jagung memiliki warna putih seperti tepung?");
        questions.add("Apakah daun jagung menggulung dan melintir?");
        questions.add("Apakah pembentukan tongkol terganggu?");
        questions.add("Apakah terdapat bercak-bercak cokelat pada daun?");
        questions.add("Apakah daunnya layu?");
        questions.add("Apakah biji terlihat bengkak?");
    }

    private void loadKnowledgeBase() {
        try (BufferedReader br = new BufferedReader(new FileReader("knowledge_base"))) {
            String line;
            String currentDisease = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Penyakit:")) {
                    currentDisease = line.split(": ")[1];
                    knowledgeBase.put(currentDisease, new HashSet<>());
                } else if (line.startsWith("Gejala:")) {
                    String[] symptoms = line.split(": ")[1].split(", ");
                    Set<String> symptomSet = knowledgeBase.get(currentDisease);
                    if (symptomSet != null) {
                        symptomSet.addAll(Arrays.asList(symptoms));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showQuestion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Jawab dengan '1' untuk ya dan '0' untuk tidak.");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println(questions.get(i));
            int answer = scanner.nextInt();
            // Validasi jawaban
            while (answer != 0 && answer != 1) {
                System.out.println("Jawaban tidak valid. Masukkan '1' untuk ya dan '0' untuk tidak.");
                answer = scanner.nextInt();
            }
            answers[i] = answer; 
        }
    }

    public Set<String> getFacts() {
        Set<String> facts = new HashSet<>();
        if (answers[0] == 1) facts.add("chlorotic_colored_leaves");
        if (answers[1] == 1) facts.add("stunted_growth");
        if (answers[2] == 1) facts.add("white_like_flour");
        if (answers[3] == 1) facts.add("leaves_curl_twist");
        if (answers[4] == 1) facts.add("disturbed_cob");
        if (answers[5] == 1) facts.add("brown_spots_on_leaves");
        if (answers[6] == 1) facts.add("leaves_look_wilted");
        if (answers[7] == 1) facts.add("swollen_seeds");
        return facts;
    }

public void inferDisease(Set<String> facts) {
    String penyakitDitemukan = "";
    int maxMatchedSymptoms = 0;
    Set<String> inferedFacts = new HashSet<>();

    for (Map.Entry<String, Set<String>> entry : knowledgeBase.entrySet()) {
        String disease = entry.getKey();
        Set<String> diseaseFacts = entry.getValue();

        int matches = matchCount(diseaseFacts, facts);
        if (matches > maxMatchedSymptoms) {
            maxMatchedSymptoms = matches;
            penyakitDitemukan = disease;
            inferedFacts = diseaseFacts; // Store the disease facts here
        }
    }

    if (maxMatchedSymptoms > 0) {
        showConclusion(true, facts, inferedFacts, penyakitDitemukan);
    } else {
        showConclusion(false, facts, inferedFacts, penyakitDitemukan);
    }
}


    public int matchCount(Set<String> diseaseFacts, Set<String> userFacts) {
        int count = 0;
        for (String fact : diseaseFacts) {
            if (userFacts.contains(fact)) {
                count++;
            }
        }
        return count;
    }

void showConclusion(boolean penyakitTeridentifikasi, Set<String> facts, Set<String> inferedFacts, String penyakitDitemukan) {
    if (penyakitTeridentifikasi) {
        System.out.println("Tanaman jagung terdeteksi penyakit: " + penyakitDitemukan + ", dikarenakan gejala berikut:");

        // gejala
        int symptomNumber = 1;
        for (String fact : facts) { 
            System.out.println("Gejala " + symptomNumber + ": " + translateFact(fact));
            symptomNumber++;
        }

        System.out.println("\nFakta yang diperoleh (gejala terdeteksi): " + facts);
    } else {
        System.out.println("Tidak cukup fakta untuk mengidentifikasi penyakit.");
    }
}

private String translateFact(String facts) {
    switch (facts) {
        case "chlorotic_colored_leaves":
            return "daun mengalami warna kuning (chlorotic)";
        case "stunted_growth":
            return "pertumbuhan tanaman terhambat";
        case "white_like_flour":
            return "daun memiliki warna putih seperti tepung";
        case "leaves_curl_twist":
            return "daun menggulung dan melintir";
        case "disturbed_cob":
            return "pembentukan tongkol terganggu";
        case "brown_spots_on_leaves":
            return "terdapat bercak-bercak cokelat pada daun";
        case "leaves_look_wilted":
            return "daun terlihat layu";
        case "swollen_seeds":
            return "biji terlihat bengkak";
        default:
            return facts;
    }
}

    public static void main(String[] args) {
        UI ui = new UI();
        ui.showQuestion(); 
        Set<String> userFacts = ui.getFacts();  
        ui.inferDisease(userFacts);  
    }
}
