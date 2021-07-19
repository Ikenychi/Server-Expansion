[placeholder-page]: https://helpch.at/placeholders#server

# Server-Expansion

Adds server-related placeholders.  
Read the [placeholder-page] for a list of placeholders.

Updating ChimneySwift's fork with the latest upstream changes.  
It adds the mspt placeholder.  
(the milliseconds needed to calculate one game tick, lower is better.)

Here's the full list of placeholders added by this fork:
```
%server_mspt%
%server_mspt_10s%
%server_mspt_1m%
%server_mspt_colored%
%server_mspt_10s_colored%
%server_mspt_1m_colored%
```

This fork requires [Paper](https://papermc.io),
it will NOT work with spigot servers.
( but you should be using Paper anyway )

## Build Isntructions

This is built using [Maven](https://maven.apache.org/),
you will have to install that first
if you don't have it already.

You also need to download PlaceholderAPI
and place it in your maven cache manually
because the official maven repository provided in pom.xml is broken.  
you can get it from there,
maven just can't recognize it
because a special file is missing.

here's the direct link to their GitHub release: [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI/releases/download/2.10.10/PlaceholderAPI-2.10.10.jar),
place it at  
`~/.m2/repository/me/clip/PlaceholderAPI/2.10.10/PlaceholderAPI-2.10.10.jar`.  
Capitalization IS important, so make sure it is called `PlaceholderAPI`,
not `placeholderapi`

then you simply run  
`mvn install`  
and the jar should end up in `target/`.
