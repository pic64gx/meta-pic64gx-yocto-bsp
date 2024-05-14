SUMMARY = "PIC64GX blinky exemple using openamp"
DESCRIPTION = "PIC64GX blinky exemple with openamp"

require recipes-kernel/zephyr-kernel/zephyr-sample.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

#SRCREV_pic64-zephyr-examples = "1dabfef38a8ea337f4bf38290ed9cacfa366d19f"
#SRC_URI_APP = "git://github.com/pic64gx/pic64gx-zephyr-examples.git;protocol=https;subpath=apps/amp_example"
#SRC_URI:append = " ${SRC_URI_APP};name=pic64-zephyr-examples;nobranch=1;"

SRCREV_pic64-zephyr-examples = "1dabfef38a8ea337f4bf38290ed9cacfa366d19f"
SRC_URI_APP = "git://github.com/pic64gx/pic64gx-zephyr-examples.git;protocol=https;subpath=apps/blinky_amp"
SRC_URI:append = " ${SRC_URI_APP};name=pic64-zephyr-examples;nobranch=1;"

# Substitute Zephyr kernel with our fork, for the moment we do that, it might
# be wise to just recreate a custom zephyr-kernel-src-x.bb at some point with
# our forks
ZEPHYR_BRANCH="pic64gx"
SRCREV_default = "005e9fb52b1e6268f937360d8d141b5c1b897926"
SRC_URI_ZEPHYR = "git://github.com/pic64gx/pic64gx-zephyr.git;protocol=https"
SRC_URI:append = " ${SRC_URI_ZEPHYR};nobranch=1;name=default;destsuffix=git/zephyr "

SRCREV_rpmsg-lite = "69010a78da75c7ae9e9bdfa510ce862f47b29dad"
SRC_URI_ZEPHYR_RMSG_LITE = "git://github.com/pic64gx/pic64gx-rpmsg-lite.git;protocol=https"
SRC_URI:append = " ${SRC_URI_ZEPHYR_RMSG_LITE};name=rpmsg-lite;nobranch=1;destsuffix=git/modules/lib/rpmsg-lite "

ZEPHYR_SRC_DIR = "${WORKDIR}/blinky_amp"

ZEPHYR_MODULES:append = "${S}/modules/lib/rpmsg-lite\;"

ALLOWED_AMP_DEMO = "zephyr"

do_install() {
    if [[ "${ALLOWED_AMP_DEMO}" =~ "${AMP_DEMO}" ]]; then
        install -d ${D}${nonarch_base_libdir}/firmware
        install -D ${B}/zephyr/${ZEPHYR_MAKE_OUTPUT} ${D}${nonarch_base_libdir}/firmware/rproc-miv-rproc-fw
    else
        bbnote "${PN} do_install() have been skipped, because ${AMP_DEMO} is not covered by this recipe"
    fi
}

do_deploy() {
    cp ${B}/zephyr/${ZEPHYR_MAKE_OUTPUT} ${DEPLOYDIR}/zephyr-amp-application.elf
}

FILES:${PN} += "/lib/firmware/"
SYSROOT_DIRS += "/lib/firmware"
INSANE_SKIP += "ldflags buildpaths"

addtask deploy after do_install

COMPATIBLE_MACHINE = "(pic64gx-curiosity-kit)"
