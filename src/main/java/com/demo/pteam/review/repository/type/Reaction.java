package com.demo.pteam.review.repository.type;

public enum Reaction {
    thumbs_up("좋아요"),
    muscle("파이팅"),
    fire("열정"),
    smile("미소");

    private final String description;

    Reaction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
