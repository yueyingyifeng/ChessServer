package org.yyyf.game.manager;

import java.util.HashSet;
import java.util.Set;
public class PlayerIdManager {
    private static PlayerIdManager instance;
    private Set<Integer> availableIds;
    private Set<Integer> usedIds;

    private PlayerIdManager() {
        availableIds = new HashSet<>();
        usedIds = new HashSet<>();
    }

    public static PlayerIdManager getInstance() {
        if (instance == null) {
            instance = new PlayerIdManager();
        }
        return instance;
    }

    public synchronized int getNewPlayerId() {
        int id;
        if (availableIds.isEmpty()) {
            id = usedIds.size() + 1;
        } else {
            id = availableIds.iterator().next();
            availableIds.remove(id);
        }
        usedIds.add(id);
        return id;
    }

    public synchronized void releasePlayerId(int id) {
        if (usedIds.contains(id)) {
            usedIds.remove(id);
            availableIds.add(id);
        }
    }
}
