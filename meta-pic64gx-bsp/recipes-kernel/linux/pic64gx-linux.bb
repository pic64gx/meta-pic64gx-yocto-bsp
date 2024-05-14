require recipes-kernel/linux/pic64gx-linux-common.inc

LINUX_VERSION ?= "6.6"
KERNEL_VERSION_SANITY_SKIP="1"

SRCREV="03429e1b25d512b82557069e6f70fabd242cc9e3"
SRC_URI = "git://github.com/pic64gx/pic64gx-linux.git;protocol=https;nobranch=1"

SRC_URI:append:pic64gx-curiosity-kit =  " file://pic64gx_cmdline.cfg "
SRC_URI:append:pic64gx-curiosity-kit-amp =  " \
    file://pic64gx_amp_cmdline.cfg \
    file://0001-riscv-dts-microchip-add-AMP-support-to-pic64gx-curio.patch \
    "

do_deploy:append() {

    if [ -n "${INITRAMFS_IMAGE}" ]; then

        if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
                ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage"
        fi
    fi
}

addtask deploy after do_install
