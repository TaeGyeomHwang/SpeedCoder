package model;

public class WordDTO {
    private String id;
    private String wordText;

    // 생성자
    public WordDTO() {
    }

    // id getter 및 setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // wordText getter 및 setter
    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }
}
