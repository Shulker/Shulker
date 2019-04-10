/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of shulker.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

// Load Nashorn compatibility script
load("nashorn:mozilla_compat.js");

// Import base packages
importPackage(java.lang, java.util, java.util.stream);

function register_command(plugin, object)
{
  if (object.usage_getter === undefined)
    object.usage_getter = (sender) => object.usage;
  if (object.description_getter === undefined)
    object.description_getter = (sender) => object.description;

  let command = plugin.new_command(object.name)
                      .usage(object.usage)
                      .usage(object.usage_getter)
                      .description(object.description)
                      .description(object.description_getter)
                      .executor(object.executor);
  if (!(object.permission === undefined))
    command.permission(object.permission);
  if (!(object.aliases === undefined))
    command.aliases(object.aliases);
  if (!(object.tab_completer === undefined))
    command.tab_completer(object.tab_completer);
  plugin.register_command(command.build())
}

function new_resource(domain, name)
{
  return new ResourceName(domain, name);
}

function log(msg)
{
  __SHULKER__.logInfo(msg)
}

function load_dependency(name)
{
  let plugins_dir = __SHULKER__.get_plugins_dir();
  load(new java.io.File(plugins_dir, name).getAbsolutePath())
}