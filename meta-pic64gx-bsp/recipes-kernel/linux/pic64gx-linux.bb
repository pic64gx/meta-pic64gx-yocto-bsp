require recipes-kernel/linux/pic64gx-linux-common.inc

LINUX_VERSION ?= "6.6"
KERNEL_VERSION_SANITY_SKIP="1"

SRCREV="linux4microchip+fpga-2024.09.1"
SRC_URI = "git://github.com/linux4microchip/linux.git;protocol=https;nobranch=1"

SRC_URI:append:pic64gx-curiosity-kit = " \
					file://pic64gx_cmdline.cfg \
					file://pic64gx_v4l2.cfg \
					"
SRC_URI:append:pic64gx-curiosity-kit-amp =  " \
    file://pic64gx_amp_cmdline.cfg \
    "

do_assemble_fitimage[depends] = "${@'dt-overlay-mchp:do_deploy' \
                                  if "pic64gx-curiosity-kit-amp" in d.getVar('MACHINE') \
                                  else ''}"

do_deploy:append() {

    if [ -n "${INITRAMFS_IMAGE}" ]; then

        if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
                ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage"
        fi
    fi
}

addtask deploy after do_install
