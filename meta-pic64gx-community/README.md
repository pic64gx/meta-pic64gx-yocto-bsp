# meta-pic64gx-community

This layer contains Microchip's partners' evaluation kits' machine
configuration files and associated configuration fragments.

## Dependencies

This layer depends on:

```text
URI: git://git.openembedded.org/openembedded-core
layers: meta
branch: kirkstone

URI: git://git.openembedded.org/meta-openembedded
layers: meta-oe, meta-python, meta-multimedia, meta-networking, meta-webserver
branch: kirkstone

URI: git://github.com/pic64gx/meta-pic64gx-yocto-bsp.git
layers: meta-pic64gx-soc-bsp
branch: master or PIC64GX Linux release version (e.g. v2024.06)
```
