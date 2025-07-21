
package co.jp.r.yomiagekbot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class OjisanStateManager {

    private LocalDateTime lastMessageTime;
    private String lastCategory;
    private int consecutiveCount;

    public OjisanStateManager() {
        this.lastMessageTime = LocalDateTime.now();
        this.lastCategory = "SMALL_TALK";
        this.consecutiveCount = 0;
    }

    public void updateState(String currentCategory) {
        LocalDateTime now = LocalDateTime.now();
        long secondsSinceLast = ChronoUnit.SECONDS.between(lastMessageTime, now);

        if (secondsSinceLast < 30) {
            consecutiveCount++;
        } else {
            consecutiveCount = 1;
        }

        this.lastMessageTime = now;
        this.lastCategory = currentCategory;
    }

    public int getConsecutiveCount() {
        return consecutiveCount;
    }

    public String getLastCategory() {
        return lastCategory;
    }

    public boolean isRapidPosting() {
        return consecutiveCount >= 3;
    }
}
