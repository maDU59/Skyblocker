package de.hysky.skyblocker.skyblock.tabhud.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hysky.skyblocker.skyblock.tabhud.widget.component.IcoTextComponent;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import de.hysky.skyblocker.skyblock.tabhud.util.PlayerListMgr;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// this widget shows info about all puzzeles in the dungeon (name and status)

public class DungeonPuzzleWidget extends Widget {

    private static final MutableText TITLE = Text.literal("Puzzles").formatted(Formatting.DARK_PURPLE,
            Formatting.BOLD);

    // match a puzzle entry
    // group 1: name
    // group 2: status
    // " ?.*" to diescard the solver's name if present
    // the teleport maze has a trailing whitespace that messes with the regex
    private static final Pattern PUZZLE_PATTERN = Pattern.compile("(?<name>.*): \\[(?<status>.*)\\] ?.*");

    public DungeonPuzzleWidget() {
        super(TITLE, Formatting.DARK_PURPLE.getColorValue());
    }

    @Override
    public void updateContent() {
        int pos = 48;

        while (pos < 60) {
            Matcher m = PlayerListMgr.regexAt(pos, PUZZLE_PATTERN);
            if (m == null) {
                break;
            }

            Formatting statcol = switch (m.group("status")) {
                case "✔" -> Formatting.GREEN;
                case "✖" -> Formatting.RED;
                default -> Formatting.WHITE;
            };

            Text t = Text.literal(m.group("name") + ": ")
                    .append(Text.literal("[").formatted(Formatting.GRAY))
                    .append(Text.literal(m.group("status")).formatted(statcol))
                    .append(Text.literal("]").formatted(Formatting.GRAY));
            IcoTextComponent itc = new IcoTextComponent(Ico.SIGN, t);
            this.addComponent(itc);
            pos++;
            // code points for puzzle status chars unsolved and solved: 10022, 10004
            // not sure which one is which
            // still need to find out codepoint for the puzzle failed char
            m.group("status").codePoints().forEach(System.out::println);
        }
        if (pos == 48) {
            this.addComponent(
                    new IcoTextComponent(Ico.BARRIER, Text.literal("No puzzles!").formatted(Formatting.GRAY)));
        }
    }

}
