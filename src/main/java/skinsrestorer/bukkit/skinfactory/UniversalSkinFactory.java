package skinsrestorer.bukkit.skinfactory;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class UniversalSkinFactory extends SkinFactory {
    private final Plugin plugin;
    private final Consumer<Player> refresh = detectRefresh();

    @Override
    public void updateSkin(Player player) {
        if (!player.isOnline())
            return;

        // Some older spigot versions only support hidePlayer(player)
        for (Player ps : Bukkit.getOnlinePlayers()) {
            try {
                ps.hidePlayer(this.plugin, player);
            } catch (Error ignored) {
                ps.hidePlayer(player);
            }
            try {
                ps.showPlayer(this.plugin, player);
            } catch (Error ignored) {
                ps.showPlayer(player);
            }
        }

        refresh.accept(player);
    }

    private static Consumer<Player> detectRefresh() {
        if (Bukkit.getName().toLowerCase().contains("paper")) {
            return new PaperSkinRefresher();
        }

        // return new LegacySkinRefresher();

        return new OldSkinRefresher();
    }
}