import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * UI sistem pakar untuk memeriksa penyakit tanaman jagung berdasarkan gejala yang dialami.
 * @author 
 */
public class Main {

    public static List<Rule> getKnowledge() throws FileNotFoundException, IOException {
        List<Rule> rules;
        try ( // Ganti dengan lokasi file knowledge_base.txt yang sesuai
            FileReader reader = new FileReader("C:\\Users\\ASUS\\Downloads\\SimpleExpertSystem-main\\SimpleExpertSystem-main\\src\\knowledge_base"); BufferedReader bufferedReader = new BufferedReader(reader)) {
            rules = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //mengambil string bagian fact dulu baru conclussion
                String[] parts = line.split("-");
                String[] factsArray = parts[0].split(",");
                List<String> facts = Arrays.asList(factsArray);
                String conclusion = parts[1];
                rules.add(new Rule(facts, conclusion));
            }          }
        return rules;
    }
    
    /**
     * Main method untuk menjalankan sistem pakar.
     */
    public static void main(String[] args) throws IOException {
        
        UI tampilan = new UI(); // Menampilkan UI untuk pertanyaan gejala penyakit jagung
        tampilan.showQuestion(); // Menampilkan pertanyaan dan mendapatkan jawaban user
        
        // Initial facts (jawaban user)
        Set<String> facts  = tampilan.getFacts();
        
        // Ambil knowledge base dari file
        List<Rule> rules = getKnowledge();
        
        // Lakukan forward chaining untuk inferensi penyakit berdasarkan gejala
        Set<String> inferedFacts = 
                InferenceForwardChaining.doForwardChaining(rules, facts);
        
        // Tentukan apakah penyakit tertentu telah teridentifikasi
        boolean penyakitTeridentifikasi = false;
        String penyakitDitemukan = null;
        
        for (String fact : inferedFacts) {
            if (fact.equals("Bulai") || fact.equals("Blight") || fact.equals("Leaf Rust") || fact.equals("Burn")) {
                penyakitTeridentifikasi = true;
                penyakitDitemukan = fact;
                break;
            }
        }
        
        // Menampilkan kesimpulan apakah penyakit ditemukan atau tidak
        tampilan.showConclusion(penyakitTeridentifikasi, facts, inferedFacts, penyakitDitemukan);
    }
}
