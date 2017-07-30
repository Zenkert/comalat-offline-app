package comalat.Application.Domain.Enum;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SyleSakis
 */
public enum Skill {

    GRAMMAR_FUNCTIONS ("Grammar Functions"),
    VOCABULARY ("Vocabulary"),
    READING_WRITING ("Readind Writing"),
    LISTENING_SPEAKING ("Listening Speaking");
    
    private final String name;
    
    private Skill(String str){
        name = str;
    }
    
    public static Skill convertShort(char shortSkill){
        if(shortSkill == 'G'){
            return Skill.GRAMMAR_FUNCTIONS;
        }else if(shortSkill == 'V'){
            return Skill.VOCABULARY;
        }else if(shortSkill == 'R'){
            return Skill.READING_WRITING;
        }else if(shortSkill == 'L'){
            return Skill.LISTENING_SPEAKING;
        }
        return null;
    }
    
    public static List<String> convertSkillstoValues(List<Skill> skills){
        List<String> strSkills = new ArrayList<>();
        if(skills == null){
            return null;
        }
        for(Skill skill : skills){
            strSkills.add(skill.toString());
        }
        return strSkills;
    }
    
    public boolean equalsName(String otherName){
        return name.equals(otherName);
    }
    
    public String toString(){
        return this.name;
    }
}
