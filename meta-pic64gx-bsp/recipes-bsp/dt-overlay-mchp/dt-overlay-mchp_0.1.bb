SUMMARY = "Device Trees overlay for BSPs"
DESCRIPTION = "Device Tree overlay generation and packaging for BSP"
SECTION = "bsp"

LICENSE = "MIT & GPL-2.0-or-later"
LIC_FILES_CHKSUM = " \
		file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d \
		file://LICENSES/GPL-2.0;md5=e6a75371ba4d16749254a51215d13f97 \
		"
PR = "r0"

inherit devicetree

COMPATIBLE_MACHINE = "(pic64gx-curiosity-kit)"

S = "${WORKDIR}/git"

DT_FILES_PATH = "${WORKDIR}/git/pic64gx_curiosity_kit"

PV = "1.0+git${SRCPV}"
SRCREV="dd16c9fc6abef99e79937e29e3ae753c32219dae"
SRC_URI="git://github.com/linux4microchip/dt-overlay-mchp.git;protocol=https;nobranch=1"

do_install() {
    cd ${B}
    for DTB_FILE in `ls *.dtbo`; do
        install -Dm 0644 ${B}/${DTB_FILE} ${D}/boot/${DTB_FILE}
    done
}

do_deploy() {
    cd ${B}
    for DTB_FILE in `ls *.dtbo`; do
        install -Dm 0644 ${B}/${DTB_FILE} ${DEPLOYDIR}/overlays/${DTB_FILE}
    done
}

FILES:${PN} += "/boot/*.dtbo"
