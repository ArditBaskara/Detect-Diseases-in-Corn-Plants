
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author AERO
 */
public class Rule {
    private List<String> antecedent;
        private String consequent;

        public Rule(List<String> antecedent, String consequent) {
            this.antecedent = antecedent;
            this.consequent = consequent;
        }

        public List<String> getAntecedent() {
            return antecedent;
        }

        public String getConsequent() {
            return consequent;
        }
}
