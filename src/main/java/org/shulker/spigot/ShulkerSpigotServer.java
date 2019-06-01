package org.shulker.spigot;

import org.apache.logging.log4j.LogManager;
import org.mcelytra.core.Server;

public class ShulkerSpigotServer extends Server
{
    public ShulkerSpigotServer()
    {
        super(LogManager.getLogger());
    }

    @Override
    public String get_version()
    {
        return null;
    }

    @Override
    public String get_mc_version()
    {
        return null;
    }

    @Override
    public String get_mc_bedrock_version()
    {
        return "Unknown";
    }

    @Override
    public int get_protocol_version()
    {
        return 0;
    }

    @Override
    public int get_bedrock_protocol_version()
    {
        return -1;
    }

    @Override
    public String get_brand()
    {
        return "Spigot";
    }

    @Override
    public void update_server_ping()
    {

    }
}
