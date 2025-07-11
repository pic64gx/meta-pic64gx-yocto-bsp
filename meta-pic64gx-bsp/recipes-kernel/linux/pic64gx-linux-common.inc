
DESCRIPTION = "PIC64GX Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
##5.6.16
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION_EXTENSION = ""

PV = "${LINUX_VERSION}+git${SRCPV}"

KCONFIG_MODE="--alldefconfig"

COMPATIBLE_MACHINE = "(pic64gx-curiosity-kit)"

KERNEL_DTBVENDORED = "1"

KERNEL_FEATURES:remove = "features/debug/printk.scc"
KERNEL_FEATURES:remove = "features/kernel-sample/kernel-sample.scc"