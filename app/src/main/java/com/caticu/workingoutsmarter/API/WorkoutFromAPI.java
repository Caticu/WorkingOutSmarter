package com.caticu.workingoutsmarter.API;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import java.io.Serializable;

public class WorkoutFromAPI implements Serializable {
    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMuscle() { return muscle; }
    public void setMuscle(String muscle) { this.muscle = muscle; }

    public String getEquipment() { return  "Equipment needed: "+'\n'+equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public SpannableStringBuilder getInstructions() { return FormatInstructions(instructions); }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    // Method to format instructions text
    private SpannableStringBuilder FormatInstructions(String text) {
        if (text == null) {
            return null;
        }
        return AddNewlinesBeforeKeywords(text, "Tip", "Caution", "Variations");
    }

    private SpannableStringBuilder AddNewlinesBeforeKeywords(String text, String... keywords)
    {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Instructions: \n" + text);

        for (String keyword : keywords) {
            String lowerCaseKeyword = keyword.toLowerCase(); // Handle case-insensitivity
            int index = spannableStringBuilder.toString().toLowerCase().indexOf(lowerCaseKeyword);
            while (index != -1) {
                if (index == 0 || spannableStringBuilder.charAt(index - 1) != '\n') {
                    spannableStringBuilder.insert(index, "\n");
                    index++;
                }
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), index, index + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index = spannableStringBuilder.toString().toLowerCase().indexOf(lowerCaseKeyword, index + keyword.length());
            }
        }

        return spannableStringBuilder;
    }

}
