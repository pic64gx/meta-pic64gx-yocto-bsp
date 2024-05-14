# meta-pic64gx-extras

This layer contains recipes that extend and supplement the
meta-pic64gx-bsp layer. These include additional applications
and demos to demonstrate PIC64GX features.

## Dependencies

This layer depends on:

```text
URI: git://git.openembedded.org/openembedded-core
layers: meta
branch: kirkstone

URI: git://git.openembedded.org/meta-openembedded
layers: meta-oe, meta-python, meta-multimedia, meta-networking, meta-webserver
branch: kirkstone

URI: git://github.com/pic64gx-soc/meta-pic64gx-soc-yocto-bsp.git
layers: meta-pic64gx-soc-bsp
branch: master or PIC64GX Linux release version (e.g. v2024.06)
```
