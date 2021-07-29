package net.verany.api.player.onlinetime;

import net.verany.api.Verany;
import net.verany.api.player.IPlayerInfo;

import java.util.concurrent.TimeUnit;

public class OnlineTimeTask implements Runnable {

    @Override
    public void run() {
        for (IPlayerInfo onlinePlayer : Verany.getOnlinePlayers()) {
            onlinePlayer.addOnlineTime();
            if (!onlinePlayer.getAfkObject().isAfk())
                onlinePlayer.addPlayTime();


            String time = "";
            long seconds = TimeUnit.MILLISECONDS.toSeconds(onlinePlayer.getPlayTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(onlinePlayer.getPlayTime());
            long hours = TimeUnit.MILLISECONDS.toHours(onlinePlayer.getPlayTime());
            if (hours == 0)
                if (minutes == 0)
                    time = seconds + "s";
                else
                    time = minutes + "m";
            else
                time = hours + "h";
        }
    }
}
