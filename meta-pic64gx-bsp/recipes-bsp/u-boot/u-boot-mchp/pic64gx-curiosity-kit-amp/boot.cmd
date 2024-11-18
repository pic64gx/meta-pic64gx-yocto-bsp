setenv fdt_high 0xffffffffffffffff
setenv initrd_high 0xffffffffffffffff

load mmc 0:${distro_bootpart} ${scriptaddr} fitImage
bootm start ${scriptaddr}#conf-microchip_pic64gx-curiosity-kit.dtb#conf-pic64gx_curiosity_kit_amp.dtbo;
bootm loados ${scriptaddr};
# Try to load a ramdisk if available inside fitImage
bootm ramdisk;
bootm prep;
fdt set /soc/ethernet@20110000 mac-address ${pic64gx_curiosity_kit_mac_addr0};
run design_overlays;
bootm go;