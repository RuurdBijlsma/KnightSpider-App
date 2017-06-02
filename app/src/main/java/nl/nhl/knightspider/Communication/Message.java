package nl.nhl.knightspider.Communication;

/**
 * Created by jorn on 6/2/17.
 */

class Message {
    private int identifier;
    private String payload;

    public int getIdentifier() {
        return identifier;
    }

    public String getPayload() {
        return payload;
    }

    public Message(int identifier, String payload) {
        this.identifier = identifier;
        this.payload = payload;
    }

    public static Message fromString(String text) {
        String[] split = text.split("::");
        if (split.length != 2) {
            return null;
        }
        try {
            int identifier = Integer.parseInt(split[0]);
            return new Message(identifier, split[1]);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }
}
