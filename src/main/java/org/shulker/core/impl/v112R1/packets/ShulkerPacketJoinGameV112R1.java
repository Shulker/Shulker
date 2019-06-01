package org.shulker.core.impl.v112R1.packets;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.mcelytra.core.GameMode;
import org.shulker.core.packets.mc.play.ShulkerPacketJoinGame;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.aperlambda.lambdacommon.utils.LambdaReflection.*;

public class ShulkerPacketJoinGameV112R1 extends ShulkerPacketJoinGame<PacketPlayOutLogin>
{
    private static final Optional<Field> mc_entity_fied              = get_field(PacketPlayOutLogin.class, "a", true);
    private static final Optional<Field> mc_game_mode_field          = get_first_field_of_type(PacketPlayOutLogin.class, EnumGamemode.class);
    private static final Optional<Field> mc_hardcore_field           = get_first_field_of_type(PacketPlayOutLogin.class, boolean.class);
    private static final Optional<Field> mc_dimension_field          = get_field(PacketPlayOutLogin.class, "d", true);
    private static final Optional<Field> mc_max_players_field        = get_field(PacketPlayOutLogin.class, "f", true);
    private static final Optional<Field> mc_level_type_field         = get_first_field_of_type(PacketPlayOutLogin.class, WorldType.class);
    private static final Optional<Field> mc_reduced_debug_info_field = get_last_field_of_type(PacketPlayOutLogin.class, boolean.class);

    public ShulkerPacketJoinGameV112R1()
    {
        this(new PacketPlayOutLogin());
        this.reset();
    }

    public ShulkerPacketJoinGameV112R1(int entity_id, GameMode game_mode, boolean hardcore, int dimension, int max_players, String level_type, boolean reduced_debug_info)
    {
        this(new PacketPlayOutLogin(entity_id, EnumGamemode.getById(game_mode.get_value()), hardcore, dimension, EnumDifficulty.NORMAL, max_players, WorldType.getType(level_type), reduced_debug_info));
    }

    public ShulkerPacketJoinGameV112R1(PacketPlayOutLogin packet)
    {
        super(packet);
    }

    @Override
    public int get_entity_id()
    {
        return mc_entity_fied.map(f -> (int) get_field_value(this.packet, f)).orElse(0);
    }

    @Override
    public void set_entity_id(int id)
    {
        mc_entity_fied.ifPresent(f -> set_value(this.packet, f, id));
    }

    @Override
    public GameMode get_game_mode()
    {
        return mc_game_mode_field.map(f -> (EnumGamemode) get_field_value(this.packet, f))
                .map(EnumGamemode::getId)
                .map(GameMode::from_value)
                .orElse(GameMode.SURVIVAL);
    }

    @Override
    public void set_game_mode(GameMode game_mode)
    {
        mc_entity_fied.ifPresent(f -> set_value(this.packet, f, EnumGamemode.getById(game_mode.get_value())));
    }

    @Override
    public boolean is_hardcore()
    {
        return mc_hardcore_field.map(f -> (boolean) get_field_value(this.packet, f)).orElse(false);
    }

    @Override
    public void set_hardcore(boolean hardcore)
    {
        mc_hardcore_field.ifPresent(f -> set_value(this.packet, f, hardcore));
    }

    @Override
    public int get_dimension()
    {
        return mc_dimension_field.map(f -> (int) get_field_value(this.packet, f)).orElse(0);
    }

    @Override
    public void set_dimension(int dimension)
    {
        mc_dimension_field.ifPresent(f -> set_value(this.packet, f, dimension));
    }

    @Override
    public int get_max_players()
    {
        return mc_max_players_field.map(f -> (int) get_field_value(this.packet, f)).orElse(0);
    }

    @Override
    public void set_max_players(int max_players)
    {
        mc_max_players_field.ifPresent(f -> set_value(this.packet, f, max_players));
    }

    @Override
    public String get_level_type()
    {
        return mc_level_type_field.map(f -> get_field_value(this.packet, f, WorldType.NORMAL)).map(WorldType::name).orElse("default");
    }

    @Override
    public void set_level_type(String level_type)
    {
        mc_level_type_field.ifPresent(f -> set_value(this.packet, f, WorldType.getType(level_type)));
    }

    @Override
    public int get_render_distance()
    {
        return 0;
    }

    @Override
    public void set_render_distance(int render_distance)
    {
    }

    @Override
    public boolean has_reduced_debug_info()
    {
        return mc_reduced_debug_info_field.map(f -> (boolean) get_field_value(this.packet, f)).orElse(false);
    }

    @Override
    public void set_reduced_debug_info(boolean reduced_debug_info)
    {
        mc_reduced_debug_info_field.ifPresent(f -> set_value(this.packet, f, reduced_debug_info));
    }
}
