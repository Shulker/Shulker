Hey! Hello to you server owner (or staff member)!

This file is here to tell you what the hell are the new folders and how to configure Shulker!

# Folders

## addons/

The addons folder stores the addons of Shulker:
 - Javascript plugins
 - Libraries
 - More?

### addons/libraries/

The libraries folder stores the libraries that plugins may need, just put here the needed jar libraries requested by plugins.

### addons/js/

The js folder stores the Javascript plugins. Just put here the JS plugins.

## configs/

Well, put the configurations of plugins in the folder plugins/ is a bit confusing and not clean,
so here are the configurations of plugins using the configuration API of Shulker!

### configs/shulker/

It's the configuration folder of Shulker, you can find some files like: 'symbols.json' and 'config.json'

WARNING: You need to know a little bit JSON!

In the symbols file you can find this:

{
  "symbols": {
    "<3": "\u2764",
    "${star}": "\u2726",
    "<=": "\u2264",
    ">=": "\u2265"
  }
}

It's a bit confusing? Sorry :c
Well the "symbols" node stores all the symbols with a key: the name (to replace in places like chat, etc... It's a placeholder but for symbols) of a symbol,
and with a value: the string to put instead of the key.
To add symbols you can just add a new line in the symbols node and don't forget to add the comma at the end of the previous symbol, example: "${block}": "█"

The config.json file stores some messages, feel free to translate them or/and modifying them!