package net.bmacattack.queue.access;

public enum Priviledge {
    POST_MESSAGES("post_messages"),
    READ_MESSAGES("read_messages");

    private String right;

    Priviledge(String right) {
        this.right = right;
    }

    public String getRight() {
        return right;
    }
}
