package ru.optimus.cooldown;

import java.util.HashMap;
import java.util.UUID;

public class CooldownSystem {

    private HashMap<UUID, Long> hashMap = new HashMap<UUID, Long>();


    public boolean has(UUID u) {
        return hashMap.containsKey(u) && hashMap.get(u) > System.currentTimeMillis();
    }

    public void add(UUID u, int sec) {
        hashMap.remove(u);
        hashMap.put(u, System.currentTimeMillis() + (sec * 1000L));
    }



}
