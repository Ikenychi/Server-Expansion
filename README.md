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

simply run `mvn install`  
and the jar should end up in `target/`.
