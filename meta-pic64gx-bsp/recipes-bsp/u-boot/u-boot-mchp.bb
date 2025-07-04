require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
require u-boot-env-pic64gx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS += "coreutils-native"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

PV = "v2023.07-mchp+git${SRCPV}"
SRCREV = "linux4microchip+fpga-2025.03"
SRC_URI = "git://github.com/linux4microchip/u-boot-mchp.git;protocol=https;nobranch=1  \
           file://${HSS_PAYLOAD}.yaml \
           file://${UBOOT_ENV}.cmd \
           file://${MACHINE}.cfg \
           file://uEnv.txt \
           "

SRC_URI:append:pic64gx-curiosity-kit-amp = " \
    file://0001-riscv-dts-add-device-tree-for-pic64gx-curiosity-kit-.patch \
    file://0002-configs-add-defconfig-for-pic64gx-curiosity-kit-in-A.patch \
    "

DEPENDS += " python3-setuptools-native u-boot-mkenvimage-native"
DEPENDS:append = " u-boot-tools-native hss-payload-generator-native"
DEPENDS:append:pic64gx-curiosity-kit-amp = " pic64gx-zephyr-amp-demo-hss"

do_deploy:append () {

    #
    # for pic64gx-curiosity-kit-amp, we'll already have an amp-application.elf in
    # DEPLOY_DIR_IMAGE, so smuggle it in here for the payload generator ...
    #
    if [ "${HSS_PAYLOAD}" = "amp" ]; then
        if [ "${AMP_DEMO}" = "zephyr" ]; then
            echo "amp - zephyr"
            cp ${DEPLOY_DIR_IMAGE}/zephyr-amp-application.elf ${DEPLOYDIR}/amp-application.elf
        elif [ -f "${DEPLOY_DIR_IMAGE}/amp-application.elf" ]; then
            cp -f ${DEPLOY_DIR_IMAGE}/amp-application.elf ${DEPLOYDIR}/amp-application.elf
        fi
        sed -i "s/<AMP_DEMO>/${AMP_DEMO}/g" ${WORKDIR}/${HSS_PAYLOAD}.yaml
    fi

    hss-payload-generator -c ${WORKDIR}/${HSS_PAYLOAD}.yaml -v ${DEPLOYDIR}/payload.bin

    #
    # for pic64gx-curiosity-kit-amp, if we smuggled in an amp-application.elf, then
    # clean-up here before the Yocto framework gets angry that we're trying to install
    # files (from DEPLOYDIR) into a shared area (DEPLOY_IMAGE_DIR) when they already
    # exist
    #
    if [ -f "${DEPLOYDIR}/amp-application.elf" ]; then
        rm -f ${DEPLOYDIR}/amp-application.elf
    fi

}

COMPATIBLE_MACHINE = "(pic64gx-curiosity-kit)"
